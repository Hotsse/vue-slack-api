package me.hotsse.vueslack.test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.hotsse.vueslack.core.base.BaseService;
import me.hotsse.vueslack.test.dao.TestDao;
import me.hotsse.vueslack.test.vo.TestVO;

@Service
public class TestService extends BaseService {

	@Autowired
	private TestDao testDao;
	
	public List<TestVO> getList() {
		return this.testDao.getList();
	}
	
	public TestVO get(int id) {
		return this.testDao.get(id);
	}
	
	public int insert(String text) {
		return this.testDao.insert(text);
	}
	
	public int delete(int id) {
		return this.testDao.delete(id);
	}
}
