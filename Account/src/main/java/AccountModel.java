import javafx.util.Pair;
import jdk.nashorn.internal.parser.Token;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class AccountModel {
    Connection connection;
    Boolean dbStatus;
    Boolean queryStatus;
    EasyAuth auth;
    Boolean _isTest = false;
    int sizePage = 2;

    AccountModel(Boolean isTest)
    {
        _isTest = isTest;
        if(!isTest)
            dbStatus = CreateConnection();
        else
            dbStatus = true;
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
        else {
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
                System.out.print("\nError get connection " + e.getMessage() + "\n");
                return false;
            }
            auth = new EasyAuth(db_uri, "Account.Info", "Username", "Password", "Cookie");
            return true;
        }
    }

    List<String> GetUserNames(String token, int numberPage)
    {
        queryStatus = true;
        List<String> UserNames = new ArrayList<>();
        if(_isTest) {
            boolean valid = false;
            for(int zz = 0; zz < Startup._UserNamesTokenTest.size(); zz++) {
                if (Startup._UserNamesTokenTest.get(zz).getValue().equals(token))
                    valid = true;
                UserNames.add(Startup._UserNamesTokenTest.get(zz).getKey());
            }
            if(valid)
                return UserNames;
            else
            {
                queryStatus = false;
                List<String> empList = new ArrayList<>();
                return empList;
            }
        }
        if(!auth.HasLogged(token)) {
            queryStatus = false;
            return UserNames;
        }
        else {
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
                                "LIMIT " + String.valueOf(sizePage) + " OFFSET " + String.valueOf(numberPage * sizePage));
            } catch (SQLException e) {
                queryStatus = false;
                return UserNames;
            }
            try {
                while (resObj.next()) {
                    UserNames.add(resObj.getString("Username") + ":" + resObj.getString("Name_Role"));
                }
            } catch (SQLException e) {
                queryStatus = false;
                return UserNames;
            }
            try {
                stmtObj.close();
            } catch (SQLException e) {
            }
        }
        return UserNames;
    }

    List<String> GetAllRoles()
    {
        Statement stmtObj = null;
        queryStatus = true;
        List<String> Roles = new ArrayList<>();
        if(_isTest) {
            for(int zz = 0 ; zz < Startup._RolesTest.size(); zz++)
                Roles.add(Startup._RolesTest.get(zz).getValue());
            return Roles;
        }
        else {
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
                while (resObj.next()) {
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
            } catch (SQLException e) {
            }
            return Roles;
        }
    }

    Boolean CreateUser(String username, String password,String role)
    {
        if(_isTest) {
            Pair<String,String> pair = new Pair<>(username,password);
            for (int zz = 0; zz < Startup._UserNamesPasswordTest.size(); zz++) {
                if(Startup._UserNamesPasswordTest.contains(pair))
                    return false;
            }
            Startup._UserNamesPasswordTest.add(pair);
            return true;
        }
        else {
            Boolean success = auth.CreateAccount(username, password);
            if (success) {
                Statement stmtObj = null;
                try {
                    stmtObj = connection.createStatement();
                } catch (SQLException e) {
                    return false;
                }
                ResultSet resObj = null;
                try {
                    stmtObj.execute("UPDATE Account.Info SET Role=" + role + " WHERE Username='" + username + "'");
                } catch (SQLException e) {
                    return false;
                }
            }
            return success;
        }
    }

    Boolean AddRole(int ID, String role)
    {
        if(_isTest) {
            Pair<Integer,String> pair = new Pair<>(ID,role);
            Startup._RolesTest.add(pair);
        }
        else {
            Statement stmtObj = null;
            try {
                stmtObj = connection.createStatement();
            } catch (SQLException e) {
                return false;
            }
            ResultSet resObj = null;
            try {
                stmtObj.execute("Insert INTO Account.Roles VALUES(" + Integer.toString(ID) + ",'" + role + "')");
            } catch (SQLException e) {
                return false;
            }
        }
        return true;
    }

    String Login(String username, String password)
    {
        queryStatus = true;

        if(_isTest) {
            Pair<String,String> pair = new Pair<>(username,password);
            if(Startup._UserNamesPasswordTest.contains(pair)) {
                ListIterator<Pair<String, String>> iter = Startup._UserNamesTokenTest.listIterator();
                while (iter.hasNext()) {
                    Pair<String, String> valueIter = iter.next();
                    Pair<String,String> pair2 = new Pair<>(username,username+password);
                    if (valueIter.getValue().equals(username))
                        iter.set(pair2);
                }

                Pair<String,String> pair2 = new Pair<>(username,username+password);
                Startup._UserNamesTokenTest.add(pair2);
                return username+password;
            }
            else {
                queryStatus = false;
                return "";
            }
        }
        else {
            String authCookie = auth.LogIn(username, password);
            if (auth.GetQueryStatus())
                return authCookie;
            else {
                queryStatus = false;
                return "";
            }
        }
    }

    Boolean Logout(String token)
    {
        queryStatus = true;
        if(_isTest) {
                ListIterator<Pair<String, String>> iter = Startup._UserNamesTokenTest.listIterator();
                while (iter.hasNext()) {
                    Pair<String, String> valueIter = iter.next();
                    Pair<String, String> newValue = new Pair<>(valueIter.getKey(),"");
                    if (valueIter.getValue().equals(token)) {
                        iter.set(newValue);
                        return true;
                    }
                }
            return false;
        }
        if(auth.HasLogged(token))
            return auth.LogOut(token);
        else return false;
    }

    Boolean IsLogged(String token)
    {
        if(_isTest) {
            for(int zz = 0; zz < Startup._UserNamesTokenTest.size(); zz++)
                if (Startup._UserNamesTokenTest.get(zz).getValue().equals(token))
                    return true;
            return false;
        }
        else return auth.HasLogged(token);
    }

    String GetUsername(String token)
    {
        queryStatus = true;
        if(_isTest) {
            if(IsLogged(token)) {
                for(int zz = 0; zz < Startup._UserNamesTokenTest.size(); zz++) {
                    if (Startup._UserNamesTokenTest.get(zz).getValue().equals(token)) {
                        return Startup._UserNamesTokenTest.get(zz).getKey();
                    }
                }
            }
            else {
                queryStatus = false;
                return "";
            }
        }
        else {
            String username = auth.GetUserName(token);
            if (auth.GetQueryStatus()) {
                return username;
            } else {
                queryStatus = false;
                return "";
            }
        }
        queryStatus = false;
        return "";

    }

    String GetRole(String token)
    {
        queryStatus = true;
        if(_isTest) {
            if(IsLogged(token))
            {
                String username = GetUsername(token);
                for(int zz = 0; zz < Startup._UserNamesRoleTest.size(); zz++)
                {
                    if(Startup._UserNamesRoleTest.get(zz).getKey().equals(username))
                        return Startup._UserNamesRoleTest.get(zz).getValue();
                }
            }
            else {
                queryStatus = false;
                return "";
            }
        }
        else {
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
                resObj = stmtObj.executeQuery("SELECT Name_Role FROM Account.Roles rl JOIN Account.Info inf ON(inf.Role = rl.ID_Role) WHERE inf.Cookie=" + token);
            } catch (SQLException e) {
                queryStatus = false;
                return role;
            }
            try {
                while (resObj.next()) {
                    role = resObj.getString("Name_Role");
                }
            } catch (SQLException e) {
                queryStatus = false;
                return role;
            }
            try {
                stmtObj.close();
            } catch (SQLException e) {
            }
            return role;
        }
        queryStatus = false;
        return "";
    }


}
