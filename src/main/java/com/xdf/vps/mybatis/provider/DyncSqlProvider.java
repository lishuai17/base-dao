package com.xdf.vps.mybatis.provider;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.scripting.xmltags.IfSqlNode;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;
import org.apache.ibatis.scripting.xmltags.WhereSqlNode;
import org.springframework.util.StringUtils;

import com.xdf.vps.annotation.alias.Column;
import com.xdf.vps.annotation.alias.ID;
import com.xdf.vps.annotation.alias.Table;
import com.xdf.vps.mybatis.exception.BaseDaoException;
import com.xdf.vps.mybatis.exception.BaseDaoException.BaseDaoExceptionType;
import com.xdf.vps.mybatis.rt.QueryFilter;
import com.xdf.vps.util.ListUtils;


public class DyncSqlProvider {
	
	public static final String SQL_TEMP = "MAPPER_";
	
	public String save(Object po)  throws BaseDaoException{
		
			
			return "MAPPER_"+"SAVE_"+">>"+po.getClass().getName();
	}
	
	public String getById(Class<?> clazz, Object id) throws BaseDaoException {
		String tableName = getTableName(clazz);
		Map<String, String> columnMap = proccessProperties(clazz);
		
		StringBuffer sql = new StringBuffer();
		sql.append("select  ");
		sql.append(ListUtils.List2String(createAliases(columnMap)));
		sql.append(" from  ");
		sql.append(tableName);
		sql.append(" where  ");
		sql.append(createIdFilter(clazz));
		return "MAPPER_"+"FINDBYID_"+">>"+clazz.getClass().getName();

	}
	
	public String findAll(Map<String, Object> map) throws BaseDaoException{
		Class<?> clazz = (Class<?>)map.get("param1");
		return "MAPPER_"+"FINDALL_"+">>"+clazz.getName();
		
	}
	
	public String find(Map<String, Object> map)
			throws BaseDaoException {
		Class<?> clazz = (Class<?>)map.get("param1");
		
//		if (null == qfs) {
//			return findAll(clazz);
//		}
//		String tableName = getTableName(clazz);
//		Map<String, String> columnMap = proccessProperties(clazz);
//
//		List<String> filters = buildFilters(qfs, columnMap);
//
//		StringBuffer sql = new StringBuffer();
//		sql.append("select  ");
//		sql.append(ListUtils.List2String(createAliases(columnMap)));
//		sql.append(" from  ");
//		sql.append(tableName);
//		sql.append(" where  ");
//		sql.append(ListUtils.ListFilter2String(filters));

		return "MAPPER_"+"FIND$_"+">>"+clazz.getName();

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
	
	private String createIdFilter(Class<?> clazz) throws BaseDaoException{
		if(clazz.isAnnotationPresent(Table.class)){
			
			Field[] declaredFields = clazz.getDeclaredFields();
			for (Field field : declaredFields) {
				
				if(field.isAnnotationPresent(ID.class)){
					String columnName = field.getAnnotation(ID.class).name();
					String fieldName = field.getName();
					return (StringUtils.hasText(columnName) ? columnName : fieldName)+"=#{id}";
				}
			}	
			return "";
		}else{
			throw new BaseDaoException(BaseDaoExceptionType.NOT_TABLE_PO);
		}
	}
	private Map<String, String> proccessProperties(Class<?> clazz) throws BaseDaoException{
		if(clazz.isAnnotationPresent(Table.class)){
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
		}else{
			throw new BaseDaoException(BaseDaoExceptionType.NOT_TABLE_PO);
		}
	}
		
	private String getTableName(Class<?> clazz) throws BaseDaoException{
		if(clazz.isAnnotationPresent(Table.class)){
			Table tableAnnotaion = clazz.getAnnotation(Table.class);
			String name = tableAnnotaion.name();
			return StringUtils.hasText(name) ? name : clazz.getSimpleName();
		}else{
			throw new BaseDaoException(BaseDaoExceptionType.NOT_TABLE_PO);
		}
			
		
	}
	
	
	private List<String> createAliases(Map<String, String>  columnMap){
		
		List<String> aliases = new ArrayList<String>();
		
		Set<Entry<String, String>> entrySet = columnMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			aliases.add(entry.getValue() + " as "+ entry.getKey());
		}
		return aliases;
	}
	
	private String createAlias(String columnName,String properties){
		return columnName + " as " + properties;
	}
	
	 private MappedStatement buildMappedStatement(MappedStatement ms) {
	        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), ms.getSqlSource(), ms.getSqlCommandType());
	        builder.resource(ms.getResource());
	        builder.fetchSize(ms.getFetchSize());
	        builder.statementType(ms.getStatementType());
	        builder.keyGenerator(ms.getKeyGenerator());
	        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
	            StringBuilder keyProperties = new StringBuilder();
	            for (String keyProperty : ms.getKeyProperties()) {
	                keyProperties.append(keyProperty).append(",");
	            }
	            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
	            builder.keyProperty(keyProperties.toString());
	        }
	        builder.timeout(ms.getTimeout());
	        builder.parameterMap(ms.getParameterMap());
	        builder.resultMaps(ms.getResultMaps());
	        builder.resultSetType(ms.getResultSetType());
	        builder.cache(ms.getCache());
	        builder.flushCacheRequired(ms.isFlushCacheRequired());
	        builder.useCache(ms.isUseCache());
	        return builder.build();
	    }
	
	

}
