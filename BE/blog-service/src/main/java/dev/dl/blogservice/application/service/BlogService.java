package dev.dl.blogservice.application.service;

import dev.dl.blogservice.application.grpc.UserGrpcService;
import dev.dl.blogservice.application.mapper.BlogMapper;
import dev.dl.blogservice.domain.dto.BlogDto;
import dev.dl.blogservice.domain.entity.Blog;
import dev.dl.blogservice.infrastructure.BlogRepository;
import dev.dl.common.exception.DLException;
import dev.dl.common.helper.DateTimeHelper;
import dev.dl.grpc.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional(rollbackFor = {DLException.class})
public class BlogService extends BaseService<Blog, BlogRepository> {

    private final UserGrpcService userGrpcService;

    @Autowired
    public BlogService(BlogRepository repository, UserGrpcService userGrpcService) {
        super(repository);
        this.userGrpcService = userGrpcService;
    }

    public BlogDto findBlogById(Long id) {
        log.info("FIND BLOG BY ID {}", id);
        Optional<Blog> optionalBlog = this.findById(id);
        if (optionalBlog.isEmpty()) {
            throw DLException.newBuilder()
                    .message("Blog does not exist")
                    .timestamp(DateTimeHelper.generateCurrentTimeDefault())
                    .build();
        }
        return BlogMapper.getInstance().entityToDto(optionalBlog.get());
    }

    public BlogDto addNewBlog(BlogDto blogDto) {
        log.info("ADD NEW BLOG");
        User user = this.userGrpcService.findUserById(blogDto.getUserId().toString());
        if (Optional.ofNullable(user).isEmpty()) {
            throw DLException.newBuilder().timestamp(DateTimeHelper.generateCurrentTimeDefault())
                    .message("Can not find user").httpStatus(HttpStatus.NOT_IMPLEMENTED).build();
        }
        Blog blog = BlogMapper.getInstance().dtoToEntity(blogDto);
        blog.setActive(true);
        blog.setUserId(UUID.fromString(user.getUserId()));
        return BlogMapper.getInstance().entityToDto(this.save(blog));
    }
}
