-- =============================================================================
-- AI Assistant 数据库初始化脚本
-- 版本: V1.0.0
-- 描述: 创建所有模块表结构
-- =============================================================================

-- =============================================================================
-- 规格模块 (Spec Module)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 表: t_spec (AI 规格配置表)
-- 描述: 存储 AI Agent 的规格配置信息
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_spec` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '规格名称',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '规格描述',
    `system_prompt` TEXT COMMENT '系统提示词',
    `temperature` DECIMAL(3, 2) DEFAULT 0.70 COMMENT '温度参数(0.00-1.00)',
    `max_tokens` INT DEFAULT 4096 COMMENT '最大生成令牌数',
    `model_id` BIGINT DEFAULT NULL COMMENT '关联模型ID',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `updated_by` BIGINT DEFAULT NULL COMMENT '更新人ID',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_spec_name` (`name`),
    KEY `idx_spec_status` (`status`),
    KEY `idx_spec_model_id` (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI规格配置表';

-- -----------------------------------------------------------------------------
-- 表: t_rule (规则配置表)
-- 描述: 存储规格关联的规则配置
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_rule` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '规则名称',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '规则描述',
    `rule_type` VARCHAR(50) NOT NULL COMMENT '规则类型: PRECONDITION/POSTCONDITION/VALIDATION/TRANSFORMATION',
    `expression` TEXT COMMENT '规则表达式(JSON格式)',
    `priority` INT DEFAULT 0 COMMENT '优先级(数值越大优先级越高)',
    `spec_id` BIGINT NOT NULL COMMENT '关联规格ID',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `updated_by` BIGINT DEFAULT NULL COMMENT '更新人ID',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_rule_name` (`name`),
    KEY `idx_rule_type` (`rule_type`),
    KEY `idx_rule_spec_id` (`spec_id`),
    KEY `idx_rule_priority` (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='规则配置表';

-- =============================================================================
-- 知识库模块 (Wiki Module)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 表: t_wiki (知识库文档表)
-- 描述: 存储知识库文档信息
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_wiki` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `title` VARCHAR(255) NOT NULL COMMENT '文档标题',
    `content` LONGTEXT COMMENT '文档内容',
    `category` VARCHAR(100) DEFAULT NULL COMMENT '文档分类',
    `tags` VARCHAR(500) DEFAULT NULL COMMENT '标签(JSON数组)',
    `source_type` VARCHAR(50) DEFAULT 'MANUAL' COMMENT '来源类型: MANUAL/FILE/URL/API',
    `source_url` VARCHAR(500) DEFAULT NULL COMMENT '来源URL',
    `spec_id` BIGINT DEFAULT NULL COMMENT '关联规格ID',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `updated_by` BIGINT DEFAULT NULL COMMENT '更新人ID',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_wiki_title` (`title`),
    KEY `idx_wiki_category` (`category`),
    KEY `idx_wiki_source_type` (`source_type`),
    KEY `idx_wiki_spec_id` (`spec_id`),
    FULLTEXT KEY `ft_wiki_content` (`title`, `content`) WITH PARSER ngram
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='知识库文档表';

-- -----------------------------------------------------------------------------
-- 表: t_wiki_chunk (知识库文档分块表)
-- 描述: 存储文档分块及向量信息
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_wiki_chunk` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `wiki_id` BIGINT NOT NULL COMMENT '关联文档ID',
    `chunk_index` INT NOT NULL COMMENT '分块索引',
    `content` TEXT NOT NULL COMMENT '分块内容',
    `embedding` TEXT COMMENT '向量嵌入(JSON格式)',
    `token_count` INT DEFAULT 0 COMMENT '令牌数量',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_chunk_wiki_id` (`wiki_id`),
    KEY `idx_chunk_index` (`chunk_index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='知识库文档分块表';

-- =============================================================================
-- 技能模块 (Skill Module)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 表: t_skill (技能配置表)
-- 描述: 存储可调用的技能配置
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_skill` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '技能名称',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '技能描述',
    `skill_type` VARCHAR(50) NOT NULL COMMENT '技能类型: HTTP/GRPC/LAMBDA/MCP',
    `endpoint` VARCHAR(500) DEFAULT NULL COMMENT '技能端点URL',
    `input_schema` TEXT COMMENT '输入参数Schema(JSON格式)',
    `output_schema` TEXT COMMENT '输出参数Schema(JSON格式)',
    `timeout` INT DEFAULT 30000 COMMENT '超时时间(毫秒)',
    `retry_count` INT DEFAULT 3 COMMENT '重试次数',
    `spec_id` BIGINT DEFAULT NULL COMMENT '关联规格ID',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `updated_by` BIGINT DEFAULT NULL COMMENT '更新人ID',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_skill_name` (`name`),
    KEY `idx_skill_type` (`skill_type`),
    KEY `idx_skill_spec_id` (`spec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='技能配置表';

-- =============================================================================
-- 命令模块 (Command Module)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 表: t_command (命令配置表)
-- 描述: 存储命令模式配置
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_command` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '命令名称',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '命令描述',
    `pattern` VARCHAR(255) NOT NULL COMMENT '命令匹配模式(正则表达式)',
    `handler_class` VARCHAR(255) NOT NULL COMMENT '处理器类名',
    `spec_id` BIGINT DEFAULT NULL COMMENT '关联规格ID',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `updated_by` BIGINT DEFAULT NULL COMMENT '更新人ID',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_command_name` (`name`),
    KEY `idx_command_spec_id` (`spec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='命令配置表';

-- -----------------------------------------------------------------------------
-- 表: t_command_execution (命令执行记录表)
-- 描述: 存储命令执行历史记录
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_command_execution` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `command_id` BIGINT NOT NULL COMMENT '关联命令ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '执行用户ID',
    `input` TEXT COMMENT '输入内容',
    `output` TEXT COMMENT '输出内容',
    `execution_time` BIGINT DEFAULT 0 COMMENT '执行耗时(毫秒)',
    `status` TINYINT DEFAULT 1 COMMENT '执行状态: 0-失败, 1-成功, 2-超时',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_execution_command_id` (`command_id`),
    KEY `idx_execution_user_id` (`user_id`),
    KEY `idx_execution_status` (`status`),
    KEY `idx_execution_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='命令执行记录表';

-- =============================================================================
-- MCP模块 (Model Context Protocol Module)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 表: t_mcp_server (MCP服务器配置表)
-- 描述: 存储MCP服务器连接配置
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_mcp_server` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '服务器名称',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '服务器描述',
    `transport_type` VARCHAR(50) NOT NULL COMMENT '传输类型: STDIO/HTTP/WEBSOCKET',
    `endpoint` VARCHAR(500) DEFAULT NULL COMMENT '服务器端点',
    `api_key` VARCHAR(255) DEFAULT NULL COMMENT 'API密钥(加密存储)',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-离线, 1-在线',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `updated_by` BIGINT DEFAULT NULL COMMENT '更新人ID',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_mcp_server_name` (`name`),
    KEY `idx_mcp_server_transport_type` (`transport_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='MCP服务器配置表';

-- -----------------------------------------------------------------------------
-- 表: t_mcp_tool (MCP工具配置表)
-- 描述: 存储MCP服务器提供的工具信息
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_mcp_tool` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `server_id` BIGINT NOT NULL COMMENT '关联服务器ID',
    `name` VARCHAR(100) NOT NULL COMMENT '工具名称',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '工具描述',
    `input_schema` TEXT COMMENT '输入参数Schema(JSON格式)',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_mcp_tool_server_id` (`server_id`),
    KEY `idx_mcp_tool_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='MCP工具配置表';

-- =============================================================================
-- 记忆模块 (Memory Module)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 表: t_memory (记忆存储表)
-- 描述: 存储AI对话记忆信息
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_memory` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `session_id` VARCHAR(64) NOT NULL COMMENT '会话ID',
    `agent_id` BIGINT DEFAULT NULL COMMENT '关联AgentID',
    `role` VARCHAR(50) NOT NULL COMMENT '角色: USER/ASSISTANT/SYSTEM',
    `content` TEXT NOT NULL COMMENT '消息内容',
    `summary` TEXT COMMENT '消息摘要',
    `importance` INT DEFAULT 0 COMMENT '重要性评分(0-100)',
    `token_count` INT DEFAULT 0 COMMENT '令牌数量',
    `memory_type` VARCHAR(50) DEFAULT 'SHORT_TERM' COMMENT '记忆类型: SHORT_TERM/LONG_TERM/EPISODIC',
    `metadata` TEXT COMMENT '元数据(JSON格式)',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_memory_session_id` (`session_id`),
    KEY `idx_memory_agent_id` (`agent_id`),
    KEY `idx_memory_role` (`role`),
    KEY `idx_memory_type` (`memory_type`),
    KEY `idx_memory_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='记忆存储表';

-- =============================================================================
-- 规划模块 (Plan Module)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 表: t_plan (规划任务表)
-- 描述: 存储AI规划任务信息
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_plan` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '规划名称',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '规划描述',
    `agent_id` BIGINT DEFAULT NULL COMMENT '关联AgentID',
    `session_id` VARCHAR(64) DEFAULT NULL COMMENT '关联会话ID',
    `strategy` VARCHAR(50) DEFAULT 'SEQUENTIAL' COMMENT '执行策略: SEQUENTIAL/PARALLEL/CONDITIONAL',
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0-待执行, 1-执行中, 2-已完成, 3-已失败, 4-已取消',
    `total_steps` INT DEFAULT 0 COMMENT '总步骤数',
    `completed_steps` INT DEFAULT 0 COMMENT '已完成步骤数',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `updated_by` BIGINT DEFAULT NULL COMMENT '更新人ID',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_plan_agent_id` (`agent_id`),
    KEY `idx_plan_session_id` (`session_id`),
    KEY `idx_plan_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='规划任务表';

-- -----------------------------------------------------------------------------
-- 表: t_plan_step (规划步骤表)
-- 描述: 存储规划任务的步骤详情
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_plan_step` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `plan_id` BIGINT NOT NULL COMMENT '关联规划ID',
    `step_index` INT NOT NULL COMMENT '步骤索引',
    `name` VARCHAR(100) NOT NULL COMMENT '步骤名称',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '步骤描述',
    `action` VARCHAR(100) NOT NULL COMMENT '执行动作',
    `input` TEXT COMMENT '输入参数(JSON格式)',
    `output` TEXT COMMENT '输出结果(JSON格式)',
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0-待执行, 1-执行中, 2-已完成, 3-已失败, 4-已跳过',
    `depends_on` VARCHAR(255) DEFAULT NULL COMMENT '依赖步骤ID列表(JSON数组)',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_step_plan_id` (`plan_id`),
    KEY `idx_step_index` (`step_index`),
    KEY `idx_step_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='规划步骤表';

-- =============================================================================
-- Agent模块 (Agent Module)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 表: t_agent (Agent配置表)
-- 描述: 存储AI Agent配置信息
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_agent` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT 'Agent名称',
    `description` VARCHAR(500) DEFAULT NULL COMMENT 'Agent描述',
    `spec_id` BIGINT DEFAULT NULL COMMENT '关联规格ID',
    `model_id` BIGINT DEFAULT NULL COMMENT '关联模型ID',
    `system_prompt` TEXT COMMENT '系统提示词',
    `temperature` DECIMAL(3, 2) DEFAULT 0.70 COMMENT '温度参数(0.00-1.00)',
    `max_tokens` INT DEFAULT 4096 COMMENT '最大生成令牌数',
    `enable_memory` TINYINT DEFAULT 1 COMMENT '是否启用记忆: 0-否, 1-是',
    `enable_planning` TINYINT DEFAULT 0 COMMENT '是否启用规划: 0-否, 1-是',
    `enable_tools` TINYINT DEFAULT 1 COMMENT '是否启用工具: 0-否, 1-是',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `updated_by` BIGINT DEFAULT NULL COMMENT '更新人ID',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_agent_name` (`name`),
    KEY `idx_agent_spec_id` (`spec_id`),
    KEY `idx_agent_model_id` (`model_id`),
    KEY `idx_agent_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Agent配置表';

-- -----------------------------------------------------------------------------
-- 表: t_agent_session (Agent会话表)
-- 描述: 存储Agent会话信息
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_agent_session` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `agent_id` BIGINT NOT NULL COMMENT '关联AgentID',
    `user_id` BIGINT DEFAULT NULL COMMENT '用户ID',
    `session_id` VARCHAR(64) NOT NULL COMMENT '会话ID(唯一标识)',
    `title` VARCHAR(255) DEFAULT NULL COMMENT '会话标题',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-已关闭, 1-进行中',
    `message_count` INT DEFAULT 0 COMMENT '消息数量',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_session_id` (`session_id`),
    KEY `idx_session_agent_id` (`agent_id`),
    KEY `idx_session_user_id` (`user_id`),
    KEY `idx_session_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Agent会话表';

-- =============================================================================
-- 反馈模块 (Feedback Module)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 表: t_feedback (用户反馈表)
-- 描述: 存储用户对AI回复的反馈信息
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_feedback` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `session_id` VARCHAR(64) NOT NULL COMMENT '会话ID',
    `agent_id` BIGINT DEFAULT NULL COMMENT '关联AgentID',
    `user_id` BIGINT DEFAULT NULL COMMENT '用户ID',
    `message_id` BIGINT DEFAULT NULL COMMENT '关联消息ID',
    `rating` INT DEFAULT NULL COMMENT '评分(1-5)',
    `comment` TEXT COMMENT '反馈评论',
    `feedback_type` VARCHAR(50) DEFAULT 'RATING' COMMENT '反馈类型: RATING/LIKE_DISLIKE/TEXT/BUG_REPORT',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-已处理, 1-待处理',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_feedback_session_id` (`session_id`),
    KEY `idx_feedback_agent_id` (`agent_id`),
    KEY `idx_feedback_user_id` (`user_id`),
    KEY `idx_feedback_rating` (`rating`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户反馈表';

-- =============================================================================
-- 评估模块 (Evaluation Module)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 表: t_evaluation (评估记录表)
-- 描述: 存储AI输出评估结果
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_evaluation` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `agent_id` BIGINT DEFAULT NULL COMMENT '关联AgentID',
    `session_id` VARCHAR(64) DEFAULT NULL COMMENT '关联会话ID',
    `input_text` TEXT COMMENT '输入文本',
    `output_text` TEXT COMMENT '输出文本',
    `expected_output` TEXT COMMENT '期望输出',
    `score` DECIMAL(5, 2) DEFAULT NULL COMMENT '评估得分(0.00-100.00)',
    `metrics` TEXT COMMENT '评估指标(JSON格式)',
    `evaluation_type` VARCHAR(50) DEFAULT 'AUTO' COMMENT '评估类型: AUTO/MANUAL/HYBRID',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-失败, 1-成功',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `updated_by` BIGINT DEFAULT NULL COMMENT '更新人ID',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_evaluation_agent_id` (`agent_id`),
    KEY `idx_evaluation_session_id` (`session_id`),
    KEY `idx_evaluation_score` (`score`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评估记录表';

-- =============================================================================
-- 提示词模块 (Prompt Module)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 表: t_prompt_template (提示词模板表)
-- 描述: 存储可复用的提示词模板
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_prompt_template` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '模板名称',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '模板描述',
    `category` VARCHAR(100) DEFAULT NULL COMMENT '模板分类',
    `template` TEXT NOT NULL COMMENT '模板内容(支持变量占位符)',
    `variables` TEXT COMMENT '变量定义(JSON格式)',
    `version` INT DEFAULT 1 COMMENT '版本号',
    `spec_id` BIGINT DEFAULT NULL COMMENT '关联规格ID',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `updated_by` BIGINT DEFAULT NULL COMMENT '更新人ID',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_template_name` (`name`),
    KEY `idx_template_category` (`category`),
    KEY `idx_template_spec_id` (`spec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='提示词模板表';

-- =============================================================================
-- 监控模块 (Monitor Module)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 表: t_monitor_metric (监控指标表)
-- 描述: 存储系统监控指标数据
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_monitor_metric` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `metric_name` VARCHAR(100) NOT NULL COMMENT '指标名称',
    `metric_type` VARCHAR(50) NOT NULL COMMENT '指标类型: COUNTER/GAUGE/HISTOGRAM/SUMMARY',
    `value` DECIMAL(20, 4) NOT NULL COMMENT '指标值',
    `tags` VARCHAR(500) DEFAULT NULL COMMENT '标签(JSON格式)',
    `agent_id` BIGINT DEFAULT NULL COMMENT '关联AgentID',
    `timestamp` DATETIME NOT NULL COMMENT '采集时间戳',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_metric_name` (`metric_name`),
    KEY `idx_metric_type` (`metric_type`),
    KEY `idx_metric_agent_id` (`agent_id`),
    KEY `idx_metric_timestamp` (`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='监控指标表';

-- -----------------------------------------------------------------------------
-- 表: t_monitor_alert (监控告警表)
-- 描述: 存储监控告警信息
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_monitor_alert` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `alert_name` VARCHAR(100) NOT NULL COMMENT '告警名称',
    `metric_name` VARCHAR(100) NOT NULL COMMENT '关联指标名称',
    `threshold` DECIMAL(20, 4) NOT NULL COMMENT '阈值',
    `operator` VARCHAR(10) NOT NULL COMMENT '比较运算符: GT/GTE/LT/LTE/EQ/NEQ',
    `message` TEXT COMMENT '告警消息',
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0-待处理, 1-已确认, 2-已解决',
    `triggered_at` DATETIME DEFAULT NULL COMMENT '触发时间',
    `resolved_at` DATETIME DEFAULT NULL COMMENT '解决时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `updated_by` BIGINT DEFAULT NULL COMMENT '更新人ID',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_alert_name` (`alert_name`),
    KEY `idx_alert_metric_name` (`metric_name`),
    KEY `idx_alert_status` (`status`),
    KEY `idx_alert_triggered_at` (`triggered_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='监控告警表';

-- =============================================================================
-- 初始化完成
-- =============================================================================
