package com.ecommerce.user.mapper;

import com.ecommerce.user.dto.*;
import com.ecommerce.user.entity.Profil;
import com.ecommerce.user.entity.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper extends StoreMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", ignore = true)
    User userDtoToUser(UserDto userDto);

    UserDto userToUserDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserMapper(UpdateUserDto dto, @MappingTarget User user);

    List<UserDto> userToUserDtos(List<User> users);

    @Mapping(target = "profils", ignore = true)
    User registerDtoToUser(RegisterDto registerDto);

    CurrentUserDto userToCurrentUserDto(User user);

    @Mapping(target = "features", ignore = true)
    ProfilDto profilToProfilDto(Profil profil);

}
