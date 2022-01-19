package com.sorbonne.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaInfo());
    }
    private ApiInfo metaInfo() {
        return new ApiInfo(
                "LIBRARY APP API",
                "Spring Boot Swagger API for Warnings.",
                "2.0",
                "Terms of service",
                new Contact("NHAILA DAEIMI", "https://www-apr.lip6.fr/~buixuan/daar2021", "kaoutar.nhaila@etu.sorbonne-universite.fr"),
                "License of API", "API license URL", Collections.emptyList());
    }
}
