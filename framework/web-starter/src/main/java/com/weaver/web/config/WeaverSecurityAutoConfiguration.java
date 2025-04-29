package com.weaver.web.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weaver.core.utils.JsonUtils;
import com.weaver.core.utils.StringUtils;
import com.weaver.web.manager.EnumManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties({WeaverSecurityProperties.class})
@ComponentScan(basePackages = {"com.weaver.web"})
public class WeaverSecurityAutoConfiguration implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WeaverSecurityAutoConfiguration.class);

    @Autowired
    private WeaverSecurityProperties weaverSecurityProperties;

    @Value("${system.swagger.enable:false}")
    private Boolean swagger;

    @Value("${error.path:/error}")
    private String errorPath;

    @Bean
    @ConditionalOnMissingBean(MappingJackson2HttpMessageConverter.class)
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        converter.setObjectMapper(mapper);
        return converter;
    }

    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jackson2HttpMessageConverter());
    }

    @Bean
    public EnumManager enumManager() {
        return EnumManager.getInstance();
    }

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    public boolean enableSwagger() {
        return swagger;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(loginInterceptor());
        // 前端如果包打过来是static和index.html，无需配置staticPath
        addInterceptor.excludePathPatterns("/static/**");
        addInterceptor.excludePathPatterns("/**/*.html");
        // swagger开启
        if (enableSwagger()) {
            addInterceptor.excludePathPatterns("/swagger-resources/**");
            addInterceptor.excludePathPatterns("/webjars/**");
            addInterceptor.excludePathPatterns("/v2/**");
            addInterceptor.excludePathPatterns("/swagger-ui.html/**");
            addInterceptor.excludePathPatterns("/doc.html/**");
        }
        // 允许的静态资源
        String staticPaths = weaverSecurityProperties.getStaticPaths();
        if (StringUtils.isNotBlank(staticPaths)) {
            for (String path : weaverSecurityProperties.getStaticPaths().split(",")) {
                if (StringUtils.isBlank(path)) {
                    continue;
                }
                addInterceptor.excludePathPatterns("/" + path + "/**");
            }
        }
        addStaticOutPath(addInterceptor);
        addInterceptor.excludePathPatterns(weaverSecurityProperties.getLoginUri());
        addInterceptor.excludePathPatterns(errorPath);
        addInterceptor.addPathPatterns("/**");
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        if (enableSwagger()) {
            registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        }
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        addStaticOutPath(registry);
    }

    // 初始化外部静态资源
    public void addStaticOutPath(Object obj) {
        String staticOutPaths = weaverSecurityProperties.getStaticOutPaths();
        if (StringUtils.isBlank(staticOutPaths)) {
            return;
        }
        Map<String, Object> jsonObject = JsonUtils.jsonToGenericMap(staticOutPaths, Object.class);
        if (jsonObject == null) {
            return;
        }
        logger.info(">>>>>>>>>>>>staticOutPaths: {}", jsonObject);
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            String innerPath = entry.getKey();
            Object outPath = entry.getValue();
            if (StringUtils.isBlank(innerPath) || outPath == null || StringUtils.isBlank(outPath)) {
                continue;
            }
            if (obj instanceof ResourceHandlerRegistry registry) {
                registry.addResourceHandler(innerPath).addResourceLocations(outPath.toString());
            } else if (obj instanceof InterceptorRegistration interceptorRegistration) {
                interceptorRegistration.excludePathPatterns(innerPath);
            }
        }
    }

}
