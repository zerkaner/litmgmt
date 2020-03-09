package litmgmt.citation.description;


/** Helper class to keep all entry descriptions. */
public class DescriptionList {


  /** Get a list with all entry types and their descriptions.
   * Based on listing from: http://bib-it.sourceforge.net/help/fieldsAndEntryTypes.php
   * @return A list containing definitions with the mandatory and optional fields for all entry types. */
  public static EntryDescription[] getEntryDescriptions() {
    return new EntryDescription[] {

      new EntryDescription(EntryType.ARTICLE,
        new FieldDescription[] {
          new FieldDescription(FieldType.AUTHOR, ""),
          new FieldDescription(FieldType.TITLE, ""),
          new FieldDescription(FieldType.JOURNAL, ""),
          new FieldDescription(FieldType.YEAR, "")
        },
        new FieldDescription[] {
          new FieldDescription(FieldType.VOLUME, ""),
          new FieldDescription(FieldType.NUMBER, ""),
          new FieldDescription(FieldType.PAGES, ""),
          new FieldDescription(FieldType.MONTH, ""),
          new FieldDescription(FieldType.NOTE, "")
        }
      ),

      new EntryDescription(EntryType.BOOK,
        new FieldDescription[] {
          new FieldDescription(FieldType.AUTHOR, ""),
          new FieldDescription(FieldType.EDITOR, ""),
          new FieldDescription(FieldType.TITLE, ""),
          new FieldDescription(FieldType.PUBLISHER, ""),
          new FieldDescription(FieldType.YEAR, "")
        },
        new FieldDescription[] {
          new FieldDescription(FieldType.VOLUME, ""),
          new FieldDescription(FieldType.NUMBER, ""),
          new FieldDescription(FieldType.SERIES, ""),
          new FieldDescription(FieldType.ADDRESS, ""),
          new FieldDescription(FieldType.EDITION, ""),
          new FieldDescription(FieldType.MONTH, ""),
          new FieldDescription(FieldType.NOTE, "")
        }
      ),

      new EntryDescription(EntryType.BOOKLET,
        new FieldDescription[] {
          new FieldDescription(FieldType.TITLE, "")
        },
        new FieldDescription[] {
          new FieldDescription(FieldType.AUTHOR, ""),
          new FieldDescription(FieldType.HOWPUBLISHED, ""),
          new FieldDescription(FieldType.ADDRESS, ""),
          new FieldDescription(FieldType.MONTH, ""),
          new FieldDescription(FieldType.YEAR, ""),
          new FieldDescription(FieldType.NOTE, "")
        }
      ),

      new EntryDescription(EntryType.INBOOK,
        new FieldDescription[] {
          new FieldDescription(FieldType.AUTHOR, ""),
          new FieldDescription(FieldType.EDITOR, ""),
          new FieldDescription(FieldType.TITLE, ""),
          new FieldDescription(FieldType.CHAPTER, ""),
          new FieldDescription(FieldType.PAGES, ""),
          new FieldDescription(FieldType.PUBLISHER, ""),
          new FieldDescription(FieldType.YEAR, "")
        },
        new FieldDescription[] {
          new FieldDescription(FieldType.VOLUME, ""),
          new FieldDescription(FieldType.NUMBER, ""),
          new FieldDescription(FieldType.SERIES, ""),
          new FieldDescription(FieldType.TYPE, ""),
          new FieldDescription(FieldType.ADDRESS, ""),
          new FieldDescription(FieldType.EDITION, ""),
          new FieldDescription(FieldType.MONTH, ""),
          new FieldDescription(FieldType.NOTE, "")
        }
      ),

      new EntryDescription(EntryType.INCOLLECTION,
        new FieldDescription[] {
          new FieldDescription(FieldType.AUTHOR, ""),
          new FieldDescription(FieldType.TITLE, ""),
          new FieldDescription(FieldType.BOOKTITLE, ""),
          new FieldDescription(FieldType.PUBLISHER, ""),
          new FieldDescription(FieldType.YEAR, "")
        },
        new FieldDescription[] {
          new FieldDescription(FieldType.EDITOR, ""),
          new FieldDescription(FieldType.VOLUME, ""),
          new FieldDescription(FieldType.NUMBER, ""),
          new FieldDescription(FieldType.SERIES, ""),
          new FieldDescription(FieldType.TYPE, ""),
          new FieldDescription(FieldType.CHAPTER, ""),
          new FieldDescription(FieldType.PAGES, ""),
          new FieldDescription(FieldType.ADDRESS, ""),
          new FieldDescription(FieldType.EDITION, ""),
          new FieldDescription(FieldType.MONTH, ""),
          new FieldDescription(FieldType.NOTE, "")
        }
      ),

      new EntryDescription(EntryType.INPROCEEDINGS,
        new FieldDescription[] {
          new FieldDescription(FieldType.AUTHOR, ""),
          new FieldDescription(FieldType.TITLE, ""),
          new FieldDescription(FieldType.BOOKTITLE, ""),
          new FieldDescription(FieldType.YEAR, "")
        },
        new FieldDescription[] {
          new FieldDescription(FieldType.EDITOR, ""),
          new FieldDescription(FieldType.VOLUME, ""),
          new FieldDescription(FieldType.NUMBER, ""),
          new FieldDescription(FieldType.SERIES, ""),
          new FieldDescription(FieldType.PAGES, ""),
          new FieldDescription(FieldType.ADDRESS, ""),
          new FieldDescription(FieldType.MONTH, ""),
          new FieldDescription(FieldType.ORGANIZATION, ""),
          new FieldDescription(FieldType.PUBLISHER, ""),
          new FieldDescription(FieldType.NOTE, "")
        }
      ),

      new EntryDescription(EntryType.MANUAL,
        new FieldDescription[] {
          new FieldDescription(FieldType.TITLE, "")
        },
        new FieldDescription[] {
          new FieldDescription(FieldType.AUTHOR, ""),
          new FieldDescription(FieldType.ORGANIZATION, ""),
          new FieldDescription(FieldType.ADDRESS, ""),
          new FieldDescription(FieldType.EDITION, ""),
          new FieldDescription(FieldType.MONTH, ""),
          new FieldDescription(FieldType.YEAR, ""),
          new FieldDescription(FieldType.NOTE, "")
        }
      ),

      new EntryDescription(EntryType.MASTERSTHESIS,
        new FieldDescription[] {
          new FieldDescription(FieldType.AUTHOR, ""),
          new FieldDescription(FieldType.TITLE, ""),
          new FieldDescription(FieldType.SCHOOL, ""),
          new FieldDescription(FieldType.YEAR, "")
        },
        new FieldDescription[] {
          new FieldDescription(FieldType.TYPE, ""),
          new FieldDescription(FieldType.ADDRESS, ""),
          new FieldDescription(FieldType.MONTH, ""),
          new FieldDescription(FieldType.NOTE, "")
        }
      ),

      new EntryDescription(EntryType.MISC,
        new FieldDescription[] {
        },
        new FieldDescription[] {
          new FieldDescription(FieldType.AUTHOR, ""),
          new FieldDescription(FieldType.TITLE, ""),
          new FieldDescription(FieldType.HOWPUBLISHED, ""),
          new FieldDescription(FieldType.MONTH, ""),
          new FieldDescription(FieldType.YEAR, ""),
          new FieldDescription(FieldType.NOTE, "")
        }
      ),

      new EntryDescription(EntryType.PHDTHESIS,
        new FieldDescription[] {
          new FieldDescription(FieldType.AUTHOR, ""),
          new FieldDescription(FieldType.TITLE, ""),
          new FieldDescription(FieldType.SCHOOL, ""),
          new FieldDescription(FieldType.YEAR, "")
        },
        new FieldDescription[] {
          new FieldDescription(FieldType.TYPE, ""),
          new FieldDescription(FieldType.ADDRESS, ""),
          new FieldDescription(FieldType.MONTH, ""),
          new FieldDescription(FieldType.NOTE, "")
        }
      ),

      new EntryDescription(EntryType.PROCEEDINGS,
        new FieldDescription[] {
          new FieldDescription(FieldType.TITLE, ""),
          new FieldDescription(FieldType.YEAR, "")
        },
        new FieldDescription[] {
          new FieldDescription(FieldType.EDITOR, ""),
          new FieldDescription(FieldType.VOLUME, ""),
          new FieldDescription(FieldType.NUMBER, ""),
          new FieldDescription(FieldType.SERIES, ""),
          new FieldDescription(FieldType.ADDRESS, ""),
          new FieldDescription(FieldType.PUBLISHER, ""),
          new FieldDescription(FieldType.NOTE, ""),
          new FieldDescription(FieldType.MONTH, ""),
          new FieldDescription(FieldType.ORGANIZATION, "")
        }
      ),

      new EntryDescription(EntryType.TECHREPORT,
        new FieldDescription[] {
          new FieldDescription(FieldType.AUTHOR, ""),
          new FieldDescription(FieldType.TITLE, ""),
          new FieldDescription(FieldType.INSTITUTION, ""),
          new FieldDescription(FieldType.YEAR, "")
        },
        new FieldDescription[] {
          new FieldDescription(FieldType.TYPE, ""),
          new FieldDescription(FieldType.NUMBER, ""),
          new FieldDescription(FieldType.ADDRESS, ""),
          new FieldDescription(FieldType.MONTH, ""),
          new FieldDescription(FieldType.NOTE, "")
        }
      ),

      new EntryDescription(EntryType.UNPUBLISHED,
        new FieldDescription[] {
          new FieldDescription(FieldType.AUTHOR, ""),
          new FieldDescription(FieldType.TITLE, ""),
          new FieldDescription(FieldType.NOTE, "")
        },
        new FieldDescription[] {
          new FieldDescription(FieldType.MONTH, ""),
          new FieldDescription(FieldType.YEAR, "")
        }
      )
    };
  }
}
