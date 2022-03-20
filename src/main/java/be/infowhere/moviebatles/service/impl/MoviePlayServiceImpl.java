package be.infowhere.moviebatles.service.impl;

import be.infowhere.moviebatles.domain.Game;
import be.infowhere.moviebatles.domain.Movie;
import be.infowhere.moviebatles.domain.MoviePlay;
import be.infowhere.moviebatles.domain.User;
import be.infowhere.moviebatles.enums.StatusGameEnum;
import be.infowhere.moviebatles.exceptions.GameException;
import be.infowhere.moviebatles.repository.MoviePlayRepository;
import be.infowhere.moviebatles.repository.MovieRepository;
import be.infowhere.moviebatles.service.GameService;
import be.infowhere.moviebatles.service.MoviePlayService;
import be.infowhere.moviebatles.utils.MessagesUtils;
import be.infowhere.moviebatles.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MoviePlayServiceImpl implements MoviePlayService {

    private final List<String> omdbapiMovies;
    private final MovieRepository movieRepository;
    private final MoviePlayRepository moviePlayRepository;
    private final NumberUtils numberUtils;

    @Autowired
    public MoviePlayServiceImpl(@Value(value = "${omdbapi.movies}") List<String> omdbapiMovies,
                                MovieRepository movieRepository,
                                MoviePlayRepository moviePlayRepository,
                                NumberUtils numberUtils) {
        this.omdbapiMovies = omdbapiMovies;
        this.movieRepository = movieRepository;
        this.moviePlayRepository = moviePlayRepository;
        this.numberUtils = numberUtils;
    }

    @Override
    public MoviePlay answerQuestion(MoviePlay moviePlay) throws GameException {
        validAnswer(moviePlay);
        return moviePlayRepository.save(moviePlay);
    }

    private void validAnswer(MoviePlay moviePlay) throws GameException {
        if(Objects.isNull(moviePlay) ||
                Objects.isNull(moviePlay.getFirstMovie()) ||
                Objects.isNull(moviePlay.getSecondMovie()) ||
                Objects.isNull(moviePlay.getAnswer()) ||
                Objects.isNull(moviePlay.getGame()) ||
                Objects.isNull(moviePlay.getId())
        ){
            throw new GameException(MessagesUtils.errorAnswerCouldNotBeProcess);
        }
    }

    @Override
    public MoviePlay nextQuestion(Game gameOngoing) throws GameException {

        if(Objects.isNull(gameOngoing)){
            throw new GameException(MessagesUtils.errorNoGameOngoing);
        }

        checkIfExistsQuestionToAnswer(gameOngoing);

        List<Movie> movies = buildQuestion(gameOngoing);

        MoviePlay newMoviePlay = new MoviePlay();
        newMoviePlay.setAnswer(null);
        newMoviePlay.setFirstMovie(movies.get(0));
        newMoviePlay.setSecondMovie(movies.get(1));
        newMoviePlay.setGame(gameOngoing);

        return moviePlayRepository.save(newMoviePlay);
    }

    private void checkIfExistsQuestionToAnswer(Game gameOngoing) throws GameException {
        if(gameOngoing.getGamePlay().stream().filter(
                moviePlay -> moviePlay.getAnswer() == null
        ).findFirst().isPresent()){
            throw new GameException(MessagesUtils.errorThereIsOpenQuestionToAnswer);
        };
    }

    private List<Movie> buildQuestion(Game gameOngoing) throws GameException {

        List<Movie> movies = new ArrayList<>();

        boolean inserted = false;

        int qtdMaxCobination = numberUtils.getQtdCombinations(omdbapiMovies);

        if(qtdMaxCobination==gameOngoing.getGamePlay().size()){
            throw new GameException(MessagesUtils.errorMaxNumberCombinationQuestionReached);
        }

        while(inserted==false){

            int randomNumber1 = numberUtils.getRandomNumber(omdbapiMovies.size());
            int randomNumber2 = numberUtils.getRandomNumber(omdbapiMovies.size());

            if(randomNumber1 != randomNumber2) {
                Optional<Movie> movie1 = movieRepository.findByImdbID(omdbapiMovies.get(randomNumber1));
                Optional<Movie> movie2 = movieRepository.findByImdbID(omdbapiMovies.get(randomNumber2));

                if(movie1.isPresent() && movie2.isPresent()
                        && !isMoviesFromMoviePlayAlreadyExists(gameOngoing,movie1.get(),movie2.get())
                ) {
                    movies.add(movie1.get());
                    movies.add(movie2.get());
                    inserted=true;
                }

            }

        }

        return movies;

    }

    private boolean isMoviesFromMoviePlayAlreadyExists(Game gameOngoing, Movie movie1, Movie movie2) {

        if(Objects.isNull(gameOngoing.getGamePlay()) || gameOngoing.getGamePlay().size()==0){
            return false;
        }

        int movies1HashCode = (movie1.getImdbID() + movie2.getImdbID()).hashCode();
        int movies2HashCode = (movie2.getImdbID() + movie1.getImdbID()).hashCode();

        return gameOngoing.getGamePlay().stream().filter(
                moviePlay ->
                        ((moviePlay.getFirstMovie().getImdbID() + moviePlay.getSecondMovie().getImdbID()).hashCode() == movies1HashCode ||
                        (moviePlay.getSecondMovie().getImdbID() + moviePlay.getFirstMovie().getImdbID()).hashCode() == movies2HashCode)

        ).findFirst().isPresent();
    }


}
