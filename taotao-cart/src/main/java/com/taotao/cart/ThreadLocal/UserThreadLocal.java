package com.taotao.cart.ThreadLocal;

import com.taotao.cart.controller.User;

public class UserThreadLocal {

	private static final ThreadLocal<User> LOCAL = new ThreadLocal<User>();
	
	public static void set(User user){
		LOCAL.set(user);
	}
	
	public static User get(){
		return LOCAL.get();
	}
}
