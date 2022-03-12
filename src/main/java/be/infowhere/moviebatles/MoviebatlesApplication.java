package be.infowhere.moviebatles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:/properties/omdbapi.properties")
public class MoviebatlesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoviebatlesApplication.class, args);
    }

}
