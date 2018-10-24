import javafx.util.Pair;

import java.sql.*;
import java.util.List;

public class Startup
{
        private static String MYSQLCONNECTION = "jdbc:mysql://194.58.121.174:3306?user=remoteuser&password=123456azsxdc&autoReconnect=true&useSSL=false";
        private static String MYSQLDRIVER = "com.mysql.cj.jdbc.Driver";
        private static Boolean isTest = true;
        static List<Pair<Integer, String>> _RolesTest;
        static List<Pair<String, String>> _UserNamesPasswordTest;
        static List<Pair<String, String>> _UserNamesTokenTest;
	static List<Pair<String, Integer>> _UserNamesRoleTest;
        
        public static String GetConnectionStr() {
            return MYSQLCONNECTION;
        }

        public static String GetDriver() {
            return MYSQLDRIVER;
        }

        public static Boolean isTest() {
    return isTest;
}
        
        public static void InitializeDB(){
            Connection connection = null;
            try {
                Class.forName(GetDriver());
            } catch (ClassNotFoundException e) {
            }
            try {
                connection = DriverManager.getConnection(GetConnectionStr());
            } catch (SQLException e) {
                return;
            }
            String query1 = "DROP TABLE Account.Info; DROP TABLE Account.Roles;";
            String query2 = "CREATE DATABASE IF NOT EXISTS Account;";
                            
            String query3 = "CREATE TABLE Account.Info\n" +
                            "\t(\n" +
                            "\tID int AUTO_INCREMENT PRIMARY KEY,\n" +
                            "\tUsername varchar(50) NOT NULL,\n" +
                            "\tPassword varchar(50) NOT NULL,\n" +
                            "\tRole int,\n" +
                            "\tCookie varchar(50) \n" +
                            "\t);\n" +
                            "CREATE TABLE Account.Roles\n" +
                            "\t(\n" +
                            "\tID_Role int AUTO_INCREMENT PRIMARY KEY,\n" +
                            "\tName_Role varchar(50) NOT NULL\n" +
                            "\t);\n" +
                            "\t);";

            Statement stmtObj = null;
            try {
                stmtObj = connection.createStatement();
            } catch (SQLException e) {
		return;
            }
            try {
                stmtObj.execute(query1);
            } catch (SQLException e) {
            }
            try {
                stmtObj.execute(query2);
            } catch (SQLException e) {
            }
            try {
                stmtObj.execute(query3);
            } catch (SQLException e) {
            }
        }
}
