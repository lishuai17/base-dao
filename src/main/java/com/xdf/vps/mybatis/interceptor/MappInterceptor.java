package com.xdf.vps.mybatis.interceptor;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

@Intercepts({
		@Signature(type = Executor.class, method = "query", args = {
				MappedStatement.class, Object.class, RowBounds.class,
				ResultHandler.class }),
		@Signature(type = Executor.class, method = "update", args = {
				MappedStatement.class, Object.class }) })
public class MappInterceptor implements Interceptor {
	
	private final static String _sql_regex = "MAPPER_";

	private void processIntercept(final Object[] queryArgs) {
		final MappedStatement ms = (MappedStatement) queryArgs[0];
		final Object parameter = queryArgs[1];
		String mapperSQL = ms.getBoundSql(parameter).getSql();
		BoundSql boundSQL = ms.getBoundSql(parameter);
		
		boolean interceptor = mapperSQL.startsWith(_sql_regex);
		if (!interceptor) {
			return;
		}

		Class<?> entityclazz = MappedStatmentHelper.getEntityClazz(mapperSQL);
		MappedStatmentHelper.setResultType(ms, entityclazz);

		if (entityclazz == null) {
			throw new RuntimeException("");
		}

		String new_sql = MapperSqlHelper.getExecuSQL(entityclazz, mapperSQL,
				parameter);

		BoundSql newBoundSql = MappedStatmentHelper.copyFromBoundSql(ms,
				boundSQL, new_sql);
		MappedStatement newMs = MappedStatmentHelper.copyFromMappedStatement(
				ms, newBoundSql);
		queryArgs[0] = newMs;

	}

	public Object intercept(Invocation invocation) throws Throwable {

		processIntercept(invocation.getArgs());

		return invocation.proceed();
	}

	public Object plugin(Object o) {
		return Plugin.wrap(o, this);
	}

	public void setProperties(Properties arg0) {
	}
}