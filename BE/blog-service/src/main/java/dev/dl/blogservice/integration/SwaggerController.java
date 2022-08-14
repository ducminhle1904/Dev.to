package dev.dl.blogservice.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping
@ApiIgnore
public class SwaggerController {

    @GetMapping
    public RedirectView swagger() {
        return new RedirectView("/swagger-ui/");
    }

}
