package com.ecommerce.product.config;

import com.ecommerce.product.entity.Tva;
import com.ecommerce.product.repository.TvaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class InitProductDatabase {

    @Bean
    public CommandLineRunner run(TvaRepository tvaRepository) {
        return args -> {
            if(tvaRepository.count() == 0) {
                tvaRepository.saveAll(List.of(
                        new Tva(null, "TVA réduite 5.5%", 5.5),
                        new Tva(null, "TVA intermédiaire 10%", 10.0),
                        new Tva(null, "TVA normale 20%", 20.0),
                        new Tva(null, "TVA spéciale 2.1%", 2.1)
                ));
            }
        };
    }
}
