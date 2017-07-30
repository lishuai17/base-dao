package com.xdf.vps.mybatis.interceptor;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.springframework.util.Assert;

public class MappedStatmentHelper {
	
	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

	/**
	 * 反射对象，增加对低版本Mybatis的支持
	 *
	 * @param object 反射对象
	 * @return
	 */
	public static MetaObject forObject(Object object) {
		return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
	}

	
	public static Class<?> getEntityClazz(String resource) {
		Assert.notNull(resource, "resource不能为空");
		String className = resource.substring(resource.indexOf(">>")+2);
	
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clazz;
	}
	
	/**
	 * 设置返回值类型
	 *
	 * @param ms
	 * @param entityClass
	 */
	public static void setResultType(MappedStatement ms, Class<?> entityClass) {
		ResultMap resultMap = ms.getResultMaps().get(0);
		MetaObject metaObject = forObject(resultMap);
		metaObject.setValue("type", entityClass);
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
