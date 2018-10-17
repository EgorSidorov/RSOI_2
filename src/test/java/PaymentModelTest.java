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
    public void CreatePursy() throws Exception {
        PaymentModel model = new PaymentModel();
        Assert.assertEquals(true,model.CreatePursy("Egor"));
    }

    @Test
    public void AddCash() throws Exception {
        PaymentModel model = new PaymentModel();
	model.CreatePursy("Egor2");
        Assert.assertEquals(true,model.AddCash("20","Egor2"));
    }

    @Test
    public void WithdrawCash() throws Exception {
        PaymentModel model = new PaymentModel();
	model.CreatePursy("Egor3");
        Assert.assertEquals(true,model.WithdrawCash("20","Egor3"));
    }

    @Test
    public void ShowCash() throws Exception {
        PaymentModel model = new PaymentModel();
	model.CreatePursy("Egor4");
        Assert.assertEquals(false,model.ShowCash("Egor4").isEmpty());
    }

}
