package com.taotao.web.service;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;
import com.taotao.web.bean.Item;

@Service
public class ItemService {

	@Autowired
	private ApiService apiService;

	@Value("${MANAGE_TAOTAO_URL}")
	private String MANAGE_TAOTAO_URL;
	
	
	public static final Integer REDIS_ITEM_TIME = 60*60*24;
	
	public static final String REDIS_ITEM_KEY = "TAOTAO_WEB_ITEM_DETAIL";
	
	@Autowired
	private RedisService redisService;

	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	

	/**
	 * 根据商品id查询商品数据
	 * 
	 * @param itemId
	 * @return
	 */
	public Item queryItemById(Long itemId) {

		String key = REDIS_ITEM_KEY + itemId;
        try {
            String jsonData = this.redisService.get(key);
            if (StringUtils.isNotEmpty(jsonData)) {
                return MAPPER.readValue(jsonData, Item.class);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

		
		String url = MANAGE_TAOTAO_URL + "/rest/item/" + itemId;
		try {
			String jsonData = this.apiService.doGet(url);
			if (StringUtils.isEmpty(jsonData)) {
				return null;
			}
			try {
				this.redisService.set(key, jsonData, REDIS_ITEM_TIME);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return MAPPER.readValue(jsonData, Item.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public ItemDesc queryItemDescByItemId(Long itemId) {
		String url = MANAGE_TAOTAO_URL + "/rest/item/desc/" + itemId;
		try {
			String jsonData = this.apiService.doGet(url);
			if (StringUtils.isEmpty(jsonData)) {
				return null;
			}
			return MAPPER.readValue(jsonData, ItemDesc.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 完成商品规格参数的html拼接
	 * 
	 * @param itemId
	 * @return
	 */
	  public String queryItemParamByItemId(Long itemId) {
	        String url = MANAGE_TAOTAO_URL + "/rest/item/param/item/" + itemId;
	        try {
	            String jsonData = this.apiService.doGet(url);
	            if (StringUtils.isEmpty(jsonData)) {
	                return null;
	            }
	            ItemParamItem itemParamItem = MAPPER.readValue(jsonData, ItemParamItem.class);
	            String paramData = itemParamItem.getParamData();
	            ArrayNode arrayNode = (ArrayNode) MAPPER.readTree(paramData);

	            // 开始拼接html
	            StringBuilder sb = new StringBuilder();
	            sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"> <tbody>");
	            for (JsonNode node : arrayNode) {
	                String group = node.get("group").asText();
	                sb.append("<tr><th class=\"tdTitle\" colspan=\"2\">" + group + "</th></tr>");
	                ArrayNode params = (ArrayNode) node.get("params");
	                for (JsonNode param : params) {
	                    sb.append("<tr><td class=\"tdTitle\">" + param.get("k").asText() + "</td><td>"
	                            + param.get("v").asText() + "</td></tr>");
	                }
	            }
	            sb.append("</tbody></table>");
	            return sb.toString();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }


}
