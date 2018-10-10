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
        String db_uri = "jdbc:sqlserver://localhost:1433;databaseName=RSOI_02;";
        String user = "some_user";
        String password = "asdfgh";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.out.print("\nError find MSSQL driver\n");
            return false;
        }
        try {
            connection = DriverManager.getConnection(db_uri,user,password);

        } catch (SQLException e) {
            System.out.print("\nError get connection "+e.getMessage() + "\n");
            return false;
        }
        return true;
    }

    List<String> GetCallsHistory(int numberPage)
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
                    "SELECT ID_Call, Duration, ID_User " +
                            "FROM Calls.History hs " +
                            "WHERE hs.ID_Call BETWEEN " + String.valueOf(numberPage*sizePage+1) + " AND " + String.valueOf((numberPage+1)*sizePage));
        } catch (SQLException e) {
            CallsList.add(e.getMessage());
            queryStatus = false;
            return CallsList;
        }
        try {
            while (resObj.next()) {
                CallsList.add(resObj.getString("ID_Call") + " "+resObj.getString("Duration") + " " +resObj.getString("ID_User"));
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

    Boolean InsertCall(Float Duration, Integer IdUser)
    {
        Statement stmtObj = null;
        try {
            stmtObj = connection.createStatement();
        } catch (SQLException e) {
            return false;
        }
        ResultSet resObj = null;
        try {
            resObj = stmtObj.executeQuery("INSERT INTO Calls.History VALUES("+Duration.toString()+","+IdUser.toString()+")");
        } catch (SQLException e) {
        }
        try {
            stmtObj.close();
        } catch (SQLException e) {}
        return true;
    }


}