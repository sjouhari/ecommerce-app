package com.ecommerce.user.config;

import com.ecommerce.user.entity.Feature;
import com.ecommerce.user.entity.Profil;
import com.ecommerce.user.entity.Store;
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

    private static final String USER_RESOURCE = "USER";
    private static final String PROFIL_RESOURCE = "PROFIL";
    private static final String FEATURE_RESOURCE = "FEATURE";
    private static final String CATEGORY_RESOURCE = "CATEGORY";
    private static final String PRODUCT_RESOURCE = "PRODUCT";
    private static final String ORDER_RESOURCE = "ORDER";

    private static final String READ_ACTION = "READ";
    private static final String CREATE_ACTION = "CREATE";
    private static final String UPDATE_ACTION = "UPDATE";
    private static final String DELETE_ACTION = "DELETE";

    public static final String ADMIN_PASSWORD = "Admin123@@";

    @Bean
    public CommandLineRunner initDatabase(ProfilRepository profilRepository, FeatureRepository featureRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
           if(featureRepository.count() == 0) {
               List<Feature> features = List.of(
                       new Feature(null, "Ajouter un utilisateur", USER_RESOURCE, CREATE_ACTION),
                       new Feature(null, "Consulter les utilisateurs", USER_RESOURCE, READ_ACTION),
                       new Feature(null, "Modifier un utilisateur", USER_RESOURCE, UPDATE_ACTION),
                       new Feature(null, "Supprimer un utilisateur", USER_RESOURCE, DELETE_ACTION),

                       new Feature(null, "Ajouter un profile", PROFIL_RESOURCE, CREATE_ACTION),
                       new Feature(null, "Consulter les profiles", PROFIL_RESOURCE, READ_ACTION),
                       new Feature(null, "Modifier un profile", PROFIL_RESOURCE, UPDATE_ACTION),
                       new Feature(null, "Supprimer un profile", PROFIL_RESOURCE, DELETE_ACTION),

                       new Feature(null, "Ajouter une feature", FEATURE_RESOURCE, CREATE_ACTION),
                       new Feature(null, "Consulter les features", FEATURE_RESOURCE, READ_ACTION),
                       new Feature(null, "Modifier une feature", FEATURE_RESOURCE, UPDATE_ACTION),
                       new Feature(null, "Supprimer une feature", FEATURE_RESOURCE, DELETE_ACTION),

                       new Feature(null, "Ajouter un produit", PRODUCT_RESOURCE, CREATE_ACTION),
                       new Feature(null, "Modifier un produit", PRODUCT_RESOURCE, UPDATE_ACTION),
                       new Feature(null, "Supprimer un produit", PRODUCT_RESOURCE, DELETE_ACTION),

                       new Feature(null, "Ajouter une commande", ORDER_RESOURCE, CREATE_ACTION),
                       new Feature(null, "Consulter les commandes", ORDER_RESOURCE, READ_ACTION),
                       new Feature(null, "Modifier une commande", ORDER_RESOURCE, UPDATE_ACTION),
                       new Feature(null, "Supprimer une commande", ORDER_RESOURCE, DELETE_ACTION),

                       new Feature(null, "Ajouter une categorie", CATEGORY_RESOURCE, CREATE_ACTION),
                       new Feature(null, "Modifier une categorie", CATEGORY_RESOURCE, UPDATE_ACTION),
                       new Feature(null, "Supprimer une categorie", CATEGORY_RESOURCE, DELETE_ACTION)
               );

               featureRepository.saveAll(features);
           }

           if(profilRepository.count() == 0) {
               profilRepository.saveAll(List.of(
                       new Profil(null, "ROLE_ADMIN", new HashSet<>(featureRepository.findAll())),
                       new Profil(null, "ROLE_SELLER", new HashSet<>(featureRepository.findAllByResourceNameIn(List.of(PRODUCT_RESOURCE, ORDER_RESOURCE, CATEGORY_RESOURCE)))),
                       new Profil(null, "ROLE_USER", new HashSet<>(List.of(
                               featureRepository.findByResourceNameAndAction(USER_RESOURCE, READ_ACTION).orElseThrow(),
                               featureRepository.findByResourceNameAndAction(ORDER_RESOURCE, READ_ACTION).orElseThrow(),
                               featureRepository.findByResourceNameAndAction(ORDER_RESOURCE, CREATE_ACTION).orElseThrow(),
                               featureRepository.findByResourceNameAndAction(ORDER_RESOURCE, UPDATE_ACTION).orElseThrow(),
                               featureRepository.findByResourceNameAndAction(ORDER_RESOURCE, DELETE_ACTION).orElseThrow()
                       )))
               ));
           }

           if(userRepository.count() == 0) {
               User admin = new User(null, "Admin", "Admin", "admin@gmail.com", passwordEncoder.encode(ADMIN_PASSWORD), true, true, 0, LocalDateTime.now(), null, new ArrayList<>(profilRepository.findAll()), LocalDateTime.now(), null);
               Store store =    new Store(null, admin, "AdminStore", "Casablanca", "0608100760", "admin@gmail.com", true, false);
               admin.setStore(store);
               userRepository.save(admin);
           }
        };
    }
}