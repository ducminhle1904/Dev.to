package com.cozwork.facehub.application.mapper;

import com.cozwork.facehub.application.exception.CozException;
import com.cozwork.facehub.application.exception.ExceptionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExceptionMapper {

    ExceptionMapper INSTANCE = Mappers.getMapper(ExceptionMapper.class);

    ExceptionResponse exceptionMapper(CozException cozException);

}
