package dev.dl.blogservice.application.service;

import dev.dl.blogservice.application.mapper.BlogMapper;
import dev.dl.blogservice.domain.dto.BlogDto;
import dev.dl.blogservice.domain.entity.Blog;
import dev.dl.blogservice.infrastructure.BlogRepository;
import dev.dl.common.exception.DLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = {DLException.class})
public class BlogService extends BaseService<Blog, BlogRepository> {

    @Autowired
    public BlogService(BlogRepository repository) {
        super(repository);
    }

    public BlogDto findBlogById(Long id) {
        log.info("FIND BLOG BY ID {}", id);
        Optional<Blog> optionalBlog = this.findById(id);
        if (optionalBlog.isEmpty()) {
            throw DLException.newBuilder().build();
        }
        return BlogMapper.getInstance().entityToDto(optionalBlog.get());
    }
}
