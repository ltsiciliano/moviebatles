package be.infowhere.moviebatles.mapper;

import be.infowhere.moviebatles.domain.MoviePlay;
import be.infowhere.moviebatles.dto.MoviePlayDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MoviePlayMapper {

    @Mapping(target = "answer", source = "answer")
    MoviePlay mapperMoviePlay(MoviePlayDto moviePlayDto);

    @InheritInverseConfiguration
    MoviePlayDto mapperMoviePlay(MoviePlay moviesPlay);

}
