package com.project.kawaiinu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 全経路についてCORS設定
                .allowedOrigins("*")  // 許容するソース
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")  // 許容するHTTPメソッド
                .allowedHeaders("*")  // 許容するヘーダ
                .allowCredentials(false)  // 権限(Cookieなど)の許容
                .maxAge(3600);  // pre-flight要請時間(秒)
    }
}
