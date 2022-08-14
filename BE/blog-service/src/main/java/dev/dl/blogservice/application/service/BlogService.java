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
import java.util.UUID;

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

    public BlogDto addNewBlog(BlogDto blogDto) {
        log.info("ADD NEW BLOG");
        Blog blog = BlogMapper.getInstance().dtoToEntity(blogDto);
        blog.setActive(true);
        //blog.setUserId(UUID.randomUUID());
        return BlogMapper.getInstance().entityToDto(this.save(blog));
    }
}
