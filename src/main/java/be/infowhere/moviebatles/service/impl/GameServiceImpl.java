package be.infowhere.moviebatles.service.impl;

import be.infowhere.moviebatles.domain.Game;
import be.infowhere.moviebatles.domain.Movie;
import be.infowhere.moviebatles.domain.MoviePlay;
import be.infowhere.moviebatles.domain.User;
import be.infowhere.moviebatles.enums.StatusGameEnum;
import be.infowhere.moviebatles.exceptions.GameException;
import be.infowhere.moviebatles.repository.GameRepository;
import be.infowhere.moviebatles.repository.MoviePlayRepository;
import be.infowhere.moviebatles.repository.MovieRepository;
import be.infowhere.moviebatles.service.GameService;
import be.infowhere.moviebatles.utils.MessagesUtils;
import be.infowhere.moviebatles.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final List<String> omdbapiMovies;
    private final MovieRepository movieRepository;
    private final MoviePlayRepository moviePlayRepository;
    private final NumberUtils numberUtils;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository,
                           @Value(value = "${omdbapi.movies}") List<String> omdbapiMovies,
                           MovieRepository movieRepository,
                           MoviePlayRepository moviePlayRepository,
                           NumberUtils numberUtils
    ) {
        this.gameRepository = gameRepository;
        this.omdbapiMovies = omdbapiMovies;
        this.movieRepository = movieRepository;
        this.moviePlayRepository = moviePlayRepository;
        this.numberUtils = numberUtils;
    }

    @Transactional
    @Override
    public Game startNewGame(User user) throws GameException {
        if(gameRepository.findByUserAndStatus(user,StatusGameEnum.ONGOING).isPresent()){
            throw new GameException(String.format(MessagesUtils.errorGameOngoing,null));
        }

        Game gameOngoing = new Game(
                user,
                new HashSet<>(),
                StatusGameEnum.ONGOING
        );

        List<Movie> movies = buildQuestion(gameOngoing);

        gameOngoing.setGamePlay(
                Set.of(new MoviePlay(
                        movies.get(0),
                        movies.get(1)
                ))
        );
        return gameRepository.save(gameOngoing);
    }

    @Transactional
    @Override
    public void finishGame(User user) throws GameException {
        Optional<Game> gameOngoing = getGame(user);
        gameOngoing.get().setStatus(StatusGameEnum.CLOSED);
        gameRepository.saveAndFlush(gameOngoing.get());
    }

    private Optional<Game> getGame(User user) throws GameException {
        Optional<Game> gameOngoing = gameRepository.findByUserAndStatus(user, StatusGameEnum.ONGOING);

        if (!gameOngoing.isPresent()) {
            throw new GameException(String.format(MessagesUtils.errorNoGameOngoing, null));
        }
        return gameOngoing;
    }

    @Override
    public MoviePlay nextQuestion(User user) throws GameException {
        Optional<Game> gameOngoing = getGame(user);
        checkIfExistsQuestionToAnswer(gameOngoing);

        List<Movie> movies = buildQuestion(gameOngoing.get());

        MoviePlay newMoviePlay = new MoviePlay();
        newMoviePlay.setAnswer(null);
        newMoviePlay.setFirstMovie(movies.get(0));
        newMoviePlay.setSecondMovie(movies.get(1));
        newMoviePlay.setGame(gameOngoing.get());

        return moviePlayRepository.save(newMoviePlay);
    }

    private void checkIfExistsQuestionToAnswer(Optional<Game> gameOngoing) throws GameException {
        if(gameOngoing.get().getGamePlay().stream().filter(
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
