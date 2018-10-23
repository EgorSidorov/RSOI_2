import org.junit.Assert;
import org.junit.Test;
import org.junit.BeforeClass;
import java.util.List;

public class AccountModelTest {

    @BeforeClass
    public static void BeforeTest(){
        Startup.InitializeDB();
        AccountModel model = new AccountModel();
        model.CreateUser("Egor","qwerty","1");
        model.CreateUser("Egor2","qwerty","1");
        model.CreateUser("Egor3","qwerty","1");
        model.CreateUser("Egor4","qwerty","1");
        model.CreateUser("Egor5","qwerty","1");
        model.CreateUser("Egor6","qwerty","1");
        if(!model.GetAllRoles().contains("master")) {
            model.AddRole(1,"master");
            model.AddRole(2,"medium");
            model.AddRole(3,"beginner");
        }
    }

    @Test
    public void getDbStatus() throws Exception {
        AccountModel model = new AccountModel();
        Assert.assertEquals(true,model.GetDbStatus());
    }

    @Test
    public void createConnection() throws Exception {
        AccountModel model = new AccountModel();
        Assert.assertEquals(true,model.CreateConnection());
    }

    @Test
    public void getAllRoles() throws Exception {
        AccountModel model = new AccountModel();
        model.GetAllRoles();
        Assert.assertEquals(true,model.queryStatus);
    }

    @Test
    public void CreateUser() throws Exception {
        AccountModel model = new AccountModel();
        Assert.assertEquals(false,model.CreateUser("Egor","qwerty","1"));
    }

    @Test
    public void Login() throws Exception {
        AccountModel model = new AccountModel();
        String token = model.Login("Egor2","qwerty");
        Assert.assertEquals(true,model.queryStatus);
        Assert.assertEquals(false,token.isEmpty());
    }

    @Test
    public void getUserNames() throws Exception {
        AccountModel model = new AccountModel();
        String token = model.Login("Egor3","qwerty");
        List<String> listString = model.GetUserNames(token,0);
        Assert.assertEquals(true,model.GetQueryStatus());
        Assert.assertEquals("Egor3",model.GetUsername(token));
        Assert.assertEquals(true,model.GetQueryStatus());
    }

    @Test
    public void Logout() throws Exception {
        AccountModel model = new AccountModel();
        String token = model.Login("Egor4","qwerty");
        Assert.assertEquals(true,model.Logout(token));
    }

    @Test
    public void GetUsername() throws Exception {
        AccountModel model = new AccountModel();
        String token = model.Login("Egor5","qwerty");
        Assert.assertEquals("Egor5",model.GetUsername(token));
    }

    @Test
    public void GetRole() throws Exception {
        AccountModel model = new AccountModel();
        String token = model.Login("Egor6","qwerty");
        Assert.assertEquals("master",model.GetRole(token));
    }

}
