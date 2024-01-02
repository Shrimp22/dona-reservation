package com.example.service;

import com.example.dto.DetailResponse;
import com.example.models.Product;
import com.example.models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import java.lang.reflect.Field;

@ApplicationScoped
public class ProductService implements PanacheRepository<Product> {

    public Product create(Product p, User u) {
        Product dbProduct = find("name", p.getName()).firstResult();
        if(dbProduct != null) {
            throw new NotFoundException();
        }
        p.setUser(u);
        persist(p);
        return p;
    }

    public Product getById(Long id) {
        Product p = findById(id);
        if(p == null) {
            throw new NotFoundException();
        }
        return p;
    }

    public Product delete(Long id) {
        Product dbProduct = findById(id);
        if(dbProduct == null) {
            throw new NotFoundException();
        }
        delete(dbProduct);
        return dbProduct;
    }

    public Product update(Product p) {
        Product dbProduct = findById(p.getId());

        if(dbProduct == null) {
            throw new NotFoundException();
        }

        Field[] fields = Product.class.getDeclaredFields();

        for(Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(p);
                if(value != null) {
                    field.set(dbProduct, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        persistAndFlush(dbProduct);
        return dbProduct;
    }

}