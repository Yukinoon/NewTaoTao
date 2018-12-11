package com.taotao.search.service;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.search.bean.Item;

@Service
public class ItemService {

	@Autowired
	private ApiService apiService;

	@Value("${MANAGE_TAOTAO_URL}")
	private String MANAGE_TAOTAO_URL;
	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * 根据商品id查询商品数据
	 * 
	 * @param itemId
	 * @return
	 */
	public Item queryItemById(Long itemId) {
		String url = MANAGE_TAOTAO_URL + "/rest/item/" + itemId;
		try {
			String jsonData = this.apiService.doGet(url);
			if (StringUtils.isEmpty(jsonData)) {
				return null;
			}

			return MAPPER.readValue(jsonData, Item.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
