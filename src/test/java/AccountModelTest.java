import org.junit.Assert;
import org.junit.Test;
import org.junit.BeforeClass;
import java.util.List;

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

    @Test
    public void getDbStatus() throws Exception {
        AccountModel model = new AccountModel();
        assertEquals(true,model.GetDbStatus());
    }

    @Test
    public void createConnection() throws Exception {
        AccountModel model = new AccountModel();
        assertEquals(true,model.CreateConnection());
    }

    @Test
    public void getAllRoles() throws Exception {
        AccountModel model = new AccountModel();
        model.GetAllRoles();
        assertEquals(true,model.queryStatus);
    }

    @Test
    public void CreateUser() throws Exception {
        AccountModel model = new AccountModel();
        assertEquals(false,model.CreateUser("Egor","qwerty","1"));
    }

    @Test
    public void Login() throws Exception {
        AccountModel model = new AccountModel();
        String token = model.Login("Egor2","qwerty");
        assertEquals(true,model.queryStatus);
        assertEquals(false,token.isEmpty());
    }

    @Test
    public void getUserNames() throws Exception {
        AccountModel model = new AccountModel();
        String token = model.Login("Egor3","qwerty");
        List<String> listString = model.GetUserNames(token,0);
        assertEquals(true,model.GetQueryStatus());
        assertEquals("Egor3",model.GetUsername(token));
        assertEquals(true,model.GetQueryStatus());
    }

    @Test
    public void Logout() throws Exception {
        AccountModel model = new AccountModel();
        String token = model.Login("Egor4","qwerty");
        assertEquals(true,model.Logout(token));
    }

    @Test
    public void GetUsername() throws Exception {
        AccountModel model = new AccountModel();
        String token = model.Login("Egor5","qwerty");
        assertEquals("Egor5",model.GetUsername(token));
    }

    @Test
    public void GetRole() throws Exception {
        AccountModel model = new AccountModel();
        String token = model.Login("Egor6","qwerty");
        assertEquals("master",model.GetRole(token));
    }

}
