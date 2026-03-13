package com.aiassistant.modules.command.infrastructure.repository;

import com.aiassistant.modules.command.domain.entity.Command;
import com.aiassistant.modules.command.domain.repository.CommandRepository;
import com.aiassistant.modules.command.infrastructure.mapper.CommandMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 命令仓储实现
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class CommandRepositoryImpl implements CommandRepository {

    private final CommandMapper commandMapper;

    @Override
    public Optional<Command> findById(Long id) {
        return Optional.ofNullable(commandMapper.selectById(id));
    }

    @Override
    public Optional<Command> findByName(String name) {
        LambdaQueryWrapper<Command> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Command::getName, name);
        return Optional.ofNullable(commandMapper.selectOne(wrapper));
    }

    @Override
    public List<Command> findBySpecId(Long specId) {
        LambdaQueryWrapper<Command> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Command::getSpecId, specId);
        return commandMapper.selectList(wrapper);
    }

    @Override
    public List<Command> findAll() {
        return commandMapper.selectList(null);
    }

    @Override
    public Command save(Command command) {
        commandMapper.insert(command);
        return command;
    }

    @Override
    public Command update(Command command) {
        commandMapper.updateById(command);
        return command;
    }

    @Override
    public void deleteById(Long id) {
        commandMapper.deleteById(id);
    }
}
