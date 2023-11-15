package com.example.resources;


import com.example.models.Reservation;
import com.example.service.ReservationService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/reservation/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservationResource {

    @Inject
    ReservationService reservationService;

    @RolesAllowed("User")
    @POST
    @Transactional
    public Response create(Reservation r) {
        return reservationService.create(r);
    }

    @RolesAllowed("User")
    @DELETE
    @Transactional
    public Response delete(Reservation r) {
        return reservationService.delete(r.getId());
    }

    @GET()
    @Path("/{id:\\d+}")
    public Response getById(Long id) {
        return reservationService.getById(id);
    }


    @RolesAllowed("User")
    @GET
    @Path("/user/{id:\\d+}")
    public Response getFromUser(Long id) {
        return reservationService.getReservationsFromUser(id);
    }
}
