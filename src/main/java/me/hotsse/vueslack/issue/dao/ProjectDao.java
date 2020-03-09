package me.hotsse.vueslack.issue.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import me.hotsse.vueslack.core.base.BaseDao;
import me.hotsse.vueslack.issue.vo.ProjectVO;

@Repository
public class ProjectDao extends BaseDao {

	public ProjectVO getProject(String key) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key", key);
		
        return sqlSession.selectOne("issue.project.getProject", map);
    }
	
	public List<ProjectVO> getAuthedProjects() {
		return sqlSession.selectList("issue.project.getAuthedProjects");
	}
	
	public int getProjectCursor(String key) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key", key);
		
		return sqlSession.selectOne("issue.project.getProjectCursor", map);
	}
	
}
