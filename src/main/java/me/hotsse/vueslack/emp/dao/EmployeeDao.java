package me.hotsse.vueslack.emp.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import me.hotsse.vueslack.core.base.BaseDao;
import me.hotsse.vueslack.emp.vo.EmployeeVO;

@Repository
public class EmployeeDao extends BaseDao {
	
	public EmployeeVO getMatchedEmployee(String empId, String empPw) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("empId", empId);
		map.put("empPw", empPw);
		
		return sqlSession.selectOne("emp.employee.getMatchedEmployee", map);		
	}
	
	public List<EmployeeVO> getEmployeeByNm(String keyword) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("keyword", keyword);
		
		return sqlSession.selectList("emp.employee.getEmployeeByNm", map);
	}

}
