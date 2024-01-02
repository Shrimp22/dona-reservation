package com.example.service;

import com.example.dto.DetailResponse;
import com.example.dto.UserDto;
import com.example.exeptions.WrongPasswordExeption;
import com.example.models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;


import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;


@ApplicationScoped
public class UserService implements PanacheRepository<User> {
    public User create(User u) {
        User findUser = find("email", u.getEmail()).firstResult();
        if(findUser != null) {
            throw new EntityExistsException();
        }
        String hashedPassword = BcryptUtil.bcryptHash(u.getPassword());
        u.setPassword(hashedPassword);
        persist(u);
        return u;
    }

    public Response getAll() {
        List<User> users = findAll().list();
        List<UserDto> usersDto = users.stream().map(UserDto::new).collect(Collectors.toList());
        return Response.ok(usersDto).build();
    }

    public String login(User u) throws WrongPasswordExeption{
        User dbUser = find("email", u.getEmail()).firstResult();
        if(dbUser == null) {
            throw new NotFoundException();
        }

        if(BcryptUtil.matches(u.getPassword(), dbUser.getPassword())) {
            try {
                String token = TokenUtils.generateToken(dbUser.getEmail(), dbUser.isAdmin());
                return "Bearer " + token;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else {
            throw new WrongPasswordExeption();
        }

    }

    public User getUserByEmail(String email) {
        User u = find("email", email).firstResult();
        if(u == null) {
            throw new NotFoundException();
        }
        return u;
    }


    public User delete(Long id) {
        User dbUser = findById(id);
        if(dbUser == null) {
            throw new NotFoundException();
        }
        delete(dbUser);
        return dbUser;
    }

    public User getById(Long id) {
        User dbUser = findById(id);
        if(dbUser == null) {
            throw new NotFoundException();
        }
        return dbUser;
    }

    public User update(User u) {
        User dbUser = findById(u.getId());
        if(dbUser == null) {
            throw new NotFoundException();
        }

        Field[] fields = User.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object sourceValue = field.get(u);
                if (sourceValue != null) {
                    field.set(dbUser, sourceValue);
                    String fieldName = field.getName();
                    if(fieldName.equalsIgnoreCase("password")) {
                        String hashPassword = BcryptUtil.bcryptHash(u.getPassword());
                        dbUser.setPassword(hashPassword);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            field.setAccessible(false);
        }
        persistAndFlush(dbUser);
        return dbUser;
    }
}