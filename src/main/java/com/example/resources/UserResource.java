package com.example.resources;


import com.example.dto.DetailResponse;
import com.example.dto.Token;
import com.example.models.User;
import com.example.service.UserService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return userService.create(u);
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
        return userService.login(u);
    }


    @DELETE
    @RolesAllowed("Admin")
    @Transactional
    public Response deleteUser(User u) {
        User deletedUser = userService.delete(u.getId());
        if(deletedUser != null) {
            return Response.ok(deletedUser).build();
        }
        return Response.status(404).entity(new DetailResponse("User not found")).build();
    }

    @RolesAllowed("Admin")
    @GET
    @Path("/{id:\\d+}")
    public Response getId(Long id) {
        User findUser = userService.getById(id);
        if(findUser != null) {
            return Response.ok(findUser).build();
        }
        return Response.status(404).entity(new DetailResponse("User not found")).build();
    }

    @RolesAllowed("User")
    @PUT
    @Transactional
    public Response updateUser(User u) {
        User updateUser = userService.update(u);
        if(updateUser != null) {
            return Response.ok(updateUser).build();
        }
        return Response.status(404).entity(new DetailResponse("User not found")).build();
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