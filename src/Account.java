import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Егор on 07.10.2018.
 */
@WebServlet(name = "Account")
public class Account extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountModel model = new AccountModel();
        if(model.GetDbStatus())
        {
            String type_command = request.getParameter("command");
            if(type_command != null)
            {
                if( type_command.equals("INSERT"))
                {
                    String FirstName = request.getParameter("First_Name");
                    String LastName = request.getParameter("Last_Name");
                    String Role = request.getParameter("Role");
                    if(FirstName != null || LastName != null || Role != null)
                    {
                        if(model.InsertUser(FirstName,LastName,Role))
                            response.getWriter().write("Success insert");
                        else response.getWriter().write("Error insert");
                    }
                    else response.getWriter().write("You should input First_Name, Last_Name_Name, Role");
                }
                else if( type_command.equals("SHOW_ALL_USERS") )
                {
                    String page = request.getParameter("page");
                    if(page != null)
                        response.getWriter().write(String.valueOf(model.GetUserNames(Integer.parseInt(page))));
                    else response.getWriter().write(String.valueOf(model.GetUserNames(0)));
                }
                else if( type_command.equals("SHOW_ALL_ROLES") )
                {
                    response.getWriter().write(String.valueOf(model.GetAllRoles()));
                }
                else response.getWriter().write("Unknown command");
            }
            else response.getWriter().write("Parameter command has passed");
        }
        else response.getWriter().write("Error db connection");
    }
}
