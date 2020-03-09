package me.hotsse.vueslack.issue.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import me.hotsse.vueslack.core.base.BaseDao;
import me.hotsse.vueslack.issue.vo.WorkflowVO;

@Repository
public class WorkflowDao extends BaseDao {
	
	public List<WorkflowVO> getWorkflows(String projectKey) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("projectKey", projectKey);
		
		return sqlSession.selectList("issue.workflow.getWorkflows", map);
	}
	
}
