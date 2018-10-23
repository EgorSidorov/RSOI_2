import java.sql.*;

public class Startup
{
        private static String MYSQLCONNECTION = "jdbc:mysql://194.58.121.174:3306?user=remoteuser&password=123456azsxdc&autoReconnect=true&useSSL=false";
        private static String MYSQLDRIVER = "com.mysql.cj.jdbc.Driver";
        
        public static String GetConnectionStr() {
            return MYSQLCONNECTION;
        }


        public static String GetDriver() {
            return MYSQLDRIVER;
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
            String query1 = "DROP TABLE Payment.Pursy;";
            String query2 = "CREATE DATABASE IF NOT EXISTS Payment;";
                            
            String query3 = "CREATE TABLE Payment.Pursy\n" +
                            "\t(\n" +
                            "\tID_Cash int AUTO_INCREMENT PRIMARY KEY,\n" +
                            "\tCash float,\n" +
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
