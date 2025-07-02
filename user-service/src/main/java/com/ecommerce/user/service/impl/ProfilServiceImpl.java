package com.ecommerce.user.service.impl;

import com.ecommerce.shared.exception.ResourceNotFoundException;
import com.ecommerce.user.dto.ProfilDto;
import com.ecommerce.user.dto.ProfilFeaturesDto;
import com.ecommerce.user.entity.Feature;
import com.ecommerce.user.entity.Profil;
import com.ecommerce.user.exception.ProfileAlreadyExistsException;
import com.ecommerce.user.mapper.ProfilMapper;
import com.ecommerce.user.repository.FeatureRepository;
import com.ecommerce.user.repository.ProfilRepository;
import com.ecommerce.user.service.ProfilService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProfilServiceImpl implements ProfilService {

    private static final String PROFILE = "Profile";

    private final ProfilRepository profilRepository;
    private final FeatureRepository featureRepository;

    public ProfilServiceImpl(ProfilRepository profilRepository, FeatureRepository featureRepository) {
        this.profilRepository = profilRepository;
        this.featureRepository = featureRepository;
    }

    @Override
    public List<ProfilDto> getAllProfils() {
        List<Profil> profils = profilRepository.findAll();
        return ProfilMapper.INSTANCE.profilsToProfilDtos(profils);
    }

    @Override
    public ProfilDto getProfilById(Long id) {
        Profil profil = profilRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(PROFILE, "id", id.toString())
        );
        return ProfilMapper.INSTANCE.profilToProfilDto(profil);
    }

    @Override
    public ProfilDto createProfil(ProfilDto profilDto) {
        if(profilRepository.existsByName(profilDto.getName())) {
            throw new ProfileAlreadyExistsException("Profil with name " + profilDto.getName() + " already exists");
        }
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
    public void deleteProfil(Long id) {
        getProfilById(id);
        profilRepository.deleteById(id);
    }

    @Override
    public ProfilDto addFeaturesToProfil(ProfilFeaturesDto profilFeaturesDto) {
        Profil profil = profilRepository.findById(profilFeaturesDto.getProfilId()).orElseThrow(
                () -> new ResourceNotFoundException(PROFILE, "id", profilFeaturesDto.getProfilId().toString())
        );

        Set<Feature> features = profilFeaturesDto.getFeatureIds().stream()
                .map(featureId -> featureRepository.findById(featureId).orElseThrow(
                        () -> new ResourceNotFoundException("Feature", "id", featureId.toString())
                )).collect(Collectors.toSet());

        Set<Feature> profilFeatures = profil.getFeatures();
        profilFeatures.addAll(features);
        Profil updatedProfil = profilRepository.save(profil);
        return ProfilMapper.INSTANCE.profilToProfilDto(updatedProfil);
    }

    @Override
    public ProfilDto removeFeaturesFromProfil(ProfilFeaturesDto profilFeaturesDto) {
        Profil profil = profilRepository.findById(profilFeaturesDto.getProfilId()).orElseThrow(
                () -> new ResourceNotFoundException(PROFILE, "id", profilFeaturesDto.getProfilId().toString())
        );

        Set<Feature> features = profilFeaturesDto.getFeatureIds().stream()
                .map(featureId -> featureRepository.findById(featureId).orElseThrow(
                        () -> new ResourceNotFoundException("Feature", "id", featureId.toString())
                )).collect(Collectors.toSet());

        Set<Feature> profilFeatures = profil.getFeatures();
        profilFeatures.removeAll(features);
        Profil updatedProfil = profilRepository.save(profil);
        return ProfilMapper.INSTANCE.profilToProfilDto(updatedProfil);
    }
}
