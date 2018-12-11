package com.taotao.web.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.HttpResult;
import com.taotao.common.service.ApiService;
import com.taotao.web.bean.Order;

@Service
public class OrderService {

	@Value("${ORDER_TAOTAO_URL}")
	private String ORDER_TAOTAO_URL;
	
	@Autowired
	private ApiService apiService;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	/**
	 * 调用订单系统的接口完成订单提交
	 * @param order
	 * @return	成功返回订单号，失败返回null
	 */
	public String submitOrder(Order order) {
		String url = ORDER_TAOTAO_URL + "/order/create";
		
		try {
			HttpResult httpResult = this.apiService.doPostJson(url, MAPPER.writeValueAsString(order));
			if(httpResult.getCode() == 200){
				JsonNode jsonNode = MAPPER.readTree(httpResult.getData());
				if(jsonNode.get("status").asInt()==200){
					//创建成功
					return jsonNode.get("data").asText();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
		
		
	}

	/**
	 * 根据订单id查询订单数据
	 * @param orderId
	 * @return
	 */
	public Order queryOrderById(String orderId) {
		String url =ORDER_TAOTAO_URL + "/order/query/"+orderId ;
		try {
			String jsonData = this.apiService.doGet(url);
			if(jsonData!=null){
				return MAPPER.readValue(jsonData,Order.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

}
