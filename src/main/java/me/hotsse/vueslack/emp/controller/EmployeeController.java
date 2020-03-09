package me.hotsse.vueslack.emp.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.hotsse.vueslack.core.base.BaseController;
import me.hotsse.vueslack.core.component.SessionManageComponent;
import me.hotsse.vueslack.emp.service.EmployeeService;
import me.hotsse.vueslack.emp.vo.EmployeeVO;

@RestController
@RequestMapping("/employee")
public class EmployeeController extends BaseController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private SessionManageComponent sessionManager;
	
	@RequestMapping("/login")
	public EmployeeVO login(@RequestParam Map<String, Object> param, HttpServletResponse res) {
		
		String empId = param.get("empId").toString();
		String empPw = param.get("empPw").toString();
		
		this.log.info("empId = " + empId);
		this.log.info("empPw = " + empPw);
		
		EmployeeVO empInfo = this.employeeService.getMatchedEmployee(empId, empPw);
		this.log.info("empInfo = " + empInfo);
				
		if(empInfo != null) {
			empInfo = sessionManager.setEmployee(empInfo);
		}
		
		return empInfo;
	}
	
	@RequestMapping("/logout")
	public void logout(@RequestParam Map<String, Object> param) {		
		UUID uuid = UUID.fromString(param.get("token").toString());
		sessionManager.invalidateEmployee(uuid);
	}
	
	@RequestMapping("/getSession")
	public EmployeeVO getSession(@RequestParam Map<String, Object> param) {
		
		EmployeeVO empInfo = null;
		
		UUID uuid = UUID.fromString(param.get("token").toString());
		empInfo = sessionManager.getEmployee(uuid);
		
		return empInfo;		
	}
	
	@RequestMapping("/getEmployeeByNm")
	public List<EmployeeVO> getEmployeeByNm(@RequestParam Map<String, Object> param) {
		
		String keyword = null;
		
		try {
			keyword = param.get("keyword").toString();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		this.log.info("keyword = " + keyword);
		
		if(keyword == null || keyword.length() < 2) return null;
		return this.employeeService.getEmployeeByNm(keyword);
	}
	

}
