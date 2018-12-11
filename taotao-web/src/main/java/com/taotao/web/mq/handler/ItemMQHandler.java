package com.taotao.web.mq.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.web.service.ItemService;

@Component
public class ItemMQHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemMQHandler.class);
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Autowired
	private RedisService redisService;

	public void excute(String msg){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("接收消息，MSG={}",msg);
		}
		try {
			JsonNode jsonNode = MAPPER.readTree(msg);
			Long itemId = jsonNode.get("itemId").asLong();
			this.redisService.del(ItemService.REDIS_ITEM_KEY+itemId);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("处理消息出错！MSG="+msg,e);
		} 
	}
}
