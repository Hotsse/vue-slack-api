package me.hotsse.vueslack.emp.service;

import java.security.MessageDigest;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.hotsse.vueslack.core.base.BaseService;
import me.hotsse.vueslack.emp.dao.EmployeeDao;
import me.hotsse.vueslack.emp.vo.EmployeeVO;

@Service
public class EmployeeService extends BaseService {
	
	@Autowired
	private EmployeeDao employeeDao;
	
	public EmployeeVO getMatchedEmployee(String empId, String empPw) {
		
		String encEmpPw = this.encryptToSHA256(empPw);		
		return this.employeeDao.getMatchedEmployee(empId, encEmpPw);		
	}
	
	public List<EmployeeVO> getEmployeeByNm(String keyword) {
		return this.employeeDao.getEmployeeByNm(keyword);
	}
	
	private String encryptToSHA256(String planText) {
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(planText.getBytes());
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				String hex = Integer.toHexString(0xff & byteData[i]);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
	}

}
