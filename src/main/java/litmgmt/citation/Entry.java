package litmgmt.citation;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;


/** A user is denoted by name, email and password and has 
 * access to a list of collections which in turn keep the citations. */
class User {
    
    private String _name;
    private String _email;
    private String _passwordHash; // MD5 password hash.
    private List<Collection> _collections;
    private MessageDigest _md5;


    public User(String name, String email, String password) {
        _name = name;
        _email = email;
        _collections = new ArrayList<Collection>();
        try {
            _md5 = MessageDigest.getInstance("MD5");
            _md5.update(password.getBytes());
            var bytes = _md5.digest();
            _passwordHash = new String(bytes, StandardCharsets.UTF_8).toUpperCase();
        } catch (Exception ex) { /* */ }
        
    }


    private String getHashForPassword(String password) {
        _md5.update(password.getBytes());
        var bytes = _md5.digest();
        _passwordHash = new String(bytes, StandardCharsets.UTF_8).toUpperCase();
        return "";
    }
}


/** A collection is an aggregation of citation entries. */
class Collection {
    String name;
    List<Entry> entries;
}


/** A citation entry. It has a cite key, a type and a number of data fields. */
class Entry {
    String citeKey;
    EntryType entryType;
    List<Field> fields;
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
