package litmgmt.users;


/** Exception to be thrown if user e-mail address is invalid. */
public class UserEmailFormatException extends Exception {

  private static final long serialVersionUID = 1L;

  public UserEmailFormatException(String eMail) {
    super("Error: User e-mail address '"+eMail+"' is of invalid format!");
  }
}
