package litmgmt.citation;

import java.util.ArrayList;
import java.util.List;
import litmgmt.users.UserAuthenticator;


/** Manages CRUD access on collections and their entries. */
public class CollectionManager {

  private UserAuthenticator _userAuth; // User authenticator, used to validate user tokens.
  private List<Collection> _collections;
  private List<Entry> _entries;


  /** Create a new collection manager.
   * @param userAuth User authenticator, used to validate user tokens. */
  public CollectionManager(UserAuthenticator userAuth) {
    _userAuth = userAuth;
    _collections = new ArrayList<Collection>();
    _entries = new ArrayList<Entry>();
  }


  /** Get all collections of a user.
   * @param userToken Token of the user that did the query.
   * @return List of colelctions or 'null' if the token is invalid. */
  public List<Collection> getCollections(String userToken) {
    var user = _userAuth.validateUserToken(userToken);
    if (user == null) return null;
    return new ArrayList<Collection>();// user.GetAllCollections();
  }


    /** Create a new collection.
   * @param colName Name of the collection to create. Must be unique per user!
   * @return The freshly created collection or 'null' on failure. */
  public Collection createNewCollection(String colName) {
    /*
    for (var collection : _collections) {
      if (collection.getName().equals(colName)) return null;
    }
    var newCollection = new Collection(colName);
    _collections.add(newCollection);
    return newCollection;
    */
    return null;
  }


  // __________________________________________________________________________
  // GET and SET methods.

  /** Set the collection list.
   * @param collections List of collections. */
  public void setCollectionList(List<Collection> collections) {
    _collections = collections;
  }


  /** Get the collection list.
   * @return List of collections. */
  public List<Collection> getCollectionList() {
    return _collections;
  }


  /** Set the entry list.
   * @param entries List of entries. */
  public void setEntryList(List<Entry> entries) {
    _entries = entries;
  }


  /** Get the entry list.
   * @return List of entries. */
  public List<Entry> getEntryList() {
    return _entries;
  }
}
