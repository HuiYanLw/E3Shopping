package cn.e3mall.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.UserService;
import cn.e3mall.utils.E3Result;

@Controller
public class RegitsterController {
	@Autowired
	private UserService userService;
	
	@RequestMapping("/page/register")
	public String showRegitster(){
		return "register";
	}
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3Result CheckDataToUser(@PathVariable String param,@PathVariable Integer type){
		return userService.CheckDataToUser(param, type);
	}
	
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public E3Result RegisterUser(TbUser tbUser){
		return userService.RegisterUser(tbUser);
	}
}
