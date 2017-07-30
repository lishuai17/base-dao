package com.xdf.vps.mybatis.exception;

/**
 * DAO访问异常基类
 * @author 李帅
 *
 */
@SuppressWarnings("serial")
public class BaseDaoException extends Exception{
	
	public enum	BaseDaoExceptionType{
		
		NOT_TABLE_PO(0,"不是一个表实体");
		
		/** 状态类型码 */
		private int code;
		/** 状态描述 */
		private String desc;
		
		private BaseDaoExceptionType(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
	
	/**
	 * 构造一个DAO数据访问层异常对象.
	 * @param message 信息描述
	 */
	public BaseDaoException(String message) {
		super(message);
	}
	
	public BaseDaoException(BaseDaoExceptionType message) {
		super(message.getDesc());
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
