package me.hotsse.vueslack.issue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.hotsse.vueslack.core.base.BaseService;
import me.hotsse.vueslack.issue.dao.IssueDao;
import me.hotsse.vueslack.issue.dao.ProjectDao;
import me.hotsse.vueslack.issue.dao.WorkflowDao;
import me.hotsse.vueslack.issue.vo.IssueVO;
import me.hotsse.vueslack.issue.vo.WorkflowVO;

@Service
public class IssueService extends BaseService {
	
	@Autowired
	private IssueDao issueDao;
	
	@Autowired
	private WorkflowDao workflowDao;
	
	@Autowired
	private ProjectDao projectDao;
		
	public IssueVO getIssue(int id) {
		return this.issueDao.getIssue(id);
	}
	
	public List<IssueVO> getIssues(String projectKey) {
		return this.issueDao.getIssues(projectKey);
	}
	
	public List<IssueVO> getIssuesByWorkflowId(String projectKey, int workflowId) {
		return this.issueDao.getIssuesByWorkflowId(projectKey, workflowId);
	}
	
	public int createIssue(IssueVO issue) {
		
		int result = 0;
		
		try {
			
			int cursor = this.projectDao.getProjectCursor(issue.getProjectKey());
			issue.setKey(cursor);
			
			List<WorkflowVO> workflows = this.workflowDao.getWorkflows(issue.getProjectKey());
			int workflowId = workflows.get(0).getId();
			issue.setWorkflowId(workflowId);
			
			result = this.issueDao.createIssue(issue);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int updateIssue(IssueVO issue) {
		return this.issueDao.updateIssue(issue);
	}

}

