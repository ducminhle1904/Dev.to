package dev.dl.userservice.domain.dto;

import dev.dl.common.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleDto extends BaseDto {

    private String roleName;
    private String roleDescription;

}
