package litmgmt.rest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import litmgmt.citation.CollectionManager;
import litmgmt.users.UserAuthenticator;
import litmgmt.users.UserEmailFormatException;
import litmgmt.users.UserNameAlreadyInUseException;


/** The Javalin web server. */
public class JavalinServer {

  private UserAuthenticator _userAuth; // User authenticator for login and register endpoints.
  private CollectionManager _colMgr;   // Collection manager for CRUD operations on collections and entries.
  private Javalin _server;             // Javalin web server process.
  private int _port;                   // Web server port.


  /** Create a new web server.
   * @param userAuth User authenticator for login and register endpoints.
   * @param colMgr Collection manager for CRUD operations on collections and entries.
   * @param port Web server port. */
  public JavalinServer(UserAuthenticator userAuth, CollectionManager colMgr, int port) {
    _userAuth = userAuth;
    _colMgr = colMgr;
    _port = port;
    _server = Javalin.create(
      config -> {
        config.enableWebjars();
        config.enableCorsForAllOrigins();
        config.defaultContentType = "application/json";
      }
    );
    defineRoutes();
  }


  /** Start the web server. */
  public void start() {
    _server.start(_port);
  }


  /** Stop the web server. */
  public void stop() {
    _server.stop();
  }


  /** Define backend routes (REST endpoints). */
  private void defineRoutes() {
    _server.get("/", ctx -> ctx.result("Hello World"));

    // curl -X POST -H "Content-Type: application/json" -d '{"name":"john", \
    //   "password":"wayne", "email":"john.wayne@web.de"}' http://localhost:7000/api/register
    _server.post("/api/register", ctx -> {
      try {
        var jsonNode = new ObjectMapper().readTree(ctx.body());
        var userName = jsonNode.get("name").asText();
        var registrationSuccessful = _userAuth.createNewUser(
          userName, jsonNode.get("email").asText(), jsonNode.get("password").asText()
        );
        if (registrationSuccessful) {
          System.out.println("User '"+userName+"' successfully created.");
          ctx.res.setStatus(201);
        }
        else ctx.res.setStatus(500);
      }
      catch (NullPointerException | JsonParseException ex) {
        System.err.println("[/api/register] Failed to parse JSON '"+ctx.body()+"'!");
        ctx.res.sendError(400, "JSON parser failure. Check input!");
      }
      catch (UserEmailFormatException ex) {
        ctx.res.sendError(400, ex.toString());
      }
      catch (UserNameAlreadyInUseException ex) {
        ctx.res.sendError(409, ex.toString());
      }
    });

    // curl -X POST -H "Content-Type: application/json" -d '{"name":"john", \
    //   "password":"wayne"}' http://localhost:7000/api/login
    _server.post("/api/login", ctx -> {
      try {
        var jsonNode = new ObjectMapper().readTree(ctx.body());
        var userName = jsonNode.get("name").asText();
        var token = _userAuth.logIn(userName, jsonNode.get("password").asText());
        if (token != null) {
          System.out.println("User '"+userName+"' logged in. Token: '"+token+"'");
          ctx.result(token);
        }
        else {
          System.out.println("User '"+userName+"' failed to log in!");
          ctx.res.sendError(403);
        }
      }
      catch (JsonParseException ex) {
        System.err.println("[/api/login] Failed to parse JSON '"+ctx.body()+"'!");
        ctx.res.sendError(400, "JSON parser failure. Check input!");
      }
    });


    // _server.get("/api/collections", ctx -> ctx.result("[]"));
    // _server.post("/api/collections", ctx -> ctx.result("[]"));
    // _server.get("/api/collections/:col-id", ctx -> userController.getOne(ctx));
    // _server.get("/api/users", userController.getAll);
  }
}
