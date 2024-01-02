package com.example.resources;

import com.example.dto.DetailResponse;
import com.example.exeptions.ReservationExpiredExeption;
import com.example.models.UserToken;
import com.example.models.request.ChangePasswordRequest;
import com.example.models.request.UserTokenRequest;
import com.example.service.UserTokenService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
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
        UserToken userToken = userTokenService.create(token.getEmail());
        return Response.ok(userToken).build();
    }


    @PATCH
    @Transactional
    @Path("/{token}")
    public Response changePassword(String token, ChangePasswordRequest request) {
        try {
            UserToken userToken = userTokenService.changePassword(token, request.getPassword());
            return Response.ok(userToken).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(new DetailResponse("Token not found")).build();
        } catch (ReservationExpiredExeption e) {
            return Response.status(403).entity(new DetailResponse("Token Expired exeption")).build();
        }
    }
}
