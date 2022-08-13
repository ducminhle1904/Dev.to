package com.cozwork.facehub.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContactDto extends BaseDto {

    private String name;
    private String address;
    private LocalDateTime dob;

}
