package litmgmt.citation;

import litmgmt.citation.description.FieldType;


/** Data field of a citation. Its information is of a
 * predefined type (e.g. "author", "chapter" or "publisher"). */
public class Field {
  FieldType fieldType;
  String value;
}
