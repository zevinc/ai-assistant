package com.aiassistant.modules.agent.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 对话响应视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponseVO {

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 响应内容
     */
    private String response;

    /**
     * 时间戳
     */
    private LocalDateTime timestamp;
}
