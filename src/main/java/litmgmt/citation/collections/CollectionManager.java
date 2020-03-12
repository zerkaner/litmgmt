package litmgmt.citation.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import litmgmt.users.User;


/** Manages CRUD access on collections and entries. */
public class CollectionManager {

  private Map<Integer, Collection> _collections;  // Mapping of all collections from ID to data structure.


  /** Create a new collection manager.
   * @param userAuth User authenticator, used to validate user tokens. */
  public CollectionManager() {
    _collections = new HashMap<Integer, Collection>();
  }


  /** Get all collections of a user.
   * @param user The user that did the query.
   * @return List of collections. May be empty. */
  public List<Collection> getAllCollections(User user) {
    var colIds = user.GetAllCollections();
    var list = new ArrayList<Collection>();
    for (var colId : colIds) {
      if (_collections.containsKey(colId)) list.add(_collections.get(colId));
      else System.err.println("[CollectionManager] Warning: Tried to access collection '"+colId+"', not found!");
    }
    return list;
  }


  /** Get one specific collection.
   * @param colId Identifier of the queried collection.
   * @return The collection or 'null' if it was not found. */
  public Collection getCollection(int colId) {
    if (_collections.containsKey(colId)) return _collections.get(colId);
    return null;
  }


  /** Create a new collection.
   * @param user The user that did the query.
   * @param colName Name of the collection to create. Must be unique per user!
   * @return The freshly created collection or 'null' on failure. */
  public Collection createCollection(User user, String colName) {
    for (var existingCol : getAllCollections(user)) {
      if (existingCol.getName().equals(colName)) return null;
    }
    var newCol = new Collection(colName);
    user.addCollection(newCol.getId());
    _collections.put(newCol.getId(), newCol);
    return newCol;
  }


  /** Delete a collection.
   * @param user The user that did the query.
   * @param col Reference to the collection to delete.
   * @return Success flag telling whether the deletion succeeded or not. */
  public void deleteCollection(User user, Collection col) {
    if (_collections.containsKey(col.getId())) {
      _collections.remove(col.getId());
    }
    user.removeCollection(col.getId());
  }


  /** Rename an existing collection.
   * @param user The user that did the query.
   * @param col Reference to the collection to rename.
   * @param newColName Desired name for the collection.
   * @return Success flag telling whether the deletion succeeded or not. */
  public boolean renameCollection(User user, Collection col, String newColName) {
    for (var existingCol : getAllCollections(user)) {
      if (existingCol.getName().equals(newColName)) return false;
    }
    col.rename(newColName);
    return true;
  }


  /** Check if the given user has access to a collection.
   * @param user The user of the query.
   * @param colId Collection identifier.
   * @return Set to 'true' if the collection belongs to the user, 'false' if not. */
  public boolean userOwnsCollection(User user, int colId) {
    return user.GetAllCollections().contains(colId);
  }


  // __________________________________________________________________________
  // GET and SET methods for file storage.

  /** Set the collection list.
   * @param collections List of collections. */
  public void setCollectionList(List<Collection> collections) {
    _collections.clear();
    for (var collection : collections) {
      _collections.put(collection.getId(), collection);
    }
  }


  /** Get the collection list.
   * @return List of collections. */
  public List<Collection> getCollectionList() {
    var list = new ArrayList<Collection>(_collections.values());
    return list;
  }
}
