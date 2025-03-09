package com.ecommerce.user.mapper;

import com.ecommerce.user.dto.UserDto;
import com.ecommerce.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", ignore = true)
    User userDtoToUser(UserDto userDto);

    UserDto userToUserDto(User user);

    List<UserDto> userToUserDtos(List<User> users);

}
