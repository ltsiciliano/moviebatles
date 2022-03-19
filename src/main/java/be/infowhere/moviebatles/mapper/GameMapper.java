package be.infowhere.moviebatles.mapper;

import be.infowhere.moviebatles.domain.Game;
import be.infowhere.moviebatles.dto.GameDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GameMapper {

    @Mapping(target = "user", source = "user")
    Game mapperGame(GameDto gameDto);

    @InheritInverseConfiguration
    GameDto mapperGame(Game game);

}
