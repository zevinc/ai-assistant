package com.aiassistant.modules.rule.infrastructure.repository;

import com.aiassistant.modules.rule.domain.entity.Rule;
import com.aiassistant.modules.rule.domain.repository.RuleRepository;
import com.aiassistant.modules.rule.infrastructure.mapper.RuleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 规则仓储实现
 * 实现规则实体的持久化操作
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class RuleRepositoryImpl implements RuleRepository {

    private final RuleMapper ruleMapper;

    @Override
    public Optional<Rule> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        Rule rule = ruleMapper.selectById(id);
        return Optional.ofNullable(rule);
    }

    @Override
    public List<Rule> findBySpecId(Long specId) {
        if (specId == null) {
            return List.of();
        }

        LambdaQueryWrapper<Rule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Rule::getSpecId, specId);
        queryWrapper.orderByAsc(Rule::getPriority);
        queryWrapper.orderByDesc(Rule::getCreatedAt);

        return ruleMapper.selectList(queryWrapper);
    }

    @Override
    public List<Rule> listByType(String ruleType) {
        if (ruleType == null || ruleType.trim().isEmpty()) {
            return List.of();
        }

        LambdaQueryWrapper<Rule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Rule::getRuleType, ruleType);
        queryWrapper.orderByAsc(Rule::getPriority);
        queryWrapper.orderByDesc(Rule::getCreatedAt);

        return ruleMapper.selectList(queryWrapper);
    }

    @Override
    public List<Rule> listAll() {
        LambdaQueryWrapper<Rule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Rule::getPriority);
        queryWrapper.orderByDesc(Rule::getCreatedAt);

        return ruleMapper.selectList(queryWrapper);
    }

    @Override
    public List<Rule> findBySpecIdAndType(Long specId, String ruleType) {
        if (specId == null || ruleType == null || ruleType.trim().isEmpty()) {
            return List.of();
        }

        LambdaQueryWrapper<Rule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Rule::getSpecId, specId);
        queryWrapper.eq(Rule::getRuleType, ruleType);
        queryWrapper.orderByAsc(Rule::getPriority);
        queryWrapper.orderByDesc(Rule::getCreatedAt);

        return ruleMapper.selectList(queryWrapper);
    }

    @Override
    public Rule save(Rule rule) {
        if (rule == null) {
            throw new IllegalArgumentException("规则不能为空");
        }

        int rows = ruleMapper.insert(rule);
        if (rows <= 0) {
            throw new RuntimeException("保存规则失败");
        }

        log.debug("保存规则成功: ID={}", rule.getId());
        return rule;
    }

    @Override
    public Rule update(Rule rule) {
        if (rule == null || rule.getId() == null) {
            throw new IllegalArgumentException("规则或规则ID不能为空");
        }

        int rows = ruleMapper.updateById(rule);
        if (rows <= 0) {
            throw new RuntimeException("更新规则失败: " + rule.getId());
        }

        log.debug("更新规则成功: ID={}", rule.getId());
        return rule;
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }

        int rows = ruleMapper.deleteById(id);
        boolean success = rows > 0;

        log.debug("删除规则: ID={}, 结果={}", id, success);
        return success;
    }

    @Override
    public int deleteBySpecId(Long specId) {
        if (specId == null) {
            return 0;
        }

        LambdaQueryWrapper<Rule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Rule::getSpecId, specId);

        int rows = ruleMapper.delete(queryWrapper);
        log.debug("根据规格ID删除规则: specId={}, 删除数量={}", specId, rows);
        return rows;
    }

    @Override
    public boolean existsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        LambdaQueryWrapper<Rule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Rule::getName, name);

        Long count = ruleMapper.selectCount(queryWrapper);
        return count != null && count > 0;
    }
}
