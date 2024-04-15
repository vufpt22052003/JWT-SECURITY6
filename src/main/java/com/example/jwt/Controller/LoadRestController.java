package com.example.jwt.Controller;

import com.example.jwt.Repository.UserRepository;
import com.example.jwt.entity.Users;
import com.example.jwt.jwt.JwtTokenProvider;
import com.example.jwt.payload.LoginRequest;
import com.example.jwt.payload.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class LoadRestController {

    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public LoginResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
        // Sinh token JWT từ thông tin đăng nhập
        Users user = userRepository.findByUsername(loginRequest.getUsername());
        String jwt = tokenProvider.generateToken(user);

        System.err.println(jwt);
        return new LoginResponse(jwt);
    }

    @GetMapping("/getToken")
    public ResponseEntity<Void> getRandom() {
        System.err.println("---");
        return ResponseEntity.ok().build();
    }
}

