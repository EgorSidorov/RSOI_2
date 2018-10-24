import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Егор on 07.10.2018.
 */
@WebServlet(name = "Calls")
public class Calls extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestGetPost(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestGetPost(request,response);
    }

    private void RequestGetPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        CallsModel model = new CallsModel(false);
        model.SetLogs(request.getRequestURI());
        if(model.GetDbStatus())
        {
            String type_command = request.getParameter("command");
            String page = request.getParameter("page");
            String duration = request.getParameter("Duration");
            String username = request.getParameter("Username");
            if(type_command != null)
            {
                if( type_command.equals("ADD_CALL"))
                {
                    if(duration != null || username != null)
                    {
                        if(model.AddCall(duration,username))
                            response.getWriter().write("Success insert");
                        else response.getWriter().write("Error insert");
                    }
                    else response.getWriter().write("Error.You should input First_Name, Last_Name_Name, Role");
                }
                else if( type_command.equals("SHOW_CALL_HISTORY") )
                {
                    if(page != null)
                        response.getWriter().write(String.valueOf(model.ShowCallHistory(username,Integer.parseInt(page))));
                    else response.getWriter().write(String.valueOf(model.ShowCallHistory(username,0)));
                }
                else if( type_command.equals("SHOW_LOGS") )
                {
                        if (page != null)
                            response.getWriter().write(String.valueOf(model.GetLogs(Integer.parseInt(page))));
                        else response.getWriter().write(String.valueOf(model.GetLogs(0)));
                }
                else response.getWriter().write("Error.Unknown command");
            }
            else response.getWriter().write("Error.Parameter command has passed");
        }
        else response.getWriter().write("Error db connection");
    }
}
