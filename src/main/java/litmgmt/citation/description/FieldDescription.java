package litmgmt.citation.description;


/** Formatting rules for a field type. */
public class FieldDescription {
  private FieldType _fieldType; // The described field type.
  private String _formatting;   // Regular expression for entry formatting.


  /** Insert a new field description.
   * @param fieldType The described field type.
   * @param formatting Regular expression for entry formatting. */
  public FieldDescription(FieldType fieldType, String formatting) {
    _fieldType = fieldType;
    _formatting = formatting;
  }


  /** Get the name of this field type.
   * @return Name of this field type. */
  public String getFieldType() {
    return _fieldType.toString().toLowerCase();
  }


  /** Get the formatting expression for this field type.
   * @return Format string. */
  public String getFormatting() {
    return _formatting;
  }
}
