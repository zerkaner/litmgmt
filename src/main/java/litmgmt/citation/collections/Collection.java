package litmgmt.citation.collections;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import litmgmt.citation.description.EntryType;
import litmgmt.persistency.IdHelper;


/** A collection is an aggregation of citation entries. */
public class Collection {

  private int _id;              // Collection identifier.
  private String _name;         // Name of the collection.
  private List<Entry> _entries; // List with citation entries.


  /** Create a new collection.
   * @param name Collection name. */
  public Collection(String name) {
    this(IdHelper.GetNextCollectionId(), name);
  }


  /** Create a new collection (private constructor with ID setter).
   * @param id Collection identifier.
   * @param name Collection name. */
  private Collection(int id, String name) {
    _id = id;
    _name = name;
    _entries = new ArrayList<Entry>();
  }


  /** Create a new entry in this collection.
   * @param citeKey Cite key (unique string identifier per collection).
   * @param entryType Predefined type of the entry.
   * @return The created entry or 'null' on conflict. */
  public Entry createEntry(String citeKey, EntryType entryType) {
    for (var entry : _entries) {
      if (entry.getCiteKey().equals(citeKey)) return null;
    }
    var newEntry = new Entry(citeKey, entryType);
    _entries.add(newEntry);
    return newEntry;
  }


  /** Rename this collection.
   * @param newName The new name for this collection. */
  public void rename(String newName) {
    _name = newName;
  }


  /** Load a collection by restoring all its fields read from a JSON object.
   * @param json JSON object to initialize from.
   * @return A collection object or 'null' on parser failure. */
  public static Collection loadCollectionFromJson(JsonNode json) {
    try {
      var collection = new Collection(
        json.get("id").asInt(),
        json.get("name").asText()
      );
      for (JsonNode entryJson : json.get("entries")) {
        var entry = Entry.loadEntryFromJson(entryJson);
        if (entry != null) collection._entries.add(entry);
      }
      return collection;
    }
    catch (Exception ex) {
      System.err.println("[Collection] Failed to initialize from JSON! Input was:\n"+json);
      return null;
    }
  }


  /** Output the properties of this collection as JSON object.
   * @return JSON containing all properties of this object. */
  public String saveCollectionAsJson() {
    var sb = new StringBuilder();
    sb.append(
      "    {\n"+
      "      \"id\": "+_id+",\n"+
      "      \"name\": \""+_name+"\",\n"+
      "      \"entries\": [\n"
    );
    for (int i = 0; i < _entries.size(); i++) {
      sb.append(_entries.get(i));
      if (i < _entries.size() - 1) sb.append(",\n");
      else sb.append("\n");
    }
    sb.append(
      "      ]\n"+
      "    }"
    );
    return sb.toString();
  }


  // __________________________________________________________________________
  // GET methods.

  /** Get collection identifier.
   * @return The collection ID. */
  public int getId() {
    return _id;
  }


  /** Get collection name.
   * @return The name of the collection. */
  public String getName() {
    return _name;
  }


  /** Get all entries of the collection.
   * @return List with all entries. Empty list, if no entry exists yet! */
  public List<Entry> getEntries() {
    return _entries;
  }
}
