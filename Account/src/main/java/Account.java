import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
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
        RequestGetPost(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestGetPost(request,response);
    }

    private void RequestGetPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        AccountModel model = new AccountModel(false);
        if(model.GetDbStatus())
        {
            String type_command = request.getParameter("command");
            if(type_command != null)
            {
                String username = request.getParameter("Username");
                String password = request.getParameter("Password");
                String role = request.getParameter("Role");
                String token = "";
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals("Token")) {
                            token = cookie.getValue();
                        }
                    }
                }
                String page = request.getParameter("page");
                if( type_command.equals("CREATE_USER"))
                {
                    if(username != null && password != null && role != null)
                    {
                        if(model.CreateUser(username,password,role))
                            response.getWriter().write("Success create");
                        else response.getWriter().write("Error create");
                    }
                    else response.getWriter().write("Error.You should input First_Name, Last_Name_Name, Role");
                }
                else if( type_command.equals("LOGIN"))
                {
                    if(username != null && password != null)
                    {
                        String authCookie = model.Login(username,password);
                        if(model.GetQueryStatus()) {
                            response.addCookie(new Cookie("Token", authCookie));
                            response.getWriter().write(authCookie);
                        }
                        else response.getWriter().write("Error login");
                    }
                    else response.getWriter().write("Error.You should input Username, Password");
                }
                else if( type_command.equals("LOGOUT"))
                {
                    if(token != null)
                    {
                        if(model.Logout(token))
                            response.getWriter().write("Success logout");
                        else response.getWriter().write("Error logout");
                    }
                    else response.getWriter().write("Error.You should login");
                }
                else if( type_command.equals("IS_LOGGED"))
                {
                    if(token!= null)
                    {
                        if(model.IsLogged(token))
                            response.getWriter().write("Logged");
                        else response.getWriter().write("Error.No logged");
                    }
                    else response.getWriter().write("Error.You should login");
                }
                else if( type_command.equals("GET_USERNAME") )
                {
                    if(token != null)
                    {
                        response.getWriter().write(model.GetUsername(token));
                    }
                    else response.getWriter().write("Error.Token is empty");
                }
                else if( type_command.equals("GET_ROLE") )
                {
                    if(token != null)
                    {
                        response.getWriter().write(model.GetRole(token));
                    }
                    else response.getWriter().write("Error.Token is empty");
                }
                else if( type_command.equals("SHOW_ALL_USERS") )
                {
                    if(token != null) {
                        if (page != null)
                            response.getWriter().write(String.valueOf(model.GetUserNames(token, Integer.parseInt(page))));
                        else response.getWriter().write(String.valueOf(model.GetUserNames(token, 0)));
                    } else response.getWriter().write("Error.Token is empty");
                }
                else if( type_command.equals("SHOW_ALL_ROLES") )
                {
                    response.getWriter().write(String.valueOf(model.GetAllRoles()));
                }
                else response.getWriter().write("Error.Unknown command");
            }
            else response.getWriter().write("Error.Parameter command has passed");
        }
        else response.getWriter().write("Error db connection");
    }
}
