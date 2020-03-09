package me.hotsse.vueslack.issue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.hotsse.vueslack.core.base.BaseService;
import me.hotsse.vueslack.issue.dao.ProjectDao;
import me.hotsse.vueslack.issue.vo.ProjectVO;

@Service
public class ProjectService extends BaseService {
	
	@Autowired
	private ProjectDao projectDao;
	
	public ProjectVO getProject(String key) {
		return this.projectDao.getProject(key);
	}
	
	public List<ProjectVO> getAuthedProjects() {
		return this.projectDao.getAuthedProjects();
	}	

}
