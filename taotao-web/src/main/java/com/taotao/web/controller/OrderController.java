package com.taotao.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.web.ThreadLocal.UserThreadLocal;
import com.taotao.web.bean.Cart;
import com.taotao.web.bean.Order;
import com.taotao.web.bean.User;
import com.taotao.web.service.CartService;
import com.taotao.web.service.ItemService;
import com.taotao.web.service.OrderService;
import com.taotao.web.service.UserService;

@Controller
@RequestMapping("order")
public class OrderController {

	@Autowired
	private ItemService itemService;

	@Autowired
	private UserService userService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private CartService cartService;

	/**
	 * 去订单确认页面
	 * 
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "{itemId}", method = RequestMethod.GET)
	public ModelAndView toOrder(@PathVariable("itemId") Long itemId) {
		ModelAndView mv = new ModelAndView("order");
		mv.addObject("item", this.itemService.queryItemById(itemId));
		return mv;
	}
	
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public ModelAndView toCartOrder() {
		ModelAndView mv = new ModelAndView("order-cart");
		List<Cart> carts = this.cartService.queryCartList();
		if(carts.isEmpty()){
			//TODO 提示用户出错，或者购物车为空
		}
		mv.addObject("carts", carts);
		return mv;
	}

	/**
	 * 提交订单
	 * 
	 * @param order
	 * @param token
	 * @return
	 */
	//原来的第二个参数@CookieValue(UserLoginHandlerInterceptor.TAOTAO_TOKEN) String token
	@RequestMapping(value = "submit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> submitOrder(Order order) {
		// 添加当前登陆用户的信息
		User user = UserThreadLocal.get();
		order.setUserId(user.getId());
		order.setBuyerNick(user.getUsername());

		Map<String, Object> result = new HashMap<String, Object>();
		String orderId = this.orderService.submitOrder(order);
		if (orderId == null) {
			result.put("status", 400);
		} else {
			result.put("status", 200);
			result.put("data", orderId);
		}

		return result;

	}
	
	/**
	 * 货到付款的成果页面
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="success")
	public ModelAndView success(@RequestParam("id")String orderId){
		ModelAndView mv = new ModelAndView("success");
		mv.addObject("order", this.orderService.queryOrderById(orderId));
		//时间往后推两天
		mv.addObject("date", new DateTime().plusDays(2).toString("MM月dd日"));
		return mv;
	}

}
