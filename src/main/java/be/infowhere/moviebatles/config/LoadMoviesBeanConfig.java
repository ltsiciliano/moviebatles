package be.infowhere.moviebatles.config;

import be.infowhere.moviebatles.dto.MovieDto;
import be.infowhere.moviebatles.mapper.MovieMapper;
import be.infowhere.moviebatles.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Configuration
@Profile("prd")
public class LoadMoviesBeanConfig {
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

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
            linksOmdbapi.stream().forEach(
                    link-> {
                        try {
                            movieRepository.save(
                                    movieMapper.mapperMovies(
                                            objectMapper.readValue(new URL(omdbapiLink + link),
                                                    MovieDto.class
                                            )
                            )
                            );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );
        System.out.println("Movies loaded");
    }

}
