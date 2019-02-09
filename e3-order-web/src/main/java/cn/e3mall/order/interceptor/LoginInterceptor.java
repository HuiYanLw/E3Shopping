package cn.e3mall.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.GetUserService;
import cn.e3mall.utils.CookieUtils;
import cn.e3mall.utils.E3Result;
import cn.e3mall.utils.JsonUtils;

public class LoginInterceptor implements HandlerInterceptor {
	@Value("${SSO_URL}")
	private String SSO_URL;
	@Autowired
	private GetUserService getUserService;
	@Autowired
	private CartService cartService;
	
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
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		// TODO Auto-generated method stub
		String value = CookieUtils.getCookieValue(request, "token");
		if(StringUtils.isBlank(value)){
			//先跳转到登录页面，登录成功后再跳转到当前请求的url
			response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL());
			return false;
		}
		E3Result e3Result = getUserService.getUserByToken(value);
		if(e3Result.getStatus()!=200){
			//先跳转到登录页面，登录成功后再跳转到当前请求的url
			response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL());
			return false;
		}
		TbUser user=(TbUser)e3Result.getData();
		request.setAttribute("user", user);
		String cookieValue = CookieUtils.getCookieValue(request, "cart",true);
		if(StringUtils.isNoneBlank(cookieValue)){
			cartService.mergeCart(user.getId(), JsonUtils.jsonToList(cookieValue, TbItem.class));
		}
		return true;
	}

}
