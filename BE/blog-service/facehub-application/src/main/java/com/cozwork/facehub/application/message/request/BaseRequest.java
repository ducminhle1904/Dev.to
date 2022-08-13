package com.cozwork.facehub.application.message.request;

import com.cozwork.facehub.application.constant.AppConstant;
import com.cozwork.facehub.application.exception.CozException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
public abstract class BaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private List<String> emptyField;

    public void validate() throws CozException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime current = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of(AppConstant.DEFAULT_TIMEZONE)).toLocalDateTime();
        log.info("REQUEST [VALIDATION] - {}", dateTimeFormatter.format(current));
        this.rules();
        this.fieldValidate();
    }

    public abstract void rules();

    public void fieldValidate() throws CozException {
        if (Optional.ofNullable(this.getEmptyField()).isPresent() && !this.getEmptyField().isEmpty()) {
            String message = String.format(
                    "%1$s can not be empty",
                    emptyField.stream().collect(Collectors.joining(", ", "[", "]"))
            );
            throw CozException.builder()
                    .message(message)
                    .build();
        }
    }

    public void addEmptyField(String emptyFieldName) {
        if (Optional.ofNullable(this.getEmptyField()).isEmpty()) {
            this.setEmptyField(new ArrayList<>());
        }
        this.getEmptyField().add(emptyFieldName);
    }

}
