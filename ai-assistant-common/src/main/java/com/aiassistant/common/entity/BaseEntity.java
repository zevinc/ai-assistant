package com.aiassistant.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * MyBatis-Plus 基础实体类
 * 所有实体类都应继承此类
 */
@Data
public class BaseEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID（使用雪花算法生成）
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 创建时间
     */
    @TableField(fill = com.baomidou.mybatisplus.annotation.FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @TableField(fill = com.baomidou.mybatisplus.annotation.FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    /**
     * 创建人
     */
    private String createdBy;
    
    /**
     * 更新人
     */
    private String updatedBy;
    
    /**
     * 逻辑删除标识（0-未删除，1-已删除）
     */
    @TableLogic
    private Integer isDeleted = 0;
}
