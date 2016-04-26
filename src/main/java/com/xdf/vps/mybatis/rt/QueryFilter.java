package com.xdf.vps.mybatis.rt;

import org.springframework.util.Assert;

/**
 * baseDao 查询条件处理类
 * 
 * @author 帅
 * 
 */
public class QueryFilter {

	private String property;
	private PMLO match;
	private Object value;

	public QueryFilter(String property, Object value) {
		this(property, PMLO.EQUAL, value);
	}

	public QueryFilter(String property, PMLO match) {
		this(property, match, null);
	}

	public QueryFilter(String property, PMLO match, Object value) {
		Assert.notNull(property);
		Assert.notNull(match);
		if (!match.isSingle())
			Assert.notNull(value);
		else
			Assert.isNull(value);
		this.property = property;
		this.match = match;
		this.value = value;
	}

	public static QueryFilter newInstance(String property, Object value) {
		return new QueryFilter(property, value);
	}

	public static QueryFilter newInstance(String property, PMLO match) {
		Assert.isTrue(match.isSingle());
		return new QueryFilter(property, match);
	}

	public static QueryFilter newInstance(String property, PMLO match,
			Object value) {
		return new QueryFilter(property, match, value);
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public PMLO getMatch() {
		return match;
	}

	public void setMatch(PMLO match) {
		this.match = match;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
