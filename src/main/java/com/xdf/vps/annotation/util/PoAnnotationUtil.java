package com.xdf.vps.annotation.util;

import java.lang.reflect.Field;

import com.xdf.vps.annotation.alias.Column;
import com.xdf.vps.annotation.alias.Table;
import com.xdf.vps.po.Student;

public class PoAnnotationUtil {
	
	public static void getPoInfo(Class<?> clazz){
		//首先判定这个类是否是一个表实体
		if(clazz.isAnnotationPresent(Table.class)){
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if(field.isAnnotationPresent(Column.class)){
					System.out.println(field.getAnnotation(Column.class).name());
				}
				
			}
		}
	}
	
	public static void main(String[] args) {
		getPoInfo(Student.class);
	}

}
