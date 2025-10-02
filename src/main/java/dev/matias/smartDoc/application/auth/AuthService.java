package dev.matias.smartDoc.application.auth;

import dev.matias.smartDoc.Domain.User.User;
import dev.matias.smartDoc.Domain.User.UserRepository;
import dev.matias.smartDoc.Domain.User.ValueObjects.UserRole;
import dev.matias.smartDoc.Interfaces.dto.user.RegisterUserDTO;
import dev.matias.smartDoc.Interfaces.dto.user.UserLoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public User register(RegisterUserDTO data, UserRole role){
        List<User> existingUsers = userRepository.findByEmailOrUsername(data.email(), data.username());

        if (!existingUsers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email or Username already exist.");
        }

        String hashedPassword = passwordEncoder.encode(data.password());
        RegisterUserDTO dto = new RegisterUserDTO(data.username(), data.name(), data.email(), hashedPassword);
        User user = new User(dto, role);

        return userRepository.save(user);
    }

    public User login(UserLoginDTO data){

        User existingUser = userRepository.findByEmail(data.email()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong Email or Password")
        );

        boolean passwordMatches = passwordEncoder.matches(data.password(), existingUser.getPassword());

        if (!passwordMatches) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong Email or Password");


        var auth = new UsernamePasswordAuthenticationToken(existingUser, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public User getMe() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        return (User) auth.getPrincipal();
    }
}
