package org.example.mapper;

import org.example.dto.GameDto;
import org.example.model.Game;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GameMapper {
    GameMapper INSTANCE = Mappers.getMapper(GameMapper.class);

    GameDto toDto(Game game);

    Game toEntity(GameDto dto);
}
