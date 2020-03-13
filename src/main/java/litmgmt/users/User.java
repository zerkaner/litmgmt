package litmgmt.users;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import litmgmt.persistency.IdHelper;


/** A user is denoted by name, email and password and has
 * access to a list of collections which in turn keep the citations. */
public class User {

  private int _id;                    // User identifier.
  private String _name;               // User name.
  private String _email;              // E-mail of the user.
  private String _passwordHash;       // SHA-1 password hash.
  private List<Integer> _collections; // List of collections this user has.
  private MessageDigest _md5;         // Hashing instance.


  /** Create a new user.
   * @param name Name of the user.
   * @param email E-mail address of that user.
   * @param password Clear-text password. It gets SHA-1 hashed before it is saved. */
  public User(String name, String email, String password) {
    this(name, email);
    _id = IdHelper.GetNextUserId();
    _collections = new ArrayList<Integer>();
    _passwordHash = getHashForString(password);
  }


  /** Private constructor for common fields and message digest initialization. */
  private User(String name, String email) {
    _name = name;
    _email = email;
    try { _md5 = MessageDigest.getInstance("SHA-1");}
    catch (Exception ex) { /* No reason to fail. */ }
  }


  /** Try to perform user authentication.
   * @param password The plain-text password used for authentication.
   * @return Either a session token or 'null' on failure. */
  public String loginUser(String password) {
    var pwHash = getHashForString(password);
    if (pwHash.equals(_passwordHash)) {
      var concat = _name+_passwordHash+System.currentTimeMillis();
      return getHashForString(concat);
    }
    return null;
  }


  /** Generate a hash from an input string.
   * @param input The string to be hashed.
   * @return Resulting hash, represented as unsigned hexadecimal byte string. */
  private String getHashForString(String input) {
    _md5.update(input.getBytes());
    var bytes = _md5.digest();
    var sb = new StringBuilder(bytes.length * 2);
    for (byte b : bytes) sb.append(String.format("%02X",  b & 0xff));
    var hash = sb.toString();
    return hash;
  }


  /** Add a collection to this user's repository.
   * @param colId Identifier of the collection to add. */
  public void addCollection(int colId) {
    _collections.add(colId);
  }


  /** Remove a collection from this user's repository.
   * @param colId Identifier of the collection to remove. */
  public void removeCollection(int colId) {
    _collections.remove(Integer.valueOf(colId));
  }


  /** Load a user by restoring all its fields read from a JSON object.
   * @param json JSON object to initialize from.
   * @return An user object or 'null' on parser failure. */
  public static User loadUserFromJson(JsonNode json) {
    try {
      var user = new User(json.get("name").asText(), json.get("email").asText());
      user._id = json.get("id").asInt();
      user._passwordHash = json.get("pwhash").asText();
      user._collections = new ArrayList<Integer>();
      for (var colId : json.get("collections")) {
        user._collections.add(colId.asInt());
      }
      return user;
    }
    catch (Exception ex) {
      System.err.println("[User] Failed to initialize from JSON! Input was:\n"+json);
      return null;
    }
  }


  /** Output this user's properties as JSON object.
   * @return JSON containing all properties of this object. */
  public String saveUserAsJson() {
    String cols = "";
    for (int i = 0; i < _collections.size(); i++) {
      cols += _collections.get(i);
      if (i < _collections.size() - 1) cols += ", ";
    }
    return
      "    {\n"+
      "      \"id\": "+_id+",\n"+
      "      \"name\": \""+_name+"\",\n"+
      "      \"email\": \""+_email+"\",\n"+
      "      \"pwhash\": \""+_passwordHash+"\",\n"+
      "      \"collections\": ["+cols+"]\n"+
      "    }";
  }


  // __________________________________________________________________________
  // GET methods.

  /** Get name of user.
   * @return User name. */
  public String getName() {
    return _name;
  }


  /** Get the user's e-mail address.
   * @return User e-mail address. */
  public String getEmail() {
    return _email;
  }


  /** Retrieve all collections of a user.
   * @return List with all collections. If none exists, the list will be empty! */
  public List<Integer> GetAllCollections() {
    return _collections;
  }
}
