package com.taotao.manage.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemParamItem;
import com.taotao.manage.service.ItemService;

@RequestMapping("item")
@Controller
public class ItemController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);
	
	@Autowired
	private ItemService itemService;

	/**
	 * 新增商品
	 * @param item
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> saveItem(Item item,@RequestParam("desc")String desc,
			@RequestParam("itemParams")String itemParams){
		try {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("新增商品! item={},desc={}",item,desc);
			}
			
			//新增商品数据
			this.itemService.saveItem(item,desc,itemParams);
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("新增商品成功! itemId={}",item.getId());
			}
			
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			LOGGER.error("新增商品出错! item= "+item,e);
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	/**
	 * 查询列表
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<EasyUIResult> queryItemList(
			@RequestParam(value="page",defaultValue="1") Integer page,
			@RequestParam(value="rows",defaultValue="30") Integer rows){
		try {
			PageInfo<Item> pageinfo= this.itemService.queryItemList(page, rows);
			return ResponseEntity.ok(new EasyUIResult(pageinfo.getTotal(), pageinfo.getList()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * 根据商品id查询商品数据
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value="{itemId}",method=RequestMethod.GET)
	public ResponseEntity<Item> queryById(@PathVariable("itemId")Long itemId){
		try {
			Item item = this.itemService.queryById(itemId);
			if(item==null){
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.ok(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * 删除商品（逻辑删除）
	 * @param ids
	 * @return
	 */
	@RequestMapping(method=RequestMethod.DELETE)
	public ResponseEntity<Void> deleteItemByIds(@RequestParam("ids")List<Object> ids){
		try {
			 this.itemService.updateByIds(ids);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	/**
	 * 更新商品
	 * @param item
	 * @param desc
	 * @return
	 */
	@RequestMapping(method=RequestMethod.PUT)
	public ResponseEntity<Void> updateItem(Item item,@RequestParam("desc")String desc,
			@RequestParam("itemParams") String itemParams,
			@RequestParam("itemParamId") Long itemParamId
			){
		try {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("更新商品! item={},desc={}",item,desc);
			}
			ItemParamItem itemParamItem=null;
			if(itemParams!=null){
				//封装规格参数数据
				itemParamItem= new ItemParamItem();
				itemParamItem.setId(itemParamId);
				itemParamItem.setParamData(itemParams);
			}
			
			
			//新增商品数据
			this.itemService.updateItem(item,desc,itemParamItem);
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("更新商品成功! itemId={}",item.getId());
			}
			
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			LOGGER.error("更新商品出错! item= "+item,e);
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
