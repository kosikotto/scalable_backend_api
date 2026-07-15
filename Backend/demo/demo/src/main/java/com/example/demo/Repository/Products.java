/*

package com.example.demo.Repository;

import com.example.demo.Entity.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

// --- OUTDATED, OLD SOLUTION ----
@Component
public class Products {
    private ArrayList<Product> products;

    public Products() {
        this.products = new ArrayList<>();

        products.add(new Product(0, "Tusfürdő"));
        products.add(new Product(1, "Szappan"));
        products.add(new Product(2, "Kutyakaja"));
    }

    public ArrayList<Product> findAll() {
        return this.products;
    }

    public Product findById(int id) {
        Product product = new Product();

        for (int i = 0; i<products.size();i++) {
            if(products.get(i).getId() == id) {
                product.setId(products.get(i).getId());
                product.setProduct_name(products.get(i).getProduct_name());
                break;
            }
        }

        return product;
    }

    public Boolean addProduct(Product product) {
        var tmp = findById(product.getId());

        if(tmp.getProduct_name() == null) {
            this.products.add(product);
            return true;
        }
        else {
            return false;
        }
    }
}

 */