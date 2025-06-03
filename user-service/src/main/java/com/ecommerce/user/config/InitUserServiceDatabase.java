package com.ecommerce.user.config;

import com.ecommerce.user.entity.Feature;
import com.ecommerce.user.entity.Profil;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.FeatureRepository;
import com.ecommerce.user.repository.ProfilRepository;
import com.ecommerce.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Configuration
public class InitUserServiceDatabase {

    @Bean
    public CommandLineRunner initDatabase(ProfilRepository profilRepository, FeatureRepository featureRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
           if(featureRepository.count() == 0) {
               List<Feature> features = List.of(
                       new Feature(null, "Ajouter un utilisateur", "USER", "CREATE"),
                       new Feature(null, "Consulter les utilisateurs", "USER", "READ"),
                       new Feature(null, "Modifier un utilisateur", "USER", "UPDATE"),
                       new Feature(null, "Supprimer un utilisateur", "USER", "DELETE"),
                       new Feature(null, "Ajouter un profile", "PROFIL", "CREATE"),
                       new Feature(null, "Consulter les profiles", "PROFIL", "READ"),
                       new Feature(null, "Modifier un profile", "PROFIL", "UPDATE"),
                       new Feature(null, "Supprimer un profile", "PROFIL", "DELETE"),
                       new Feature(null, "Ajouter une feature", "FEATURE", "CREATE"),
                       new Feature(null, "Consulter les features", "FEATURE", "READ"),
                       new Feature(null, "Modifier une feature", "FEATURE", "UPDATE"),
                       new Feature(null, "Supprimer une feature", "FEATURE", "DELETE"),
                       new Feature(null, "Ajouter un produit", "PRODUCT", "CREATE"),
                       new Feature(null, "Consulter les produits", "PRODUCT", "READ"),
                       new Feature(null, "Modifier un produit", "PRODUCT", "UPDATE"),
                       new Feature(null, "Supprimer un produit", "PRODUCT", "DELETE"),
                       new Feature(null, "Ajouter une commande", "ORDER", "PLACE"),
                       new Feature(null, "Consulter les commandes", "ORDER", "READ"),
                       new Feature(null, "Modifier une commande", "ORDER", "UPDATE"),
                       new Feature(null, "Supprimer une commande", "ORDER", "DELETE"),
                       new Feature(null, "Ajouter une categorie", "CATEGORY", "CREATE"),
                       new Feature(null, "Consulter les categories", "CATEGORY", "READ"),
                       new Feature(null, "Modifier une categorie", "CATEGORY", "UPDATE"),
                       new Feature(null, "Supprimer une categorie", "CATEGORY", "DELETE")
               );

               featureRepository.saveAll(features);
           }

           if(profilRepository.count() == 0) {
               profilRepository.saveAll(List.of(
                       new Profil(null, "ROLE_ADMIN", new HashSet<>(featureRepository.findAll())),
                       new Profil(null, "ROLE_SELLER", new HashSet<>(featureRepository.findAllByResourceNameIn(List.of("PRODUCT", "ORDER")))),
                       new Profil(null, "ROLE_USER", new HashSet<>(List.of(
                               featureRepository.findByResourceNameAndAction("USER", "READ").orElseThrow(),
                               featureRepository.findByResourceNameAndAction("PRODUCT", "READ").orElseThrow(),
                               featureRepository.findByResourceNameAndAction("ORDER", "READ").orElseThrow(),
                               featureRepository.findByResourceNameAndAction("ORDER", "PLACE").orElseThrow(),
                               featureRepository.findByResourceNameAndAction("ORDER", "UPDATE").orElseThrow(),
                               featureRepository.findByResourceNameAndAction("ORDER", "DELETE").orElseThrow()
                       )))
               ));
           }

           if(userRepository.count() == 0) {
               userRepository.save(new User(null, "Admin", "Admin", "admin@gmail.com", passwordEncoder.encode("Admin123@@"), true, true, 0, LocalDateTime.now(), true, "AdminStore", new ArrayList<>(profilRepository.findAll())));
               userRepository.save(new User(null, "Seller", "Seller", "seller@gmail.com", passwordEncoder.encode("Seller123@@"), true, true, 0, LocalDateTime.now(), true, "AdminStore", new ArrayList<>(profilRepository.findAll())));
           }
        };
    }
}