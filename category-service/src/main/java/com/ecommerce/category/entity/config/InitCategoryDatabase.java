package com.ecommerce.category.entity.config;

import com.ecommerce.category.entity.Category;
import com.ecommerce.category.entity.Size;
import com.ecommerce.category.entity.SubCategory;
import com.ecommerce.category.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class InitCategoryDatabase {

    @Bean
    public CommandLineRunner commandLineRunner(CategoryRepository categoryRepository) {
        return args -> {
            if(categoryRepository.count() == 0) {
                Category phonesCategory = new Category();
                phonesCategory.setName("Téléphones");
                phonesCategory.setDescription("Catégorie des Téléphones");

                // Phones category subcategories
                SubCategory samsungPhone = new SubCategory();
                samsungPhone.setName("Samsung");
                samsungPhone.setDescription("Catégorie des Telephones Samsung");
                samsungPhone.setCategory(phonesCategory);

                SubCategory redmiPhone = new SubCategory();
                redmiPhone.setName("Redmi");
                redmiPhone.setDescription("Catégorie des Telephones Redmi");
                redmiPhone.setCategory(phonesCategory);

                SubCategory huaweiPhone = new SubCategory();
                huaweiPhone.setName("Huawei");
                huaweiPhone.setDescription("Catégorie des Telephones Huawei");
                huaweiPhone.setCategory(phonesCategory);

                // Phones category sizes
                Size size1 = new Size();
                size1.setLibelle("128-4");
                size1.setCategory(phonesCategory);

                Size size2 = new Size();
                size2.setLibelle("64-4");
                size2.setCategory(phonesCategory);

                Size size3 = new Size();
                size3.setLibelle("128-6");
                size3.setCategory(phonesCategory);

                Size size4 = new Size();
                size4.setLibelle("64-6");
                size4.setCategory(phonesCategory);

                Size size5 = new Size();
                size5.setLibelle("128-8");
                size5.setCategory(phonesCategory);

                Size size6 = new Size();
                size6.setLibelle("64-8");
                size6.setCategory(phonesCategory);

                phonesCategory.setSubCategories(List.of(samsungPhone, redmiPhone, huaweiPhone));
                phonesCategory.setSizes(List.of(size1, size2, size3, size4, size5, size6));

                categoryRepository.save(phonesCategory);
            }
        };
    }

}
