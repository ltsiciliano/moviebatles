package be.infowhere.moviebatles.mapper;

import be.infowhere.moviebatles.domain.Game;
import be.infowhere.moviebatles.dto.GameDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GameMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "gamePlay", source = "gamePlay")
    @Mapping(target = "status", source = "status")
//    @Mapping(target="gamePlay.game", ignore = true)
    Game mapperDtoToGame(GameDto gameDto);

    @InheritInverseConfiguration
    GameDto mapperGameToDto(Game game);

}
