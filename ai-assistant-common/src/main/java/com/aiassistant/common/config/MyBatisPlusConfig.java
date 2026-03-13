package com.aiassistant.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus配置类
 * 注意：分页插件由 mybatis-plus-spring-boot3-starter 自动配置
 */
@Configuration
public class MyBatisPlusConfig {
    
    /**
     * 元数据处理器实现
     * 自动填充创建时间和更新时间
     */
    public static class MetaObjectHandlerImpl implements MetaObjectHandler {
        
        /**
         * 插入时自动填充
         * @param metaObject 元数据对象
         */
        @Override
        public void insertFill(MetaObject metaObject) {
            LocalDateTime now = LocalDateTime.now();
            this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, now);
            this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, now);
        }
        
        /**
         * 更新时自动填充
         * @param metaObject 元数据对象
         */
        @Override
        public void updateFill(MetaObject metaObject) {
            this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
        }
    }
    
    /**
     * 配置元数据处理器
     * @return MetaObjectHandler
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandlerImpl();
    }
}
