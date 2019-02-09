package cn.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.sso.service.LoginService;
import cn.e3mall.utils.CookieUtils;
import cn.e3mall.utils.E3Result;

@Controller
public class LoginController {
	@Autowired
	private LoginService loginService;
	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;
	
	@RequestMapping("/page/login")
	public String showlogin(){
		return "login";
	}
	
	@RequestMapping("/user/login")
	@ResponseBody
	public E3Result LoginByUser(String username,String password,
			HttpServletRequest request,HttpServletResponse response){
		E3Result e3Result = loginService.LoginByUser(username, password);
		if(e3Result.getStatus()==200){
			String token=(String)e3Result.getData();
			CookieUtils.setCookie(request, response, COOKIE_TOKEN_KEY, token);
		}
		return e3Result;
	}
}
