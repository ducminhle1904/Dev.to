package dev.dl.common.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.dl.common.constant.Constant;
import dev.dl.common.exception.DLException;
import dev.dl.common.helper.ObjectHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
public abstract class BaseRequest implements Serializable {

    @JsonIgnore
    private List<String> emptyField;

    public void validate() throws DLException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime current = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of(Constant.DEFAULT_TIMEZONE)).toLocalDateTime();
        log.info("PROCESS TIME [VALIDATION] - {}", dateTimeFormatter.format(current));
        this.rules();
        this.fieldValidate();
    }

    public abstract void rules() throws DLException;

    public void fieldValidate() throws DLException {
        LocalDateTime current = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of(Constant.DEFAULT_TIMEZONE)).toLocalDateTime();
        if (!ObjectHelper.isNullOrEmpty(this.emptyField)) {
            String message = String.format(
                    "%1$s can not be empty",
                    emptyField.stream().collect(Collectors.joining(", ", "[", "]"))
            );
            throw DLException.newBuilder()
                    .timestamp(current)
                    .message(message)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    public void addEmptyField(String emptyFieldName) {
        if (ObjectHelper.objectIsNull(this.getEmptyField())) {
            this.setEmptyField(new ArrayList<>());
        }
        this.getEmptyField().add(emptyFieldName);
    }
}