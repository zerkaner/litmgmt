package litmgmt.citation.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import litmgmt.citation.description.EntryType;
import litmgmt.citation.description.FieldType;
import litmgmt.persistency.IdHelper;


/** A citation entry. It has a cite key, a type and a number of data fields. */
@JsonPropertyOrder({"id", "citeKey", "entryType", "fields"})
public class Entry {

  private int _id;                         // Identifier of this entry.
  private String _citeKey;                 // Cite key (unique string identifier per collection).
  private EntryType _entryType;            // Predefined type of the entry.
  private Map<FieldType, String> _fields;  // The entry's data fields.
  private List<FieldType> _keyOrder;       // Ordering for the fields.


  /** Create a new entry.
   * @param citeKey Cite key (unique string identifier per collection).
   * @param entryType Predefined type of the entry. */
  public Entry(String citeKey, EntryType entryType) {
    this(IdHelper.GetNextEntryId(), citeKey, entryType);
  }


  /** Create a new entry (private constructor with ID setter).
   * @param id Identifier of this entry.
   * @param citeKey Cite key (unique string identifier per collection).
   * @param entryType Predefined type of the entry. */
  private Entry(int id, String citeKey, EntryType entryType) {
    _id = id;
    _citeKey = citeKey;
    _entryType = entryType;
    _fields = new HashMap<FieldType, String>();
    _keyOrder = new ArrayList<FieldType>();
  }


  /** Create a new field or edit the entry of an existing field.
   * @param fieldType Type of the input field.
   * @param value Input value. */
  public void setField(FieldType fieldType, String value) {
    if (!_keyOrder.contains(fieldType)) _keyOrder.add(fieldType);
    _fields.put(fieldType, value);
  }


  /** Rename this entry.
   * @param newCiteKey The new cite key for this entry. */
  public void rename(String newCiteKey) {
    _citeKey = newCiteKey;
  }


  /** Load an entry by restoring all its fields read from a JSON object.
   * @param json JSON object to initialize from.
   * @return An entry object or 'null' on parser failure. */
  public static Entry loadEntryFromJson(JsonNode json) {
    try {
      var entry = new Entry(
        json.get("id").asInt(),
        json.get("citeKey").asText(),
        EntryType.valueOf(json.get("entryType").asText().toUpperCase())
      );
      for (JsonNode field : json.get("fields")) {
        entry.setField(
          FieldType.valueOf(field.get("fieldType").asText().toUpperCase()),
          field.get("value").asText()
        );
      }
      return entry;
    }
    catch (Exception ex) {
      System.err.println("[Entry] Failed to initialize from JSON! Input was:\n"+json);
      return null;
    }
  }


  /** Output the properties of this entry as JSON object.
   * @return JSON containing all properties of this object. */
  public String saveEntryAsJson() {
    var sb = new StringBuilder();
    sb.append(
      "      {\n"+
      "        \"id\": "+_id+",\n"+
      "        \"citeKey\": \""+_citeKey+"\",\n"+
      "        \"entryType\": \""+_entryType.toString().toLowerCase()+"\",\n"+
      "        \"fields\": [\n"
    );
    for (int i = 0; i < _keyOrder.size(); i++) {
      var ft = _keyOrder.get(i);
      sb.append(
        "          {\"fieldType\": \""+ft.toString().toLowerCase()+"\", \"value\": \""+_fields.get(ft)+"\"}");
      if (i < _fields.size() - 1) sb.append(",\n");
      else sb.append("\n");
    }
    sb.append(
      "        ]\n"+
      "      }"
    );
    return sb.toString();
  }


  // __________________________________________________________________________
  // GET methods.

  /** Get the entry identifier.
   * @return The entry ID. */
  public int getId() {
    return _id;
  }


  /** Get the cite key of the entry.
   * @return The entry's cite key. */
  public String getCiteKey() {
    return _citeKey;
  }


  /** Get the type of this entry.
   * @return The entry type as lowercase string. */
  public String getEntryType() {
    return _entryType.toString().toLowerCase();
  }


  /** Get all fields of this entry.
   * @return JSON array with fields, comprising of field types and their values. */
  public ArrayNode getFields() {
    var mapper = new ObjectMapper();
    var arrayNode = mapper.createArrayNode();
    for (var ft : _keyOrder) {
      var node = mapper.createObjectNode();
      node.put("fieldType", ft.toString().toLowerCase());
      node.put("value", _fields.get(ft));
      arrayNode.add(node);
    }
    return arrayNode;
  }
}
