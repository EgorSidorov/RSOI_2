package com.rsoi2app_calls.models;

import com.rsoi2app_calls.config.Startup;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CallsModel {
    public Connection connection;
    Boolean dbStatus;
    Boolean queryStatus;
    int sizePage = 2;
    public ResultSet resObj = null;
    Boolean _isTest = false;

    public CallsModel()
    {
        dbStatus = CreateConnection();
    }

    public Boolean GetDbStatus()
    {
        return  dbStatus;
    }

    public Boolean GetQueryStatus()
    {
        return  queryStatus;
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
            connection = DriverManager.getConnection(db_uri);

        } catch (SQLException e) {
            System.out.print("\nError get connection "+e.getMessage() + "\n");
            return false;
        }
        return true;
    }

    public List<String> ShowCallHistory(String username,int numberPage)
    {
        queryStatus = true;
        List<String> CallsList = new ArrayList<>();
        Statement stmtObj = RequestDB(
                "SELECT Duration, Username " +
                        "FROM Calls.History hs " +
                        "WHERE hs.Username ='" + username + "' LIMIT " + String.valueOf(sizePage) + " OFFSET " + String.valueOf(numberPage*sizePage),true);
        if(!GetQueryStatus())
            return  CallsList;
        try {
            while (resObj.next()) {
                CallsList.add(resObj.getString("Duration") + " " +resObj.getString("Username"));
            }
        } catch (SQLException e) {
            queryStatus = false;
            return CallsList;
        }
        try {
            if(stmtObj != null)
                stmtObj.close();
        } catch (SQLException e) {}

        return CallsList;
    }

    public Boolean AddCall(String duration, String username)
    {
        Statement stmtObj = RequestDB("INSERT INTO Calls.History (Duration, Username) VALUES("+duration+",'"+username+"')",false);
        if(!GetQueryStatus())
        {
            queryStatus = false;
            return null;
        }
        try {
            if(stmtObj != null)
                stmtObj.close();
        } catch (SQLException e) {}
        return true;
    }

    public List<String> GetLogs(int numberPage)
    {
        queryStatus = true;
        List<String> Logs = new ArrayList<>();
        Statement stmtObj = RequestDB("SELECT str FROM Calls.Logger LIMIT "+String.valueOf(sizePage)+" OFFSET "+String.valueOf(numberPage),true);
        if(!GetQueryStatus()) {
            return Logs;
        }
        try {
            while(resObj.next()) {
                Logs.add(resObj.getString("str"));
            }
        } catch (SQLException e) {
            queryStatus = false;
            return Logs;
        }
        try {
            if(stmtObj != null)
                stmtObj.close();
        } catch (SQLException e) {}
        return Logs;
    }

    public Boolean SetLogs(String logString)
    {
        queryStatus = true;
        Statement stmtObj = RequestDB("INSERT INTO Calls.Logger (str) VALUES('"+logString+"')",false);
        if(!GetQueryStatus()) {
            return false;
        }
        try {
            if(stmtObj != null)
                stmtObj.close();
        } catch (SQLException e) {}
        return true;
    }

    public Statement RequestDB(String sql, Boolean response)
    {
        queryStatus = true;
        if(!GetDbStatus())
        {
            queryStatus = false;
            return null;
        }
        Statement stmtObj = null;
        try {
            stmtObj = connection.createStatement();
        } catch (SQLException e) {
            queryStatus = false;
            return null;
        }
        try {
            if(response)
                resObj = stmtObj.executeQuery(sql);
            else
                stmtObj.execute(sql);
        } catch (SQLException e) {
            queryStatus = false;
        }
        finally {
            return stmtObj;
        }
    }


}
