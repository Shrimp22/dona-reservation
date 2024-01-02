package com.example.service;

import com.example.dto.DetailResponse;
import com.example.dto.UserReservations;
import com.example.exeptions.ProductNotFoundExeption;
import com.example.models.Product;
import com.example.models.Reservation;
import com.example.models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
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


    public List<Reservation> findByDateTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return find("startDate between ?1 and ?2 or endDate between ?1 and ?2 or (startDate <= ?1 and endDate >= ?2)",
                startDateTime, endDateTime)
                .list();
    }

    public UserReservations create(Reservation r) {
        String email = securityContext.getUserPrincipal().getName();
        User activeUser = userService.find("email", email).firstResult();
        r.setUser(activeUser);
        Product product = productService.findById(r.getProductId());
        if(product == null) {
            throw new NotFoundException();
        }
        r.setProduct(product);
        List<Reservation> reservationsInTerm = findByDateTimeRange(r.getStartDate(), r.getEndDate());
        r.setTerm(reservationsInTerm.isEmpty());
        persist(r);
        UserReservations res = new UserReservations(r);
        return res;
    }


    public Reservation delete(Long id) {
        Reservation dbRes = findById(id);
        if(dbRes == null) {
            throw new NotFoundException();
        }
        dbRes.setProduct(null);
        dbRes.setUser(null);
        delete(dbRes);
        return dbRes;
    }

    public Reservation getById(Long id) {
        Reservation dbRes = findById(id);
        if(dbRes == null) {
            throw new NotFoundException();
        }
        return dbRes;
    }

    public Set<UserReservations> getReservationsFromUser(Long id) {
        User dbUser = userService.getById(id);
        if(dbUser == null) {
            throw new NotFoundException();
        }
        Set<Reservation> userRes = dbUser.getReservations();
        Set<UserReservations> res = userRes.stream().map(UserReservations::new).collect(Collectors.toSet());
        return res;
    }


    public Reservation update(Reservation r) throws ProductNotFoundExeption {
        Reservation dbRes = findById(r.getId());
        if(dbRes == null) {
            throw new NotFoundException();
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
                            throw new ProductNotFoundExeption();
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

        return dbRes;
    }

}
