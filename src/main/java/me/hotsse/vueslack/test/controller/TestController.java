package me.hotsse.vueslack.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.hotsse.vueslack.core.base.BaseController;
import me.hotsse.vueslack.test.service.TestService;
import me.hotsse.vueslack.test.vo.TestVO;

@RestController
@RequestMapping("/test")
public class TestController extends BaseController {

	@Autowired
	private TestService testService;
	
	@GetMapping()
	public List<TestVO> getList() {
		return this.testService.getList();
	}
	
	@PostMapping()
	public int insert(@RequestBody TestVO test) {
		return this.testService.insert(test.getText());
	}
	
	@DeleteMapping()
	public int delete(@RequestBody TestVO test) {
		return this.testService.delete(test.getId());
	}	
}
