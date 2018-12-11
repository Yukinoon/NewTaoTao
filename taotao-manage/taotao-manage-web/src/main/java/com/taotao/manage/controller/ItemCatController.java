package com.taotao.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.bean.ItemCatResult;
import com.taotao.manage.pojo.ItemCat;
import com.taotao.manage.service.ItemCatService;


@Controller
@RequestMapping("item/cat")
public class ItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
	/**
	 * 查询类目数据
	 * 
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<ItemCat>> queryItemCatList(@RequestParam(value="id",defaultValue="0")Long parentId){
//		System.out.println("111");
		try {
			ItemCat itemCat = new ItemCat();
			itemCat.setParentId(parentId);
			List<ItemCat> itemCats = this.itemCatService.queryByWhere(itemCat);
			if(itemCats==null || itemCats.isEmpty()){
				//资源不存在,返回404
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.ok(itemCats);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//资源不存在,返回500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//		List<ItemCat> itemCats = this.itemCatService.queryItemCatList(parentId);
//		return ResponseEntity.ok(itemCats);
	}
	
	/**
	 * 按照前台系统的结构返回商品类目数据
	 * @return
	 */
	/*
	  @RequestMapping(value="all",method=RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
		public ResponseEntity<String> queryItemCatAll(){
			try {
				String json =MAPPER.writeValueAsString(this.itemCatService.queryAllToTree()) ;
				return ResponseEntity.ok("category.getDataService("+json+")");
			} catch (Exception e) {
				e.printStackTrace();
			}
			//返回500
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	*/
	@RequestMapping(value="all",method=RequestMethod.GET)
	public ResponseEntity<ItemCatResult> queryItemCatAll(){
		return ResponseEntity.ok(this.itemCatService.queryAllToTree());
	}
}
