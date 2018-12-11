package com.taotao.cart.pojo;

import org.apache.commons.lang3.StringUtils;

public class Item extends com.taotao.manage.pojo.Item {

	public String[] getImages() {
		return super.getImage() == null ? null : StringUtils.split(
				super.getImage(), ",");
	}
}
