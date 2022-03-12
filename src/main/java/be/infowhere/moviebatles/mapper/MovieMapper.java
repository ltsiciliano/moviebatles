package be.infowhere.moviebatles.mapper;

import be.infowhere.moviebatles.domain.Movie;
import be.infowhere.moviebatles.dto.MoviesDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    @Mapping(target = "title", source = "title")
    Movie mapperMovies(MoviesDto moviesDto);

    @InheritInverseConfiguration
    MoviesDto mapperMovies(Movie moviesDto);

}
