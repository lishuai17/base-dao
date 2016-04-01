package com.xdf.vps.mybatis.exception;

/**
 * DAO访问异常基类
 * @author 李帅
 *
 */
public class BaseDaoException extends Exception{
	/**
	 * 构造一个DAO数据访问层异常对象.
	 * @param message 信息描述
	 */
	public BaseDaoException(String message) {
		super(message);
	}

	/**
	 * 构造一个DAO数据访问层异常对象.
	 * @param message 信息描述
	 * @param cause 根异常类（可以存入任何异常）
	 */
	public BaseDaoException(String message, Throwable cause) {
		super(message, cause);
	}
}
