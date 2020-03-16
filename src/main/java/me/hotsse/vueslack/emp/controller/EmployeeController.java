package me.hotsse.vueslack.emp.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.hotsse.vueslack.core.base.BaseController;
import me.hotsse.vueslack.emp.service.EmployeeService;
import me.hotsse.vueslack.emp.vo.EmployeeVO;

@RestController
@RequestMapping("/employee")
public class EmployeeController extends BaseController {
	
	@Autowired
	private EmployeeService employeeService;
	
	private final String cookieDomain = ".hotsse.me";
	
	@RequestMapping("/login")
	public EmployeeVO login(@RequestParam Map<String, Object> param, HttpServletResponse res) {
		
		String accessToken = null;
		
		String empId = param.get("empId").toString();
		String empPw = param.get("empPw").toString();
		
		this.log.info("empId = " + empId);
		this.log.info("empPw = " + empPw);
		
		EmployeeVO empInfo = this.employeeService.getMatchedEmployee(empId, empPw);
		this.log.info("empInfo = " + empInfo);
				
		if(empInfo != null) {
			
			accessToken = this.employeeService.makeToken(empInfo);
			
			Cookie cookie = new Cookie("accessToken", accessToken);
	        cookie.setPath("/");
	        cookie.setDomain(cookieDomain);
	        res.addCookie(cookie);
		}
		
		return empInfo;
	}
	
	@RequestMapping("/logout")
	public void logout() {
		Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setDomain(cookieDomain);
        cookie.setMaxAge(0);
	}
	
	@RequestMapping("/isValidToken")
	public EmployeeVO getSession(@RequestHeader("Authorization") String accessToken) {
		
		EmployeeVO empInfo = this.employeeService.isValidToken(accessToken);
		
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
		
		if(keyword == null || keyword.length() < 2) return null;
		return this.employeeService.getEmployeeByNm(keyword);
	}
	

}
