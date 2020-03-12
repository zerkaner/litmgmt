package litmgmt.rest;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import litmgmt.citation.collections.CollectionManager;
import litmgmt.citation.collections.Entry;
import litmgmt.citation.description.DescriptionList;
import litmgmt.citation.description.EntryType;
import litmgmt.citation.description.FieldType;
import litmgmt.users.User;
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

    // Register a new user.
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
        System.err.println("[JavalinServer] Register User: Failed to parse JSON '"+ctx.body()+"'!");
        ctx.res.sendError(400, "JSON parser failure. Check input!");
      }
      catch (UserEmailFormatException ex) {
        ctx.res.sendError(400, ex.toString());
      }
      catch (UserNameAlreadyInUseException ex) {
        ctx.res.sendError(409, ex.toString());
      }
    });


    // Login an existing user.
    // curl -X POST -H "Content-Type: application/json" -d '{"name":"john", \
    //   "password":"wayne"}' http://localhost:7000/api/login
    _server.post("/api/login", ctx -> {
      try {
        var jsonNode = new ObjectMapper().readTree(ctx.body());
        var userName = jsonNode.get("name").asText();
        var token = _userAuth.logIn(userName, jsonNode.get("password").asText());
        if (token != null) {
          System.out.println("[JavalinServer] User '"+userName+"' logged in. Token: '"+token+"'");
          ctx.result(token);
        }
        else {
          System.out.println("[JavalinServer] User '"+userName+"' failed to log in!");
          ctx.res.sendError(403);
        }
      }
      catch (JsonParseException ex) {
        System.err.println("[JavalinServer] Login User: Failed to parse JSON '"+ctx.body()+"'!");
        ctx.res.sendError(400, "JSON parser failure. Check input!");
      }
    });


    // Fetch a list with static entry type descriptions.
    // curl http://localhost:7000/api/entrydescriptions
    _server.get("/api/entrydescriptions", ctx -> {
      ctx.json(DescriptionList.getEntryDescriptions());
    });


    // Fetch all collections for a user.
    // curl -H "Accept: application/json" -H "Authorization: Bearer <token>" \
    //   http://localhost:7000/api/collections
    _server.get("/api/collections", ctx -> {
      var user = checkLogin(ctx);
      if (user != null) {
        ctx.json(_colMgr.getAllCollections(user));
      }
    });


    // Fetch a single collection for a user.
    // curl -H "Accept: application/json" -H "Authorization: Bearer <token>" \
    //   http://localhost:7000/api/collections/0
    _server.get("/api/collections/:col-id", ctx -> {
      var user = checkLogin(ctx);
      var colId = parseCollectionFromQuery(ctx, user, "Read collection");
      if (colId != -1) {
        var collection = _colMgr.getCollection(colId);
        if (collection != null) ctx.json(collection);
        else ctx.res.sendError(404, "404 - Collection #"+colId+" not found!");
      }
    });


    // Create a new collection.
    // curl -X POST -H "Accept: application/json" -H "Authorization: Bearer <token>" \
    //   -d '{"name":"col01"}' http://localhost:7000/api/collections
    _server.post("/api/collections", ctx -> {
      var user = checkLogin(ctx);
      if (user != null) {
        try {
          var jsonNode = new ObjectMapper().readTree(ctx.body());
          var colName = jsonNode.get("name").asText();
          var newCol = _colMgr.createCollection(user, colName);
          if (newCol != null) ctx.json(newCol);
          else {
            var msg = "409 Conflict - Collection with name '"+colName+"' already exists!";
            ctx.res.sendError(409, msg);
            System.err.println("[JavalinServer] Create collection: "+msg);
          }
        }
        catch (NullPointerException | JsonParseException ex) {
          System.err.println("[JavalinServer] Create collection: Failed to parse JSON '"+ctx.body()+"'!");
          ctx.res.sendError(400, "JSON parser failure. Check input!");
        }
      }
    });


    // Delete a collection.
    // curl -X DELETE -H "Accept: application/json" -H "Authorization: Bearer <token>" \
    //   http://localhost:7000/api/collections/0
    _server.delete("/api/collections/:col-id", ctx -> {
      var user = checkLogin(ctx);
      var colId = parseCollectionFromQuery(ctx, user, "Delete collection");
      if (colId != -1) {
        var col = _colMgr.getCollection(colId);
        if (col != null) {
          _colMgr.deleteCollection(user, col);
          ctx.res.setStatus(200);
        }
        else ctx.res.sendError(404, "404 - Collection #"+colId+" not found!");
      }
    });


    // Rename a collection.
    // curl -X PUT -H "Accept: application/json" -H "Authorization: Bearer <token>" \
    //   -d '{"name":"New collectio name"}' http://localhost:7000/api/collections/0
    _server.put("/api/collections/:col-id", ctx -> {
      var user = checkLogin(ctx);
      var colId = parseCollectionFromQuery(ctx, user, "Rename collection");
      if (colId != -1) {
        try {
          var jsonNode = new ObjectMapper().readTree(ctx.body());
          var newColName = jsonNode.get("name").asText();
          var col = _colMgr.getCollection(colId);
          if (col != null) {
            var success = _colMgr.renameCollection(user, col, newColName);
            if (success) ctx.res.setStatus(200);
            else {
              var msg = "409 Conflict - Collection with name '"+newColName+"' already exists!";
              ctx.res.sendError(409, msg);
              System.err.println("[JavalinServer] Rename collection: "+msg);
            }
          }
          else ctx.res.sendError(404, "404 - Collection #"+colId+" not found!");
        }
        catch (NullPointerException | JsonParseException ex) {
          System.err.println("[JavalinServer] Rename collection: Failed to parse JSON '"+ctx.body()+"'!");
          ctx.res.sendError(400, "JSON parser failure. Check input!");
        }
      }
    });


    // Testing endpoint to run debugging code.
    _server.get("/api/debug", ctx -> {
      var entry = new Entry("citeKey", EntryType.ARTICLE);
      entry.setField(FieldType.ADDRESS, "some road");
      entry.setField(FieldType.AUTHOR, "hodor");
      ctx.json(entry);
    });
  }


  /** Helper function to perform a header check for the user bearer token.
   * @param ctx The web server context.
   * @return The user for the token or 'null' if the request was invalid. */
  private User checkLogin(Context ctx) {
    var auth = ctx.req.getHeader("Authorization");
    try {
      if (auth == null) ctx.res.sendError(401, "Unauthorized");
      else {
        var authArr = auth.split(" ");
        if (authArr.length!= 2 || !authArr[0].equals("Bearer")) {
          ctx.res.sendError(400, "Bad request, login malformed!");
        }
        else {
          var user = _userAuth.validateUserToken(authArr[1]);
          if (user == null) ctx.res.sendError(403, "Forbidden");
          else return user;
        }
      }
    }
    catch (IOException ex) {
      System.err.println("[JavalinServer] checkLogin() IOException: "+ex);
    }
    return null;
  }


  /** Try to parse a collection from the URL. This also performs user access validation.
   * @param ctx The web server context.
   * @param user The user that did the query.
   * @param endpointDesc Optional description of endpoint for console error message. May be null!
   * @return The collection identifier or -1 if access failed. */
  private int parseCollectionFromQuery(Context ctx, User user, String endpointDesc) {
    if (user != null) {
      try {
        try {
          var colId = Integer.parseInt(ctx.pathParam("col-id"));
          if (_colMgr.userOwnsCollection(user, colId)) return colId;
          else {
            var msg = "403 Forbidden - User '"+user.getName()+"' does not own collection #"+colId+"!";
            if (endpointDesc != null) System.err.println("[JavalinServer] "+endpointDesc+": "+msg);
            ctx.res.sendError(403, msg);
          }
        }
        catch (NumberFormatException ex) {
          System.err.println("[/api/collections/:col-id] Failed to parse parameter!");
          ctx.res.sendError(400, "Collection identifier could not been parsed!");
        }
      }
      catch (IOException ex) {
        System.err.println("[JavalinServer] parseCollectionFromQuery() IOException: "+ex);
      }
    }
    return -1;
  }
}

// REST Tutorial: https://www.baeldung.com/javalin-rest-microservices
