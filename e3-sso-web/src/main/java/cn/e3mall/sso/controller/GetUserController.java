package cn.e3mall.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.sso.service.GetUserService;
import cn.e3mall.utils.E3Result;

@Controller
public class GetUserController {
	@Autowired
	private GetUserService getUserService;
	
	@RequestMapping("/user/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token,String callback){
		E3Result userByToken = getUserService.getUserByToken(token);
		if(StringUtils.isNoneBlank(callback)){
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(userByToken);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return userByToken;
		
	}
}
