package com.taotao.manage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageInfo;
import com.taotao.common.service.ApiService;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;

@Service
public class ItemService extends BaseService<Item> {
	
	//注意：事务的传播性
	@Autowired
	private ItemDescService itemDescService;
	
	@Autowired
	private ItemParamItemService itemParamItemService;
	
	@Autowired
	private ApiService apiService;
	
	@Value("${TAOTAO_WEB_URL}")
	private String TAOTAO_WEB_URL;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * 新增商品
	 * 
	 * @param item
	 * @param desc
	 * @param itemParams 
	 */
	public void saveItem(Item item, String desc, String itemParams) {
		
		item.setStatus(1);//初始状态为1
		item.setId(null);//强制为null，为了安全性
		//新增商品
		super.save(item);
		
		//新增商品描述数据
		ItemDesc itemDesc=new ItemDesc();
		itemDesc.setItemDesc(desc);
		itemDesc.setItemId(item.getId());
		this.itemDescService.save(itemDesc);
		
		if(StringUtils.isNotEmpty(itemParams)){
			ItemParamItem itemParamItem=new ItemParamItem();
			itemParamItem.setItemId(item.getId());
			itemParamItem.setParamData(itemParams);
			this.itemParamItemService.save(itemParamItem);
		}
		
		//发送商品新增的消息到rabbitMQ
		sendMsg(item.getId(),"insert");
	}

	public PageInfo<Item> queryItemList(Integer page, Integer rows) {
		Example example = new Example(Item.class);
		example.setOrderByClause("updated DESC");
		example.createCriteria().andNotEqualTo("status", 3);
		return super.queryPageListByExample(example, page, rows);
	}

	/**
	 * 实现商品的逻辑删除
	 * @param ids
	 */
	public void updateByIds(List<Object> ids) {
		Example example = new Example(Item.class);
		example.createCriteria().andIn("id", ids);
		Item item = new Item();
		item.setStatus(3);
		super.getMapper().updateByExampleSelective(item, example);
		
		for (Object object : ids) {
			//发送商品删除的消息到rabbitMQ
			sendMsg(Long.valueOf(object.toString()),"delete");
		}
		
	}

	/**
	 * 更新商品
	 * @param item
	 * @param desc
	 */
	public void updateItem(Item item, String desc,ItemParamItem itemParamItem) {
		//强制设置不能更新的字段为null
		item.setStatus(null);
		item.setCreated(null);
		//更新商品数据
		super.updateSelective(item);
		
		//更新商品描述数据
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);		
		this.itemDescService.updateSelective(itemDesc);
		
		if(itemParamItem!=null){
			//更新规格参数数据
			this.itemParamItemService.updateSelective(itemParamItem);
		}
		
		//通知其他系统商品已经更新
//		String url = TAOTAO_WEB_URL+ "/item/cache/"+item.getId()+".html" ;
//		try {
//			this.apiService.doPost(url);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		//发送商品更新的消息到rabbitMQ
		sendMsg(item.getId(),"update");
	}
	
	private void sendMsg(Long itemId,String type){
		
		try {
			Map<String, Object> msg = new HashMap<String, Object>();
			msg.put("itemId", itemId);
			msg.put("type", type);
			msg.put("created", System.currentTimeMillis());
			this.rabbitTemplate.convertAndSend("item."+type, MAPPER.writeValueAsString(msg));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
