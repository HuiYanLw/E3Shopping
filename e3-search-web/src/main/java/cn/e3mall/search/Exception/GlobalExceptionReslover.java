package cn.e3mall.search.Exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class GlobalExceptionReslover implements HandlerExceptionResolver {

	Logger logger=LoggerFactory.getLogger(GlobalExceptionReslover.class);
	@Override
	public ModelAndView resolveException(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
			Exception arg3) {
		// TODO Auto-generated method stub
		logger.error("系统发生异常",arg3);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", "系统发生异常，请稍后重试");
		modelAndView.setViewName("error/exception");
		return modelAndView;
	}

}
