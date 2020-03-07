package litmgmt.persistency;

public class IdHelper {

  private static int _userId, _collectionId, _entryId;


  /** Initialize the ID helper with its last values. */
  public static void initialize(int userId, int collectionId, int entryId) {
    _userId = userId;
    _collectionId = collectionId;
    _entryId = entryId;
  }


  /** Get a fresh user ID.
   * @return An unused user identifier. */
  public static int GetNextUserId() {
    return _userId++;
  }


  /** Get a fresh collection ID.
   * @return An unused collection identifier. */
  public static int GetNextCollectionId() {
    return _collectionId++;
  }


  /** Get a fresh entry ID.
   * @return An unused entry identifier. */
  public static int GetNextEntryId() {
    return _entryId++;
  }
}
