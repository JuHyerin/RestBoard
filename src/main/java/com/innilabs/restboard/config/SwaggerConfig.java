package com.innilabs.restboard.config;

import java.util.ArrayList;
import java.util.List;

import com.innilabs.restboard.auth.JwtProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	 @Bean
	    public Docket api() { 
			//Authentication header 처리를 위해 사용
			List<Parameter> global = new ArrayList<>();
			global.add(new ParameterBuilder()
							.name(JwtProvider.AUTH_HEADER_STRING)
							.description("Access Token")
							.parameterType("header")
							.required(false)
							.modelRef(new ModelRef("string"))
							.build());
			return new Docket(DocumentationType.SWAGGER_2)  
				.globalOperationParameters(global)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();                                           
	    }

}
