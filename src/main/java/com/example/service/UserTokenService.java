package com.example.service;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.dto.DetailResponse;
import com.example.exeptions.ReservationExpiredExeption;
import com.example.mailer.MailHandler;
import com.example.models.User;
import com.example.models.UserToken;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class UserTokenService implements PanacheRepository<UserToken>{
    
    @Inject
    UserService userService;

    @Inject
    MailHandler mailHandler;

    public UserToken create(String email) {
        User dbUser = userService.find("email", email).firstResult();
        if(dbUser == null) {
            throw new NotFoundException("User not found");
        }
        
        UserToken token = new UserToken();
        token.setUser(dbUser);
        LocalDateTime expTime = LocalDateTime.now().plusHours(4);
        token.setExprTime(expTime);
        
        String tokenStr = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        token.setToken(tokenStr);
        

        persist(token);
        mailHandler.sendEmail(email, "Password reset request", tokenStr);
        return token;
    }

    public UserToken changePassword(String token, String password) throws ReservationExpiredExeption {
        UserToken dbToken = find("token", token).firstResult();
        LocalDateTime now = LocalDateTime.now();
        if(dbToken == null) {
            throw new NotFoundException();
        }else if(dbToken.getExprTime().isBefore(now)) {
            delete(dbToken);
            throw new ReservationExpiredExeption();
        }

        User dbUser = dbToken.getUser();
        String hashPassword = BcryptUtil.bcryptHash(password);
        dbUser.setPassword(hashPassword);

        userService.persistAndFlush(dbUser);
        delete(dbToken);
        return dbToken;
    }


}
