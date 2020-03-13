package litmgmt;

import java.nio.file.Paths;
import litmgmt.citation.collections.CollectionManager;
import litmgmt.persistency.SaveFileReader;
import litmgmt.rest.JavalinServer;
import litmgmt.users.UserAuthenticator;


/** REST-style Javalin server for the literature management web app. */
public class App {

  public static void main(final String[] args) {

    var userAuth = new UserAuthenticator();
    var colMgr = new CollectionManager();
    var server = new JavalinServer(userAuth, colMgr, 7000);

    // Restore the previous program state and add hook for save on ordered shutdown.
    var saveFile = Paths.get(System.getProperty("user.dir"), "savefile.json").toString();
    var persistency = new SaveFileReader(saveFile, userAuth, colMgr);
    persistency.readFromFile();
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run()  {
        server.stop();
        persistency.saveToFile();
      }
    });

    // Start the server!
    server.start();
  }
}
