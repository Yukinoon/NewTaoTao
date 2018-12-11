package com.taotao.manage.controller;

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
import com.taotao.manage.pojo.ItemParam;
import com.taotao.manage.pojo.ItemParamItem;
import com.taotao.manage.service.ItemParamItemService;
import com.taotao.manage.service.ItemParamService;

@RequestMapping("item/param/item")
@Controller
public class ItemParamItemController {
	
	@Autowired
	private ItemParamItemService itemParamItemService;
	
	/**
	 * 根据商品id查找数据
	 * @param itemCatId
	 * @return
	 */
	@RequestMapping(value="{itemId}",method=RequestMethod.GET)
	public ResponseEntity<ItemParamItem> queryByItemId(@PathVariable("itemId")Long itemId){
		try {
			ItemParamItem param=new ItemParamItem();
			param.setItemId(itemId);
			ItemParamItem itemParamItem= this.itemParamItemService.queryOne(param);
			if(itemParamItem==null){
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.ok(itemParamItem);
		} catch (Exception e) {
			e.printStackTrace();
		}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	
}
