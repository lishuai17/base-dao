package com.xdf.vps.mybatis.provider;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.xdf.vps.annotation.alias.Column;
import com.xdf.vps.annotation.alias.Table;
import com.xdf.vps.util.ListUtils;

public class DyncSqlProvider {
	
	public String save(Object po){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into ");
		if(po.getClass().isAnnotationPresent(Table.class)){
			Table tableAnnotaion = po.getClass().getAnnotation(Table.class);
			sql.append(tableAnnotaion.name());
			List<String> properties = new ArrayList<String>();
			List<Object> values = new ArrayList<Object>();
			Field[] declaredFields = po.getClass().getDeclaredFields();
			for (Field field : declaredFields) {
				if(field.isAnnotationPresent(Column.class)){
					properties.add(field.getAnnotation(Column.class).name());
					field.setAccessible(true);
					try {
						values.add(field.get(po));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			sql.append(" ( ");
			sql.append(ListUtils.List2String(properties));
			sql.append(" )") ;
			sql.append(" VALUES (");
			sql.append(ListUtils.ListObj2String(values));
			sql.append(" )") ;
			
		}
		return sql.toString();
	}
	
	public String getById(){
		return "";
	}

}
