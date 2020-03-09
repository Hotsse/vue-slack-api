package me.hotsse.vueslack.issue.vo;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("ProjectVO")
public class ProjectVO {

	private String key;
	private String title;
	private Integer cursor;
}
