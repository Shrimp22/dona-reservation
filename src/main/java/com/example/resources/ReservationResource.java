package com.example.resources;

import com.example.dto.DetailResponse;
import com.example.dto.UserReservations;
import com.example.exeptions.ProductNotFoundExeption;
import com.example.models.Reservation;
import com.example.service.ReservationService;
import io.quarkus.qute.Results;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Set;

@Path("/api/reservation/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservationResource {
    @Inject
    ReservationService reservationService;

    // TODO: Fix roles
    @RolesAllowed("User")
    @POST
    @Transactional
    public Response create(Reservation r) {
        try {
            UserReservations reservation = reservationService.create(r);
            return Response.ok(reservation).build();
        }catch (NotFoundException e) {
            return Response.status(404).entity(new DetailResponse("Product not found")).build();
        }
    }

    @RolesAllowed("User")
    @DELETE
    @Transactional
    public Response delete(Reservation r) {
        try {
            Reservation reservation = reservationService.delete(r.getId());
            return Response.ok(reservation).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(new DetailResponse("Reservation not found")).build();

        }

    }

    @RolesAllowed("User")
    @GET
    @Path("/{id:\\d+}")
    public Response getById(Long id) {
        try {
            Reservation reservation = reservationService.getById(id);
            return Response.ok(reservation).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(new DetailResponse("Reservation not found")).build();
        }
    }


    @RolesAllowed("Admin")
    @GET
    @Path("/user/{id:\\d+}")
    public Response getFromUser(Long id) {
        try {
            Set<UserReservations> userReservations = reservationService.getReservationsFromUser(id);
            return Response.ok(userReservations).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity("User not found").build();
        }
}
    @RolesAllowed("User")
    @PUT
    @Transactional
    public Response update(Reservation r) {
        try {
            Reservation reservation = reservationService.update(r);
            return Response.ok(reservation).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(new DetailResponse("Reservation not found")).build();
        } catch (ProductNotFoundExeption e) {
            return Response.status(404).entity(new DetailResponse("Product not found")).build();
        }
    }
}