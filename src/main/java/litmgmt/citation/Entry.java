package litmgmt.citation;

import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import litmgmt.citation.description.EntryType;


/** A citation entry. It has a cite key, a type and a number of data fields. */
public class Entry {
  private String _citeKey;
  private EntryType _entryType;
  private List<Field> _fields;


  /** Load an entry by restoring all its fields read from a JSON object.
   * @param json JSON object to initialize from.
   * @return An entry object or 'null' on parser failure. */
  public static Entry loadEntryFromJson(JsonNode json) {
    return null;
  }


  /** Output the properties of this entry as JSON object.
   * @return JSON containing all properties of this object. */
  public String saveEntryAsJson() {
    return
      "    {\n"+
      "    }";
  }
}
