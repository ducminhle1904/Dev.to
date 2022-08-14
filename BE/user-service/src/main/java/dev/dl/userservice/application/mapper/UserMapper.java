package dev.dl.userservice.application.mapper;

import dev.dl.common.helper.ObjectHelper;
import dev.dl.userservice.domain.dto.UserDto;
import dev.dl.userservice.domain.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class UserMapper implements BaseMapper<User, UserDto> {

    private static volatile UserMapper INSTANCE;

    private UserMapper() {
    }

    public static synchronized UserMapper getInstance() {
        if (Optional.ofNullable(INSTANCE).isEmpty()) {
            INSTANCE = new UserMapper();
        }
        return INSTANCE;
    }
    
    @Override
    public User dtoToEntity(UserDto dto) {
        if (Optional.ofNullable(dto).isEmpty()) {
            return null;
        }
        try {
            return ObjectHelper.mapObjects(dto, User.class);
        } catch (Exception e) {
            log.warn("EXCEPTION OCCUR WHEN MAPPING {}", e.getMessage());
            return null;
        }
    }

    @Override
    public UserDto entityToDto(User entity) {
        if (Optional.ofNullable(entity).isEmpty()) {
            return null;
        }
        try {
            return ObjectHelper.mapObjects(entity, UserDto.class);
        } catch (Exception e) {
            log.warn("EXCEPTION OCCUR WHEN MAPPING {}", e.getMessage());
            return null;
        }
    }
}
