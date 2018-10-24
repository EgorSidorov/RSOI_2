import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class PaymentModel {
    Connection connection;
    Boolean dbStatus;
    Boolean queryStatus;
    int sizePage = 2;
    boolean _isTest = false;

    PaymentModel(boolean isTest)
    {
        _isTest = isTest;
        dbStatus = CreateConnection();
    }

    Boolean GetDbStatus()
    {
        return  dbStatus;
    }

    Boolean GetQueryStatus()
    {
        return  queryStatus;
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

    List<String> GetPaymentHistory(int numberPage)
    {
        queryStatus = true;
        List<String> PaymentList = new ArrayList<>();
        Statement stmtObj = null;
        try {
            stmtObj = connection.createStatement();
        } catch (SQLException e) {
            PaymentList.add(e.getMessage());
            queryStatus = false;
            return PaymentList;
        }
        ResultSet resObj = null;
        try {
            resObj = stmtObj.executeQuery(
                    "SELECT ID_Pay, ID_Call " +
                            "FROM Payment.History ps " +
                            "WHERE ps.ID_Pay BETWEEN " + String.valueOf(numberPage*sizePage+1) + " AND " + String.valueOf((numberPage+1)*sizePage));
        } catch (SQLException e) {
            PaymentList.add(e.getMessage());
            queryStatus = false;
            return PaymentList;
        }
        try {
            while (resObj.next()) {
                PaymentList.add(resObj.getString("ID_Pay") + " "+resObj.getString("ID_Call"));
            }
        } catch (SQLException e) {
            PaymentList.clear();
            PaymentList.add(e.getMessage());
            queryStatus = false;
            return PaymentList;
        }
        try {
            stmtObj.close();
        } catch (SQLException e) {queryStatus = false;}

        return PaymentList;
    }

    List<String> GetPursys()
    {
        Statement stmtObj = null;
        queryStatus = true;
        List<String> Pursys = new ArrayList<>();
        try {
            stmtObj = connection.createStatement();
        } catch (SQLException e) {
            Pursys.add(e.getMessage());
            queryStatus = false;
            return Pursys;
        }
        ResultSet resObj = null;
        try {
            resObj = stmtObj.executeQuery("SELECT ID_Cash,Cash,ID_User FROM Payment.Pursy");
        } catch (SQLException e) {
            Pursys.add(e.getMessage());
            queryStatus = false;
            return Pursys;
        }
        try {
            while(resObj.next()) {
                Pursys.add(resObj.getString("ID_Cash") + " "+resObj.getString("Cash")+ " "+resObj.getString("ID_User"));;
            }
        } catch (SQLException e) {
            Pursys.clear();
            Pursys.add(e.getMessage());
            queryStatus = false;
            return Pursys;
        }
        try {
            stmtObj.close();
        } catch (SQLException e) {queryStatus = false;}
        return Pursys;
    }
    
    List<String> GetLogs(int numberPage)
    {
        Statement stmtObj = null;
        queryStatus = true;
        List<String> Logs = new ArrayList<>();
        try {
            stmtObj = connection.createStatement();
        } catch (SQLException e) {
            Logs.add(e.getMessage());
            queryStatus = false;
            return Logs;
        }
        ResultSet resObj = null;
        try {
            resObj = stmtObj.executeQuery("SELECT str FROM Payment.Logger LIMIT "+String.valueOf(sizePage)+" OFFSET "+String.valueOf(numberPage));
        } catch (SQLException e) {
            Logs.add(e.getMessage());
            queryStatus = false;
            return Logs;
        }
        try {
            while(resObj.next()) {
                Logs.add(resObj.getString("str"));;
            }
        } catch (SQLException e) {
            Logs.clear();
            Logs.add(e.getMessage());
            queryStatus = false;
            return Logs;
        }
        try {
            stmtObj.close();
        } catch (SQLException e) {queryStatus = false;}
        return Logs;
    }
    
    Boolean SetLogs(String logString)
    {
        Statement stmtObj = null;
        try {
            stmtObj = connection.createStatement();
        } catch (SQLException e) {
            return false;
        }
        try {
            stmtObj.execute("INSERT INTO Payment.Logger (str) VALUES('"+logString+"')");
        } catch (SQLException e) {
            return false;
        }
        try {
            stmtObj.close();
        } catch (SQLException e) {}
        return true;
    }

    Boolean CreatePursy(String username)
    {
        if(_isTest) {
            Startup._PursysTest.add(new Pair<>(Float.valueOf(0), username));
            return true;
        }
        Statement stmtObj = null;
        try {
            stmtObj = connection.createStatement();
        } catch (SQLException e) {
            return false;
        }
        try {
            stmtObj.execute("INSERT INTO Payment.Pursy (Cash,Username) VALUES("+String.valueOf(0)+",'"+username+"')");
        } catch (SQLException e) {
            return false;
        }
        try {
            stmtObj.close();
        } catch (SQLException e) {}
        return true;
    }

    Boolean AddCash(String cash, String username)
    {
        if(_isTest) {
            ListIterator<Pair<Float,String>> iter = Startup._PursysTest.listIterator();
            while (iter.hasNext())
            {
                Pair<Float,String> pair = iter.next();
                if(pair.getValue().equals(username)) {
                    iter.set(new Pair<>((pair.getKey() + Float.valueOf(cash)), username));
                    return true;
                }
            }
            return false;
        }
        Statement stmtObj = null;
        try {
            stmtObj = connection.createStatement();
        } catch (SQLException e) {
            return false;
        }
        try {
            stmtObj.execute("UPDATE Payment.Pursy SET Cash = Cash + "+cash+" WHERE Username='"+username+"'");
        } catch (SQLException e) {
            return false;
        }
        try {
            stmtObj.close();
        } catch (SQLException e) {}
        return true;
    }

    Boolean WithdrawCash(String cash, String username)
    {
        Statement stmtObj = null;
        if(_isTest) {
            ListIterator<Pair<Float,String>> iter = Startup._PursysTest.listIterator();
            while (iter.hasNext())
            {
                Pair<Float,String> pair = iter.next();
                if(pair.getValue().equals(username)) {
                    iter.set(new Pair<>((pair.getKey() - Float.valueOf(cash)), username));
                    return true;
                }
            }
            return false;
        }
        try {
            stmtObj = connection.createStatement();
        } catch (SQLException e) {
            return false;
        }
        try {
            stmtObj.execute("UPDATE Payment.Pursy SET Cash = Cash - "+cash+" WHERE Username='"+username+"'");
        } catch (SQLException e) {
            return false;
        }
        try {
            stmtObj.close();
        } catch (SQLException e) {}
        return true;
    }

    String ShowCash(String username)
    {
        queryStatus = true;
        Statement stmtObj = null;
        String outputCash = "";
        if(_isTest) {
            ListIterator<Pair<Float,String>> iter = Startup._PursysTest.listIterator();
            while (iter.hasNext())
            {
                Pair<Float,String> pair = iter.next();
                if(pair.getValue().equals(username)) {
                    return String.valueOf(pair.getKey());
                }
            }
            queryStatus = false;
            return "";
        }
        try {
            stmtObj = connection.createStatement();
        } catch (SQLException e) {
            queryStatus = false;
            return "";
        }
        ResultSet resObj = null;
        try {
            resObj = stmtObj.executeQuery("SELECT Cash FROM Payment.Pursy WHERE Username='"+username+"'");
        } catch (SQLException e) {
            queryStatus = false;
            return "";
        }
        try {
            if (resObj.next()) {
                outputCash = resObj.getString("Cash");
            }
        } catch (SQLException e) {
            queryStatus = false;
            return "";
        }
        try {
            stmtObj.close();
        } catch (SQLException e) {}
        return outputCash;
    }
}
