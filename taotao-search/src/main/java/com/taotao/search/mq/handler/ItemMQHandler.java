package com.taotao.search.mq.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.search.bean.Item;
import com.taotao.search.service.ItemService;

@Component
public class ItemMQHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemMQHandler.class);
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Autowired
	private HttpSolrServer httpSolrServer;
	
	@Autowired
	private ItemService itemService;

	public void excute(String msg){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("接收消息，MSG={}",msg);
		}
		try {
			JsonNode jsonNode = MAPPER.readTree(msg);
			Long itemId = jsonNode.get("itemId").asLong();
			String type = jsonNode.get("type").asText();
			if(StringUtils.equals(type, "insert")||StringUtils.equals(type, "update")){
				//查询商品数据
				Item item = this.itemService.queryItemById(itemId);
				this.httpSolrServer.addBean(item);
			}else if(StringUtils.equals(type, "delete")){
				this.httpSolrServer.deleteById(String.valueOf(itemId));
			}
			
			//提交
			this.httpSolrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("处理消息出错！MSG="+msg,e);
		} 
	}
}
