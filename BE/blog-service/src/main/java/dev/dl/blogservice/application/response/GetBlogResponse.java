package dev.dl.blogservice.application.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.dl.blogservice.domain.dto.BlogDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetBlogResponse {

    @JsonProperty("blog_detail")
    private BlogDto blogDto;

}
