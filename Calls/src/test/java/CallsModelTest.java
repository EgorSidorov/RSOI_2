import org.junit.Assert;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import java.util.List;

public class CallsModelTest {

    @BeforeClass
    public static void BeforeTest(){
        if(!Startup.isTest)
            Startup.InitializeDB();
    }

    @Test
    public void getDbStatus() throws Exception {
        CallsModel model = new CallsModel(Startup.isTest);
        Assert.assertEquals(true,model.GetDbStatus());
    }

    @Test
    public void createConnection() throws Exception {
        CallsModel model = new CallsModel(Startup.isTest);
        Assert.assertEquals(true,model.CreateConnection());
    }

    @Test
    public void AddCall() throws Exception {
        CallsModel model = new CallsModel(Startup.isTest);
        Assert.assertEquals(true,model.AddCall("20","Egor30"));
    }

    @Test
    public void ShowAllHistory() throws Exception {
        CallsModel model = new CallsModel(Startup.isTest);
	model.AddCall("20","Egor31");
        Assert.assertEquals(false,model.ShowCallHistory("Egor31",0).isEmpty());
    }

}
