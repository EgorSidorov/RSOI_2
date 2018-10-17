import java.sql.*;

public class Startup
{
        //private static String MYSQLCONNECTION = "jdbc:mysql://localhost:3306/test";
        //private static String MSSQLCONNECTION = "jdbc:sqlserver://localhost:1433;databaseName=RSOI_02;user=some_user;password=asdfgh";
        //private static String MYSQLDRIVER = "com.mysql.jdbc.Driver";
        //private static String MSSQLDRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        
        public static String GetConnectionStr() {
            return "jdbc:mysql://localhost:3306/test";
        }

        public static String GetDriver() {
            return "com.mysql.jdbc.Driver";
        }
        
        public static void InitializeDB(){
            Connection connection = null;
            try {
                Class.forName(GetDriver());
            } catch (ClassNotFoundException e) {
            }
            try {
                connection = DriverManager.getConnection(GetConnectionStr(),"root","");
            } catch (SQLException e) {
                return;
            }
            String query1 = "DROP TABLE Account.Users; DROP TABLE Account.Roles; DROP TABLE Payment.Pursy; DROP TABLE Calls.History;";
            String query2 = "CREATE SCHEMA Account;\n" +
                            "CREATE SCHEMA Payment;\n" +
                            "CREATE SCHEMA Calls;\n" +
                            "GO";
            String query3 = "CREATE TABLE Account.Users\n" +
                            "\t(\n" +
                            "\tID int IDENTITY(1,1) PRIMARY KEY,\n" +
                            "\tUsername varchar(50) NOT NULL,\n" +
                            "\tPssword varchar(50) NOT NULL,\n" +
                            "\tRole int\n" +
                            "\t);\n" +
                            "CREATE TABLE Account.Roles\n" +
                            "\t(\n" +
                            "\tID_Role int IDENTITY(1,1) PRIMARY KEY,\n" +
                            "\tName_Role varchar(50) NOT NULL,\n" +
                            "\t);\n" +
                            "\n" +
                            "CREATE TABLE Payment.Pursy\n" +
                            "\t(\n" +
                            "\tID_Cash int IDENTITY(1,1) PRIMARY KEY,\n" +
                            "\tCash float,\n" +
                            "\tUsername varchar(50) NOT NULL\n" +
                            "\t);\n" +
                            "\n" +
                            "CREATE TABLE Calls.History\n" +
                            "\t(\n" +
                            "\tID_Call int IDENTITY(1,1) PRIMARY KEY,\n" +
                            "\tDuration float,\n" +
                            "\tUsername varchar(50) NOT NULL\n" +
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
