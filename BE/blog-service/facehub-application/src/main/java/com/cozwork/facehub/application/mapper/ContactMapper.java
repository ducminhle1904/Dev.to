package com.cozwork.facehub.application.mapper;

import com.cozwork.facehub.application.message.request.AddContactRequest;
import com.cozwork.facehub.application.message.response.AddNewContactResponse;
import com.cozwork.facehub.domain.dto.ContactDto;
import com.cozwork.facehub.domain.entity.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContactMapper {

    ContactMapper INSTANCE = Mappers.getMapper(ContactMapper.class);

    Contact dtoToEntity(ContactDto dto);

    ContactDto entityToDto(Contact contact);

    ContactDto addContactRequestToDto(AddContactRequest request);

    AddNewContactResponse dtoToAddNewContactResponse(ContactDto dto);

}
