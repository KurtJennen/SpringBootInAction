package be.ss.readinglist

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ComponentScan
@SpringBootApplication
class Application implements WebMvcConfigurer {
	
	static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/readingList").setViewName("readingList");
	}
	
	void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new ReaderHandlerMethodArgumentResolver());
	}
}
