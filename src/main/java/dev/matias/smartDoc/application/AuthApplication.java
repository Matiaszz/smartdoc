package dev.matias.smartDoc.application;

import dev.matias.smartDoc.Domain.User.User;
import dev.matias.smartDoc.Domain.User.ValueObjects.UserRole;
import dev.matias.smartDoc.Interfaces.dto.user.RegisterUserDTO;
import dev.matias.smartDoc.Interfaces.dto.user.UserDTO;
import dev.matias.smartDoc.Interfaces.dto.user.UserLoginDTO;
import dev.matias.smartDoc.application.auth.AuthService;
import dev.matias.smartDoc.application.auth.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthApplication {

    @Autowired
    private AuthService authService;

    @Autowired
    private JWTService jwtService;

    public String loginToken(UserLoginDTO data){
        User user = authService.login(data);
        return jwtService.generateToken(user);
    }

    public UserDTO registerUser(RegisterUserDTO data, UserRole role) {
        User user = authService.register(data, role);
        return new UserDTO(user);
    }

}
