/**
 * Created by Егор on 10.10.2018.
 */
import static org.junit.jupiter.api.Assertions.assertEquals;
public class CallsModelTest {
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
        model.GetCallsHistory(0);
        assertEquals(true,model.queryStatus);
    }

    @org.junit.Test
    public void insertCall() throws Exception {
        CallsModel model = new CallsModel();
        assertEquals(true,model.InsertCall(Float.valueOf(100),1));
    }

}
