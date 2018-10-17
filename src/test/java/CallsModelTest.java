import org.junit.Assert;
import org.junit.Test;
import org.junit.BeforeClass;
public class CallsModelTest {

    @BeforeClass
    public static void BeforeTest(){
        CallsModel model = new CallsModel();
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
        CallsModel model = new CallsModel();
        assertEquals(true,model.GetDbStatus());
    }

    @org.junit.Test
    public void createConnection() throws Exception {
        CallsModel model = new CallsModel();
        assertEquals(true,model.CreateConnection());
    }

    @org.junit.Test
    public void GetCallsHistory() throws Exception {
        CallsModel model = new CallsModel();
        //model.GetCallsHistory(0);
        //assertEquals(true,model.queryStatus);
    }

    @org.junit.Test
    public void insertCall() throws Exception {
        CallsModel model = new CallsModel();
        //assertEquals(true,model.InsertCall(Float.valueOf(100),1));
    }

}
