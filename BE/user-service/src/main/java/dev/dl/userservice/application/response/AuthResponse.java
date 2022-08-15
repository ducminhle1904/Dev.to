package dev.dl.userservice.application.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthResponse {
    private String userId;
    private List<String> roles;
    private boolean nonLock;
}
