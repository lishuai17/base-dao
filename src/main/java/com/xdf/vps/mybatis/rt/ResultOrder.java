package com.xdf.vps.mybatis.rt;

public class ResultOrder {
	
	public static final String ASC = "ASC";
	
	public static final String DESC = "DESC";
	
	private String property;
	
	private String orderType;
	
	public ResultOrder(String property,String orderType) {
		this.property = property;
		this.orderType = orderType;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	

}
