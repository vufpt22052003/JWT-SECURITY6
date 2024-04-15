package com.example.jwt.jwt;

import com.example.jwt.Service.UserService;
import com.example.jwt.entity.Users;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Slf4j
@Component
@NoArgsConstructor(force = true)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (jwt != null && !jwt.isEmpty() && tokenProvider.validateToken(jwt)) {
                // Tải thông tin người dùng từ cơ sở dữ liệu bằng username
                Users user = new Users();
                UserDetails userDetails = user.getUser();

                if (userDetails != null) {
                    // Nếu người dùng hợp lệ, set thông tin cho Seturity Context
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception ex) {
            log.error("Failed to set user authentication", ex);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // kiểm tra xem header Authorization có chưa thông tin jwt không
        if (bearerToken != null && !bearerToken.isEmpty() && bearerToken.startsWith("Bearer ")) {
            // Trả về JWT bằng cách loại bỏ từ "Bearer " từ chuỗi bearerToken
            return bearerToken.substring(7);
        }
        return null;
    }
}
