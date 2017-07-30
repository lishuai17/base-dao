package com.xdf.vps.mybatis.interceptor;

import org.apache.ibatis.mapping.BoundSql;

public class SimpleSqlSource implements org.apache.ibatis.mapping.SqlSource {

	private BoundSql boundSql;

	public SimpleSqlSource(BoundSql boundSql) {
		this.boundSql = boundSql;
	}

	@Override
	public BoundSql getBoundSql(Object parameterObject) {
		return boundSql;
	}

}
