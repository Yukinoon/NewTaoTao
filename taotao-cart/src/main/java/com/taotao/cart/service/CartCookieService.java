package com.taotao.cart.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.ThreadLocal.UserThreadLocal;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.pojo.Item;
import com.taotao.common.utils.CookieUtils;

@Service
public class CartCookieService {

	public static final String COOKIE_CART = "TT_CART";

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private static final Integer COOKIE_TIME = 60 * 60 * 24 * 30 * 3;

	@Autowired
	private ItemService itemService;

	public void addItemToCart(Long itemId, HttpServletRequest request,
			HttpServletResponse response) {
		// 判断该商品是否存在
		String cookieCart = CookieUtils.getCookieValue(request, COOKIE_CART,
				true);
		try {
			List<Cart> carts = this.queryCatList(request);
			Cart cart = null;
			for (Cart c : carts) {
				if (c.getItemId().intValue() == itemId.intValue()) {
					// 该商品已经存在
					cart = c;
					break;
				}
			}
			if (cart == null) {
				// 不存在
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
				carts.add(cart);
			} else {
				// 存在
				cart.setNum(cart.getNum() + 1);// TODO:默认为1
			}
			// 将carts集合写入到cookie中
			CookieUtils.setCookie(request, response, COOKIE_CART,
					MAPPER.writeValueAsString(carts), COOKIE_TIME, true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Cart> queryCatList(HttpServletRequest request) {
		try {
			//判断该商品是否存在
			String cookieCart = CookieUtils.getCookieValue(request, COOKIE_CART, true);
			if(StringUtils.isEmpty(cookieCart)){
				return new ArrayList<Cart>(0);
			}
			return MAPPER.readValue(cookieCart,
					MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Cart>(0);
	}

	public void deleteItemToCart(Long itemId, HttpServletRequest request,
			HttpServletResponse response) {
		List<Cart> carts = this.queryCatList(request);
		for (Cart c : carts) {
			if (c.getItemId().intValue() == itemId.intValue()) {
				// 该商品已经存在
				carts.remove(c);
				break;
			}
		}
		try {
			// 将carts集合写入到cookie中
			CookieUtils.setCookie(request, response, COOKIE_CART,
					MAPPER.writeValueAsString(carts), COOKIE_TIME, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateItemToCart(Long itemId, Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		List<Cart> carts = this.queryCatList(request);
		for (Cart c : carts) {
			if (c.getItemId().intValue() == itemId.intValue()) {
				// 该商品已经存在
				c.setNum(num);
				break;
			}
		}
		try {
			// 将carts集合写入到cookie中
			CookieUtils.setCookie(request, response, COOKIE_CART,
					MAPPER.writeValueAsString(carts), COOKIE_TIME, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
