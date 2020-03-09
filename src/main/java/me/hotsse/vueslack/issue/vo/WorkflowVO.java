package me.hotsse.vueslack.issue.vo;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("WorkflowVO")
public class WorkflowVO {
	
	private Integer id;
	private String title;
	private Integer sort;
	private String projectKey;
}
