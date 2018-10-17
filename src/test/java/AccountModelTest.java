/**
 * Created by Егор on 10.10.2018.
 */

import org.junit.BeforeClass;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
public class AccountModelTest {

    @BeforeClass
    public static void BeforeTest(){
        Startup.initializeDB();
        AccountModel model = new AccountModel();
        model.CreateUser("Egor","qwerty","1");
        model.CreateUser("Egor2","qwerty","1");
        model.CreateUser("Egor3","qwerty","1");
        model.CreateUser("Egor4","qwerty","1");
        model.CreateUser("Egor5","qwerty","1");
        model.CreateUser("Egor6","qwerty","1");
        if(!model.GetAllRoles().contains("master")) {
            model.AddRole("master");
            model.AddRole("medium");
            model.AddRole("beginner");
        }
    }

    @org.junit.Test
    public void getDbStatus() throws Exception {
        AccountModel model = new AccountModel();
        assertEquals(true,model.GetDbStatus());
    }

    @org.junit.Test
    public void createConnection() throws Exception {
        AccountModel model = new AccountModel();
        assertEquals(true,model.CreateConnection());
    }

    @org.junit.Test
    public void getAllRoles() throws Exception {
        AccountModel model = new AccountModel();
        model.GetAllRoles();
        assertEquals(true,model.queryStatus);
    }

    @org.junit.Test
    public void CreateUser() throws Exception {
        AccountModel model = new AccountModel();
        assertEquals(false,model.CreateUser("Egor","qwerty","1"));
    }

    @org.junit.Test
    public void Login() throws Exception {
        AccountModel model = new AccountModel();
        String token = model.Login("Egor2","qwerty");
        assertEquals(true,model.queryStatus);
        assertEquals(false,token.isEmpty());
    }

    @org.junit.Test
    public void getUserNames() throws Exception {
        AccountModel model = new AccountModel();
        String token = model.Login("Egor3","qwerty");
        List<String> listString = model.GetUserNames(token,0);
        assertEquals(true,model.GetQueryStatus());
        assertEquals("Egor3",model.GetUsername(token));
        assertEquals(true,model.GetQueryStatus());
    }

    @org.junit.Test
    public void Logout() throws Exception {
        AccountModel model = new AccountModel();
        String token = model.Login("Egor4","qwerty");
        assertEquals(true,model.Logout(token));
    }

    @org.junit.Test
    public void GetUsername() throws Exception {
        AccountModel model = new AccountModel();
        String token = model.Login("Egor5","qwerty");
        assertEquals("Egor5",model.GetUsername(token));
    }

    @org.junit.Test
    public void GetRole() throws Exception {
        AccountModel model = new AccountModel();
        String token = model.Login("Egor6","qwerty");
        assertEquals("master",model.GetRole(token));
    }

}