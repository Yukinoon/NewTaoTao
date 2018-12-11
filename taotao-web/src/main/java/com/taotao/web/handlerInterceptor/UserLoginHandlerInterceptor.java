package com.taotao.web.handlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.utils.CookieUtils;
import com.taotao.web.ThreadLocal.UserThreadLocal;
import com.taotao.web.bean.User;
import com.taotao.web.service.UserService;

public class UserLoginHandlerInterceptor implements HandlerInterceptor {
	
	public static final String TAOTAO_TOKEN = "TT_TOKEN";
	
	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//先获取cookie中的token
		String token = CookieUtils.getCookieValue(request, TAOTAO_TOKEN);
		if(StringUtils.isEmpty(token)){
			//未登陆，跳转到登陆页面
			response.sendRedirect(userService.SSO_TAOTAO_URL+"/login.html");
			UserThreadLocal.set(null);
			return false;
		}
		//通过SSO接口查询用户登陆数据
		User user = this.userService.queryUserByToken(token);
		if(user==null){
			//登陆超时
			response.sendRedirect(userService.SSO_TAOTAO_URL+"/login.html");
			UserThreadLocal.set(null);
			return false;
		}
		//已经登陆
		UserThreadLocal.set(user);//放到本地线程中
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
