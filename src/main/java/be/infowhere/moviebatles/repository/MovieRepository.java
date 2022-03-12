package be.infowhere.moviebatles.repository;

import be.infowhere.moviebatles.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository  extends JpaRepository<Movie, Long> {
}
