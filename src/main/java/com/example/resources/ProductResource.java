package com.example.resources;


import com.example.dto.ProductUserDto;
import com.example.models.Product;
import com.example.models.User;
import com.example.service.ProductService;
import com.example.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.HashMap;
import java.util.Map;

@Path("/api/product/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("Admin")
public class ProductResource {

    @Inject
    ProductService productService;

    @Inject
    UserService userService;

    @Inject
    SecurityContext securityContext;

    @POST
    @Transactional
    public Response create(Product p) {
        Map<String, String> response = new HashMap<>();
        String email = securityContext.getUserPrincipal().getName();
        User activeUser = userService.getUserByEmail(email);
        Product product = productService.create(p, activeUser);
        if(product == null) {
            response.put("detail", "Product already exist");
            return Response.status(400).entity(response).build();
        }

        return Response.ok(new ProductUserDto(product)).build();
    }


    @GET
    @Path("{id:\\d+}")
    public Response getById(Long id) {
        return productService.getById(id);
    }

    @DELETE
    @Transactional
    public Response delete(Product p) {
        return productService.delete(p.getId());
    }

    @PUT
    @Transactional
    public Response update(Product p) {
        return productService.update(p);
    }
}