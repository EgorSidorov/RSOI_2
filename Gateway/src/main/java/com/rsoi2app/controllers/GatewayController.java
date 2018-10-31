package com.rsoi2app.controllers;

import com.rsoi2app.config.Startup;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@Controller
public class GatewayController {

	@GetMapping("/Login")
	public String Login(@RequestParam(name="username", required=false, defaultValue= "") String username,
										  @RequestParam(name="password", required=false, defaultValue= "") String password,
										  Model model,
										  HttpServletResponse response) throws IOException, ParseException {
		if(!username.isEmpty() && !password.isEmpty() && !username.contains(" ") && !password.contains(" "))
		{
			String responseStr1 = RequestForService(Startup.GetGatewayHostPort()+"/service/account/Login?username="+username+"&password="+password,"none");
			if (!responseStr1.contains("Error:")) {
				JSONParser parser = new JSONParser();
				JSONObject userJson = (JSONObject) parser.parse(responseStr1);
				if(userJson.get("Status").toString().equals("Success")) {
					response.addCookie(new Cookie("Token", userJson.get("Cookie").toString()));
					model.addAttribute("name","Success");
				}
				else {
					model.addAttribute("name","Error login");
					response.setStatus(401);
				}
			}
			else {
				model.addAttribute("name", "Error login");
				response.setStatus(500);
			}


		}
		else {
			model.addAttribute("name","Error parameters");
			response.setStatus(400);
		}
		return "greeting";
	}

	@GetMapping("/Show_Calls")
	public String ShowCalls(
						@RequestParam(name="page", required=false, defaultValue= "") String page,
						@CookieValue(name="Token", defaultValue= "") String token,
						Model model,
						HttpServletResponse response) throws IOException, ParseException {
		if(!token.isEmpty() && !token.contains(" ") && !page.contains(" "))
		{
			boolean status1 = false;
			boolean status2 = false;
			JSONObject userJson = new JSONObject();
			String username = "";
			String responseStr1 = RequestForService(Startup.GetGatewayHostPort()+"/service/account/UserInfo",token);
			if (!responseStr1.contains("Error:")) {
				JSONParser parser = new JSONParser();
				userJson = (JSONObject) parser.parse(responseStr1);
				if(userJson.get("Status").equals("Success") && userJson.get("IsLogged").toString().equals("true")) {
					username = userJson.get("username").toString();
					status1 = true;
				}
			}
			if(status1) {
				String responseStr2 = RequestForService(Startup.GetGatewayHostPort() + "/service/calls/Show?username=" + username+"&page="+page, "none");
				if (!responseStr2.contains("Error:")) {
					JSONParser parser = new JSONParser();
					userJson = (JSONObject) parser.parse(responseStr2);
					if (userJson.get("Status").toString().equals("Success")) {
						status2 = true;
					}
				}
			}
			if(status2)
				model.addAttribute("name",userJson.get("History"));
			else
				model.addAttribute("name","Error token");
		}
		else {
			model.addAttribute("name","Error parameters");
			response.setStatus(400);
		}
		return "greeting";
	}

	@GetMapping("/Show_Cash")
	public String ShowCash(@CookieValue(name="Token", defaultValue= "") String token,
						Model model,
						HttpServletResponse response) throws IOException, ParseException {
		if(!token.isEmpty() && !token.contains(" "))
		{
			boolean status1 = false;
			boolean status2 = false;
			JSONObject userJson = new JSONObject();
			String username = "";
			String responseStr1 = RequestForService(Startup.GetGatewayHostPort()+"/service/account/UserInfo",token);
			if (!responseStr1.contains("Error:")) {
				JSONParser parser = new JSONParser();
				userJson = (JSONObject) parser.parse(responseStr1);
				if(userJson.get("Status").equals("Success") && userJson.get("IsLogged").toString().equals("true")) {
					username = userJson.get("username").toString();
					status1 = true;
				}
			}
			if(status1) {
				String responseStr2 = RequestForService(Startup.GetGatewayHostPort() + "/service/payment/Show_cash?username=" + username, "none");
				if (!responseStr2.contains("Error:")) {
					JSONParser parser = new JSONParser();
					userJson = (JSONObject) parser.parse(responseStr2);
					if (userJson.get("Status").equals("Success")) {
						status2 = true;
					}
				}
			}
			if(status2)
				model.addAttribute("name",userJson.getAsString("cash"));
			else
				model.addAttribute("name","Error token");
		}
		else {
			model.addAttribute("name","Error parameters");
			response.setStatus(400);
		}
		return "greeting";
	}

