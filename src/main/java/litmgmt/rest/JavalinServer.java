package litmgmt.rest;

import java.io.IOException;
import java.nio.file.Paths;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.staticfiles.Location;
import litmgmt.citation.collections.Collection;
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
   * @param port Web server port.
   * @param htmlDir HTML directory for static content delivery. May be set to 'null' to disable it! */
  public JavalinServer(UserAuthenticator userAuth, CollectionManager colMgr, int port, String htmlDir) {
    _userAuth = userAuth;
    _colMgr = colMgr;
    _port = port;
    _server = Javalin.create(
      config -> {
        config.enableWebjars();
        config.enableCorsForAllOrigins();
        if (htmlDir != null) {
          var staticPath = Paths.get(System.getProperty("user.dir"), htmlDir).toString();
          config.addStaticFiles(staticPath, Location.EXTERNAL);
        }
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
    // | curl -X POST -H "Content-Type: application/json" -d '{"name":"john",
    // |   "password":"wayne", "email":"john.wayne@web.de"}' http://localhost/api/register
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
    // | curl -X POST -H "Content-Type: application/json" -d '{"name":"john",
    // |   "password":"wayne"}' http://localhost/api/login
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
    // | curl http://localhost/api/entrydescriptions
    _server.get("/api/entrydescriptions", ctx -> {
      ctx.json(DescriptionList.getEntryDescriptions());
    });


    // Fetch all collections for a user.
    // | curl -H "Accept: application/json" -H "Authorization: Bearer <token>"
    // |   http://localhost/api/collections
    _server.get("/api/collections", ctx -> {
      var user = checkLogin(ctx);
      if (user != null) ctx.json(_colMgr.getAllCollections(user));
    });


    // Create a new collection.
    // | curl -X POST -H "Accept: application/json" -H "Authorization: Bearer <token>"
    // |   -d '{"name":"col01"}' http://localhost/api/collections
    _server.post("/api/collections", ctx -> {
      var user = checkLogin(ctx);
      if (user != null) {
        try {
          var jsonNode = new ObjectMapper().readTree(ctx.body());
          var colName = jsonNode.get("name").asText();
          var newCol = _colMgr.createCollection(user, colName);
          if (newCol != null) {
            ctx.status(201);
            ctx.json(newCol);
          }
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


    // Fetch a single collection for a user.
    // | curl -H "Accept: application/json" -H "Authorization: Bearer <token>"
    // |   http://localhost/api/collections/0
    _server.get("/api/collections/:col-id", ctx -> {
      var user = checkLogin(ctx);
      var collection = fetchCollectionFromQuery(ctx, user, "Get collection");
      if (collection != null) ctx.json(collection);
    });


    // Delete a collection.
    // | curl -X DELETE -H "Accept: application/json" -H "Authorization: Bearer <token>"
    // |   http://localhost/api/collections/0
    _server.delete("/api/collections/:col-id", ctx -> {
      var user = checkLogin(ctx);
      var collection = fetchCollectionFromQuery(ctx, user, "Delete collection");
      if (collection != null) {
        _colMgr.deleteCollection(user, collection);
        ctx.res.setStatus(200);
      }
    });


    // Rename a collection.
    // | curl -X PUT -H "Accept: application/json" -H "Authorization: Bearer <token>"
    // |   -d '{"name":"New collection name"}' http://localhost/api/collections/0
    _server.put("/api/collections/:col-id", ctx -> {
      var user = checkLogin(ctx);
      var collection = fetchCollectionFromQuery(ctx, user, "Rename collection");
      if (collection != null) {
        try {
          var jsonNode = new ObjectMapper().readTree(ctx.body());
          var newColName = jsonNode.get("name").asText();
          var success = _colMgr.renameCollection(user, collection, newColName);
          if (success) ctx.json(collection);
          else {
            var msg = "409 Conflict - Collection with name '"+newColName+"' already exists!";
            System.err.println("[JavalinServer] Rename collection: "+msg);
            ctx.res.sendError(409, msg);
          }
        }
        catch (NullPointerException | JsonParseException ex) {
          System.err.println("[JavalinServer] Rename collection: Failed to parse JSON '"+ctx.body()+"'!");
          ctx.res.sendError(400, "JSON parser failure. Check input!");
        }
      }
    });


    // Get all entries of a collection.
    // | curl -H "Accept: application/json" -H "Authorization: Bearer <token>"
    // |   http://localhost/api/collections/0/entries
    _server.get("/api/collections/:col-id/entries", ctx -> {
      var user = checkLogin(ctx);
      var collection = fetchCollectionFromQuery(ctx, user, "Get all entries");
      if (collection != null) ctx.json(collection.getEntries());
    });


    // Create a new entry in a collection.
    // | curl -X POST -H "Accept: application/json" -H "Authorization: Bearer <token>"
    // |   -d '{"citeKey":"myCiteRef2018", "entryType":"article", "fields":[
    // |     {"fieldType": "author", "value":"Jim Raynor"}, {"fieldType":"address", "value":"Tarsonis"}]}'
    // |   http://localhost/api/collections/0/entries
    _server.post("/api/collections/:col-id/entries", ctx -> {
      var user = checkLogin(ctx);
      var collection = fetchCollectionFromQuery(ctx, user, "Create entry");
      if (collection != null) {
        try {
          var jsonNode = new ObjectMapper().readTree(ctx.body());
          var citeKey = jsonNode.get("citeKey").asText();
          var entryType = EntryType.valueOf(jsonNode.get("entryType").asText().toUpperCase());
          var entry = collection.createEntry(citeKey, entryType);
          if (entry != null) {
            if (jsonNode.get("fields") != null) {
              for (JsonNode field : jsonNode.get("fields")) {
                entry.setField(
                  FieldType.valueOf(field.get("fieldType").asText().toUpperCase()),
                  field.get("value").asText()
                );
              }
            }
            ctx.status(201);
            ctx.json(entry);
          }
          else {
            var msg = "409 Conflict - Entry with cite key '"+citeKey+"' already exists!";
            ctx.res.sendError(409, msg);
            System.err.println("[JavalinServer] Create entry: "+msg);
          }
        }
        catch (Exception ex) {
          System.err.println("[JavalinServer] Create entry: Failed to parse JSON '"+ctx.body()+"'!");
          ctx.res.sendError(400, "JSON parser failure. Check input!");
        }
      }
    });


    // Get a single entry on a collection.
    // | curl -H "Accept: application/json" -H "Authorization: Bearer <token>"
    // |   http://localhost/api/collections/0/entries/2
    _server.get("/api/collections/:col-id/entries/:entry-id", ctx -> {
      var user = checkLogin(ctx);
      var entry = fetchEntryFromQuery(ctx, user, "Get entry");
      if (entry != null) ctx.json(entry);
    });


    // Delete an entry from a collection.
    // | curl -X DELETE -H "Accept: application/json" -H "Authorization: Bearer <token>"
    // |   http://localhost/api/collections/0/entries/2
    _server.delete("/api/collections/:col-id/entries/:entry-id", ctx -> {
      var user = checkLogin(ctx);
      var collection = fetchCollectionFromQuery(ctx, user, "Delete entry");
      if (collection != null) {
        var entry = fetchEntryFromQuery(ctx, user, "Delete entry");
        collection.deleteEntry(entry);
        ctx.res.setStatus(200);
      }
    });


    // Update an entry in a collection.
    // | curl -X PUT -H "Accept: application/json" -H "Authorization: Bearer <token>"
    // |   -d '{"citeKey":"myCiteRef2018", "entryType":"article", "fields":[
    // |     {"fieldType": "author", "value":"Tychus Findlay"}, {"fieldType":"address", "value":"Tarsonis"}]}'
    // |   http://localhost/api/collections/0/entries/2
    _server.put("/api/collections/:col-id/entries/:entry-id", ctx -> {
      var user = checkLogin(ctx);
      var collection = fetchCollectionFromQuery(ctx, user, "Update entry");
      if (collection != null) {
        var entry = fetchEntryFromQuery(ctx, user, "Update entry");
        if (entry != null) {
          try {
            var jsonNode = new ObjectMapper().readTree(ctx.body());

            // Check for renaming.
            if (jsonNode.get("citeKey") != null) {
              var citeKey = jsonNode.get("citeKey").asText();
              if (!citeKey.equals(entry.getCiteKey())) {
                var success = collection.renameEntry(entry, citeKey);
                if (!success) {
                  var msg = "409 Conflict - Entry with name '"+citeKey+"' already exists in collection #"+
                    collection.getId()+"!";
                  System.err.println("[JavalinServer] Rename entry: "+msg);
                  ctx.res.sendError(409, msg);
                  return;
                }
              }
            }

            // Check for entry type change --> not allowed!
            if (jsonNode.get("entryType") != null) {
              var entryType = jsonNode.get("entryType").asText();
              if (!entryType.equals(entry.getEntryType())) {
                var msg = "405 Not Allowed - Entry type cannot be changed!";
              System.err.println("[JavalinServer] Change entry type: "+msg);
              ctx.res.sendError(405, msg);
              return;
              }
            }

            // If fields are set, iterate and update them.
            if (jsonNode.get("fields") != null) {
              for (JsonNode field : jsonNode.get("fields")) {
                entry.setField(
                  FieldType.valueOf(field.get("fieldType").asText().toUpperCase()),
                  field.get("value").asText()
                );
              }
            }
            ctx.json(entry);
          }
          catch (Exception ex) {
            System.err.println("[JavalinServer] Update entry: Failed to parse JSON '"+ctx.body()+"'!");
            ctx.res.sendError(400, "JSON parser failure. Check input!");
          }
        }
      }
    });


    // Testing endpoint to run debugging code.
    // | curl http://localhost/api/debug
    _server.get("/api/debug", ctx -> {
      var entry = new Entry("citeKey", EntryType.ARTICLE);
      entry.setField(FieldType.ADDRESS, "some road");
      entry.setField(FieldType.AUTHOR, "hodor");
      ctx.json(entry);
    });
  }


  // ----------------------------------------------------------------------------------------------


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


  /** Try to parse a collection from the URL and retrieve it. This also performs user access validation.
   * @param ctx The web server context.
   * @param user The user that did the query.
   * @param endpointDesc Optional description of endpoint for console error message. May be null!
   * @return The collection or 'null', if access failed (unauthorized or not found). */
  private Collection fetchCollectionFromQuery(Context ctx, User user, String endpointDesc) {
    if (user != null) {
      try {
        try {
          var colId = Integer.parseInt(ctx.pathParam("col-id"));
          if (_colMgr.userOwnsCollection(user, colId)) {
            var col = _colMgr.getCollection(colId);
            if (col != null) return col;
            else {
              var msg = "404 - Collection #"+colId+" not found!";
              if (endpointDesc != null) System.err.println("[JavalinServer] "+endpointDesc+": "+msg);
              ctx.res.sendError(404, msg);
            }
          }
          else {
            var msg = "403 Forbidden - User '"+user.getName()+"' does not own collection #"+colId+"!";
            if (endpointDesc != null) System.err.println("[JavalinServer] "+endpointDesc+": "+msg);
            ctx.res.sendError(403, msg);
          }
        }
        catch (NumberFormatException ex) {
          var msg = "400 Bad Request - Failed to parse collection identifier!";
          if (endpointDesc != null) System.err.println("[JavalinServer] "+endpointDesc+": "+msg);
          ctx.res.sendError(400, msg);
        }
      }
      catch (IOException ex) {
        System.err.println("[JavalinServer] fetchCollectionFromQuery() IOException: "+ex);
      }
    }
    return null;
  }


  /** Try to parse an entry from URL paramters and retrieve it.
   * This function also chains the user access validation to the underlying collection!
   * @param ctx The web server context.
   * @param user The user that did the query.
   * @param endpointDesc Optional description of endpoint for console error message. May be null!
   * @return The entry or 'null', if access failed (unauthorized or not found). */
  private Entry fetchEntryFromQuery(Context ctx, User user, String endpointDesc) {
    var collection = fetchCollectionFromQuery(ctx, user, endpointDesc);
    if (collection != null) {
      try {
        try {
          var entryId = Integer.parseInt(ctx.pathParam("entry-id"));
          for (var entry : collection.getEntries()) {
            if (entry.getId() == entryId) return entry;
          }
        }
        catch (NumberFormatException ex) {
          var msg = "400 Bad Request - Failed to parse entry identifier!";
          if (endpointDesc != null) System.err.println("[JavalinServer] "+endpointDesc+": "+msg);
          ctx.res.sendError(400, msg);
        }
      }
      catch (IOException ex) {
        System.err.println("[JavalinServer] fetchEntryFromQuery() IOException: "+ex);
      }
    }
    return null;
  }
}

// REST Tutorial: https://www.baeldung.com/javalin-rest-microservices
