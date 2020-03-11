package me.hotsse.vueslack.emp.service;

import java.security.Key;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.hotsse.vueslack.core.base.BaseService;
import me.hotsse.vueslack.emp.dao.EmployeeDao;
import me.hotsse.vueslack.emp.vo.EmployeeVO;

@Service
public class EmployeeService extends BaseService {
	
	@Autowired
	private EmployeeDao employeeDao;
	
	// 해싱 알고리즘 Secret Key
    private final String secretKey = "mehotssevueslacksecretkey";
     
    public String makeToken(EmployeeVO empInfo) {
         
        // 해싱 알고리즘 설정
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
 
        // 토큰 만료 시간 설정
        Date expireTime = new Date();
        expireTime.setTime(expireTime.getTime() + 1000 * 60 * 1);
 
        // Header 생성
        Map<String, Object> header = new HashMap<String, Object>();
        header.put("typ","JWT");
        header.put("alg","HS256");
 
        // Payload 생성
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("empNo", empInfo.getEmpNo());
        payload.put("empNm", empInfo.getEmpNm());
         
        // JWT Builder를 통한 토큰 생성
        JwtBuilder builder = Jwts.builder().setHeader(header)
            .setClaims(payload)
            .setExpiration(expireTime)
            .signWith(signatureAlgorithm, signingKey);
 
        return builder.compact();
    }
     
    public EmployeeVO isValidToken(String accessToken) {
         
        EmployeeVO empInfo = null;
         
        try {
            // Secret Key 를 사용한 복호화 진행
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .parseClaimsJws(accessToken).getBody();
 
            this.log.info("expireTime :" + claims.getExpiration());
             
            empInfo = new EmployeeVO();
            empInfo.setEmpNo(Integer.parseInt(claims.get("empNo").toString()));
            empInfo.setEmpNm(claims.get("empNm").toString());
 
        } catch (ExpiredJwtException exception) {
            this.log.info("Token Expired");
        } catch (JwtException exception) {
            this.log.warn("Token Tampered");
        } catch (Exception e) {
            this.log.error(e.toString());
        }
         
        return empInfo;     
    }
	
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
