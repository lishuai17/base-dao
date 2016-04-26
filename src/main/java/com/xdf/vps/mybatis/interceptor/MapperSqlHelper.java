package com.xdf.vps.mybatis.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.util.StringUtils;

import com.xdf.vps.annotation.alias.Column;
import com.xdf.vps.annotation.alias.ID;
import com.xdf.vps.annotation.alias.Table;
import com.xdf.vps.mybatis.exception.BaseDaoException;
import com.xdf.vps.mybatis.exception.BaseDaoException.BaseDaoExceptionType;
import com.xdf.vps.mybatis.rt.QueryFilter;
import com.xdf.vps.po.Student;
import com.xdf.vps.util.ListUtils;

public class MapperSqlHelper {

	public String getUpdateSQL() {
		return null;
	}

	/**
	 * 传入mapper接口class
	 * 
	 * @param mapperclazz
	 * @return
	 */
	private String insertEntity(Class<?> po) {
		StringBuffer sql = new StringBuffer();
		sql.append("insert into ");
		String tableName = null;
		if (po.getClass().isAnnotationPresent(Table.class)) {
			Table tableAnnotaion = po.getClass().getAnnotation(Table.class);
			tableName = tableAnnotaion.name();
		} else {

			tableName = po.getSimpleName();
		}
		sql.append(tableName);
		List<String> properties = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		Field[] declaredFields = po.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			if (field.isAnnotationPresent(Column.class)) {
				String columnName = field.getAnnotation(Column.class).name();
				properties.add(StringUtils.hasText(columnName) ? columnName
						: field.getName());
				field.setAccessible(true);
				values.add(field.getName());
			}
			if (field.isAnnotationPresent(ID.class)) {
				String columnName = field.getAnnotation(ID.class).name();
				properties.add(StringUtils.hasText(columnName) ? columnName
						: field.getName());
				field.setAccessible(true);
				values.add(field.getName());

			}
		}

