package dev.dl.userservice.integration;

import dev.dl.common.helper.ObjectHelper;
import dev.dl.userservice.application.mapper.UserMapper;
import dev.dl.userservice.application.request.AddNewUserRequest;
import dev.dl.userservice.application.response.AddNewUserResponse;
import dev.dl.userservice.application.service.UserService;
import dev.dl.userservice.domain.dto.UserDto;
import dev.dl.userservice.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public AddNewUserResponse addNewUser(@RequestBody AddNewUserRequest request) throws Exception {
        UserDto userDto = ObjectHelper.mapObjects(request, UserDto.class);
        User user = UserMapper.getInstance().dtoToEntity(userDto);
        user.setUserId(UUID.randomUUID());
        this.userService.save(user);
        return new AddNewUserResponse();
    }

    @PostMapping
    public void login() {
        
    }
}