package com.ecommerce.user.mapper;

import com.ecommerce.user.dto.ProfilDto;
import com.ecommerce.user.entity.Profil;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProfilMapper {

    ProfilMapper INSTANCE = Mappers.getMapper(ProfilMapper.class);

    Profil profilDtoToProfil(ProfilDto profilDto);

    ProfilDto profilToProfilDto(Profil profil);

    List<ProfilDto> profilsToProfilDtos(List<Profil> profils);

}
