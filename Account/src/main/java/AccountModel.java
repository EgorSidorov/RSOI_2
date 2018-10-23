import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountModel {
    Connection connection;
    Boolean dbStatus;
    Boolean queryStatus;
    EasyAuth auth;
    int sizePage = 2;

    AccountModel()
    {
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
        auth = new EasyAuth(db_uri,"Account.Info","Username","Password","Cookie");
        return true;
    }

    List<String> GetUserNames(String token, int numberPage)
    {
        queryStatus = true;
        List<String> UserNames = new ArrayList<>();
        if(!auth.HasLogged(token)) {
            queryStatus = false;
            return UserNames;
        }
        Statement stmtObj = null;
        try {
            stmtObj = connection.createStatement();
        } catch (SQLException e) {
            queryStatus = false;
            return UserNames;
        }
        ResultSet resObj = null;
        try {
            resObj = stmtObj.executeQuery(
                            "SELECT Username,  Name_Role " +
                            "FROM Account.Info us " +
                            "JOIN Account.Roles rl ON(us.Role=rl.ID_Role) " +
                            "WHERE us.ID BETWEEN " + String.valueOf(numberPage*sizePage+1) + " AND " + String.valueOf((numberPage+1)*sizePage));
        } catch (SQLException e) {
            queryStatus = false;
            return UserNames;
        }
        try {
            while (resObj.next()) {
                UserNames.add(resObj.getString("Username") + ":" +resObj.getString("Name_Role"));
            }
        } catch (SQLException e) {
            queryStatus = false;
            return UserNames;
        }
        try {
            stmtObj.close();
        } catch (SQLException e) {}

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
        } catch (SQLException e) {}
        return Roles;
    }

    Boolean CreateUser(String username, String password,String role)
    {
        Boolean success =  auth.CreateAccount(username,password);
        if(success) {
            Statement stmtObj = null;
            try {
                stmtObj = connection.createStatement();
            } catch (SQLException e) {
                return false;
            }
            ResultSet resObj = null;
            try {
                stmtObj.execute("UPDATE Account.Info SET Role="+role+ " WHERE Username='"+username+"'");
            } catch (SQLException e) {
                return false;
            }
        }
        return success;
    }

    Boolean AddRole(int ID, String role)
    {
            Statement stmtObj = null;
            try {
                stmtObj = connection.createStatement();
            } catch (SQLException e) {
                return false;
            }
            ResultSet resObj = null;
            try {
                stmtObj.execute("Insert INTO Account.Roles VALUES("+Integer.toString(ID)+",'"+role+"')");
            } catch (SQLException e) {
                return false;
            }
        return true;
    }

    String Login(String username, String password)
    {
        queryStatus = true;
        String authCookie = auth.LogIn(username,password);
        if (auth.GetQueryStatus())
            return authCookie;
        else {
            queryStatus = false;
            return "";}
    }

    Boolean Logout(String token)
    {
        queryStatus = true;
        if(auth.HasLogged(token))
            return auth.LogOut(token);
        else return false;
    }

    Boolean IsLogged(String token)
    {
        return auth.HasLogged(token);
    }

    String GetUsername(String token)
    {
        queryStatus = true;
        String username = auth.GetUserName(token);
        if (auth.GetQueryStatus()) {
            return username;
        }
        else {
            queryStatus = false;
            return "";
        }
    }

    String GetRole(String token)
    {
        queryStatus = true;
        String role = "";
        ResultSet resObj = null;
        Statement stmtObj = null;
        try {
            stmtObj = connection.createStatement();
        } catch (SQLException e) {
            queryStatus = false;
            return role;
        }
        try {
            resObj = stmtObj.executeQuery("SELECT Name_Role FROM Account.Roles rl JOIN Account.Info inf ON(inf.Role = rl.ID_Role) WHERE inf.Cookie="+token);
        } catch (SQLException e) {
            queryStatus = false;
            return role;
        }
        try {
            while(resObj.next()) {
                role = resObj.getString("Name_Role");
            }
        } catch (SQLException e) {
            queryStatus = false;
            return role;
        }
        try {
            stmtObj.close();
        } catch (SQLException e) {}
        return role;
    }


}