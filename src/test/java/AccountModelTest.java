/**
 * Created by Егор on 10.10.2018.
 */
import static org.junit.jupiter.api.Assertions.assertEquals;
public class AccountModelTest {
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
    public void getUserNames() throws Exception {
        AccountModel model = new AccountModel();
        model.GetUserNames(0);
        assertEquals(true,model.queryStatus);
    }

    @org.junit.Test
    public void getAllRoles() throws Exception {
        AccountModel model = new AccountModel();
        model.GetAllRoles();
        assertEquals(true,model.queryStatus);
    }

    @org.junit.Test
    public void insertUser() throws Exception {
        AccountModel model = new AccountModel();
        assertEquals(true,model.InsertUser("Egor","Sidorov","1"));
    }

}