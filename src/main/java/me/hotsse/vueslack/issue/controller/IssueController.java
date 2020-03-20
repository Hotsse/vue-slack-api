package me.hotsse.vueslack.issue.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.hotsse.vueslack.core.base.BaseController;
import me.hotsse.vueslack.issue.service.IssueService;
import me.hotsse.vueslack.issue.vo.IssueVO;

@RestController
@RequestMapping(value="/issue")
public class IssueController extends BaseController {
	
	@Autowired
	private IssueService issueService;
	
	@RequestMapping(value="/getIssue")
	public IssueVO getIssue(@RequestParam Map<String, Object> param) {
		
		Integer id = null;
		try {
			id = Integer.parseInt(param.get("id").toString());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return this.issueService.getIssue(id);
	}
	
	@RequestMapping(value="/getIssues")
	public List<IssueVO> getIssues(@RequestParam Map<String, Object> param) {
		
		String projectKey = null;
		try {
			projectKey = (String) param.get("projectKey");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return this.issueService.getIssues(projectKey);		
	}
	
	@RequestMapping(value="/getIssuesByWorkflowId")
	public List<IssueVO> getIssuesByWorkflowId(@RequestParam Map<String, Object> param) {
		
		String projectKey = null;
		Integer workflowId = null;
		try {
			projectKey = (String)param.get("projectKey");
			workflowId = Integer.parseInt(param.get("workflowId").toString());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return this.issueService.getIssuesByWorkflowId(projectKey, workflowId);		
	}
	
	@RequestMapping(value="/createIssue", method = RequestMethod.POST)
	public int createIssue(IssueVO issue) {
		return this.issueService.createIssue(issue);
	}
	
	@RequestMapping(value="/updateIssue", method = RequestMethod.POST)
	public int updateIssue(IssueVO issue) {
		return this.issueService.updateIssue(issue);
	}
}