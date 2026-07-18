package com.example.demo.config;

import com.example.demo.Repository.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Feltételezve, hogy van egy WebshopProduct entitásod és egy ProductRepository-d
@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(ProductService repository) {
        return args -> {
            // Csak akkor szúrunk be adatot, ha a tábla még üres
            if (repository.readAllData().size() == 0) {
                System.out.println("Adatbázis üres, kezdőadatok betöltése...");

                // Entitás létrehozása és mentése
                repository.insertIntoDb("sampon");

                repository.insertIntoDb("tusfürdő");

                System.out.println("Kezdőadatok sikeresen betöltve!");
            }
        };
    }
}
