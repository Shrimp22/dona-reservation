package com.example.service;

import com.example.dto.UserReservations;
import com.example.models.Product;
import com.example.models.Reservation;
import com.example.models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@ApplicationScoped
public class ReservationService implements PanacheRepository<Reservation> {

    @Inject
    SecurityContext securityContext;
    @Inject
    UserService userService;

    @Inject
    ProductService productService;

    // check is term taken
    public Response create(Reservation r) {
        // Find  active user and wanted product based on id
        String email = securityContext.getUserPrincipal().getName();
        User activeUser = userService.find("email", email).firstResult();
        r.setUser(activeUser);
        Product product = productService.findById(r.getProductId());
        if(product == null) {
            return Response.status(404).build();
        }
        r.setProduct(product);
        persist(r);
        return Response.ok(r).build();
    }


    public Response delete(Long id) {
        Reservation dbRes = findById(id);
        if(dbRes == null) {
            Map<String, String> resp = new HashMap<>();
            resp.put("detail", "Reservation not found");
            return Response.status(404).entity(resp).build();
        }
        dbRes.setProduct(null);
        dbRes.setUser(null);
        delete(dbRes);
        return Response.ok(dbRes).build();
    }

    public Response getById(Long id) {
        Reservation dbRes = findById(id);
        if(dbRes == null) {
            return Response.status(404).build();
        }
        return Response.ok(dbRes).build();
    }

    public Response getReservationsFromUser(Long id) {
        User dbUser = userService.getById(id);
        if(dbUser == null) {
            return Response.status(404).build();
        }

        Set<Reservation> userRes = dbUser.getReservations();
        Set<UserReservations> res = userRes.stream().map(UserReservations::new).collect(Collectors.toSet());
        return Response.ok(res).build();

    }

    // crud: update, get reservations based on product, get reservations based on author
}
