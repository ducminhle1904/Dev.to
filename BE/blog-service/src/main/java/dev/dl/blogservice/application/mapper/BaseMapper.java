package dev.dl.blogservice.application.mapper;

import dev.dl.common.dto.BaseDto;
import dev.dl.common.entity.BaseEntity;

public interface BaseMapper<E extends BaseEntity, D extends BaseDto> {

    E dtoToEntity(D dto) throws Exception;

    D entityToDto(E entity) throws Exception;

}
