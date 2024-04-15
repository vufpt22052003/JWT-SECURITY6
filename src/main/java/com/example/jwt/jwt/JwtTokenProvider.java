package com.example.jwt.jwt;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

import com.example.jwt.entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)

//Sau khi có các thông tin về người dùng (CLASS USERS), chúng ta cần mã hóa thông tin người dùng thành chuỗi JWT
public class JwtTokenProvider {
    // Thay thế chuỗi JWT_SECRET bằng một key có kích thước đủ lớn
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    //Thời gian có hiệu lực của chuỗi jwt
    final long JWT_EXPIRATION = 5 * 1000;

    // Tạo ra jwt từ thông tin user
    public String generateToken(Users userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        // Tạo chuỗi json web token từ id của user.
        return Jwts.builder()
                .setSubject(Long.toString(userDetails.getId())) // tạo ra 1 chuổi đại điện diện chủ thể cho token
                .setIssuedAt(now) // thời gian bắn đầu token
                .setExpiration(expiryDate) // time hết hiệu lực token
                .signWith(secretKey, SignatureAlgorithm.HS512) // ký (sign) JWT bằng 1 thuật toán và key được cung cấp
                .compact();
    }

    // Lấy thông tin user từ jwt
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}