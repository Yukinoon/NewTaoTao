package com.taotao.cart.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.taotao.cart.ThreadLocal.UserThreadLocal;
import com.taotao.cart.mapper.CartMapper;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.pojo.Item;

@Service
public class CartService {

	@Autowired
	private CartMapper cartMapper;

	@Autowired
	private ItemService itemService;

	public void addItemToCart(Long itemId) {
		// 判断该商品是否已经存在于购物车中，存在数量相加，不存在加入到购物车

		Cart record = new Cart();
		record.setUserId(UserThreadLocal.get().getId());
		record.setItemId(itemId);
		Cart cart = this.cartMapper.selectOne(record);

		if (cart == null) {
			
			//不存在
			// 查询商品数据
			Item item = this.itemService.queryItemById(itemId);

			// 该商品在购物车中不存在
			cart = new Cart();
			cart.setCreated(new Date());
			cart.setUpdated(cart.getCreated());
			cart.setId(null);
			cart.setItemId(itemId);
			cart.setItemImage(item.getImages()[0]);
			cart.setItemPrice(item.getPrice());
			cart.setItemTitle(item.getTitle());
			cart.setNum(1);// TODO
			cart.setUserId(UserThreadLocal.get().getId());
			this.cartMapper.insert(cart);
		} else {
			// 该商品已经存在购物车中
			cart.setNum(cart.getNum() + 1);// TODO:默认为1
			cart.setUpdated(new Date());
			this.cartMapper.updateByPrimaryKeySelective(cart);
		}

	}

	public List<Cart> queryCatList() {
		return this.queryCatList(UserThreadLocal.get().getId());
	}

	public List<Cart> queryCatList(Long userId) {
		Example example = new Example(Cart.class);
		example.createCriteria().andEqualTo("userId",userId);
		example.setOrderByClause("created DESC");// 按照需求
		return this.cartMapper.selectByExample(example);
	}
	
	/**
	 * 删除当前登陆用户的商品
	 * 
	 * @param itemId
	 */
	public void deleteItemToCart(Long itemId) {
		Cart record = new Cart();
		record.setItemId(itemId);
		record.setUserId(UserThreadLocal.get().getId());
		this.cartMapper.delete(record);
	}

	/**
	 * 更新商品数量
	 * @param itemId
	 * @param num
	 */
	public void updateItemToCart(Long itemId, Integer num) {
		Cart record = new Cart();
		record.setNum(num);
		record.setUpdated(new Date());
		
		Example example = new Example(Cart.class);
		example.createCriteria().andEqualTo("userId", UserThreadLocal.get().getId())
		.andEqualTo("itemId", itemId);
		this.cartMapper.updateByExampleSelective(record, example);
	}
}
