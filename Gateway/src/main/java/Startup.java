public class Startup
{
        private static int timeout = 5000;
        private static String gatewayHostPort = "194.58.121.174:8080";
        private static String restApiCommand = "\n\nList API Command:\n\n"+
        "http://194.58.121.174:8080/?service=Cross&arguments=\"ADD_CALL\"&Duration=20\n"+
        "http://194.58.121.174:8080/?service=Cross&arguments=\"ADD_CASH\"&Cash=20\n"+
        "http://194.58.121.174:8080/?service=Cross&arguments=\"WITHDRAW_CASH\"&Cash=20\n"+
        "http://194.58.121.174:8080/?service=Cross&arguments=\"CREATE_USER\"&Username=Pavel&Password=qwerty&Role=2\n"+
        "http://194.58.121.174:8080/?service=Cross&arguments=\"LOGIN\"&Username=Pavel&Password=qwerty\n"+
        "http://194.58.121.174:8080/?service=Cross&arguments=\"SHOW_CASH\"\n"+
        "http://194.58.121.174:8080/?service=Cross&arguments=\"SHOW_CALL_HISTORY\"\n"+
        "\n"+
        "http://194.58.121.174:8080/?service=account&arguments=\"command=GET_USERNAME\"\n"+
        "http://194.58.121.174:8080/?service=account&arguments=\"command=GET_ROLE\"\n"+
        "http://194.58.121.174:8080/?service=account&arguments=\"command=SHOW_ALL_USERS\"\n"+
        "http://194.58.121.174:8080/?service=account&arguments=\"command=SHOW_ALL_ROLES\"\n"+
        "http://194.58.121.174:8080/?service=account&arguments=\"command=IS_LOGGED\"\n"+
        "http://194.58.121.174:8080/?service=account&arguments=\"command=LOGOUT\"\n"+
        "http://194.58.121.174:8080/?service=account&arguments=\"command=LOGIN amp Username=Egor amp Password=qwerty\"\n"+
        "http://194.58.121.174:8080/?service=account&arguments=\"command=CREATE_USER amp Username=Pavel amp Password=qwerty amp Role=2\"\n"+
        "\n"+
        "http://194.58.121.174:8080/?service=calls&arguments=\"command=SHOW_CALL_HISTORY\"\n"+
        "http://194.58.121.174:8080/?service=calls&arguments=\"command=ADD_CALL amp Duration=20\"\n"+
        "\n"+
        "http://194.58.121.174:8080/?service=payment&arguments=\"command=CREATE_PURSY amp Cash=20 amp Username=Egor\"\n"+
        "http://194.58.121.174:8080/?service=payment&arguments=\"command=ADD_CASH amp Cash=20\"\n"+
        "http://194.58.121.174:8080/?service=payment&arguments=\"command=WITHDRAW_CASH amp Cash=20\"\n"+
        "http://194.58.121.174:8080/?service=payment&arguments=\"command=SHOW_CASH\"\n";

        public static String GetGatewayHostPort() {
            return gatewayHostPort;
        }
        public static String getRestApiCommand() {
            return restApiCommand;
        }
        public static int GetTimeout() {
        return timeout;
    }
}
