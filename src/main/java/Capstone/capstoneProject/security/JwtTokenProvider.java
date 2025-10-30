package Capstone.capstoneProject.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final Key key;

    private static final long ACCESS_TOKEN_VALIDITY = 1000L * 60 * 60;         // 60분
    private static final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 7; // 7일

    // JWT 시크릿 키를 application.properties에서 주입
    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // AT 생성
    public String createToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + ACCESS_TOKEN_VALIDITY);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // RT 생성
    public String createRefreshToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + REFRESH_TOKEN_VALIDITY);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT에서 username 추출
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //JWT에서 권한 추출
    public List<GrantedAuthority> getRoles(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Object rolesObject = claims.get("roles");
        List<String> roles;

        if (rolesObject instanceof List<?>) {
            roles = ((List<?>) rolesObject).stream()
                    .map(String::valueOf)  // int, Long, String 모두 String으로 변환
                    .collect(Collectors.toList());
        } else {
            roles = new ArrayList<>();
        }

        // Spring Security에서 사용 가능한 GrantedAuthority로 변환
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    // OAuth2 여부 확인
    public boolean isOauthUser(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Object oauthClaim = claims.get("oauth");
            return oauthClaim != null && Boolean.parseBoolean(oauthClaim.toString());
        } catch (Exception e) {
            return false; // 토큰 문제 시 일반 사용자로 처리
        }
    }




    public String getUserIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
