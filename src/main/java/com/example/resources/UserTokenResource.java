package com.example.resources;

import com.example.models.request.ChangePasswordRequest;
import com.example.models.request.UserTokenRequest;
import com.example.service.UserTokenService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/api/token/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserTokenResource {
    @Inject
    UserTokenService userTokenService;

    @POST
    @Transactional
    public Response create(UserTokenRequest token) {
        return userTokenService.create(token.getEmail());
    }


    @PATCH
    @Transactional
    @Path("/{token}")
    public Response changePassword(String token, ChangePasswordRequest request) {
        return userTokenService.changePassword(token, request.getPassword());
    }
}
