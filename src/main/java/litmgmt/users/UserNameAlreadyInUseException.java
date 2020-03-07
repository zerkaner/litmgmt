package litmgmt.users;


/** Exception to be thrown if user name is already in use. */
public class UserNameAlreadyInUseException extends Exception {

  private static final long serialVersionUID = 1L;

  public UserNameAlreadyInUseException(String name) {
    super("Error: User name '"+name+"' is already in use! Please choose another name.");
  }
}
