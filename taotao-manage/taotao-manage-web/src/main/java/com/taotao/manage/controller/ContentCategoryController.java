package com.taotao.manage.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.manage.pojo.ContentCategory;
import com.taotao.manage.service.ContentCategoryService;

@RequestMapping("content/category")
@Controller
public class ContentCategoryController {
	
	@Autowired
	private ContentCategoryService contentCategoryService;
	
	
	/**
	 * 查询类目数据
	 * 
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<ContentCategory>> queryContentCategoryList(@RequestParam(value="id",defaultValue="0")Long parentId){
//		System.out.println("111");
		try {
			ContentCategory param = new ContentCategory();
			param.setParentId(parentId);
			List<ContentCategory> contentCategories = this.contentCategoryService.queryByWhere(param);
			if(contentCategories==null || contentCategories.isEmpty()){
				//资源不存在,返回404
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.ok(contentCategories);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//资源不存在,返回500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		
	}
	
	/**
	 * 新增子节点
	 * @param contentCategory
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<ContentCategory> saveContentCategory(ContentCategory contentCategory){
		try {
			contentCategory.setStatus(1);
			contentCategory.setSortOrder(1);
			contentCategory.setIsParent(false);
			//新增节点
			this.contentCategoryService.save(contentCategory);
			
			//查找父节点
			ContentCategory parent = this.contentCategoryService.queryById(contentCategory.getParentId());
			if(!parent.getIsParent()){
				//如果该父节点isParent为false要设置为true
				parent.setIsParent(true);
				this.contentCategoryService.updateSelective(parent);
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(contentCategory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//返回500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * 重命名
	 * @param contentCategory
	 * @return
	 */
	@RequestMapping(method=RequestMethod.PUT)
	public ResponseEntity<Void> updateContentCategory(ContentCategory contentCategory){
		try {
			this.contentCategoryService.updateSelective(contentCategory);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//返回500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * 删除节点
	 * @param contentCategory
	 * @return
	 */
	@RequestMapping(method=RequestMethod.DELETE)
	public ResponseEntity<Void> deleteContentCategory(ContentCategory contentCategory){
		try {
			//定义list用来收集待删除的数据id
			List<Object> ids = new ArrayList<Object>();
			ids.add(contentCategory.getId());
			
			//查找该节点的所有子节点
			findAllSubNode(contentCategory.getId(),ids);
			
			this.contentCategoryService.deleteByIds(ids, "id", ContentCategory.class);
			
			//查看当前节点的父节点是否有其他子节点，如果没有，设置isParent为false
			ContentCategory param = new ContentCategory();
			param.setParentId(contentCategory.getParentId());
			List<ContentCategory> list = this.contentCategoryService.queryByWhere(param);
			if(list==null || list.isEmpty()){
				//说明没有其他子节点
				ContentCategory parent = new ContentCategory();
				parent.setId(contentCategory.getParentId());
				parent.setIsParent(false);
				this.contentCategoryService.updateSelective(parent);
			}
			
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//返回500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	/**
	 * 传递父节点id，寻找子节点
	 * @param parentId
	 * @param ids
	 */
	private void findAllSubNode(Long parentId,List<Object> ids){
		ContentCategory param = new ContentCategory();
		param.setParentId(parentId);
		List<ContentCategory> list = this.contentCategoryService.queryByWhere(param);
		for (ContentCategory contentCategory : list) {
			ids.add(contentCategory.getId());
			if(contentCategory.getIsParent()){
				//是父节点，开始递归
				findAllSubNode(contentCategory.getId(),ids);
			}
		}
	}
}
