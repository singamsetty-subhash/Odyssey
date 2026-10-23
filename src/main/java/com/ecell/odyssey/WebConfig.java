// package com.ecell.odyssey;

// import java.nio.file.Path;
// import java.nio.file.Paths;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
// import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class WebConfig implements WebMvcConfigurer {

//     @Override
//     public void addResourceHandlers(ResourceHandlerRegistry registry) {
//         Path uploadDir = Paths.get("uploads");
//         String uploadPath = uploadDir.toFile().getAbsolutePath();
//         registry.addResourceHandler("/uploads/**")
//                 .addResourceLocations("file:/" + uploadPath.replace("\\", "/") + "/");
//     }

//     @Override
//     public void addViewControllers(ViewControllerRegistry registry) {
//         registry.addViewController("/calendar").setViewName("calendar");
//     }
// }

package com.ecell.odyssey;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("uploads")
                .toAbsolutePath()
                .normalize();

        String uploadLocation = uploadDir.toUri().toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadLocation);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/calendar")
                .setViewName("calendar");
    }
}
