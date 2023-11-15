package com.example.service;

import com.example.models.Product;
import com.example.models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

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
            Map<String, String> resp = new HashMap<>();
            resp.put("detail", "product not found");
            return Response.status(404).entity(resp).build();
        }
        return Response.ok(p).build();
    }

    public Response delete(Long id) {
        Product dbProduct = findById(id);
        Map<String, String> resp = new HashMap<>();
        if(dbProduct == null) {
            resp.put("detail", "product not found");
            return Response.status(404).entity(resp).build();
        }
        delete(dbProduct);
        resp.put("detail", "Product is deleted");
        return Response.ok(resp).build();
    }

    public Response update(Product p) {
        Product dbProduct = findById(p.getId());
        Map<String, String> resp = new HashMap<>();

        if(dbProduct == null) {
            resp.put("detail", "Not found");
            return Response.status(404).entity(resp).build();
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

    // crud: update
}