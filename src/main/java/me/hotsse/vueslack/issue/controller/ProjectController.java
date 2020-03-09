package me.hotsse.vueslack.issue.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.hotsse.vueslack.core.base.BaseController;
import me.hotsse.vueslack.issue.service.ProjectService;
import me.hotsse.vueslack.issue.vo.ProjectVO;

@RestController
@RequestMapping("/project")
public class ProjectController extends BaseController {
	
	@Autowired
	private ProjectService projectService;
	
	@RequestMapping(value="/getProject")
	public ProjectVO getProject(@RequestParam Map<String, Object> param) {
		
		String key = null;
		try {
			key = (String)param.get("key");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return this.projectService.getProject(key);
	}
	
	@RequestMapping(value="/getAuthedProjects")
	public List<ProjectVO> getAuthedProjects(@RequestParam Map<String, Object> param) {
		return this.projectService.getAuthedProjects();
	}

}
