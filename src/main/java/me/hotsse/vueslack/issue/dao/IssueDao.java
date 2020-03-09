package me.hotsse.vueslack.issue.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import me.hotsse.vueslack.core.base.BaseDao;
import me.hotsse.vueslack.issue.vo.IssueVO;

@Repository
public class IssueDao extends BaseDao {
	
	public IssueVO getIssue(int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		
		return sqlSession.selectOne("issue.issue.getIssue", map);
	}
	
	public List<IssueVO> getIssues(String projectKey) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("projectKey", projectKey);
		
		return sqlSession.selectList("issue.issue.getIssues", map);
	}
	
	public List<IssueVO> getIssuesByWorkflowId(String projectKey, int workflowId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("projectKey", projectKey);
		map.put("workflowId", workflowId);
		
		return sqlSession.selectList("issue.issue.getIssuesByWorkflowId", map);
	}
	
	public int createIssue(IssueVO issue) {
		return sqlSession.insert("issue.issue.createIssue", issue);
	}
	
	public int updateIssue(IssueVO issue) {
		return sqlSession.update("issue.issue.updateIssue", issue);
	}
	
}
