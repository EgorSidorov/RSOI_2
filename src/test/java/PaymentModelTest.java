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
        Assert.assertEquals(true,model.CreatePursy("Egor21"));
    }

    @Test
    public void AddCash() throws Exception {
        PaymentModel model = new PaymentModel();
	model.CreatePursy("Egor20");
        Assert.assertEquals(true,model.AddCash("20","Egor20"));
    }

    @Test
    public void WithdrawCash() throws Exception {
        PaymentModel model = new PaymentModel();
	model.CreatePursy("Egor22");
        Assert.assertEquals(true,model.WithdrawCash("20","Egor22"));
    }

    @Test
    public void ShowCash() throws Exception {
        PaymentModel model = new PaymentModel();
	model.CreatePursy("Egor23");
        Assert.assertEquals(false,model.ShowCash("Egor23").isEmpty());
    }

}
