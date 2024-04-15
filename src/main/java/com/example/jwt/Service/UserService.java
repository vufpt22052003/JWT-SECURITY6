package com.example.jwt.Service;

import com.example.jwt.Repository.UserRepository;
import com.example.jwt.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

//    public UserDetails loadUserById(Long userId) {
//        Optional<Users> user = Optional.ofNullable(
//                userRepository.findById(userId).orElseThrow(()
//                        -> new UsernameNotFoundException("User not found with id: " + userId)));
//        return new Users(user.get());
//    }
}
