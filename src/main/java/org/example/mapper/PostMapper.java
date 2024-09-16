package org.example.mapper;

import org.example.dto.PostDto;
import org.example.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);


    @Mapping(source = "profileId.id", target = "profileId")
    PostDto toDto(Post post);

    @Mapping(source = "profileId", target = "profileId.id")
    Post toEntity(PostDto postDto);


}
