package com.aiassistant.config;

import com.aiassistant.common.web.ApiVersionHandlerMapping;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * API 版本控制 WebMvc 配置类
 * 通过 WebMvcRegistrations 注册自定义 HandlerMapping
 */
@Configuration
public class ApiVersionWebMvcConfig implements WebMvcRegistrations {

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        ApiVersionHandlerMapping handlerMapping = new ApiVersionHandlerMapping();
        handlerMapping.setOrder(0);
        return handlerMapping;
    }
}
