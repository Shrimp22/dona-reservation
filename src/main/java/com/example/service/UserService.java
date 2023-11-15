package com.example.service;

import com.example.models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.vertx.codegen.doc.Token;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.elytron.security.common.BcryptUtil;


import java.lang.reflect.Field;
import java.util.List;


@ApplicationScoped
public class UserService implements PanacheRepository<User> {
    public User create(User u) {
        User findUser = find("email", u.getEmail()).firstResult();
        if(findUser != null) {
            return null;
        }

        String hashedPassword = BcryptUtil.bcryptHash(u.getPassword());
        u.setPassword(hashedPassword);
        persist(u);
        return u;
    }

    public List<User> getAll() {
        List<User> users = findAll().list();
        return users;
    }

    public String login(User u) {
        User dbUser = find("email", u.getEmail()).firstResult();
        if(dbUser == null) {
            return null;
        }
        if(BcryptUtil.matches(u.getPassword(), dbUser.getPassword())) {
            try {
                String token = TokenUtils.generateToken(u.getEmail());
                return token;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    public User getUserByEmail(String email) {
        User u = find("email", email).firstResult();
        return u;
    }


    public User delete(Long id) {
        User dbUser = findById(id);
        if(dbUser == null) {
            return null;
        }
        delete(dbUser);
        return dbUser;
    }

    public User getById(Long id) {
        User dbUser = findById(id);
        if(dbUser == null) {
            return null;
        }
        return dbUser;
    }

    public User update(User u) {
        User dbUser = findById(u.getId());
        if(dbUser == null) {
            return  null;
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