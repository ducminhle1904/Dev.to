package dev.dl.userservice.application.mapper;

import dev.dl.common.helper.ObjectHelper;
import dev.dl.userservice.domain.dto.RoleDto;
import dev.dl.userservice.domain.entity.Role;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class RoleMapper implements BaseMapper<Role, RoleDto> {

    private static volatile RoleMapper INSTANCE;

    private RoleMapper() {
    }

    public static synchronized RoleMapper getInstance() {
        if (Optional.ofNullable(INSTANCE).isEmpty()) {
            INSTANCE = new RoleMapper();
        }
        return INSTANCE;
    }

    @Override
    public Role dtoToEntity(RoleDto dto) {
        if (Optional.ofNullable(dto).isEmpty()) {
            return null;
        }
        try {
            return ObjectHelper.mapObjects(dto, Role.class);
        } catch (Exception e) {
            log.warn("EXCEPTION OCCUR WHEN MAPPING {}", e.getMessage());
            return null;
        }
    }

    @Override
    public RoleDto entityToDto(Role entity) {
        if (Optional.ofNullable(entity).isEmpty()) {
            return null;
        }
        try {
            return ObjectHelper.mapObjects(entity, RoleDto.class);
        } catch (Exception e) {
            log.warn("EXCEPTION OCCUR WHEN MAPPING {}", e.getMessage());
            return null;
        }
    }
}
