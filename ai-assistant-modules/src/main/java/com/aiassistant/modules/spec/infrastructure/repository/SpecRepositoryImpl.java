package com.aiassistant.modules.spec.infrastructure.repository;

import com.aiassistant.modules.spec.domain.entity.Spec;
import com.aiassistant.modules.spec.domain.repository.SpecRepository;
import com.aiassistant.modules.spec.infrastructure.mapper.SpecMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 规格仓储实现
 * 实现规格实体的持久化操作
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SpecRepositoryImpl implements SpecRepository {

    private final SpecMapper specMapper;

    @Override
    public Optional<Spec> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        Spec spec = specMapper.selectById(id);
        return Optional.ofNullable(spec);
    }

    @Override
    public Optional<Spec> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }

        LambdaQueryWrapper<Spec> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Spec::getName, name);

        Spec spec = specMapper.selectOne(queryWrapper);
        return Optional.ofNullable(spec);
    }

    @Override
    public List<Spec> listByStatus(Integer status) {
        LambdaQueryWrapper<Spec> queryWrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            queryWrapper.eq(Spec::getStatus, status);
        }
        queryWrapper.orderByDesc(Spec::getCreatedAt);

        return specMapper.selectList(queryWrapper);
    }

    @Override
    public List<Spec> listAll() {
        LambdaQueryWrapper<Spec> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Spec::getCreatedAt);

        return specMapper.selectList(queryWrapper);
    }

    @Override
    public Spec save(Spec spec) {
        if (spec == null) {
            throw new IllegalArgumentException("规格不能为空");
        }

        int rows = specMapper.insert(spec);
        if (rows <= 0) {
            throw new RuntimeException("保存规格失败");
        }

        log.debug("保存规格成功: ID={}", spec.getId());
        return spec;
    }

    @Override
    public Spec update(Spec spec) {
        if (spec == null || spec.getId() == null) {
            throw new IllegalArgumentException("规格或规格ID不能为空");
        }

        int rows = specMapper.updateById(spec);
        if (rows <= 0) {
            throw new RuntimeException("更新规格失败: " + spec.getId());
        }

        log.debug("更新规格成功: ID={}", spec.getId());
        return spec;
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }

        int rows = specMapper.deleteById(id);
        boolean success = rows > 0;

        log.debug("删除规格: ID={}, 结果={}", id, success);
        return success;
    }

    @Override
    public boolean existsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        LambdaQueryWrapper<Spec> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Spec::getName, name);

        Long count = specMapper.selectCount(queryWrapper);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Long excludeId) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        LambdaQueryWrapper<Spec> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Spec::getName, name);
        if (excludeId != null) {
            queryWrapper.ne(Spec::getId, excludeId);
        }

        Long count = specMapper.selectCount(queryWrapper);
        return count != null && count > 0;
    }
}
