import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountModel {
    Connection connection;
    Boolean dbStatus;
    Boolean queryStatus;
    int sizePage = 2;

    AccountModel()
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

    List<String> GetUserNames(int numberPage)
    {
        queryStatus = true;
        List<String> UserNames = new ArrayList<>();
        Statement stmtObj = null;
        try {
            stmtObj = connection.createStatement();
        } catch (SQLException e) {
            UserNames.add(e.getMessage());
            queryStatus = false;
            return UserNames;
        }
        ResultSet resObj = null;
        try {
            resObj = stmtObj.executeQuery(
                    "SELECT ID, First_Name, Last_Name, Name_Role " +
                            "FROM Account.Users us " +
                            "JOIN Account.Roles rl ON(us.Role=rl.ID_Role) " +
                            "WHERE us.ID BETWEEN " + String.valueOf(numberPage*sizePage+1) + " AND " + String.valueOf((numberPage+1)*sizePage));
        } catch (SQLException e) {
            UserNames.add(e.getMessage());
            queryStatus = false;
            return UserNames;
        }
        try {
            while (resObj.next()) {
                UserNames.add(resObj.getString("ID") + " " +resObj.getString("First_Name") + " "+resObj.getString("Last_Name") + " " +resObj.getString("Name_Role"));
            }
        } catch (SQLException e) {
            UserNames.clear();
            UserNames.add(e.getMessage());
            queryStatus = false;
            return UserNames;
        }
        try {
            stmtObj.close();
        } catch (SQLException e) {queryStatus = false;}

        return UserNames;
    }

    List<String> GetAllRoles()
    {
        Statement stmtObj = null;
        queryStatus = true;
        List<String> Roles = new ArrayList<>();
        try {
            stmtObj = connection.createStatement();
        } catch (SQLException e) {
            Roles.add(e.getMessage());
            queryStatus = false;
            return Roles;
        }
        ResultSet resObj = null;
        try {
            resObj = stmtObj.executeQuery("SELECT Name_Role FROM Account.Roles");
        } catch (SQLException e) {
            Roles.add(e.getMessage());
            queryStatus = false;
            return Roles;
        }
        try {
            while(resObj.next()) {
                Roles.add(resObj.getString("Name_Role"));
            }
        } catch (SQLException e) {
            Roles.clear();
            Roles.add(e.getMessage());
            queryStatus = false;
            return Roles;
        }
        try {
            stmtObj.close();
        } catch (SQLException e) {queryStatus = false;}
        return Roles;
    }

    Boolean InsertUser(String FirstName, String LastName,String Role)
    {
        Statement stmtObj = null;
        try {
            stmtObj = connection.createStatement();
        } catch (SQLException e) {
           return false;
        }
        ResultSet resObj = null;
        try {
            resObj = stmtObj.executeQuery("INSERT INTO Account.Users VALUES('"+FirstName+"','"+LastName+"',"+Role+")");
        } catch (SQLException e) {
        }
        try {
            stmtObj.close();
        } catch (SQLException e) {}
        return true;
    }


}
