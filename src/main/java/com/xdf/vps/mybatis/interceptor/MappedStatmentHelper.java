package com.xdf.vps.mybatis.interceptor;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.springframework.util.Assert;

public class MappedStatmentHelper {


	public static Class<?> getEntityClazz(String resource) {
		Assert.notNull(resource, "resource不能为空");
		String className = resource;
		if(className.indexOf("Dao.xml")>0){
			className = className.substring(0, resource.indexOf("Dao.xml"));
		}
		if(className.indexOf("Dao.java")>0){
			className = className.substring(0, resource.indexOf("Dao.java"));
		}
		className = className.replace("/", ".").replace(".dao.", ".model.");
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clazz;
	}

	public static MappedStatement setMSReturnSetMap(MappedStatement ms, Class<?> entityclazz) {
		return ms;
	}

	public static BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSQL, String newSql) {
		BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), newSql, boundSQL.getParameterMappings(), boundSQL.getParameterObject());  
		
/*		ParameterMapping.Builder builder = new ParameterMapping.Builder(ms.getConfiguration(),"id",Server.class);
		List<ParameterMapping> pms = new ArrayList<ParameterMapping>();
		pms.add(builder.build());
		BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), newSql, pms, 123L); */
		
		for (ParameterMapping mapping : boundSQL.getParameterMappings()) {  
	        String prop = mapping.getProperty();  
	        if (boundSQL.hasAdditionalParameter(prop)) {  
	            newBoundSql.setAdditionalParameter(prop, boundSQL.getAdditionalParameter(prop));  
	        }  
	    }  
	    return newBoundSql; 
/*		try {
			Class<?> cla =boundSQL.getClass();  
			Field field = cla.getDeclaredField("sql");  
			field.setAccessible(true);  
			field.set(boundSQL, new_sql);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return boundSQL;*/
	}

	public static MappedStatement copyFromMappedStatement(MappedStatement ms, BoundSql boundSQL) {
		
		MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), new SimpleSqlSource(boundSQL), ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
       // builder.keyProperty(ms.getKeyProperties());
       
        //setStatementTimeout()
        builder.timeout(ms.getTimeout());
       
        //setStatementResultMap()
        builder.parameterMap(ms.getParameterMap());
       
        //setStatementResultMap()
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
       
        //setStatementCache()
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
       
        return builder.build();
	}


}
