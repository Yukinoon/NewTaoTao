package com.taotao.manage.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.ItemCatData;
import com.taotao.common.bean.ItemCatResult;
import com.taotao.common.service.RedisService;
import com.taotao.manage.pojo.ItemCat;

@Service
public class ItemCatService extends BaseService<ItemCat> {

	// @Autowired
	// private Mapper<ItemCat> itemCatMapper;

	// public List<ItemCat> queryItemCatList(long parentId) {
	// //根据父节点id查询类目数据
	// ItemCat record = new ItemCat();
	// record.setParentId(parentId);
	// return this.itemCatMapper.select(record);
	// }

	// @Override
	// public Mapper<ItemCat> getMapper() {
	// return itemCatMapper;
	// }

	@Autowired
	private RedisService redisService;

	private static final ObjectMapper MAPPER = new ObjectMapper();

	// 从缓存中命中,key的规则：项目名_模块名_业务名
	private static final String REDIS_KEY = "TAOTAO_MANAGE_ITEMCAT_WEB_ALL";

	private static final Integer REDIS_TIME = 60 * 60 * 24 * 30;

	public ItemCatResult queryAllToTree() {

		// 命中
		try {
			String cacheData = this.redisService.get(REDIS_KEY);
			if (StringUtils.isNotEmpty(cacheData)) {
				return MAPPER.readValue(cacheData, ItemCatResult.class);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		ItemCatResult result = new ItemCatResult();
		// 全部查出，并且在内存中生成树形结构
		List<ItemCat> cats = super.queryAll();

		// 转为map存储，key为父节点ID，value为数据集合
		Map<Long, List<ItemCat>> itemCatMap = new HashMap<Long, List<ItemCat>>();
		for (ItemCat itemCat : cats) {
			if (!itemCatMap.containsKey(itemCat.getParentId())) {
				itemCatMap.put(itemCat.getParentId(), new ArrayList<ItemCat>());
			}
			itemCatMap.get(itemCat.getParentId()).add(itemCat);
		}

		// 封装一级对象
		List<ItemCat> itemCatList1 = itemCatMap.get(0L);
		for (ItemCat itemCat : itemCatList1) {
			ItemCatData itemCatData = new ItemCatData();
			itemCatData.setUrl("/products/" + itemCat.getId() + ".html");
			itemCatData.setName("<a href='" + itemCatData.getUrl() + "'>"
					+ itemCat.getName() + "</a>");
			result.getItemCats().add(itemCatData);
			if (!itemCat.getIsParent()) {
				continue;
			}

			// 封装二级对象
			List<ItemCat> itemCatList2 = itemCatMap.get(itemCat.getId());
			List<ItemCatData> itemCatData2 = new ArrayList<ItemCatData>();
			itemCatData.setItems(itemCatData2);
			for (ItemCat itemCat2 : itemCatList2) {
				ItemCatData id2 = new ItemCatData();
				id2.setName(itemCat2.getName());
				id2.setUrl("/products/" + itemCat2.getId() + ".html");
				itemCatData2.add(id2);
				if (itemCat2.getIsParent()) {
					// 封装三级对象
					List<ItemCat> itemCatList3 = itemCatMap.get(itemCat2
							.getId());
					List<String> itemCatData3 = new ArrayList<String>();
					id2.setItems(itemCatData3);
					for (ItemCat itemCat3 : itemCatList3) {
						itemCatData3.add("/products/" + itemCat3.getId()
								+ ".html|" + itemCat3.getName());
					}
				}
			}
			if (result.getItemCats().size() >= 14) {
				break;
			}
		}
		try {
			// 将结果写入到缓存
			this.redisService.set(REDIS_KEY, MAPPER.writeValueAsString(result),
					REDIS_TIME);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
