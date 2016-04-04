package com.xdf.vps.mybatis.provider;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.xdf.vps.annotation.alias.Column;
import com.xdf.vps.annotation.alias.ID;
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
					String columnName = field.getAnnotation(Column.class).name();
					properties.add(StringUtils.hasText(columnName) ? columnName :field.getName());
					field.setAccessible(true);
					try {
						values.add(field.get(po));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(field.isAnnotationPresent(ID.class)){
					String columnName = field.getAnnotation(ID.class).name();
					properties.add(StringUtils.hasText(columnName) ? columnName :field.getName());
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
	
	public String getById(Class<?> clazz,Object id){
		String tableName = null;
		if(clazz.isAnnotationPresent(Table.class)){
			Table tableAnnotaion = clazz.getAnnotation(Table.class);
			tableName = tableAnnotaion.name();
			List<String> properties = new ArrayList<String>();
			List<Object> values = new ArrayList<Object>();
			Field[] declaredFields = clazz.getDeclaredFields();
			for (Field field : declaredFields) {
				if(field.isAnnotationPresent(Column.class)){
					String columnName = field.getAnnotation(Column.class).name();
					properties.add(createAlias(columnName, field.getName()));
					
				}
			}
			
			StringBuffer sql = new StringBuffer();
			sql.append("select  ");
			sql.append(ListUtils.List2String(properties));
			sql.append(" from  ");
			sql.append(tableName);
			sql.append(" ( ");
			sql.append(" )") ;
			sql.append(" VALUES (");
			sql.append(ListUtils.ListObj2String(values));
			sql.append(" )") ;
			
			return sql.toString();
		}
		return "";
	
	}
	
	private String createAlias(String columnName,String properties){
		return columnName + " as " + properties;
	}

}
