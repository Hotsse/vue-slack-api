package me.hotsse.vueslack.issue.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("IssueVO")
public class IssueVO {
	
	private Integer id;
	private Integer key;
	private String summary;
	private String description;
	private Integer reporterNo;
	private Integer assigneeNo;
	private Date regDt;
	private Date dueDt;
	private String useYn;
	private String projectKey;
	private Integer workflowId;
	
	private String reporterNm;
	private String assigneeNm;
	private String workflowNm;
	
	public void setDueDt(String dueDt) {
		if(dueDt == null) return;
		
		try {
			this.dueDt = new SimpleDateFormat("yyyy-MM-dd").parse(dueDt);
		}
		catch(Exception e) {
			e.printStackTrace();
		}	      
	}

}
