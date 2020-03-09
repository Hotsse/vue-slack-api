package me.hotsse.vueslack.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ContextConfig implements WebMvcConfigurer {
	
	/**
	 * <pre>
	 *		CORS 맵핑 설정
	 * </pre>
	 * @methodName	addCorsMappings
	 * @author		dkdlrja
	 * @param		registry
	 * @return
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("*")
			.allowedHeaders("*")
			.allowCredentials(true);			
	}

}
