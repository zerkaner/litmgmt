package litmgmt.users;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.HashMap;


/** This class performs user authentication, log in, log out and registration actions. */
public class UserAuthenticator {

  private List<User> _users;                // List of all users known to the system.
  private Map<String, User> _loggedInUsers; // Mapping of access tokens to the currently logged in users.


  /** Create the user authenticator. */
  public UserAuthenticator() {
    _users = new ArrayList<User>();
    _loggedInUsers = new HashMap<String, User>();
  }


  /** Create a new user account.
   * @param userName Name of the user.
   * @param eMail The user's e-mail address
   * @param password User password.
   * @return Success flag. Set to 'true' if registration succeeded.
   * @throws UserNameAlreadyInUseException Thrown if the entered name is alredy used by another user.
   * @throws UserEmailFormatException Thrown if the entered e-mail dows not comply with the e-mail syntax. */
  public boolean createNewUser(String userName, String eMail, String password)
    throws UserNameAlreadyInUseException, UserEmailFormatException {
    if (userName == null || userName.isEmpty() || eMail == null ||
      eMail.isEmpty() || password == null || password.isEmpty()) return false;
    userName = userName.toLowerCase();
    for (var user : _users) {
      if (user.getName().equals(userName)) {
        throw new UserNameAlreadyInUseException(userName);
      }
    }
    if (!isValidMail(eMail)) throw new UserEmailFormatException(eMail);
    var newUser = new User(userName, eMail, password);
    _users.add(newUser);
    return true;
  }


  /** Log in a user.
   * @param userName Name of the user to log in.
   * @param password The user's password.
   * @return User token if login succeeded or 'null' on failure. */
  public String logIn(String userName, String password) {
    if (userName == null || userName.isEmpty() || password == null || password.isEmpty()) return null;
    userName = userName.toLowerCase();
    for(var token : _loggedInUsers.keySet()) {
      if (_loggedInUsers.get(token).getName().equals(userName)) {
        return token; // In case the user is already logged in, return their current token.
      }
    }
    for (var user : _users) {
      if (user.getName().equals(userName)) {
        var token = user.loginUser(password);
        if (token != null) _loggedInUsers.put(token, user);
        return token;
      }
    }
    return null;
  }


  /** Log out an active user.
   * @param userToken Token of the user to log out. */
  public void logOut(String userToken) {
    if (userToken == null || userToken.isEmpty()) return;
    if (_loggedInUsers.containsKey(userToken)) {
      _loggedInUsers.remove(userToken);
    }
  }


  /** Check a user token for validity.
   * @param userToken The user token to check.
   * @return The corresponding user or 'null' if authentication failed. */
  public User validateUserToken(String userToken) {
    if (userToken == null || userToken.isEmpty()) return null;
    if (_loggedInUsers.containsKey(userToken)) {
      return _loggedInUsers.get(userToken);
    }
    return null;
  }


  /** Check if an e-mail is valid.
   * @param eMail E-mail address to check.
   * @return 'True' if mail is valid, otherwise 'false'. */
  private static boolean isValidMail(String eMail) {
    if (eMail == null) return false;
    var pat = Pattern.compile(
      "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
      "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    return pat.matcher(eMail).matches();
  }


  // __________________________________________________________________________
  // GET and SET methods.

  /** Set the user list.
   * @param users List of users. */
  public void setUserList(List<User> users) {
    _users = users;
  }


  /** Get the user list.
   * @return List of users. */
  public List<User> getUserList() {
    return _users;
  }
}
