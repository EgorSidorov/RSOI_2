import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Егор on 07.10.2018.
 */
@WebServlet(name = "Payment")
public class Payment extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestGetPost(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestGetPost(request,response);
    }

    private void RequestGetPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        PaymentModel model = new PaymentModel();
        if(model.GetDbStatus())
        {
            String type_command = request.getParameter("command");
            String cash = request.getParameter("Cash");
            String username = request.getParameter("Username");
            if(type_command != null)
            {
                if( type_command.equals("ADD_CASH"))
                {
                    if(cash != null || username != null)
                    {
                        if(model.AddCash(cash,username))
                            response.getWriter().write("Success add");
                        else response.getWriter().write("Error add");
                    }
                    else response.getWriter().write("Error.You should input First_Name, Last_Name_Name, Role");
                }
                else if( type_command.equals("WITHDRAW_CASH"))
                {
                    if(cash != null || username != null)
                    {
                        if(model.WithdrawCash(cash,username))
                            response.getWriter().write("Success withdraw");
                        else response.getWriter().write("Error withdraw");
                    }
                    else response.getWriter().write("Error.You should input First_Name, Last_Name_Name, Role");
                }
                else if( type_command.equals("CREATE_PURSY"))
                {
                    if(username != null)
                    {
                        if(model.CreatePursy(username))
                            response.getWriter().write("Success create pursy");
                        else response.getWriter().write("Error create pursy");
                    }
                    else response.getWriter().write("Error.You should input First_Name, Last_Name_Name, Role");
                }
                else if( type_command.equals("SHOW_CASH"))
                {
                    if(username != null)
                    {
                        String outputCash = model.ShowCash(username);
                        if(model.GetQueryStatus())
                            response.getWriter().write(outputCash);
                        else response.getWriter().write("Error get cash");
                    }
                    else response.getWriter().write("Error.You should input First_Name, Last_Name_Name, Role");
                }
                else response.getWriter().write("Unknown command");
            }
            else response.getWriter().write("Parameter command has passed");
        }
        else response.getWriter().write("Error db connection");
    }
}