	@GetMapping("/Register")
	public String Login(@RequestParam(name="username", required=false, defaultValue= "") String username,
						@RequestParam(name="password", required=false, defaultValue= "") String password,
						@RequestParam(name="role", required=false, defaultValue= "") String role,
						Model model,
						HttpServletResponse response) throws IOException, ParseException {
		if(!username.isEmpty() && !password.isEmpty() && !username.contains(" ") && !password.contains(" "))
		{
			boolean status1 = false;
			boolean status2 = false;
			String responseStr1 = RequestForService(Startup.GetGatewayHostPort()+"/service/account/Create?username="+username+"&password="+password+"&role="+role,"none");
			if (!responseStr1.contains("Error:")) {
				JSONParser parser = new JSONParser();
				JSONObject userJson = (JSONObject) parser.parse(responseStr1);
				if(userJson.get("Status").equals("Success")) {
					response.addCookie(new Cookie("Token", userJson.get("Cookie").toString()));
					status1 = true;
				}
			}
			String responseStr2 = RequestForService(Startup.GetGatewayHostPort()+"/service/payment/New_purse?username="+username,"none");
			if (!responseStr2.contains("Error:")) {
				JSONParser parser = new JSONParser();
				JSONObject userJson = (JSONObject) parser.parse(responseStr2);
				if(userJson.get("Status").equals("Success")) {
					status2 = true;
				}
			}
			if(status1 && status2)
				model.addAttribute("name","Success");
			else
				model.addAttribute("name","Error create user or purse");
		}
		else {
			model.addAttribute("name","Error parameters");
			response.setStatus(400);
		}
		return "greeting";
	}

	@GetMapping("/Add_Cash")
	public String AddCash(@CookieValue(name="Token", defaultValue= "") String token,
						  @RequestParam(name="cash", required=false, defaultValue= "") String cash,
						   Model model,
						   HttpServletResponse response) throws IOException, ParseException {
		if(!token.isEmpty() && !token.contains(" ") && !cash.contains(" "))
		{
			boolean status1 = false;
			boolean status2 = false;
			JSONObject userJson = new JSONObject();
			String username = "";
			String responseStr1 = RequestForService(Startup.GetGatewayHostPort()+"/service/account/UserInfo",token);
			if (!responseStr1.contains("Error:")) {
				JSONParser parser = new JSONParser();
				userJson = (JSONObject) parser.parse(responseStr1);
				if(userJson.get("Status").equals("Success") && userJson.get("IsLogged").toString().equals("true")) {
					username = userJson.get("username").toString();
					status1 = true;
				}
			}
			if(status1) {
				String responseStr2 = RequestForService(Startup.GetGatewayHostPort() + "/service/payment/Add_cash?username=" + username+"&cash="+cash, "none");
				if (!responseStr2.contains("Error:")) {
					JSONParser parser = new JSONParser();
					userJson = (JSONObject) parser.parse(responseStr2);
					if (userJson.get("Status").equals("Success")) {
						status2 = true;
					}
				}
			}
			if(status2)
				model.addAttribute("name","Success add");
			else
				model.addAttribute("name","Error token");
		}
		else {
			model.addAttribute("name","Error parameters");
			response.setStatus(400);
		}
		return "greeting";
	}


