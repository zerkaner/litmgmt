package litmgmt.citation;

import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;


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




/** Data field of a citation. Its information is of a
 * predefined type (e.g. "author", "chapter" or "publisher"). */
class Field {
  FieldType fieldType;
  String value;
}


/** Source of citation (book, article etc.). Depending on the
 * type of source, different fields are available and/or mandatory. */
enum EntryType {
  ARTICLE, BOOK, INCOLLECTION, MANUAL, PROCEEDINGS, TECHREPORT;
}

/** Type of a field, which is part of an entry. */
enum FieldType {
  AUTHOR, BOOKTITLE, CHAPTER, JOURNAL, MONTH, YEAR, PUBLISHER;
}


/** An entry description defines of which mandatory and optional fields an entry
 * consists. In addition, it also specifies how the output shall be formatted. */
class EntryDescription {
  EntryType entryType;
  FieldDescription[] requiredFields;
  FieldDescription[] optionalFields;
}

class FieldDescription {
  FieldType fieldType;
  String formatting;  // Regular expression for entry formatting.
}
