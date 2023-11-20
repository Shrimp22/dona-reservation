package com.example.service;

import com.example.dto.DetailResponse;
import com.example.dto.UserReservations;
import com.example.models.Product;
import com.example.models.Reservation;
import com.example.models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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

    // check is term taken and mark it with boolean

    public List<Reservation> findByDateTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return find("startDate between ?1 and ?2 or endDate between ?1 and ?2 or (startDate <= ?1 and endDate >= ?2)",
                startDateTime, endDateTime)
                .list();
    }

    public Response create(Reservation r) {
        String email = securityContext.getUserPrincipal().getName();
        User activeUser = userService.find("email", email).firstResult();
        r.setUser(activeUser);
        Product product = productService.findById(r.getProductId());
        if(product == null) {
            return Response.status(404).build();
        }
        r.setProduct(product);
        List<Reservation> reservationsInTerm = findByDateTimeRange(r.getStartDate(), r.getEndDate());
        r.setTerm(reservationsInTerm.size() > 0);
        persist(r);
        UserReservations res = new UserReservations(r);
        return Response.ok(res).build();
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


    public Response update(Reservation r) {
        Reservation dbRes = findById(r.getId());
        if(dbRes == null) {
            return Response.status(404).entity(new DetailResponse("Reservation not found")).build();
        }
        Field[] fields = Reservation.class.getDeclaredFields();

        for(Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(r);
                if(value != null) {
                    field.set(dbRes, value);
                    String fieldName = field.getName();
                    if(fieldName.equalsIgnoreCase("productId")) {
                        Product dbProduct = productService.findById((Long) value);
                        if(dbProduct == null) {
                            return Response.status(404).entity(new DetailResponse("Product not found")).build();
                        }
                        dbRes.setProduct(dbProduct);
                    }
                }
                field.setAccessible(false);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }

        dbRes.deny();
        persistAndFlush(dbRes);

        return Response.ok(new UserReservations(dbRes)).build();
    }

}
