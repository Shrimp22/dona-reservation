package com.example.service;

import com.example.dto.DetailResponse;
import com.example.models.Product;
import com.example.models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

import java.lang.reflect.Field;

@ApplicationScoped
public class ProductService implements PanacheRepository<Product> {

    public Product create(Product p, User u) {
        Product dbProduct = find("name", p.getName()).firstResult();
        // product exist
        if(dbProduct != null) {
            return null;
        }
        p.setUser(u);
        persist(p);
        return p;
    }

    public Response getById(Long id) {
        Product p = findById(id);
        if(p == null) {
            return Response.status(404).entity(new DetailResponse("product not found")).build();
        }
        return Response.ok(p).build();
    }

    public Response delete(Long id) {
        Product dbProduct = findById(id);
        if(dbProduct == null) {
            return Response.status(404).entity(new DetailResponse("Product not found")).build();
        }
        delete(dbProduct);
        return Response.ok(new DetailResponse("Product is deleted")).build();
    }

    public Response update(Product p) {
        Product dbProduct = findById(p.getId());

        if(dbProduct == null) {
            return Response.status(404).entity(new DetailResponse("Product not found")).build();
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
        return Response.ok(dbProduct).build();
    }

}