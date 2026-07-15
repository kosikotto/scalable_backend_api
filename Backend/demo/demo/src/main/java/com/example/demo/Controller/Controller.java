package com.example.demo.Controller;

import com.example.demo.Entity.Product;
import com.example.demo.Repository.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("")
public class Controller {
    //private Products products;
    private final ProductService service;

    public Controller(ProductService service) {
        this.service = service;
        //this.products = new Products();
    }
    @GetMapping("/")
    public ResponseEntity<List<Product>> GetTermekek() {
        return new ResponseEntity<>(service.readAllData(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Product>> GetTermek(@Valid @NumberFormat @NotBlank @NotEmpty @NotNull @PathVariable String id) {
        try {
            return new ResponseEntity<>(service.readById(Integer.valueOf(id)), HttpStatus.OK);
        }

        catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add/{product}")
    public ResponseEntity<?> PostTermek(@Valid @NotBlank @NotEmpty @NotNull @PathVariable String product) {
        service.insertIntoDb(product.toLowerCase());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}