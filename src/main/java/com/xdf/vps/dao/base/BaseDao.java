package com.xdf.vps.dao.base;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;

import com.xdf.vps.mybatis.provider.DyncSqlProvider;

public interface BaseDao {
	
	@InsertProvider(type=DyncSqlProvider.class,method="save")
	public <T> void save(T po);
	
	@SelectProvider(type=DyncSqlProvider.class,method="getById")
	public <T,ID> T getById(ID id);
	

}
