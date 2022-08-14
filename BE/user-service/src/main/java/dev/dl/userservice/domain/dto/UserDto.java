package dev.dl.userservice.domain.dto;

import dev.dl.common.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@SuppressWarnings("Lombok")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto extends BaseDto {
    private UUID userId;
    private String firstName;
    private String lastName;
}
