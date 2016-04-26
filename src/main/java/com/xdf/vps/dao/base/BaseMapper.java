package com.xdf.vps.dao.base;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;

import com.xdf.vps.mybatis.provider.DyncSqlProvider;
import com.xdf.vps.mybatis.rt.QueryFilter;
import com.xdf.vps.po.Student;

/**
 * 基础dao
 * @author 帅
 *
 */
public interface BaseMapper {
	
	@InsertProvider(type=DyncSqlProvider.class,method="save")
	public <T> void save(T po);
	
//	@SelectProvider(type=DyncSqlProvider.class,method="getById")
	public <T,ID> T getById(Class<T> clazz,ID id);
	
	@SelectProvider(type=DyncSqlProvider.class,method="findAll")
	public <T> List<T> findAll(Class<T> clazz);
	
	@SelectProvider(type=DyncSqlProvider.class,method="find")
	public <T> List<T> find(Class<T> clazz,QueryFilter[] qfs);
	
	

}
