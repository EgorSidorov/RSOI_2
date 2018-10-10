import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nameService = request.getParameter("service");
        if( nameService!=null)
        {
            String arguments = request.getParameter("arguments");
            arguments = arguments.substring(1).substring(0,arguments.length()-2);
            String urlString = "http://localhost:8080/"+nameService+"?"+arguments;
            if(nameService.equals("Account") && arguments.equals("INSERT_USER_WITH_CASH")) {
                int aaa;
            }
            else if(nameService.equals("Calls") && arguments.equals("INSERT_CALL_WITH_CASH")) {
                int bbb;
            }
            else if(nameService.equals("Calls") && arguments.equals("GET_CALL_HISTORY")) {
                int ccc;
            }
            URLConnection connection = new URL(urlString).openConnection();
            InputStream is = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(is);
            char[] buffer = new char[1024];
            int rc;

            StringBuilder sb = new StringBuilder();

            while ((rc = reader.read(buffer)) != -1)
                sb.append(buffer, 0, rc);

            reader.close();

            response.getWriter().write(sb.toString());
        }
        else response.getWriter().write("You can request for service Account, Calls, Payment");
    }
}