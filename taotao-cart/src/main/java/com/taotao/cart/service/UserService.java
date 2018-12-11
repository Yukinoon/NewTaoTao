package com.taotao.cart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.controller.User;
import com.taotao.common.service.ApiService;

@Service
public class UserService {
	
	@Autowired
	private ApiService apiService;
	
	@Value("${SSO_TAOTAO_URL}")
	public String SSO_TAOTAO_URL;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();

	public User queryUserByToken(String token){
		try {
			String url =SSO_TAOTAO_URL+"/service/user/"+token;
			String jsonData = this.apiService.doGet(url);
			if(jsonData == null){
				return null;
			}
			return MAPPER.readValue(jsonData, User.class);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
}
