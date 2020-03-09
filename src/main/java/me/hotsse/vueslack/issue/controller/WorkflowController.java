package me.hotsse.vueslack.issue.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.hotsse.vueslack.core.base.BaseController;
import me.hotsse.vueslack.issue.service.WorkflowService;
import me.hotsse.vueslack.issue.vo.WorkflowVO;

@RestController
@RequestMapping("/workflow")
public class WorkflowController extends BaseController {
	
	@Autowired
	private WorkflowService workflowService;
	
	@RequestMapping(value="/getWorkflows")
	public List<WorkflowVO> getWorkflows(@RequestParam Map<String, Object> param) {
		
		String projectKey = null;
		try {
			projectKey = (String)param.get("projectKey");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return this.workflowService.getWorkflows(projectKey);
	}

}
