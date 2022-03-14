package be.infowhere.moviebatles.repository;

import be.infowhere.moviebatles.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository  extends JpaRepository<Movie, Long> {
    Optional<Movie> findByImdbID(String imdbId);
}
