package com.taotao.sso.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.utils.CookieUtils;
import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	private static final String TAOTAO_TOKEN = "TT_TOKEN";

	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String toRegister() {
		return "register";
	}

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String toLogin() {
		return "login";
	}

	
	@RequestMapping(value = "user/check/{param}/{type}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> check(@PathVariable("param") String param,
			@PathVariable("type") Integer type) {
		try {
			Boolean bool = this.userService.check(param, type);
			if (bool == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
			return ResponseEntity.ok(bool);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}

	/**
	 * 注册
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "user/doRegister", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doRegister(@Valid User user,
			BindingResult bindingResult) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (bindingResult.hasErrors()) {
			// 输入内容有误
			result.put("status", "4");
			List<ObjectError> allErrors = bindingResult.getAllErrors();
			List<String> msg = new ArrayList<String>();
			for (ObjectError objectError : allErrors) {
				msg.add(objectError.getDefaultMessage());
			}
			result.put("msg", StringUtils.join(msg, "|"));
			return result;
		}

		try {
			Boolean bool = this.userService.saveUser(user);
			if (bool) {
				// 注册成功
				result.put("status", "200");
			} else {
				// 注册失败
				result.put("status", "400");
			}
		} catch (Exception e) {
			// 注册失败
			result.put("status", "500");
			e.printStackTrace();
		}

		return result;

	}

	/**
	 * 登录
	 * @param user
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "user/doLogin", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doRegister(User user,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			String token = this.userService.doLogin(user);
			if (token == null) {
				// 登录失败
				result.put("status", 4);
			} else {
				// 登录成功
				result.put("status", 200);
				// 将token写入到cookie中,会话级别
				CookieUtils.setCookie(request, response, TAOTAO_TOKEN,token);
						
			}
		} catch (Exception e) {
			result.put("status", 5);
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * 根据token查询用户信息
	 * @param token
	 * @return
	 */
	@RequestMapping(value="user/{token}",method=RequestMethod.GET)
	public ResponseEntity<User> queryUserByToken(@PathVariable("token")String token){
		try {
			User user = this.userService.queryUserByToken(token);
			if(user==null){
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

}
