package be.infowhere.moviebatles.service;

import be.infowhere.moviebatles.domain.Game;
import be.infowhere.moviebatles.domain.Movie;
import be.infowhere.moviebatles.domain.MoviePlay;
import be.infowhere.moviebatles.domain.User;
import be.infowhere.moviebatles.enums.StatusGameEnum;
import be.infowhere.moviebatles.exceptions.GameException;
import com.sun.istack.NotNull;

import java.util.List;
import java.util.Optional;

public interface GameService {
    Game startNewGame(User user) throws GameException;
    void finishGame(User user) throws GameException;
    Game nextQuestion(User user) throws GameException;
}
