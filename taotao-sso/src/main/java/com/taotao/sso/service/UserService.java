package com.taotao.sso.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.sso.mapper.UserMapper;
import com.taotao.sso.pojo.User;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	private static final Map<Integer, Boolean> TYPE = new HashMap<Integer, Boolean>();

	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	private static final Integer REDIS_TIME = 60*30;
	
	@Autowired
	private RedisService redisService;
	
	static {
		TYPE.put(1, true);
		TYPE.put(2, true);
		TYPE.put(3, true);
	}

	/**
	 * 检测数据是否可用
	 * @param param
	 * @param type
	 * @return
	 */
	public Boolean check(String param, Integer type) {
		if (!TYPE.containsKey(type)) {
			return null;
		}

		User record = new User();

		switch (type) {
		case 1:
			record.setUsername(param);
			break;

		case 2:
			record.setPhone(param);
			break;

		case 3:
			record.setEmail(param);
			break;
		}
		User user = this.userMapper.selectOne(record);

		return user != null;

	}

	/**
	 * 注册用户
	 * @param user
	 * @return
	 */
	public Boolean saveUser(User user) {
		user.setId(null);
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		
		//加密处理,使用MD5
		user.setPassword(DigestUtils.md5Hex(user.getPassword())); 
		
		return this.userMapper.insert(user) == 1;
	}

	public String doLogin(User param) throws Exception {
		User record = new User();
		record.setUsername(param.getUsername());
		
		User user = this.userMapper.selectOne(record);
		if(null==user){
			//该用户不存在
			return null;
		}
		
		if(!StringUtils.equals(user.getPassword(),DigestUtils.md5Hex(param.getPassword()))){
			//密码错误
			return null;
		}
		
		//登录成功
		String token = DigestUtils.md5Hex(System.currentTimeMillis()+user.getUsername());
		this.redisService.set("TOKEN_"+token, MAPPER.writeValueAsString(user), REDIS_TIME);
		
		return token;
	}

	public User queryUserByToken(String token) throws Exception{
		String key =  "TOKEN_"+token;
		String jsonData = this.redisService.get(key);
		if(jsonData==null){
			//登录超时
			return null;
		}
		User user = MAPPER.readValue(jsonData, User.class);
		//重新设置生存时间
		this.redisService.expire(key, REDIS_TIME);
		return user;
	}
}
