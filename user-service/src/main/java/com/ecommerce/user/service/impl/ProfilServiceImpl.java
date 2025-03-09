package com.ecommerce.user.service.impl;

import com.ecommerce.user.dto.ProfilDto;
import com.ecommerce.user.entity.Profil;
import com.ecommerce.user.exception.ResourceNotFoundException;
import com.ecommerce.user.mapper.ProfilMapper;
import com.ecommerce.user.repository.ProfilRepository;
import com.ecommerce.user.service.ProfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfilServiceImpl implements ProfilService {

    @Autowired
    private ProfilRepository profilRepository;

    @Override
    public List<ProfilDto> getAllProfils() {
        List<Profil> profils = profilRepository.findAll();
        return ProfilMapper.INSTANCE.profilsToProfilDtos(profils);
    }

    @Override
    public ProfilDto getProfilById(Long id) {
        Profil profil = profilRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Profil", "id", id.toString())
        );
        return ProfilMapper.INSTANCE.profilToProfilDto(profil);
    }

    @Override
    public ProfilDto createProfil(ProfilDto profilDto) {
        Profil profil = ProfilMapper.INSTANCE.profilDtoToProfil(profilDto);
        Profil savedProfil = profilRepository.save(profil);
        return ProfilMapper.INSTANCE.profilToProfilDto(savedProfil);
    }

    @Override
    public ProfilDto updateProfil(Long id, ProfilDto profilDto) {
        getProfilById(id);
        Profil profil = ProfilMapper.INSTANCE.profilDtoToProfil(profilDto);
        profil.setId(id);
        Profil savedProfil = profilRepository.save(profil);
        return ProfilMapper.INSTANCE.profilToProfilDto(savedProfil);
    }

    @Override
    public String deleteProfil(Long id) {
        getProfilById(id);
        profilRepository.deleteById(id);
        return "Le profil a été bien supprimé";
    }
}