	@GetMapping("/Withdraw_Cash")
	public String WithdrawCash(@CookieValue(name="Token", defaultValue= "") String token,
							   @RequestParam(name="cash", required=false, defaultValue= "") String cash,
							   Model model,
							   HttpServletResponse response) throws IOException, ParseException {
		if(!token.isEmpty() && !token.contains(" ") && !cash.contains(" "))
		{
			boolean status1 = false;
			boolean status2 = false;
			JSONObject userJson = new JSONObject();
			String username = "";
			String responseStr1 = RequestForService(Startup.GetGatewayHostPort()+"/service/account/UserInfo",token);
			if (!responseStr1.contains("Error:")) {
				JSONParser parser = new JSONParser();
				userJson = (JSONObject) parser.parse(responseStr1);
				if(userJson.get("Status").equals("Success") && userJson.get("IsLogged").toString().equals("true")) {
					username = userJson.get("username").toString();
					status1 = true;
				}
			}
			if(status1) {
				String responseStr2 = RequestForService(Startup.GetGatewayHostPort() + "/service/payment/Withdraw_cash?username=" + username+"&cash="+cash, "none");
				if (!responseStr2.contains("Error:")) {
					JSONParser parser = new JSONParser();
					userJson = (JSONObject) parser.parse(responseStr2);
					if (userJson.get("Status").equals("Success")) {
						status2 = true;
					}
				}
			}
			if(status2)
				model.addAttribute("name","Success withdraw");
			else
				model.addAttribute("name","Error token");
		}
		else {
			model.addAttribute("name","Error parameters");
			response.setStatus(400);
		}
		return "greeting";
	}

	@GetMapping("/Add_call")
	public String AddCall(@CookieValue(name="Token", defaultValue= "") String token,
						  @RequestParam(name="duration", required=false, defaultValue= "") String duration,
						  Model model,
						  HttpServletResponse response) throws IOException, ParseException {
		if(!token.isEmpty() && !token.contains(" ") && !duration.contains(" "))
		{
			boolean status1 = false;
			boolean status2 = false;
			JSONObject userJson = new JSONObject();
			String username = "";
			String responseStr1 = RequestForService(Startup.GetGatewayHostPort()+"/service/account/UserInfo",token);
			if (!responseStr1.contains("Error:")) {
				JSONParser parser = new JSONParser();
				userJson = (JSONObject) parser.parse(responseStr1);
				if(userJson.get("Status").equals("Success") && userJson.get("IsLogged").toString().equals("true")) {
					username = userJson.get("username").toString();
					status1 = true;
				}
			}
			if(status1) {
				String responseStr2 = RequestForService(Startup.GetGatewayHostPort() + "/service/calls/New?username=" + username+"&duration="+duration, "none");
				if (!responseStr2.contains("Error:")) {
					JSONParser parser = new JSONParser();
					userJson = (JSONObject) parser.parse(responseStr2);
					if (userJson.get("Status").equals("Success")) {
						status2 = true;
					}
				}
			}
			if(status2)
				model.addAttribute("name","Success add");
			else
				model.addAttribute("name","Error token");
		}
		else {
			model.addAttribute("name","Error parameters");
			response.setStatus(400);
		}
		return "greeting";
	}

	@GetMapping("/Account_logs")
	public String AccountLogs(@CookieValue(name="Token", defaultValue= "") String token,
						  @RequestParam(name="page", required=false, defaultValue= "") String page,
						  Model model,
						  HttpServletResponse response) throws IOException, ParseException {
		if(!token.isEmpty() && !token.contains(" ") && !page.contains(" "))
		{
			boolean status1 = false;
			JSONObject userJson = new JSONObject();
			String logs = "";
			String responseStr1 = RequestForService(Startup.GetGatewayHostPort()+"/service/account/Logs?page="+page,token);
			if (!responseStr1.contains("Error:")) {
				JSONParser parser = new JSONParser();
				userJson = (JSONObject) parser.parse(responseStr1);
				if(userJson.get("Status").equals("Success") && userJson.containsKey("logs")) {
					logs = userJson.get("logs").toString();
					status1 = true;
				}
			}
			if(status1)
				model.addAttribute("name",logs);
			else
				model.addAttribute("name","Error token");
		}
		else {
			model.addAttribute("name","Error parameters");
			response.setStatus(400);
		}
		return "greeting";
	}

