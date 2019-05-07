package com.hust.smarthotel.configs;

import com.hust.smarthotel.components.photo.domain_service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    @Autowired
    private PhotoService photoService;


    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry){
        String imagePath = PhotoService.ABSOLUTE_PATH.concat(photoService.dirPath);
        System.out.println(imagePath);
        registry.addResourceHandler(photoService.baseUrl.concat("**")).addResourceLocations("file:".concat(imagePath));
    }
}