		sql.append(" ( ");
		sql.append(ListUtils.List2String(properties));
		sql.append(" )");
		sql.append(" VALUES (");
		sql.append(ListUtils.ListObj2String(values));
		sql.append(" )");
		return sql.toString();
	}

	public String findAll(Class<?> clazz) {
		Map<String, String> columnMap = proccessProperties(clazz);
		String aliases = ListUtils.List2String(createAliases(columnMap));
		StringBuilder sql = new StringBuilder();
		if (!clazz.isAnnotationPresent(Table.class)) {
			sql.append("select "+aliases+" from  " + clazz.getName());
		} else {
			Table antable = (Table) clazz.getAnnotation(Table.class);
			if (!StringUtils.hasText(antable.name())) {
				sql.append("select "+aliases+" from  " + clazz.getSimpleName());
			} else {
				sql.append("select "+aliases+" from  " + antable.name());
			}
		}
		return sql.toString();
	}
	
	private String find(Class<?> clazz ,QueryFilter[] qfs){
		Map<String, String> columnMap = proccessProperties(clazz);
		String aliases = ListUtils.List2String(createAliases(columnMap));
		StringBuilder sql = new StringBuilder();
		if (!clazz.isAnnotationPresent(Table.class)) {
			sql.append("select "+aliases+" from  " + clazz.getName());
		} else {
			Table antable = (Table) clazz.getAnnotation(Table.class);
			if (!StringUtils.hasText(antable.name())) {
				sql.append("select "+aliases+" from  " + clazz.getSimpleName());
			} else {
				sql.append("select "+aliases+" from  " + antable.name());
			}
		}
		List<String> buildFilters = buildFilters(qfs, columnMap);
		if(!buildFilters.isEmpty()){
			sql.append(" where ");
			sql.append(ListFilter2String(buildFilters));
		}
		return sql.toString();
	}

	/**
	 * 传入mapper接口class
	 * 
	 * @param mapperclazz
	 * @return
	 */

	private String findById(Class<?> clazz) {
		StringBuilder sql = new StringBuilder();
		if (!clazz.isAnnotationPresent(Table.class)) {
			sql.append("select * from  " + clazz.getName());
		} else {
			Table antable = (Table) clazz.getAnnotation(Table.class);
			if (!StringUtils.hasText(antable.name())) {
				sql.append("select * from  " + clazz.getSimpleName());
			} else {
				sql.append("select * from  " + antable.name());
			}
		}
		Field[] files = clazz.getDeclaredFields();
		boolean falg = false;
		for (Field file : files) {
			file.setAccessible(true);
			if (file.isAnnotationPresent(ID.class)) {
				Column anColumn = file.getAnnotation(Column.class);
				if (StringUtils.hasText(anColumn.name())) {
					sql.append("  where " + anColumn.name() + " = #{"
							+ file.getName() + "} ");
				} else {
					sql.append("  where " + file.getName() + " = #{"
							+ file.getName() + "} ");
				}

			}
		}
		if (!falg) {
			throw new RuntimeException("不能通過id查詢實體,實體中沒有定義@mybatisID");
		}

		return sql.toString();
	}

	private static MapperSqlHelper App() {
		return new MapperSqlHelper();
	}

	public static String getExecuSQL(Class<?> clazz, String mapperDBsql,
			Object param) {
		if (mapperDBsql.startsWith("MAPPER_" + "SAVE")) {
			return MapperSqlHelper.App().insertEntity(clazz);// 条件查询实体列表
		} else if (mapperDBsql.startsWith("MAPPER_" + "FINDBYID")) {
			return MapperSqlHelper.App().findById(clazz);// id查询实体
		} else if (mapperDBsql.startsWith("MAPPER_" + "FINDALL")) {
			return MapperSqlHelper.App().findAll(clazz);//
		} else if (mapperDBsql.startsWith("MAPPER_" + "FIND$")) {
			return MapperSqlHelper.App().find(clazz, (QueryFilter[])( ((Map<String,Object>)param).get("param2")));//
		}

		return null;
	}
	
	private Map<String, String> proccessProperties(Class<?> clazz) {
			Map<String, String> map = new HashMap<String, String>();
			Field[] declaredFields = clazz.getDeclaredFields();
			for (Field field : declaredFields) {
				if(field.isAnnotationPresent(Column.class)){
					String columnName = field.getAnnotation(Column.class).name();
					String fieldName = field.getName();
					map.put(fieldName, StringUtils.hasText(columnName) ? columnName : fieldName);
				}
				if(field.isAnnotationPresent(ID.class)){
					String columnName = field.getAnnotation(ID.class).name();
					String fieldName = field.getName();
					map.put(fieldName, StringUtils.hasText(columnName) ? columnName : fieldName);
				}
			}	
			return map;
	}
	
	private List<String> createAliases(Map<String, String>  columnMap){
		
		List<String> aliases = new ArrayList<String>();
		
		Set<Entry<String, String>> entrySet = columnMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			aliases.add(entry.getValue() + " as "+ entry.getKey());
		}
		return aliases;
	}
	
	private List<String> buildFilters(QueryFilter[] qfs,
			Map<String, String> columnMap) {
		List<String> filters = new ArrayList<String>();
		for (QueryFilter qf : qfs) {
			String property = qf.getProperty();
			if (columnMap.containsKey(property)) {
				if (qf.getMatch().isSingle()) {
					filters.add(columnMap.get(property) + " "
							+ qf.getMatch().getOperatorName());
				} else {
					Object value = qf.getValue();
					if (value instanceof String) {

						filters.add(columnMap.get(property) + " "
								+ qf.getMatch().getOperatorName() + " '"
								+ value + "'");
					} else {
						filters.add(columnMap.get(property) + " "
								+ qf.getMatch().getOperatorName() + " " + value);
					}

				}
			}
		}
		return filters;
	}
	
	public static String ListFilter2String(List<String> list){
		StringBuilder rlt = new StringBuilder();
		if(list == null || list.size() == 0){
			return rlt.toString();
		}
		for (int i = 0; i < list.size(); i++) {
			String str = list.get(i);
			if(i != 0){
				rlt.append(" and ");
			}
			rlt.append(str);
		}
		
		return rlt.toString();
	}


	public void getParam(Object param) {
		StringBuffer bf = new StringBuffer();
		if (isPrimitiveType(param.getClass())) {
			bf.append(param);
		} else if (param instanceof Map) {
			Map<String, Object> map = (Map) param;
		}
	}

	public static boolean isPrimitiveType(Class clazz) {
		return clazz != null
				&& (clazz.isPrimitive() || clazz.equals(Long.class)
						|| clazz.equals(Integer.class)
						|| clazz.equals(Short.class)
						|| clazz.equals(Byte.class)
						|| clazz.equals(Double.class)
						|| clazz.equals(Float.class)
						|| clazz.equals(Boolean.class)
						|| clazz.equals(Character.class) || clazz
							.equals(String.class));
	}

}
