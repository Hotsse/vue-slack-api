package me.hotsse.vueslack.emp.service;

import java.security.Key;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import javax.xml.ws.http.HTTPException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.hotsse.vueslack.core.base.BaseService;
import me.hotsse.vueslack.emp.dao.EmployeeDao;
import me.hotsse.vueslack.emp.vo.EmployeeVO;
import me.hotsse.vueslack.emp.vo.OauthLoginVO;
import me.hotsse.vueslack.emp.vo.OauthProfileVO;

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
    
    public OauthProfileVO oauthLogin(String code) {
    	
    	final String CLIENT_ID = "6167bfaa-932e-4c97-a077-fd059a00dc2d";
        final String CLIENT_SECRET = "6FI5xVdsqyNSggrb86vNvDJLURRloD4mMBL4HGua";
        final String SSO_URL ="https://dev-nosts.nexon.com";
        final String REDIRECT_URI ="http://local.msign.weboffice.co.kr:8080/login";
        final String SSO_RESOURCE_URL="http://dev.service.aqua.nexon.co.kr:9971";
    	
    	OauthProfileVO oauthProfile = null;
    	
    	try{
            //1.nosts에 토큰 발급 요청
            WebClient client = WebClient
                    .builder()
                    .baseUrl(SSO_URL)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .build();

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("code", code);
            params.add("client_id", CLIENT_ID);
            params.add("client_secret", CLIENT_SECRET);
            params.add("redirect_uri", REDIRECT_URI);

            WebClient.RequestHeadersSpec uri = client
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/oauth/token")
                            .build()
                    )
                    .body(BodyInserters.fromFormData(params));

            OauthLoginVO result = uri.retrieve().bodyToMono(OauthLoginVO.class).block();

            //2.리소스 서버에 유저 정보 요청
            WebClient client2 = WebClient
                    .builder()
                    .baseUrl(SSO_RESOURCE_URL)
                    .defaultHeader(HttpHeaders.AUTHORIZATION, result.getToken_type() + " " + result.getAccess_token())
                    .build();
            WebClient.RequestHeadersSpec uri2 = client2
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/OAuthResourceService/rest/getprofile")
                            .build()
                    );

            String result2 = uri2.retrieve().bodyToMono(String.class).block();

            final ObjectMapper mapper = new ObjectMapper();
            mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JsonNode resultJsonNode = mapper.readTree(result2);
            JsonNode profileJsonNode = resultJsonNode.get("GetProfileResult");

            oauthProfile = mapper.readValue(profileJsonNode.toString(), OauthProfileVO.class);

        } catch (HTTPException | JsonProcessingException ex){
            ex.printStackTrace();
        }
    	
    	return oauthProfile;
    }
    
    public String makeOAuthToken(OauthProfileVO profile) {
        
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
        payload.put("CMPCode", profile.getCMPCode());
        payload.put("EMPNO", profile.getEMPNO());
        payload.put("EMPName", profile.getEMPName());
         
        // JWT Builder를 통한 토큰 생성
        JwtBuilder builder = Jwts.builder().setHeader(header)
            .setClaims(payload)
            .setExpiration(expireTime)
            .signWith(signatureAlgorithm, signingKey);
 
        return builder.compact();
    }
     
    public OauthProfileVO isValidOAuthToken(String accessToken) {
         
    	OauthProfileVO profile = null;
         
        try {
            // Secret Key 를 사용한 복호화 진행
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .parseClaimsJws(accessToken).getBody();
 
            this.log.info("expireTime :" + claims.getExpiration());
             
            profile = new OauthProfileVO();
            profile.setCMPCode(claims.get("CMPCode").toString());
            profile.setEMPNO(claims.get("EMPNO").toString());
            profile.setEMPName(claims.get("EMPName").toString());
 
        } catch (ExpiredJwtException exception) {
            this.log.info("Token Expired");
        } catch (JwtException exception) {
            this.log.warn("Token Tampered");
        } catch (Exception e) {
            this.log.error(e.toString());
        }
         
        return profile;     
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
