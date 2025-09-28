package dev.matias.smartDoc.Interfaces;

import dev.matias.smartDoc.Domain.User.ValueObjects.UserRole;
import dev.matias.smartDoc.Interfaces.dto.user.RegisterUserDTO;
import dev.matias.smartDoc.Interfaces.dto.user.UserDTO;
import dev.matias.smartDoc.Interfaces.dto.user.UserLoginDTO;
import dev.matias.smartDoc.application.AuthApplication;
import dev.matias.smartDoc.application.auth.utils.CookieOptions;
import dev.matias.smartDoc.application.cookie.CookieService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthApplication authApplication;

    @Autowired
    private CookieService cookieService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid RegisterUserDTO data){
        UserDTO response = authApplication.registerUser(data, UserRole.USER);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody @Valid UserLoginDTO data){
        String token = authApplication.loginToken(data);
        ResponseCookie cookie = cookieService.createCookie(token, "userToken", new CookieOptions());
        return ResponseEntity.ok().header("Set-Cookie", cookie.toString()).build();
    }


}
