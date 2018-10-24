import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CallsModel {
    Connection connection;
    Boolean dbStatus;
    Boolean queryStatus;
    int sizePage = 2;
    Boolean _isTest = false;

    CallsModel(boolean isTest)
    {
        _isTest = isTest;
        dbStatus = CreateConnection();
    }

    Boolean GetDbStatus()
    {
        return  dbStatus;
    }

    Boolean CreateConnection()
    {
        if(_isTest)
            return true;
        String db_uri = Startup.GetConnectionStr();
        try {
            Class.forName(Startup.GetDriver());
        } catch (ClassNotFoundException e) {
            System.out.print("\nError find MSSQL driver\n");
            return false;
        }
        try {
            connection = DriverManager.getConnection(db_uri);

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
        if(_isTest) {
            for(int zz = 0; zz<Startup._CallsTest.size(); zz++)
                if(Startup._CallsTest.get(zz).getValue().equals(username))
                    CallsList.add(String.valueOf(Startup._CallsTest.get(zz).getKey()));
            if(CallsList.isEmpty())
                queryStatus = false;
            return CallsList;
        }
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
                            "WHERE hs.Username ='" + username + "' LIMIT " + String.valueOf(sizePage) + " OFFSET " + String.valueOf(numberPage*sizePage));
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
        if(_isTest) {
            Startup._CallsTest.add(new Pair<>(Float.valueOf(duration),username));
            return true;
        }
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
