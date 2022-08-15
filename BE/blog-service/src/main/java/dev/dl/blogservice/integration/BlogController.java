package dev.dl.blogservice.integration;

import dev.dl.blogservice.application.grpc.UserGrpcService;
import dev.dl.blogservice.application.mapper.BlogMapper;
import dev.dl.blogservice.application.request.AddNewBlogRequest;
import dev.dl.blogservice.application.response.AddNewBlogResponse;
import dev.dl.blogservice.application.response.BlogDetailResponse;
import dev.dl.blogservice.application.service.BlogService;
import dev.dl.blogservice.domain.dto.BlogDto;
import dev.dl.common.helper.ObjectHelper;
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
    private final UserGrpcService userGrpcService;

    @Autowired
    public BlogController(BlogService blogService, UserGrpcService userGrpcService) {
        this.blogService = blogService;
        this.userGrpcService = userGrpcService;
    }

    @PostMapping
    public AddNewBlogResponse addNewBlog(@RequestBody AddNewBlogRequest request) {
        request.validate();
        BlogDto blogDto = BlogMapper.getInstance().addNewBlogRequestToDto(request);
        this.blogService.addNewBlog(blogDto);
        return new AddNewBlogResponse();
    }

    @GetMapping("/{id}")
    public BlogDetailResponse getBlogById(@PathVariable(name = "id") Long id) {
        BlogDto blogDto = this.blogService.findBlogById(id);
        User user = this.userGrpcService.findUserById(blogDto.getUserId().toString());
        return BlogMapper.getInstance().dtoToDetailResponse(blogDto, user);
    }

    @GetMapping
    public void getUserById() {
        this.blogService.findAll();
    }

}
