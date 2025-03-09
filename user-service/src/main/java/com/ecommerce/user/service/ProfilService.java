package com.ecommerce.user.service;

import com.ecommerce.user.dto.ProfilDto;
import com.ecommerce.user.dto.ProfilFeaturesDto;

import java.util.List;

public interface ProfilService {

    List<ProfilDto> getAllProfils();

    ProfilDto getProfilById(Long id);

    ProfilDto createProfil(ProfilDto profilDto);

    ProfilDto updateProfil(Long id, ProfilDto profilDto);

    String deleteProfil(Long id);

    ProfilDto addFeaturesToProfil(ProfilFeaturesDto profilFeaturesDto);

    ProfilDto removeFeaturesFromProfil(ProfilFeaturesDto profilFeaturesDto);

}
