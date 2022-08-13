package com.cozwork.facehub.application.service;

import com.cozwork.facehub.application.exception.CozException;
import com.cozwork.facehub.application.mapper.ContactMapper;
import com.cozwork.facehub.domain.dto.ContactDto;
import com.cozwork.facehub.domain.entity.Contact;
import com.cozwork.facehub.infrastructure.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class ContactService extends BaseService<Contact, ContactRepository> {


    @Autowired
    public ContactService(ContactRepository repository) {
        super(repository);
    }

    public ContactDto findContactById(Long id) throws CozException {
        log.info("FIND CONTACT BY ID: {}", id);
        Optional<Contact> optionalContact = this.getRepository().findById(id);
        if (optionalContact.isEmpty()) {
            throw CozException.builder()
                    .message("Can not find contact with id " + id)
                    .build();
        }
        Contact contact = optionalContact.get();
        return ContactMapper.INSTANCE.entityToDto(contact);
    }

    public ContactDto addContact(ContactDto dto) {
        Contact contact = ContactMapper.INSTANCE.dtoToEntity(dto);
        log.info("SAVE NEW CONTACT");
        contact.setCreatedAt(LocalDateTime.now());
        contact.setUpdatedAt(LocalDateTime.now());
        contact.setActive(true);
        return ContactMapper.INSTANCE.entityToDto(this.getRepository().save(contact));
    }

}
