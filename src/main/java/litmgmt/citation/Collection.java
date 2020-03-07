package litmgmt.citation;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import litmgmt.persistency.IdHelper;


/** A collection is an aggregation of citation entries. */
public class Collection {

  private int _id;              // Collection identifier.
  private String _name;         // Name of the collection.
  private List<Entry> _entries; // List with citation entries.


  /** Create a new collection.
   * @param name Collection name. */
  public Collection(String name) {
    _id = IdHelper.GetNextCollectionId();
    _name = name;
    _entries = new ArrayList<Entry>();
  }


  /** Load a collection by restoring all its fields read from a JSON object.
   * @param json JSON object to initialize from.
   * @return A collection object or 'null' on parser failure. */
  public static Collection loadCollectionFromJson(JsonNode json) {
    return null;
  }


  /** Output the properties of this collection as JSON object.
   * @return JSON containing all properties of this object. */
  public String saveCollectionAsJson() {
    return
      "    {\n"+
      "    }";
  }


  // __________________________________________________________________________
  // GET methods.

  /** Get collection identifier.
   * @return The collection ID. */
  public int GetId() {
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
