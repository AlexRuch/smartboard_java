package itmo.apkvt.ruchyov.smartboard.config;

import itmo.apkvt.ruchyov.smartboard.service.impl.ProjectServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.Properties;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {


    private Properties imageProperties = new Properties();

    {
        try {
            imageProperties.load(ProjectServiceImpl.class.getResourceAsStream("/image.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler(imageProperties.getProperty("image.db_path") + "**")
                .addResourceLocations("file:" + imageProperties.getProperty("image.fs_path"));
    }
}
