package org.example.mapper;

import org.example.dto.ProfileDto;
import org.example.model.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProfileMapper {
    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    @Mapping(source = "userId.id", target = "userId")
    ProfileDto toDto(Profile profile);


    @Mapping(source = "userId", target = "userId.id")
    Profile toEntity(ProfileDto dto);


}
