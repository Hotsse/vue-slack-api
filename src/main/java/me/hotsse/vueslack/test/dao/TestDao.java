package me.hotsse.vueslack.test.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import me.hotsse.vueslack.core.base.BaseDao;
import me.hotsse.vueslack.test.vo.TestVO;

@Repository
public class TestDao extends BaseDao {

	public List<TestVO> getList() {
		return sqlSession.selectList("test.test.getList");
	}
	
	public TestVO get(int id) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		return sqlSession.selectOne("test.test.get", param);
	}
	
	public int insert(String text) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("text", text);
		return sqlSession.insert("test.test.insert", param);
	}
	
	public int delete(int id) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		return sqlSession.delete("test.test.delete", param);
	}
}
