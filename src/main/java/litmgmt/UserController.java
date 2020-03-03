package litmgmt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.BadRequestResponse;


public class UserController {

    private class User {   
        public int id;
        public String name;
        public String eMail;
        public UserDetails userDetails;
    }

    private class UserDetails {
        public LocalDate dateOfBirth;
        public int salary;
    }  
    
        

    private List<User> _users;
    private static int _idCnt = 0;


    public UserController() {
        _users = new ArrayList<User>();
        createTestUser("John", "john@fake.co", "1964-02-21", 2353);
        createTestUser("Mary", "mary@fake.co", "1985-05-13", 2462);
        createTestUser("Dave", "dave@fake.co", "1993-04-24", 2155);
    }


    private void createTestUser(String name, String eMail, String birthday, int salary) {
        var user = new User();
        user.id = _idCnt++;
        user.name = name;
        user.eMail = eMail;
        user.userDetails = new UserDetails();
        user.userDetails.dateOfBirth = LocalDate.parse(birthday);
        user.userDetails.salary = salary;
        _users.add(user);
    }



    public Handler getAll = ctx -> {
        
        var reducedList = new ArrayList<User>();
        for (User user : _users) {
            reducedList.add(new User() {{
                id = user.id;
                name = user.name;
                eMail = user.eMail;
            }});
        }
        ctx.json(reducedList);
    };


    public void getOne(Context ctx) {
        try {
            int queryId = Integer.parseInt(ctx.pathParam("user-id"));
            for (User user : _users) {
                if (user.id == queryId) {
                    ctx.json(user/*.getInsensitiveInformation()*/);
                    return;
                }
            }            
            throw new NotFoundResponse();
        }
        catch (Exception ex) {
            throw new BadRequestResponse();
        }
    }
}
