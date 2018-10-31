package com.rsoi2app.controllers;

import com.rsoi2app.config.Startup;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
		if(!username.isEmpty() && !password.isEmpty())
		{
			boolean status1 = false;
			String responseStr1 = RequestForService(Startup.GetGatewayHostPort()+"/service/account/Login?username="+username+"&password="+password,"none");
			if (!responseStr1.contains("Error:")) {
				JSONParser parser = new JSONParser();
				JSONObject userJson = (JSONObject) parser.parse(responseStr1);
				if(userJson.get("Status").equals("Success")) {
					response.addCookie(new Cookie("Token", userJson.get("Cookie").toString()));
					status1 = true;
				}
			}
			else {
				model.addAttribute("name", "Service is not responsed");
				response.setStatus(500);
			}


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
		if(!username.isEmpty() && !password.isEmpty())
		{
			boolean status1 = false;
			boolean status2 = true;
			String responseStr1 = RequestForService(Startup.GetGatewayHostPort()+"/service/account/Create?username="+username+"&password="+password+"&role="+role,"none");
			if (!responseStr1.contains("Error:")) {
				JSONParser parser = new JSONParser();
				JSONObject userJson = (JSONObject) parser.parse(responseStr1);
				if(userJson.get("Status").equals("Success")) {
					response.addCookie(new Cookie("Token", userJson.get("Cookie").toString()));
					status1 = true;
				}
			}
			String responseStr2 = RequestForService(Startup.GetGatewayHostPort()+"/service/payment/New_pursy?username="+username,"none");
			if (!responseStr2.contains("Error:")) {
				JSONParser parser = new JSONParser();
				JSONObject userJson = (JSONObject) parser.parse(responseStr2);
				if(userJson.get("Status").equals("Success")) {
					response.addCookie(new Cookie("Token", userJson.get("Cookie").toString()));
					status2 = true;
				}
			}
			if(status1 && status2)
				model.addAttribute("name","Success");
			else
				model.addAttribute("name","Error create user");
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