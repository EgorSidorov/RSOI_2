import java.sql.*;

public class Startup
{
        private static String MYSQLCONNECTION = "jdbc:mysql://194.58.121.174:3306?user=remoteuser&password=123456azsxdc&autoReconnect=true&useSSL=false";
        private static String MSSQLCONNECTION = "jdbc:sqlserver://localhost:1433;databaseName=RSOI_02;user=some_user;password=asdfgh";
        private static String POSTGRESSQLCONNECTION = "jdbc:postgres://zjdesrfelofglf:a598d189e41fa7f4ac01182aa1448ca6e042b5658c5f4061533b4f27f899b8e1@ec2-46-137-75-170.eu-west-1.compute.amazonaws.com:5432/danbtbgklnp8s6";
        private static String MYSQLDRIVER = "com.mysql.cj.jdbc.Driver";
        private static String MSSQLDRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        private static String POSTGRESSQLDRIVER = "org.postgresql.Driver";
        private static String gatewayHostPort = "194.58.121.174:8080";

        static boolean hasInitialized = false;
        
        public static String GetConnectionStr() {
            return MYSQLCONNECTION;
        }

        public static String GetGatewayHostPort() {
            return gatewayHostPort;
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
            String query2 = "CREATE DATABASE IF NOT EXISTS Account;";
            String query3 = "CREATE DATABASE IF NOT EXISTS Payment;";
            String query4 = "CREATE DATABASE IF NOT EXISTS Calls;";
                            
            String query5 = "CREATE TABLE Account.Info\n" +
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
                            "\n" +
                            "CREATE TABLE Payment.Pursy\n" +
                            "\t(\n" +
                            "\tID_Cash int AUTO_INCREMENT PRIMARY KEY,\n" +
                            "\tCash float,\n" +
                            "\tUsername varchar(50) NOT NULL\n" +
                            "\t);\n" +
                            "\n" +
                            "CREATE TABLE Calls.History\n" +
                            "\t(\n" +
                            "\tID_Call int AUTO_INCREMENT PRIMARY KEY,\n" +
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
