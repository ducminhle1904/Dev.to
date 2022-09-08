package dev.dl.blogservice.application.mapper;

import dev.dl.blogservice.application.request.AddNewBlogRequest;
import dev.dl.blogservice.application.response.AddNewBlogResponse;
import dev.dl.blogservice.application.response.BlogDetailResponse;
import dev.dl.blogservice.domain.dto.BlogDto;
import dev.dl.blogservice.domain.entity.Blog;
import dev.dl.blogservice.domain.graphql.BlogGql;
import dev.dl.common.helper.ObjectHelper;
import dev.dl.grpc.user.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class BlogMapper implements BaseMapper<Blog, BlogDto> {

    private static volatile BlogMapper INSTANCE;

    private BlogMapper() {
    }

    public static synchronized BlogMapper getInstance() {
        if (Optional.ofNullable(INSTANCE).isEmpty()) {
            INSTANCE = new BlogMapper();
        }
        return INSTANCE;
    }

    @Override
    public Blog dtoToEntity(BlogDto dto) {
        if (Optional.ofNullable(dto).isEmpty()) {
            return null;
        }
        try {
            return ObjectHelper.mapObjects(dto, Blog.class);
        } catch (Exception e) {
            log.warn("EXCEPTION OCCUR WHEN MAPPING {}", e.getMessage());
            return null;
        }
    }

    @Override
    public BlogDto entityToDto(Blog entity) {
        if (Optional.ofNullable(entity).isEmpty()) {
            return null;
        }
        try {
            return ObjectHelper.mapObjects(entity, BlogDto.class);
        } catch (Exception e) {
            log.warn("EXCEPTION OCCUR WHEN MAPPING {}", e.getMessage());
            return null;
        }
    }

    public BlogDto addNewBlogRequestToDto(AddNewBlogRequest request) {
        if (Optional.ofNullable(request).isEmpty()) {
            return null;
        }
        try {
            return ObjectHelper.mapObjects(request, BlogDto.class);
        } catch (Exception e) {
            log.warn("EXCEPTION OCCUR WHEN MAPPING {}", e.getMessage());
            return null;
        }
    }

    public BlogDetailResponse dtoToDetailResponse(BlogDto dto, User user) {
        if (Optional.ofNullable(dto).isEmpty() || Optional.ofNullable(user).isEmpty()) {
            return null;
        }
        try {
            BlogDetailResponse response = ObjectHelper.mapObjects(dto, BlogDetailResponse.class);
            response.setAuthorName(
                    String.format(
                            "%1$s %2$s",
                            user.getFirstName(),
                            user.getLastName()
                    )
            );
            return response;
        } catch (Exception e) {
            log.warn("EXCEPTION OCCUR WHEN MAPPING {}", e.getMessage());
            return null;
        }
    }

    public BlogGql dtoToGraphQl(BlogDto dto) {
        if (Optional.ofNullable(dto).isEmpty()) {
            return null;
        }
        return new BlogGql(
                dto.getId(),
                dto.getActive(),
                dto.getCreatedAt(),
                dto.getUpdatedAt(),
                dto.getCreatedBy(),
                dto.getUpdatedBy(),
                dto.getTitle(),
                dto.getBody(),
                null
        );
    }
}
