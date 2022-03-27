package be.infowhere.moviebatles.repository;

import be.infowhere.moviebatles.domain.Game;
import be.infowhere.moviebatles.domain.User;
import be.infowhere.moviebatles.enums.StatusGameEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByUserAndStatus(User user, StatusGameEnum statusGame);
    List<Game> findByStatus(StatusGameEnum statusGame);
}
