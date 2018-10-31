package com.rsoi2app_account.controllers;

import com.rsoi2app_account.models.AccountModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
public class AccountController {

	@GetMapping("/Login")
	@ResponseBody
	public HashMap<String, Object> Login(@RequestParam(name="username", required=false, defaultValue= "") String username,
										  @RequestParam(name="password", required=false, defaultValue= "") String password,
										  HttpServletResponse response) {
		AccountModel model = new AccountModel();
		model.SetLogs("/Login?username="+username+"&password="+password);
		HashMap<String, Object> jsonAnswer = new LinkedHashMap<String, Object>();
		if(!username.isEmpty() && !password.isEmpty())
		{
			String authCookie = model.Login(username,password);
			if(model.GetQueryStatus()) {
				response.addCookie(new Cookie("Token", authCookie));
				jsonAnswer.put("Cookie",authCookie);
				setStatus(200,response,jsonAnswer);
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
			setStatus(400,response,jsonAnswer);
		}
		return jsonAnswer;
	}


	@GetMapping("/UsersList")
	@ResponseBody
	public HashMap<String, Object> UserList(@RequestParam(name="page", required=false, defaultValue= "0") String page,
											@CookieValue(name="Token", defaultValue="") String token,
										 	HttpServletResponse response) {
		AccountModel model = new AccountModel();
		model.SetLogs("/UsersList?Token="+token+"&page="+page);
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
		model.SetLogs("/RoleList");
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
		model.SetLogs("/UserInfo?Token="+token);
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
		model.SetLogs("/Create?username="+username+"&password="+password+"&password="+role);
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



	void setStatus(int status, HttpServletResponse response, HashMap<String, Object> jsonAnswer)
	{
		response.setStatus(status);
		if(status == 200)//Success
		{
			jsonAnswer.put("Status","Success");
			response.setStatus(200);//Success
		}
		if(status == 201)//Created
		{
			jsonAnswer.put("Status","Success");
		}
		if(status == 500)//Internal Server Error
		{
			jsonAnswer.put("Status", "Error");
			jsonAnswer.put("Status message", "Internal Server Error");
		}
		if(status == 404)//Not Found
		{
			jsonAnswer.put("Status","Error");
			jsonAnswer.put("Status message","Not Found");
		}
		if(status == 401)//Unauthorized
		{
			jsonAnswer.put("Status","Error");
			jsonAnswer.put("Status message","Unauthorized");
		}
		if(status == 400)//Bad Request
		{
			jsonAnswer.put("Status","Error");
			jsonAnswer.put("Status message","Bad Request");
		}
		if(status == 406)//Not Acceptable
		{
			jsonAnswer.put("Status","Error");
			jsonAnswer.put("Status message","Username is used");
		}
	}

}