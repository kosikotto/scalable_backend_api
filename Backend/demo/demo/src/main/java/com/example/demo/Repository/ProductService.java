package com.example.demo.Repository;

import com.example.demo.Entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {

    private final ProductRepo repo;
    public ProductService(ProductRepo repo) {
        this.repo = repo;
    }

    @Cacheable(value = "allProducts")
    public List<Product> readAllData() {
        log.warn("-> CACHE MISS: querying DB: readAllData()");
        return repo.findAll();
    }

    @Cacheable(value = "products", key = "#id")
    public Optional<Product> readById(int id) {
        log.warn("-> CACE MISS: querying DB: readById({})", id);
        return repo.findById(id);
    }

    @CacheEvict(value = {"products", "allProducts"}, key = "#id", allEntries = true)
    public void deleteById(int id) {
        repo.deleteById(id);
    }

    @CacheEvict(value = "allProducts", allEntries = true)
    public void insertIntoDb(String productName) {
        var tmp = repo.findAll().stream().filter(x -> x.getProduct_name().toLowerCase().equals(productName)).findAny();
        if (tmp.isPresent() == false) {
            Product product = new Product();
            product.setProduct_name(String.valueOf(productName.charAt(0)).toUpperCase() + productName.substring(1));
            repo.save(product);
        }
    }
}
