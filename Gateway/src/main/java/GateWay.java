import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Егор on 07.10.2018.
 */
@WebServlet(name = "GateWay")
public class GateWay extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestGetPost(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestGetPost(request,response);
    }

    private void RequestGetPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String nameService = request.getParameter("service");
        String token = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Token")) {
                    token = cookie.getValue();
                }
            }
        }
        if( nameService!=null)
        {
            String arguments = request.getParameter("arguments");
            String username = request.getParameter("Username");
            String password = request.getParameter("Password");
            String cash = request.getParameter("Cash");
            String role = request.getParameter("Role");
            String duration = request.getParameter("Duration");
            arguments = arguments.substring(1).substring(0,arguments.length()-2);
            arguments = arguments.replaceAll(" amp ","&");
            String urlString = "http://"+Startup.GetGatewayHostPort()+"/"+nameService+"?"+arguments;
            if(nameService.equals("Cross") && arguments.equals("CREATE_USER")){
                if(RequestForService("http://"+Startup.GetGatewayHostPort()+"/account?command=CREATE_USER&Username=" + username + "&Password=" + password+"&Role=" + role,token).contains("Error")) {
                    response.getWriter().write("Error create account");
                    return;
                }
                if(RequestForService("http://"+Startup.GetGatewayHostPort()+"/payment?command=CREATE_PURSY&Username=" + username,token).contains("Error")){
                    response.getWriter().write("Error create pursy");
                    return;
                }
                response.getWriter().write("Success create account");
            }
            else if(nameService.equals("Cross") && arguments.equals("LOGIN")){
                String responseString = RequestForService("http://"+Startup.GetGatewayHostPort()+"/account?command=LOGIN&Username=" + username + "&Password=" + password,token);
                if(responseString.contains("Error")) {
                    response.getWriter().write("Error login");
                    return;
                }
                response.addCookie(new Cookie("Token", responseString));
                response.getWriter().write("Success login");
            }
            else if(nameService.equals("Cross") && arguments.equals("WITHDRAW_CASH")) {
                String responseString = RequestForService("http://"+Startup.GetGatewayHostPort()+"/account?command=GET_USERNAME",token);
                if(responseString.contains("Error")) {
                    response.getWriter().write("Error get username");
                    return;
                }
                responseString = RequestForService("http://"+Startup.GetGatewayHostPort()+"/payment?command=WITHDRAW_CASH&Username="+responseString+"&Cash="+cash,token);
                if(responseString.contains("Error")) {
                    response.getWriter().write("Error wildcard cash");
                    return;
                }
                response.getWriter().write("Success withdraw cash");
            }
            else if(nameService.equals("Cross") && arguments.equals("ADD_CASH")) {
                String responseString = RequestForService("http://"+Startup.GetGatewayHostPort()+"/account?command=GET_USERNAME",token);
                if(responseString.contains("Error")) {
                    response.getWriter().write("Error get username");
                    return;
                }
                responseString = RequestForService("http://"+Startup.GetGatewayHostPort()+"/payment?command=ADD_CASH&Username="+responseString+"&Cash="+cash,token);
                if(responseString.contains("Error")) {
                    response.getWriter().write("Error add cash");
                    return;
                }
                response.getWriter().write("Success add cash");
            }
            else if(nameService.equals("Cross") && arguments.equals("ADD_CALL")) {
                String responseString = RequestForService("http://"+Startup.GetGatewayHostPort()+"/account?command=GET_USERNAME",token);
                if(responseString.contains("Error")) {
                    response.getWriter().write("Error get username");
                    return;
                }
                responseString = RequestForService("http://"+Startup.GetGatewayHostPort()+"/calls?command=ADD_CALL&Username="+responseString+"&Duration="+duration,token);
                if(responseString.contains("Error")) {
                    response.getWriter().write("Error add call");
                    return;
                }
                response.getWriter().write("Success add call");
            }
            else if(nameService.equals("Cross") && arguments.equals("SHOW_CASH")) {
                String responseString = RequestForService("http://"+Startup.GetGatewayHostPort()+"/account?command=GET_USERNAME",token);
                if(responseString.contains("Error")) {
                    response.getWriter().write("Error get username");
                    return;
                }
                responseString = RequestForService("http://"+Startup.GetGatewayHostPort()+"/payment?command=SHOW_CASH&Username="+responseString,token);
                if(responseString.contains("Error")) {
                    response.getWriter().write("Error get cash");
                    return;
                }
                response.getWriter().write(responseString);
            }
            else if(nameService.equals("Cross") && arguments.equals("SHOW_CALL_HISTORY")) {
                String responseString = RequestForService("http://"+Startup.GetGatewayHostPort()+"/account?command=GET_USERNAME",token);
                if(responseString.contains("Error")) {
                    response.getWriter().write("Error get username");
                    return;
                }
                responseString = RequestForService("http://"+Startup.GetGatewayHostPort()+"/calls?command=SHOW_CALL_HISTORY&Username="+responseString,token);
                if(responseString.contains("Error")) {
                    response.getWriter().write("Error get call history");
                    return;
                }
                response.getWriter().write(responseString);
            } else response.getWriter().write(RequestForService(urlString,token));
        } else response.getWriter().write("You can request for service Account, Calls, Payment, Cross\n" +Startup.getRestApiCommand());
    }

    private String RequestForService(String urlString,String cookie)  throws IOException
    {
        URLConnection connection = new URL(urlString).openConnection();
        connection.addRequestProperty("Cookie","Token="+cookie);
        InputStream is = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(is);
        char[] buffer = new char[1024];
        int rc;

        StringBuilder sb = new StringBuilder();
        while ((rc = reader.read(buffer)) != -1)
            sb.append(buffer, 0, rc);
        reader.close();
        return sb.toString();
    }
}
