import org.junit.Assert;
import org.junit.Test;
import org.junit.BeforeClass;
import java.util.List;

public class PaymentModelTest {

    @BeforeClass
    public static void BeforeTest(){
        Startup.InitializeDB();
    }

    @Test
    public void getDbStatus() throws Exception {
        PaymentModel model = new PaymentModel();
        Assert.assertEquals(true,model.GetDbStatus());
    }

    @Test
    public void createConnection() throws Exception {
        PaymentModel model = new PaymentModel();
        Assert.assertEquals(true,model.CreateConnection());
    }

    @Test
    public void AddCall() throws Exception {
        PaymentModel model = new PaymentModel();
        Assert.assertEquals(true,model.AddCall(20,"Egor"));
    }

    @Test
    public void ShowAllHistory() throws Exception {
        PaymentModel model = new PaymentModel();
	model.AddCall(20,"Egor");
        Assert.assertEquals(false,model.ShowAllHistory("Egor",0).isEmpty());
    }

}
