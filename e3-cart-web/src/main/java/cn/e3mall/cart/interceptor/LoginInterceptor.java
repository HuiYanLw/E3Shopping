package cn.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.GetUserService;
import cn.e3mall.utils.CookieUtils;
import cn.e3mall.utils.E3Result;

public class LoginInterceptor implements HandlerInterceptor{
	@Autowired
	private GetUserService getUserService;
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse arg1, Object arg2) throws Exception {
		// TODO Auto-generated method stub
		//前处理，执行handler之前执行方法
		String token = CookieUtils.getCookieValue(request, "token");
		//未登录状态
		if(StringUtils.isBlank(token)){
			return true;
		}
		E3Result e3Result = getUserService.getUserByToken(token);
		//登录但已过期状态
		if(e3Result.getStatus()!=200){
			return true;
		}
		TbUser user = (TbUser)e3Result.getData();
		//将user放入request域中，在Controller判断是否有值来判断是否登录
		request.setAttribute("user", user);
		return true;
	}

}
