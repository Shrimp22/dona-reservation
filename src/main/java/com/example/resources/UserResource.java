package com.example.resources;


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
    public User addUser(User u) {
        return userService.create(u);
    }

    @GET
    @RolesAllowed("Admin")
    public List<User> getAllUsers() {
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
        Map<String, String> response = new HashMap<>();
        response.put("detail", "User not found");
        return Response.status(404).entity(response).build();
    }

    @RolesAllowed("Admin")
    @GET
    @Path("/{id:\\d+}")
    public Response getId(Long id) {
        User findUser = userService.getById(id);
        if(findUser != null) {
            return Response.ok(findUser).build();
        }
        Map<String, String> response = new HashMap<>();
        response.put("detail", "User not found");
        return Response.status(404).entity(response).build();
    }

    @RolesAllowed("User")
    @PUT
    @Transactional
    public Response updateUser(User u) {
        User updateUser = userService.update(u);
        if(updateUser != null) {
            return Response.ok(updateUser).build();
        }
        Map<String, String> response = new HashMap<>();
        response.put("detail", "User not found");
        return Response.status(404).entity(response).build();
    }

}