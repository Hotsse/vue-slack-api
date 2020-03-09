package me.hotsse.vueslack.emp.controller;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
	
	private String secretKey = "mehotssevueslacksecretkey";
	
	@RequestMapping("/jwt/login")
	public String jwtLogin(@RequestParam Map<String, Object> param) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Date expireTime = new Date();
        expireTime.setTime(expireTime.getTime() + 1000 * 60 * 1);
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        Map<String, Object> headerMap = new HashMap<String, Object>();

        headerMap.put("typ","JWT");
        headerMap.put("alg","HS256");

        Map<String, Object> map= new HashMap<String, Object>();

        String name = param.get("name").toString();
        String email = param.get("email").toString();

        map.put("name", name);
        map.put("email", email);

        JwtBuilder builder = Jwts.builder().setHeader(headerMap)
                .setClaims(map)
                .setExpiration(expireTime)
                .signWith(signatureAlgorithm, signingKey);

        return builder.compact();
	}
	
	@RequestMapping("/jwt/check")
	public void jwtCheck(@RequestParam Map<String, Object> param) {
		try {
			String jwt = param.get("jwt").toString();
			
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .parseClaimsJws(jwt).getBody(); // 정상 수행된다면 해당 토큰은 정상토큰

            this.log.info("expireTime :" + claims.getExpiration());
            this.log.info("name :" + claims.get("name"));
            this.log.info("Email :" + claims.get("email"));

        } catch (ExpiredJwtException exception) {
            this.log.info("Token Expired");
        } catch (JwtException exception) {
        	this.log.warn("Token Tampered");
        }
	}
	
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
