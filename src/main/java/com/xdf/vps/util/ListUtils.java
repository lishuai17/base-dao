package com.xdf.vps.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理集合的公共方法
 * @author Bruce
 * @Date 2016/3/25
 *
 */
public class ListUtils {

	/**
	 * List转换为逗号分隔的字符串
	 * @param list
	 * @return
	 */
	public static String List2String(List<String> list){
		StringBuilder rlt = new StringBuilder();
		if(list == null || list.size() == 0){
			return rlt.toString();
		}
		for(String str : list){
			rlt.append(str).append(",");
		}
		if(rlt.charAt(rlt.length()-1) == ','){
			rlt.deleteCharAt(rlt.length()-1);
		}
		return rlt.toString();
	}
	
	public static String ListObj2String(List<Object> list){
		StringBuilder rlt = new StringBuilder();
		if(list == null || list.size() == 0){
			return rlt.toString();
		}
		for(Object str : list){
			rlt.append("'").append(str).append("',");
		}
		if(rlt.charAt(rlt.length()-1) == ','){
			rlt.deleteCharAt(rlt.length()-1);
		}
		return rlt.toString();
	}
	
	public static void main(String[] args) {
		List<String> a = new ArrayList<>();
		a.add("a");
		a.add("b");
		System.out.println("rlt: " + List2String(a));
	}

}
