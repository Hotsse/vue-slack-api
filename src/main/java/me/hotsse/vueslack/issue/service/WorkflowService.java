package me.hotsse.vueslack.issue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.hotsse.vueslack.core.base.BaseService;
import me.hotsse.vueslack.issue.dao.WorkflowDao;
import me.hotsse.vueslack.issue.vo.WorkflowVO;

@Service
public class WorkflowService extends BaseService {
	
	@Autowired
	private WorkflowDao workflowDao;
	
	public List<WorkflowVO> getWorkflows(String projectKey) {
		return this.workflowDao.getWorkflows(projectKey);
	}	

}
