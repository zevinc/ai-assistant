package com.aiassistant.modules.rule.application.service;

import com.aiassistant.common.event.DomainEventPublisher;
import com.aiassistant.common.event.SimpleDomainEvent;
import com.aiassistant.modules.rule.domain.entity.Rule;
import com.aiassistant.modules.rule.domain.enums.RuleTypeEnum;
import com.aiassistant.modules.rule.domain.repository.RuleRepository;
import com.aiassistant.modules.rule.domain.service.RuleEvaluator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 规则应用服务
 * 提供规则的CRUD操作和评估功能
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RuleApplicationService {

    private final RuleRepository ruleRepository;
    private final RuleEvaluator ruleEvaluator;
    private final DomainEventPublisher domainEventPublisher;

    /**
     * 创建规则
     *
     * @param rule 规则实体
     * @return 创建后的规则
     */
    @Transactional(rollbackFor = Exception.class)
    public Rule create(Rule rule) {
        // 验证规则
        validateRule(rule);

        // 验证表达式语法
        if (!ruleEvaluator.validateExpression(rule.getExpression())) {
            throw new IllegalArgumentException("规则表达式语法无效: " + rule.getExpression());
        }

        // 检查名称是否已存在
        if (ruleRepository.existsByName(rule.getName())) {
            throw new IllegalArgumentException("规则名称已存在: " + rule.getName());
        }

        // 设置默认状态
        if (rule.getStatus() == null) {
            rule.setStatus(1); // 默认启用
        }

        // 设置默认优先级
        if (rule.getPriority() == null) {
            rule.setPriority(100);
        }

        // 保存规则
        Rule savedRule = ruleRepository.save(rule);

        // 发布领域事件
        publishEvent(new SimpleDomainEvent(String.valueOf(savedRule.getId()), "Rule", "rule.created"));

        log.info("创建规则成功: {} (ID: {})", savedRule.getName(), savedRule.getId());
        return savedRule;
    }

    /**
     * 更新规则
     *
     * @param rule 规则实体
     * @return 更新后的规则
     */
    @Transactional(rollbackFor = Exception.class)
    public Rule update(Rule rule) {
        // 检查规则是否存在
        Rule existingRule = ruleRepository.findById(rule.getId())
                .orElseThrow(() -> new IllegalArgumentException("规则不存在: " + rule.getId()));

        // 验证规则
        validateRule(rule);

        // 验证表达式语法
        if (!ruleEvaluator.validateExpression(rule.getExpression())) {
            throw new IllegalArgumentException("规则表达式语法无效: " + rule.getExpression());
        }

        // 保留不可修改的字段
        rule.setCreatedAt(existingRule.getCreatedAt());
        rule.setCreatedBy(existingRule.getCreatedBy());

        // 更新规则
        Rule updatedRule = ruleRepository.update(rule);

        // 发布领域事件
        publishEvent(new SimpleDomainEvent(String.valueOf(updatedRule.getId()), "Rule", "rule.updated"));

        log.info("更新规则成功: {} (ID: {})", updatedRule.getName(), updatedRule.getId());
        return updatedRule;
    }

    /**
     * 根据ID查询规则
     *
     * @param id 规则ID
     * @return 规则实体
     */
    public Optional<Rule> findById(Long id) {
        return ruleRepository.findById(id);
    }

    /**
     * 根据规格ID查询规则列表
     *
     * @param specId 规格ID
     * @return 规则列表
     */
    public List<Rule> findBySpecId(Long specId) {
        return ruleRepository.findBySpecId(specId);
    }

    /**
     * 根据类型查询规则列表
     *
     * @param ruleType 规则类型
     * @return 规则列表
     */
    public List<Rule> listByType(String ruleType) {
        return ruleRepository.listByType(ruleType);
    }

    /**
     * 查询所有规则
     *
     * @return 规则列表
     */
    public List<Rule> listAll() {
        return ruleRepository.listAll();
    }

    /**
     * 删除规则
     *
     * @param id 规则ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        // 检查规则是否存在
        Rule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("规则不存在: " + id));

        // 删除规则
        ruleRepository.deleteById(id);

        // 发布领域事件
        publishEvent(new SimpleDomainEvent(String.valueOf(id), "Rule", "rule.deleted"));

        log.info("删除规则成功: {} (ID: {})", rule.getName(), id);
    }

    /**
     * 评估规则
     *
     * @param ruleId 规则ID
     * @param context 评估上下文
     * @return 评估结果
     */
    public boolean evaluate(Long ruleId, Map<String, Object> context) {
        Rule rule = ruleRepository.findById(ruleId)
                .orElseThrow(() -> new IllegalArgumentException("规则不存在: " + ruleId));

        return ruleEvaluator.evaluate(rule, context);
    }

    /**
     * 评估规则（带详情）
     *
     * @param ruleId 规则ID
     * @param context 评估上下文
     * @return 评估结果详情
     */
    public RuleEvaluator.EvaluationResult evaluateWithDetails(Long ruleId, Map<String, Object> context) {
        Rule rule = ruleRepository.findById(ruleId)
                .orElseThrow(() -> new IllegalArgumentException("规则不存在: " + ruleId));

        return ruleEvaluator.evaluateWithDetails(rule, context);
    }

    /**
     * 批量评估规则
     *
     * @param specId 规格ID
     * @param ruleType 规则类型
     * @param context 评估上下文
     * @return 评估结果列表
     */
    public List<RuleEvaluator.EvaluationResult> batchEvaluate(Long specId, String ruleType, Map<String, Object> context) {
        List<Rule> rules;
        if (specId != null && ruleType != null) {
            rules = ruleRepository.findBySpecIdAndType(specId, ruleType);
        } else if (specId != null) {
            rules = ruleRepository.findBySpecId(specId);
        } else if (ruleType != null) {
            rules = ruleRepository.listByType(ruleType);
        } else {
            rules = ruleRepository.listAll();
        }

        // 按优先级排序
        rules = rules.stream()
                .sorted((r1, r2) -> {
                    int p1 = r1.getPriority() != null ? r1.getPriority() : Integer.MAX_VALUE;
                    int p2 = r2.getPriority() != null ? r2.getPriority() : Integer.MAX_VALUE;
                    return Integer.compare(p1, p2);
                })
                .collect(Collectors.toList());

        return rules.stream()
                .map(rule -> ruleEvaluator.evaluateWithDetails(rule, context))
                .collect(Collectors.toList());
    }

    /**
     * 启用规则
     *
     * @param id 规则ID
     * @return 更新后的规则
     */
    @Transactional(rollbackFor = Exception.class)
    public Rule enable(Long id) {
        Rule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("规则不存在: " + id));

        rule.setStatus(1);
        Rule updatedRule = ruleRepository.update(rule);

        log.info("启用规则成功: {} (ID: {})", rule.getName(), id);
        return updatedRule;
    }

    /**
     * 禁用规则
     *
     * @param id 规则ID
     * @return 更新后的规则
     */
    @Transactional(rollbackFor = Exception.class)
    public Rule disable(Long id) {
        Rule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("规则不存在: " + id));

        rule.setStatus(0);
        Rule updatedRule = ruleRepository.update(rule);

        log.info("禁用规则成功: {} (ID: {})", rule.getName(), id);
        return updatedRule;
    }

    /**
     * 验证规则
     *
     * @param rule 规则实体
     */
    private void validateRule(Rule rule) {
        if (rule == null) {
            throw new IllegalArgumentException("规则不能为空");
        }

        if (rule.getName() == null || rule.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("规则名称不能为空");
        }

        if (rule.getRuleType() == null || rule.getRuleType().trim().isEmpty()) {
            throw new IllegalArgumentException("规则类型不能为空");
        }

        if (!RuleTypeEnum.isValidCode(rule.getRuleType())) {
            throw new IllegalArgumentException("无效的规则类型: " + rule.getRuleType());
        }
    }

    /**
     * 发布领域事件
     *
     * @param event 领域事件
     */
    private void publishEvent(SimpleDomainEvent event) {
        try {
            domainEventPublisher.publish(event);
        } catch (Exception e) {
            log.warn("发布领域事件失败: {}", event, e);
        }
    }
}
