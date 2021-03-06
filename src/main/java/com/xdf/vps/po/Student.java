package com.xdf.vps.po;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.xdf.vps.annotation.alias.Column;
import com.xdf.vps.annotation.alias.ID;
import com.xdf.vps.annotation.alias.Table;

@Alias("Student")
@Table(name="t_student")
public class Student implements Serializable {

	private static final long serialVersionUID = -78007369487296020L;
	
	@ID
	private String id;
	
	@Column(name="stu_number",nullable=false)
	private String studentNumber;
	
	@Column(name="name",nullable=false)
	private String studentName;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	

}
