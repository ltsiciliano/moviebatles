package be.infowhere.moviebatles.repository;

import be.infowhere.moviebatles.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
