package litmgmt;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.vue.VueComponent;

/** REST-Endpunkt mit Javalin und Vue.js. */
public class App 
{
    public static void main( final String[] args)
    {
        System.out.println( "Hello World 2!" );
        Javalin app = Javalin.create(
            config -> {
                config.enableWebjars();
                config.enableCorsForAllOrigins();
                config.defaultContentType = "application/json";
            }
        ).start(7000);
        //app.get("/", ctx -> ctx.result("Hello World 2"));
        app.get("/", new VueComponent("<hello-world></hello-world>"));
        app.get("/users", new VueComponent("<user-overview></user-overview>"));
        app.get("/users/:user-id", new VueComponent("<user-profile></user-profile>"));


        UserController userController = new UserController();
        app.get("/api/users", userController.getAll);
        app.get("/api/users/:user-id", ctx -> userController.getOne(ctx));
        
    }
}


// https://www.baeldung.com/javalin-rest-microservices
