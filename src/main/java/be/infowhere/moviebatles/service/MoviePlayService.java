package be.infowhere.moviebatles.service;

import be.infowhere.moviebatles.domain.Game;
import be.infowhere.moviebatles.domain.MoviePlay;
import be.infowhere.moviebatles.domain.User;
import be.infowhere.moviebatles.exceptions.GameException;

public interface MoviePlayService {
    MoviePlay nextQuestion(Game gameOngoing) throws GameException;
    MoviePlay answerQuestion(MoviePlay moviePlay) throws GameException;
}
