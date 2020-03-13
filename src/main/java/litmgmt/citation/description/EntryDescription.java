package litmgmt.citation.description;


/** An entry description defines of which mandatory and optional fields an entry
 * consists. In addition, it also specifies how the output shall be formatted. */
public class EntryDescription {
  private EntryType _entryType;
  private FieldDescription[] _requiredFields;  // List of required/mandatory fields.
  private FieldDescription[] _optionalFields;  // List of optional fields.


  /** Create a new entry description.
   * @param entryType Name of the described entry type.
   * @param reqFields List of required/mandatory fields.
   * @param optFields List of optional fields. */
  public EntryDescription(EntryType entryType, FieldDescription[] reqFields, FieldDescription[] optFields) {
    _entryType = entryType;
    _requiredFields = reqFields;
    _optionalFields = optFields;
  }


  /** Get the name of this entry type.
   * @return Name of this entry type. */
  public String getEntryType() {
    return _entryType.toString().toLowerCase();
  }


  /** Get all mandatory fields.
   * @return List of required fields. */
  public FieldDescription[] getRequiredFields() {
    return _requiredFields;
  }


  /** Get all optionally available fields.
   * @return List of optional fields. */
  public FieldDescription[] getOptionalFields() {
    return _optionalFields;
  }
}
