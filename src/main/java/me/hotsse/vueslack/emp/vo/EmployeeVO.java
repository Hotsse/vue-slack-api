package me.hotsse.vueslack.emp.vo;

import java.util.UUID;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("EmployeeVO")
public class EmployeeVO {
	
	private Integer empNo;
	private String empId;
	// private String empPw;
	private String empNm;
	private String isAdmin;	
	
	private UUID token;
}
