/**
 * Created by Егор on 10.10.2018.
 */
import static org.junit.jupiter.api.Assertions.assertEquals;
public class PaymentModelTest {
    @org.junit.Test
    public void getDbStatus() throws Exception {
        PaymentModel model = new PaymentModel();
        assertEquals(true,model.GetDbStatus());
    }

    @org.junit.Test
    public void createConnection() throws Exception {
        PaymentModel model = new PaymentModel();
        assertEquals(true,model.CreateConnection());
    }

    @org.junit.Test
    public void GetPaymentHistory() throws Exception {
        PaymentModel model = new PaymentModel();
        model.GetPaymentHistory(0);
        assertEquals(true,model.queryStatus);
    }

    @org.junit.Test
    public void GetPursys() throws Exception {
        PaymentModel model = new PaymentModel();
        model.GetPursys();
        assertEquals(true,model.queryStatus);
    }

    @org.junit.Test
    public void InsertPursy() throws Exception {
        PaymentModel model = new PaymentModel();
        assertEquals(true,model.InsertPursy(Float.valueOf(100),1));
    }

    @org.junit.Test
    public void AddPay() throws Exception {
        PaymentModel model = new PaymentModel();
        assertEquals(true,model.AddPay(Float.valueOf(100),1));
    }

}
