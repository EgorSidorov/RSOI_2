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

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CallsModel model = new CallsModel();
        if(model.GetDbStatus())
        {
            String type_command = request.getParameter("command");
            if(type_command != null)
            {
                if( type_command.equals("INSERT"))
                {
                    String Duration = request.getParameter("Duration");
                    String IdUser = request.getParameter("ID_User");
                    if(Duration != null || IdUser != null)
                    {
                        if(model.InsertCall(Float.valueOf(Duration),Integer.valueOf(IdUser)))
                            response.getWriter().write("Success insert");
                        else response.getWriter().write("Error insert");
                    }
                    else response.getWriter().write("You should input First_Name, Last_Name_Name, Role");
                }
                else if( type_command.equals("SHOW_ALL_CALLS") )
                {
                    String page = request.getParameter("page");
                    if(page != null)
                        response.getWriter().write(String.valueOf(model.GetCallsHistory(Integer.parseInt(page))));
                    else response.getWriter().write(String.valueOf(model.GetCallsHistory(0)));
                }
                else response.getWriter().write("Unknown command");
            }
            else response.getWriter().write("Parameter command has passed");
        }
        else response.getWriter().write("Error db connection");
    }
}
