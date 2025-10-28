package Capstone.capstoneProject.config;

import Capstone.capstoneProject.dto.ApiResponse;

import Capstone.capstoneProject.repository.AuthTokenRepository;

import Capstone.capstoneProject.security.CustomSecurityUserDetails;
import Capstone.capstoneProject.security.JwtAuthenticationFilter;
import Capstone.capstoneProject.security.JwtTokenProvider;
import Capstone.capstoneProject.security.oauth.CustomOauth2UserService;
import Capstone.capstoneProject.security.oauth.OAuth2LoginSuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;
    private final CustomSecurityUserDetails userDetailsService;
    private final AuthTokenRepository authTokenRepository;
    private final CustomOauth2UserService customOauth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenProvider, userDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
              .csrf(AbstractHttpConfigurer::disable) // Spring Security 6.1 이상에서 권장되는 방식
               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // kakao 로그인
                        .requestMatchers("/oauth2/**").permitAll()
                        // google 로그인
                        .requestMatchers("/login/**").permitAll()
                        // 인증 관련 API
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/signup").permitAll()
                        // Swagger/OpenAPI
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        // 에러 페이지
                        .requestMatchers("/error").permitAll()
                        // WebSocket 관련
                        .requestMatchers("/ws-chat/**", "/room/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler((request, response, exception) -> {
                            // 로그인 실패 시 JSON 반환
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                            String message = "OAuth2 login failed";
                            if (exception instanceof OAuth2AuthenticationException oAuth2Exception) {
                                message = oAuth2Exception.getError().getDescription();
                            }

                            response.getWriter().write(
                                    new ObjectMapper().writeValueAsString(ApiResponse.error(message))
                            );
                        })
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOauth2UserService))
                );

        return http.build();
    }
}