	@GetMapping("/Calls_logs")
	public String CallsLogs(
							  @RequestParam(name="page", required=false, defaultValue= "") String page,
							  Model model,
							  HttpServletResponse response) throws IOException, ParseException {
		if(!page.contains(" "))
		{
			boolean status1 = false;
			JSONObject userJson = new JSONObject();
			String logs = "";
			String responseStr1 = RequestForService(Startup.GetGatewayHostPort()+"/service/calls/Logs?page="+page,"none");
			if (!responseStr1.contains("Error:")) {
				JSONParser parser = new JSONParser();
				userJson = (JSONObject) parser.parse(responseStr1);
				if(userJson.get("Status").equals("Success") && userJson.containsKey("logs")) {
					logs = userJson.get("logs").toString();
					status1 = true;
				}
			}
			if(status1)
				model.addAttribute("name",logs);
			else
				model.addAttribute("name","Error");
		}
		else {
			model.addAttribute("name","Error parameters");
			response.setStatus(400);
		}
		return "greeting";
	}

	@GetMapping("/Payment_logs")
	public String PaymentLogs(
			@RequestParam(name="page", required=false, defaultValue= "") String page,
			Model model,
			HttpServletResponse response) throws IOException, ParseException {
		if(!page.contains(" "))
		{
			boolean status1 = false;
			JSONObject userJson = new JSONObject();
			String logs = "";
			String responseStr1 = RequestForService(Startup.GetGatewayHostPort()+"/service/payment/Logs?page="+page,"none");
			if (!responseStr1.contains("Error:")) {
				JSONParser parser = new JSONParser();
				userJson = (JSONObject) parser.parse(responseStr1);
				if(userJson.get("Status").equals("Success") && userJson.containsKey("logs")) {
					logs = userJson.get("logs").toString();
					status1 = true;
				}
			}
			if(status1)
				model.addAttribute("name",logs);
			else
				model.addAttribute("name","Error");
		}
		else {
			model.addAttribute("name","Error parameters");
			response.setStatus(400);
		}
		return "greeting";
	}




/*
	@GetMapping("/UsersList")
	@ResponseBody
	public HashMap<String, Object> UserList(@RequestParam(name="page", required=false, defaultValue= "0") String page,
											@CookieValue(name="Token", defaultValue="") String token,
										 	HttpServletResponse response) {
		AccountModel model = new AccountModel();
		HashMap<String, Object> jsonAnswer = new LinkedHashMap<String, Object>();
		if(!token.isEmpty())
		{
			List<String> userList = model.GetUserNames(token,Integer.parseInt(page));
			if(model.GetQueryStatus()) {
				setStatus(200,response,jsonAnswer);
				jsonAnswer.put("UserList",userList);
			}
			else {
				if(!model.GetDbStatus()) {
					setStatus(500,response,jsonAnswer);
				} else {
					setStatus(404,response,jsonAnswer);
				}
			}
		}
		else {
			setStatus(401,response,jsonAnswer);
		}
		return jsonAnswer;
	}

	@GetMapping("/RoleList")
	@ResponseBody
	public HashMap<String, Object> user(HttpServletResponse response) {
		AccountModel model = new AccountModel();
		HashMap<String, Object> jsonAnswer = new LinkedHashMap<String, Object>();
			List<String> userRoles = model.GetAllRoles();
			if(model.GetQueryStatus()) {
				setStatus(200,response,jsonAnswer);
				jsonAnswer.put("UserRoles",userRoles);
			}
			else {
					setStatus(500,response,jsonAnswer);
			}
		return jsonAnswer;
	}


	@GetMapping("/UserInfo")
	@ResponseBody
	public HashMap<String, Object> UserInfo(@CookieValue(name="Token", defaultValue="") String token,
											HttpServletResponse response) {
		AccountModel model = new AccountModel();
		HashMap<String, Object> jsonAnswer = new LinkedHashMap<String, Object>();
		if(!token.isEmpty())
		{
			boolean IsLogged = model.IsLogged(token);
			boolean queryStatus1 = IsLogged;
			String role = model.GetRole(token);
			boolean queryStatus2 = model.GetQueryStatus();
			String username = model.GetUsername(token);
			boolean queryStatus3 = model.GetQueryStatus();
			if(queryStatus1 & queryStatus2 & queryStatus1) {
				setStatus(200,response,jsonAnswer);
				jsonAnswer.put("IsLogged",IsLogged);
				jsonAnswer.put("username",username);
				jsonAnswer.put("role",role);
			}
			else {
				if(!model.GetDbStatus()) {
					setStatus(500,response,jsonAnswer);
				} else {
					setStatus(401,response,jsonAnswer);
				}
			}
		}
		else {
			setStatus(401,response,jsonAnswer);
		}
		return jsonAnswer;
	}


	@GetMapping("/Create")
	@ResponseBody
	public HashMap<String, Object> Create(@RequestParam(name="username", required=false, defaultValue= "") String username,
										@RequestParam(name="password", required=false, defaultValue= "") String password,
										  @RequestParam(name="role", required=false, defaultValue= "") String role,
										HttpServletResponse response) {
		AccountModel model = new AccountModel();
		HashMap<String, Object> jsonAnswer = new LinkedHashMap<String, Object>();
		if (!username.isEmpty() && !password.isEmpty() && !role.isEmpty()) {
			if (model.CreateUser(username, password, role)) {
				setStatus(201, response, jsonAnswer);
			} else {
				if (!model.GetDbStatus()) {
					setStatus(500, response, jsonAnswer);
				} else {
					setStatus(406, response, jsonAnswer);
				}
			}
		} else {
			setStatus(400, response, jsonAnswer);
		}
		return jsonAnswer;
	}

	@GetMapping("/Logs")
	@ResponseBody
	public HashMap<String, Object> Logs(@CookieValue(name="Token", defaultValue="") String token,
										@RequestParam(name="page", required=false, defaultValue= "0") String page,
											HttpServletResponse response) {
		AccountModel model = new AccountModel();
		HashMap<String, Object> jsonAnswer = new LinkedHashMap<String, Object>();
		if(!token.isEmpty())
		{
			if(model.IsLogged(token)) {
				List<String> logs = model.GetLogs(Integer.parseInt(page));
				if (model.GetQueryStatus()){
					setStatus(200, response, jsonAnswer);
					jsonAnswer.put("logs", logs);
				}
				else {
					setStatus(500,response,jsonAnswer);
				}
			}
			else {
				if(!model.GetDbStatus()) {
					setStatus(500,response,jsonAnswer);
				} else {
					setStatus(401,response,jsonAnswer);
				}
			}
		}
		else {
			setStatus(401,response,jsonAnswer);
		}
		return jsonAnswer;
	}


*/

private String RequestForService(String urlString,String cookie)  throws IOException
{
	InputStream is;
	InputStreamReader reader;
	try {
		URLConnection connection = new URL(urlString).openConnection();
		connection.setConnectTimeout(Startup.GetTimeout());
		connection.setDoOutput(true);
		connection.setDoInput(true);
		if(!cookie.contains("none"))
			connection.addRequestProperty("Cookie", "Token=" + cookie);
		is = connection.getInputStream();
		reader = new InputStreamReader(is);
	}
	catch (Exception e)
	{
		return "Error:"+e.getMessage();
	}
	char[] buffer = new char[1024];
	int rc;

	StringBuilder sb = new StringBuilder();
	while ((rc = reader.read(buffer)) != -1)
		sb.append(buffer, 0, rc);
	reader.close();
	return sb.toString();
}
}