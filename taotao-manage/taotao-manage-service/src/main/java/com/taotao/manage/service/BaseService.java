package com.taotao.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.pojo.BasePojo;

public abstract class BaseService<T extends BasePojo> {
	
	//由子类实现该方法，返回具体的Mapper的实现类
//	public abstract Mapper<T> getMapper();
	
	@Autowired
	private Mapper<T> mapper;
	
	
	
	public Mapper<T> getMapper() {
		return mapper;
	}

	/**
	 * 根据主键查询数据
	 * 
	 * @param id
	 * @return
	 */
	public T queryById(Long id){
		return this.mapper.selectByPrimaryKey(id);
	}
	
	/**
	 * 查询所有数据
	 * @return
	 */
	public List<T> queryAll(){
		return this.mapper.select(null);
	}
	
	/**
	 * 根据条件查询数据集合
	 * @param t
	 * @return
	 */
	public List<T> queryByWhere(T t){
		return this.mapper.select(t);
	}
	
	/**
	 * 根据条件查询一条数据
	 * @param t
	 * @return
	 */
	public T queryOne(T t){
		return this.mapper.selectOne(t);
	}
	
	/**
	 * 分页查询数据
	 * @param t
	 * @param page
	 * @param rows
	 * @return
	 */
	public PageInfo<T> queryPageListByWhere(T t,Integer page,Integer rows){
		PageHelper.startPage(page, rows,true);//设置分页参数
		
		//查询数据
		List<T> list = this.queryByWhere(t);
		return new PageInfo<T>(list);
	}
	
	/**
	 * 自定义查询条件，分页查询
	 * @param example
	 * @param page
	 * @param rows
	 * @return
	 */
	public PageInfo<T> queryPageListByExample(Example example,Integer page,Integer rows){
		
		//设置分页参数
		PageHelper.startPage(page,rows,true);
		
		//查询数据
		List<T> list = this.mapper.selectByExample(example);
		return new PageInfo<T>(list);
	}
	
	/**
	 * 新增数据
	 * @param t
	 * @return
	 */
	public Integer save(T t){
		t.setCreated(new Date());
		t.setUpdated(t.getCreated());
		return this.mapper.insert(t);
	}
	
	/**
	 * 新增数据，使用不为null的字段
	 * @param t
	 * @return
	 */
	public Integer saveSelective(T t){
		t.setCreated(new Date());
		t.setUpdated(t.getCreated());
		return this.mapper.insertSelective(t);
	}
	
	/**
	 * 更新数据
	 * @param t
	 * @return
	 */
	public Integer update(T t){
		t.setUpdated(new Date());
		return this.mapper.updateByPrimaryKey(t);
	}
	
	/**
	 * 更新数据，使用不为null的字段
	 * @param t
	 * @return
	 */
	public Integer updateSelective(T t){
		t.setUpdated(new Date());
		return this.mapper.updateByPrimaryKeySelective(t);
	}
	
	/**
	 * 根据主键id删除数据
	 * @param id
	 * @return
	 */
	public Integer deleteById(Long id){
		return this.mapper.deleteByPrimaryKey(id);
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @param property
	 * @param clazz
	 * @return
	 */
	public Integer deleteByIds(List<Object> ids,String property,Class<T> clazz){
		Example example = new Example(clazz);
		example.createCriteria().andIn(property, ids);
		return this.mapper.deleteByExample(example);
	}
	
}
