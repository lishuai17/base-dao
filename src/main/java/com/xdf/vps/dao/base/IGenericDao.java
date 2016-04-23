package com.xdf.vps.dao.base;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface IGenericDao<T,PK> {
  
  @Select("MapperGD.find.entityById")
  public T findEntityById(PK id);
  
  @Select("MapperGD.find.entitys")
  public List<T> findEntity(Object... obj);
  
  @Select("MapperGD.find.ListByLike")
  public List<T> findLikeEntity(Object... obj);
  
  @Insert("MapperGD.insert.entity")
  public void insertEntity(T t);
  
  @Update("MapperGD.update.entity")
  public void updateEntityById(T entity);
  
  @Delete("MapperGD.delete.id")
  public void deleteById(PK id);
  
  @Delete("MapperGD.delete.condition")
  public void deleteByCondition(Object param);
  
  @Select("MapperGD.find.entity.queryByVo")
  public List<T> queryByVo(int i,int c,Object... obj);
  
  @Select("MapperGD.find.entity.queryByVoLike")
  public List<T> LikequeryByVo(int i,int c,Object... obj);
  
}
  