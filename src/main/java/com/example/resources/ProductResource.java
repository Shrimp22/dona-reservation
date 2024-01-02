package com.example.resources;

import com.example.dto.DetailResponse;
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
        String email = securityContext.getUserPrincipal().getName();
        User activeUser = userService.getUserByEmail(email);
        try {
            Product product = productService.create(p, activeUser);
            return Response.ok(new ProductUserDto(product)).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(new DetailResponse("Product not found")).build();
        }
    }


    @GET
    @Path("{id:\\d+}")
    public Response getById(Long id) {
        try {
            Product p = productService.getById(id);
            return Response.ok(p).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(new DetailResponse("Product not foudn")).build();
        }
    }

    @DELETE
    @Transactional
    public Response delete(Product p) {
        try {
            Product product = productService.delete(p.getId());
            return Response.ok(product).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(new DetailResponse("Product not foudn")).build();
        }
    }

    @PUT
    @Transactional
    public Response update(Product p) {
        try {
            Product product = productService.update(p);
            return Response.ok(product).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(new DetailResponse("Product not foudn")).build();
        }
    }
}