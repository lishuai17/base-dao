package com.xdf.vps.dao.base;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;

import com.xdf.vps.mybatis.provider.DyncSqlProvider;

public interface BaseDao {
	
	@InsertProvider(type=DyncSqlProvider.class,method="save")
	public <T> void save(T po);
	
	public <T> void saveBatch(List<T> pos);
	
	public <T> void update(T po);
	
	public <T> void deleteById(T po);
	
	public <T> List<T> findAll(Class<T> clazz);
	
	public <T> List<T> find(Class<T> clazz);
	
	@SelectProvider(type=DyncSqlProvider.class,method="getById")
	public <T,ID> T getById(ID id);
	

}
