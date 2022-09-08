package dev.dl.blogservice.application.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.dl.common.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@SuppressWarnings("Lombok")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlogDetailResponse extends BaseResponse {
    @JsonProperty("author_name")
    private String authorName;
    @JsonProperty("title")
    private String title;
    @JsonProperty("body")
    private String body;
    @JsonProperty("posted_at")
    private LocalDateTime createdAt;
    @JsonProperty("last_update")
    private LocalDateTime updatedAt;
}
