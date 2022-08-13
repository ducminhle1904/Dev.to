package com.cozwork.facehub.integration;

import com.cozwork.facehub.application.exception.CozException;
import com.cozwork.facehub.application.grpc.GrpcService;
import com.cozwork.facehub.application.mapper.ContactMapper;
import com.cozwork.facehub.application.message.request.AddContactRequest;
import com.cozwork.facehub.application.message.response.AddNewContactResponse;
import com.cozwork.facehub.application.service.ContactService;
import com.cozwork.facehub.application.service.KafkaProducerService;
import com.cozwork.facehub.domain.dto.ContactDto;
import com.cozwork.kafka.message.KafkaRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/contact")
public class ContactController extends BaseController {

    private final ContactService contactService;

    private final KafkaProducerService kafkaProducerService;

    private final GrpcService grpcService;

    @Autowired
    public ContactController(ContactService contactService, KafkaProducerService kafkaProducerService, GrpcService grpcService) {
        this.contactService = contactService;
        this.kafkaProducerService = kafkaProducerService;
        this.grpcService = grpcService;
    }

    @PostMapping("/add")
    public AddNewContactResponse addNewContact(@RequestBody AddContactRequest request) throws CozException {
        request.validate();
        ContactDto contactDto = ContactMapper.INSTANCE.addContactRequestToDto(request);
        return ContactMapper.INSTANCE.dtoToAddNewContactResponse(this.contactService.addContact(contactDto));
    }

    @GetMapping("/{id}")
    public ContactDto getContact(@PathVariable(name = "id") Long id) throws CozException {
        return this.contactService.findContactById(id);
    }

    @PostMapping("/{id}")
    public void demoKafka(@PathVariable(name = "id") Long id) throws CozException {
        KafkaRequest<ContactDto> kafkaRequest = new KafkaRequest<>();
        kafkaRequest.setBody(this.contactService.findContactById(id));
        this.kafkaProducerService.sendMessage(kafkaRequest, "facehub-reminder", 0);
    }

    @PostMapping()
    public void demoGrpc() throws CozException {
        KafkaRequest<ContactDto> kafkaRequest = new KafkaRequest<>();
        kafkaRequest.setBody(this.contactService.findContactById(1L));
        grpcService.sendRequest(this.contactService.findContactById(1L), "Something", Map.class);
    }

}
