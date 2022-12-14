package dev.dl.blogservice.integration;

import dev.dl.blogservice.application.grpc.UserGrpcServiceClient;
import dev.dl.blogservice.application.mapper.BlogMapper;
import dev.dl.blogservice.application.request.AddNewBlogRequest;
import dev.dl.blogservice.application.request.graphql.BlogInputGql;
import dev.dl.blogservice.application.response.AddNewBlogResponse;
import dev.dl.blogservice.application.response.BlogDetailResponse;
import dev.dl.blogservice.application.service.BlogService;
import dev.dl.blogservice.application.service.RabbitProducerService;
import dev.dl.blogservice.domain.dto.BlogDto;
import dev.dl.blogservice.domain.graphql.BlogGql;
import dev.dl.blogservice.domain.graphql.valueobject.UserGql;
import dev.dl.grpc.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@SuppressWarnings("DuplicatedCode")
@RestController
@Slf4j
@RequestMapping("/api/blog")
public class BlogController {

    private final BlogService blogService;
    private final UserGrpcServiceClient userGrpcServiceClient;
    private final RabbitProducerService rabbitProducerService;

    @Autowired
    public BlogController(BlogService blogService, UserGrpcServiceClient userGrpcServiceClient, RabbitProducerService rabbitProducerService) {
        this.blogService = blogService;
        this.userGrpcServiceClient = userGrpcServiceClient;
        this.rabbitProducerService = rabbitProducerService;
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
        User user = this.userGrpcServiceClient.findUserById(blogDto.getUserId().toString());
        return BlogMapper.getInstance().dtoToDetailResponse(blogDto, user);
    }

    @GetMapping("/r/{id}")
    public BlogDetailResponse rab(@PathVariable(name = "id") Long id) {
        BlogDto blogDto = this.blogService.findBlogById(id);
        User user = this.userGrpcServiceClient.findUserById(blogDto.getUserId().toString());
        rabbitProducerService.send(BlogMapper.getInstance().dtoToDetailResponse(blogDto, user));
        return BlogMapper.getInstance().dtoToDetailResponse(blogDto, user);
    }

    @GetMapping
    public void getUserById() {
        this.blogService.findAll();
    }

    @QueryMapping(name = "blogById")
    public BlogGql findBlogById(@Argument(name = "id") Long id) {
        BlogDto blogDto = this.blogService.findBlogById(id);
        User user = this.userGrpcServiceClient.findUserById(blogDto.getUserId().toString());
        UserGql userGql = new UserGql();
        if (Optional.ofNullable(user).isPresent()) {
            userGql.setId(user.getUserId());
            userGql.setFirstName(user.getFirstName());
            userGql.setLastName(user.getLastName());
        }
        BlogGql blogGql = BlogMapper.getInstance().dtoToGraphQl(blogDto);
        blogGql.setUser(userGql);
        return blogGql;
    }

    @QueryMapping(name = "blogByDto")
    public BlogGql findBlogByDto(@Argument(name = "request") BlogInputGql request) {
        BlogDto blogDto = this.blogService.findBlogById(request.getId());
        User user = this.userGrpcServiceClient.findUserById(blogDto.getUserId().toString());
        UserGql userGql = new UserGql();
        if (Optional.ofNullable(user).isPresent()) {
            userGql.setId(user.getUserId());
            userGql.setFirstName(user.getFirstName());
            userGql.setLastName(user.getLastName());
        }
        BlogGql blogGql = BlogMapper.getInstance().dtoToGraphQl(blogDto);
        blogGql.setUser(userGql);
        return blogGql;
    }

}
