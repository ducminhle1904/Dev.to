package dev.dl.blogservice.application.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.dl.common.exception.DLException;
import dev.dl.common.helper.ObjectHelper;
import dev.dl.common.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddNewBlogRequest extends BaseRequest {

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String body;

    @Override
    public void rules() throws DLException {
        if (ObjectHelper.isNullOrEmpty(title)) {
            this.addEmptyField("title");
        }
        if (ObjectHelper.isNullOrEmpty(body)) {
            this.addEmptyField("body");
        }
    }
}
