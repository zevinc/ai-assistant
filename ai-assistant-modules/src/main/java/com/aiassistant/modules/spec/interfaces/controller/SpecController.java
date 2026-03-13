package com.aiassistant.modules.spec.interfaces.controller;

import com.aiassistant.common.result.Result;
import com.aiassistant.modules.spec.application.service.SpecApplicationService;
import com.aiassistant.modules.spec.domain.entity.Spec;
import com.aiassistant.modules.spec.domain.enums.SpecStatusEnum;
import com.aiassistant.modules.spec.interfaces.dto.SpecCreateRequest;
import com.aiassistant.modules.spec.interfaces.dto.SpecUpdateRequest;
import com.aiassistant.modules.spec.interfaces.vo.SpecVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 规格控制器
 * 提供规格管理的REST API
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/specs")
@RequiredArgsConstructor
public class SpecController {

    private final SpecApplicationService specApplicationService;

    /**
     * 创建规格
     *
     * @param request 创建请求
     * @return 创建后的规格
     */
    @PostMapping
    public Result<SpecVO> create(@Valid @RequestBody SpecCreateRequest request) {
        log.info("创建规格请求: {}", request.getName());

        Spec spec = Spec.builder()
                .name(request.getName())
                .description(request.getDescription())
                .systemPrompt(request.getSystemPrompt())
                .temperature(request.getTemperature())
                .maxTokens(request.getMaxTokens())
                .modelId(request.getModelId())
                .build();

        Spec createdSpec = specApplicationService.create(spec);
        return Result.ok(toVO(createdSpec));
    }

    /**
     * 更新规格
     *
     * @param request 更新请求
     * @return 更新后的规格
     */
    @PutMapping
    public Result<SpecVO> update(@Valid @RequestBody SpecUpdateRequest request) {
        log.info("更新规格请求: ID={}", request.getId());

        Spec spec = Spec.builder()
                .name(request.getName())
                .description(request.getDescription())
                .systemPrompt(request.getSystemPrompt())
                .temperature(request.getTemperature())
                .maxTokens(request.getMaxTokens())
                .modelId(request.getModelId())
                .build();
        spec.setId(request.getId());

        Spec updatedSpec = specApplicationService.update(spec);
        return Result.ok(toVO(updatedSpec));
    }

    /**
     * 根据ID查询规格
     *
     * @param id 规格ID
     * @return 规格详情
     */
    @GetMapping("/{id}")
    public Result<SpecVO> getById(@PathVariable Long id) {
        log.info("查询规格: ID={}", id);

        return specApplicationService.findById(id)
                .map(spec -> Result.ok(toVO(spec)))
                .orElse(Result.fail(404, "规格不存在"));
    }

    /**
     * 根据名称查询规格
     *
     * @param name 规格名称
     * @return 规格详情
     */
    @GetMapping("/name/{name}")
    public Result<SpecVO> getByName(@PathVariable String name) {
        log.info("根据名称查询规格: {}", name);

        return specApplicationService.findByName(name)
                .map(spec -> Result.ok(toVO(spec)))
                .orElse(Result.fail(404, "规格不存在"));
    }

    /**
     * 查询所有规格
     *
     * @return 规格列表
     */
    @GetMapping
    public Result<List<SpecVO>> listAll() {
        log.info("查询所有规格");

        List<SpecVO> specVOList = specApplicationService.listAll().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return Result.ok(specVOList);
    }

    /**
     * 根据状态查询规格列表
     *
     * @param status 状态
     * @return 规格列表
     */
    @GetMapping("/status/{status}")
    public Result<List<SpecVO>> listByStatus(@PathVariable Integer status) {
        log.info("根据状态查询规格: status={}", status);

        List<SpecVO> specVOList = specApplicationService.listByStatus(status).stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return Result.ok(specVOList);
    }

    /**
     * 删除规格
     *
     * @param id 规格ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("删除规格: ID={}", id);

        specApplicationService.deleteById(id);
        return Result.ok();
    }

    /**
     * 发布规格
     *
     * @param id 规格ID
     * @return 发布后的规格
     */
    @PostMapping("/{id}/publish")
    public Result<SpecVO> publish(@PathVariable Long id) {
        log.info("发布规格: ID={}", id);

        Spec spec = specApplicationService.publish(id);
        return Result.ok(toVO(spec));
    }

    /**
     * 归档规格
     *
     * @param id 规格ID
     * @return 归档后的规格
     */
    @PostMapping("/{id}/archive")
    public Result<SpecVO> archive(@PathVariable Long id) {
        log.info("归档规格: ID={}", id);

        Spec spec = specApplicationService.archive(id);
        return Result.ok(toVO(spec));
    }

    /**
     * 将实体转换为VO
     *
     * @param spec 规格实体
     * @return 规格VO
     */
    private SpecVO toVO(Spec spec) {
        SpecStatusEnum statusEnum = SpecStatusEnum.getByCode(spec.getStatus());

        return SpecVO.builder()
                .id(spec.getId())
                .name(spec.getName())
                .description(spec.getDescription())
                .systemPrompt(spec.getSystemPrompt())
                .temperature(spec.getTemperature())
                .maxTokens(spec.getMaxTokens())
                .modelId(spec.getModelId())
                .status(spec.getStatus())
                .statusDesc(statusEnum != null ? statusEnum.getDescription() : null)
                .createdAt(spec.getCreatedAt())
                .updatedAt(spec.getUpdatedAt())
                .build();
    }
}
