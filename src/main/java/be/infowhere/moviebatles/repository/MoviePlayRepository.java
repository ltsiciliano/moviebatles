package be.infowhere.moviebatles.repository;

import be.infowhere.moviebatles.domain.MoviePlay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoviePlayRepository extends JpaRepository<MoviePlay, Long> {
}
