package dev.dl.blogservice.integration;

import dev.dl.blogservice.application.grpc.GrpcService;
import dev.dl.blogservice.application.mapper.BlogMapper;
import dev.dl.blogservice.application.request.AddNewBlogRequest;
import dev.dl.blogservice.application.response.AddNewBlogResponse;
import dev.dl.blogservice.application.service.BlogService;
import dev.dl.blogservice.domain.dto.BlogDto;
import dev.dl.grpc.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/blog")
public class BlogController {

    private final BlogService blogService;
    private final GrpcService grpcService;

    @Autowired
    public BlogController(BlogService blogService, GrpcService grpcService) {
        this.blogService = blogService;
        this.grpcService = grpcService;
    }

    @PostMapping
    public AddNewBlogResponse addNewBlog(@RequestBody AddNewBlogRequest request) {
        request.validate();
        BlogDto blogDto = BlogMapper.getInstance().addNewBlogRequestToDto(request);
        this.blogService.addNewBlog(blogDto);
        return new AddNewBlogResponse();
    }

    @GetMapping("/{id}")
    public BlogDto getBlogById(@PathVariable(name = "id") Long id) {
        return this.blogService.findBlogById(id);
    }

    @GetMapping("/user/{id}")
    public String getUserById(@PathVariable(name = "id") Long id) {
        return this.grpcService.findUserById(id).toString();
    }

}
