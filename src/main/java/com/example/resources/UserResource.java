package com.example.resources;


import com.example.dto.DetailResponse;
import com.example.dto.UserDto;
import com.example.exeptions.WrongPasswordExeption;
import com.example.models.User;
import com.example.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;

@Path("/api/user/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    @Inject
    UserService userService;

    @Inject
    SecurityContext securityContext;

    @POST
    @PermitAll
    @Transactional
    public Response addUser(User u) {
        try {
            User user = userService.create(u);
            return Response.ok(new UserDto(user)).build();
        } catch (EntityExistsException e) {
            return Response.status(409).entity(new DetailResponse("User already exist")).build();
        }
    }

    @GET
    @RolesAllowed("Admin")
    public Response getAllUsers() {
        return userService.getAll();
    }

    @POST
    @PermitAll
    @Path("/login")
    public Response loginUser(User u) {
        try {
            String token = userService.login(u);
            return Response.ok(new DetailResponse(token)).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(new DetailResponse("User not found")).build();
        } catch (WrongPasswordExeption e) {
            return Response.status(404).entity(new DetailResponse("Wrong password")).build();
        }
    }


    @DELETE
    @RolesAllowed("Admin")
    @Transactional
    public Response deleteUser(User u) {
        try {
            User deletedUser = userService.delete(u.getId());
            return Response.ok(deletedUser).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(new DetailResponse("User not found")).build();
        }
    }

    @RolesAllowed("Admin")
    @GET
    @Path("/{id:\\d+}")
    public Response getId(Long id) {
        try {
            User findUser = userService.getById(id);
            return Response.ok(findUser).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(new DetailResponse("User not found")).build();
        }
    }

    @RolesAllowed("User")
    @PUT
    @Transactional
    public Response updateUser(User u) {
        try {
            User updateUser = userService.update(u);
            return Response.ok(updateUser).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(new DetailResponse("User not found")).build();
        }
    }

    @GET
    @Path("/token")
    public Response validateToken() {
        Principal email = securityContext.getUserPrincipal();
        if(email == null) {
            return Response.status(404).entity(new DetailResponse("Token is not valid")).build();
        }
        return Response.ok(new DetailResponse("Token is valid")).build();
    }

}