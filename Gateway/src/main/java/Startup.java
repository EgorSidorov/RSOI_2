public class Startup
{
        private static String gatewayHostPort = "194.58.121.174:8080";
        private static String restApiCommand = "http://194.58.121.174:8080/?service=Cross&arguments=\"ADD_CALL\"&Duration=20";
        public static String GetGatewayHostPort() {
            return gatewayHostPort;
        }
        public static String getRestApiCommand() {
            return restApiCommand;
        }
}
