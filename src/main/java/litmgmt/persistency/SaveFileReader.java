package litmgmt.persistency;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import litmgmt.citation.Collection;
import litmgmt.citation.CollectionManager;
import litmgmt.citation.Entry;
import litmgmt.users.User;
import litmgmt.users.UserAuthenticator;


/** File reader and writer to persist the user and collection data. */
public class SaveFileReader {

  private String _filePath;            // Path to the save file.
  private UserAuthenticator _userAuth; // User authenticator for login and register endpoints.
  private CollectionManager _colMgr;   // Collection manager for CRUD operations on collections and entries.


  /** Create a new save file reader.
   * @param filePath Path to the save file.
   * @param userAuth User authenticator for login and register endpoints.
   * @param colMgr Collection manager for CRUD operations on collections and entries. */
  public SaveFileReader(String filePath, UserAuthenticator userAuth, CollectionManager colMgr) {
    _filePath = filePath;
    _userAuth = userAuth;
    _colMgr = colMgr;
  }


  /** Read a previous state from the savefile and apply it to the program. */
  public void readFromFile() {
    try {
      var jsonStr = Files.readString(Path.of(_filePath));
      var jsonObj = new ObjectMapper().readTree(jsonStr);
      var ids = jsonObj.get("idCounters");
      IdHelper.initialize(ids.get(0).asInt(), ids.get(1).asInt(), ids.get(2).asInt());

      // Restore users.
      var users = new ArrayList<User>();
      for (var userStr : jsonObj.get("users")) {
        var user = User.loadUserFromJson(userStr);
        if (user != null) users.add(user);
      }
      _userAuth.setUserList(users);

      // Restore collections.
      var collections = new ArrayList<Collection>();
      for (var colStr : jsonObj.get("collections")) {
        var collection = Collection.loadCollectionFromJson(colStr);
        if (collection != null) collections.add(collection);
      }
      _colMgr.setCollectionList(collections);

      // Restore entries.
      var entries = new ArrayList<Entry>();
      for (var entryStr : jsonObj.get("entries")) {
        var entry = Entry.loadEntryFromJson(entryStr);
        if (entry != null) entries.add(entry);
      }
      _colMgr.setEntryList(entries);
    }
    catch (Exception ex) {
      System.err.println("[SaveFileReader] Failed to read file '"+_filePath+"'!");
    }
  }


  /** Save the current program state to the savefile. */
  public void saveToFile() {
    var sb = new StringBuilder();
    sb.append("{\n");
    var idStr = IdHelper.GetNextUserId()+", "+IdHelper.GetNextCollectionId()+", "+IdHelper.GetNextEntryId();
    var date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    sb.append("  \"writtenOn\": \""+date+"\",\n");
    sb.append("  \"idCounters\": ["+idStr+"],\n");
    sb.append("  \"users\": [\n");
    var users = _userAuth.getUserList();
    for (int i = 0; i < users.size(); i++) {
      sb.append(users.get(i).saveUserAsJson());
      if (i < users.size() - 1) sb.append(",");
      sb.append("\n");
    }
    sb.append("  ],\n");
    sb.append("  \"collections\": [\n");
    var collections = _colMgr.getCollectionList();
    for (int i = 0; i < collections.size(); i++) {
      sb.append(collections.get(i).saveCollectionAsJson());
      if (i < collections.size() - 1) sb.append(",");
      sb.append("\n");
    }
    sb.append("  ],\n");
    sb.append("  \"entries\": [\n");
    var entries = _colMgr.getEntryList();
    for (int i = 0; i < entries.size(); i++) {
      sb.append(entries.get(i).saveEntryAsJson());
      if (i < entries.size() - 1) sb.append(",");
      sb.append("\n");
    }
    sb.append("  ]\n");
    sb.append("}\n");
    try {
      var writer = new PrintWriter(new File(_filePath), "UTF-8");
      writer.print(sb.toString());
      writer.flush();
      writer.close();
      System.out.println("[SaveFileReader] Saved current program state to file.");
    }
    catch (Exception ex) {
      System.err.println("[SaveFileReader] Failed to write save file!");
    }
  }
}
