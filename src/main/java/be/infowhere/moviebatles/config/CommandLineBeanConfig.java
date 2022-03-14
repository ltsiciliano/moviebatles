package be.infowhere.moviebatles.config;

import be.infowhere.moviebatles.domain.Movie;
import be.infowhere.moviebatles.domain.User;
import be.infowhere.moviebatles.dto.MoviesDto;
import be.infowhere.moviebatles.dto.ResultSearchMoviesDto;
import be.infowhere.moviebatles.mapper.MovieMapper;
import be.infowhere.moviebatles.repository.MovieRepository;
import be.infowhere.moviebatles.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CommandLineBeanConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @Value(value = "${omdbapi.link}")
    private String omdbapiLink;

    @Value(value = "${omdbapi.movies}")
    private List<String> linksOmdbapi;

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private MovieRepository movieRepository;

    @Bean
    public CommandLineRunner insertUsers(UserRepository userRepository) {
        return args -> {
            userRepository.save(new User("sassa","mutema","1234"));
            userRepository.save(new User("roque","santeiro","5678"));
            userRepository.save(new User("carminha","malvada","9999"));
        };
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void runAfterStartup() {
//            linksOmdbapi.stream().forEach(
//                    link-> {
//                        try {
//                            movieRepository.save(
//                                    movieMapper.mapperMovies(
//                                            objectMapper.readValue(new URL(omdbapiLink + link),
//                                                    MoviesDto.class
//                                            )
//                            )
//                            );
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//            );
//        System.out.println("Movies loaded");
//    }

}
