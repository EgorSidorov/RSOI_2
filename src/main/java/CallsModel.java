import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CallsModel {
    Connection connection;
    Boolean dbStatus;
    Boolean queryStatus;
    int sizePage = 2;

    CallsModel()
    {
        dbStatus = CreateConnection();
    }

    Boolean GetDbStatus()
    {
        return  dbStatus;
    }

    Boolean CreateConnection()
    {
        String db_uri = Startup.GetConnectionStr();
        try {
            Class.forName(Startup.GetDriver());
        } catch (ClassNotFoundException e) {
            System.out.print("\nError find MSSQL driver\n");
            return false;
        }
        try {
            connection = DriverManager.getConnection(db_uri,"root","");

        } catch (SQLException e) {
            System.out.print("\nError get connection "+e.getMessage() + "\n");
            return false;
        }
        return true;
    }

    List<String> ShowCallHistory(String username,int numberPage)
    {
        queryStatus = true;
        List<String> CallsList = new ArrayList<>();
        Statement stmtObj = null;
        try {
            stmtObj = connection.createStatement();
        } catch (SQLException e) {
            CallsList.add(e.getMessage());
            queryStatus = false;
            return CallsList;
        }
        ResultSet resObj = null;
        try {
            resObj = stmtObj.executeQuery(
                    "SELECT Duration, Username " +
                            "FROM Calls.History hs " +
                            "WHERE hs.Username ='" + username + "' AND hs.ID_Call BETWEEN " + String.valueOf(numberPage*sizePage+1) + " AND " + String.valueOf((numberPage+1)*sizePage));
        } catch (SQLException e) {
            CallsList.add(e.getMessage());
            queryStatus = false;
            return CallsList;
        }
        try {
            while (resObj.next()) {
                CallsList.add(resObj.getString("Duration") + " " +resObj.getString("Username"));
            }
        } catch (SQLException e) {
            CallsList.clear();
            CallsList.add(e.getMessage());
            queryStatus = false;
            return CallsList;
        }
        try {
            stmtObj.close();
        } catch (SQLException e) {queryStatus = false;}

        return CallsList;
    }

    Boolean AddCall(String duration, String username)
    {
        Statement stmtObj = null;
        try {
            stmtObj = connection.createStatement();
        } catch (SQLException e) {
            return false;
        }
        try {
            stmtObj.execute("INSERT INTO Calls.History (Duration, Username) VALUES("+duration+",'"+username+"')");
        } catch (SQLException e) {
            return false;
        }
        try {
            stmtObj.close();
        } catch (SQLException e) {}
        return true;
    }


}
