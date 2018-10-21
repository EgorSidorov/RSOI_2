import java.sql.*;

public class Startup
{
        private static String MYSQLCONNECTION = "jdbc:mysql://localhost:3306/test?user=root&password=&useUnicode=true";
        private static String MSSQLCONNECTION = "jdbc:sqlserver://localhost:1433;databaseName=RSOI_02;user=some_user;password=asdfgh";
        private static String MYSQLDRIVER = "com.mysql.jdbc.Driver";
        private static String MSSQLDRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

        static boolean hasInitialized = false;
        
        public static String GetConnectionStr() {
            return MYSQLCONNECTION;
        }

        public static String GetDriver() {
            return MYSQLDRIVER;
        }
        
        public static void InitializeDB(){
            if(hasInitialized)
                return;
            hasInitialized = true;
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
            String query1 = "DROP TABLE Account.Info; DROP TABLE Account.Roles; DROP TABLE Payment.Pursy; DROP TABLE Calls.History;";
            String query2 = "CREATE SCHEMA Account;";
            String query3 = "CREATE SCHEMA Payment;";
            String query4 = "CREATE SCHEMA Calls;";
                            
            String query5 = "CREATE TABLE Account.Info\n" +
                            "\t(\n" +
                            "\tID int IDENTITY(1,1) PRIMARY KEY,\n" +
                            "\tUsername varchar(50) NOT NULL,\n" +
                            "\tPassword varchar(50) NOT NULL,\n" +
                            "\tRole int,\n" +
                            "\tCookie varchar(50) \n" +
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
	    try {
                stmtObj.execute(query4);
            } catch (SQLException e) {
            }
	    try {
                stmtObj.execute(query5);
            } catch (SQLException e) {
            }
        }
}
