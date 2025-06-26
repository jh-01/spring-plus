package org.example.expert.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter implements Filter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        log.info(">>> JwtFilter called for path: {}", request.getRequestURI());

        if (path.equals("/auth/signup") || path.equals("/auth/signin")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String bearerJwt = request.getHeader("Authorization");

            if (!StringUtils.hasText(bearerJwt) || !bearerJwt.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is missing or malformed");
                return;
            }

            // JWT 유효성 검사와 Claims 추출
            String jwt = jwtUtil.substringToken(bearerJwt);
            Claims claims = jwtUtil.extractClaims(jwt);

            if(claims == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다.");
                return;
            }

            Long userId = jwtUtil.getUserId(jwt);
            String email = claims.get("email", String.class);
            String userRole = claims.get("userRole", String.class);
            String nickname = claims.get("nickname", String.class);

            AuthUser authUser = new AuthUser(userId, email, UserRole.valueOf(userRole), nickname);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    authUser, "", List.of(new SimpleGrantedAuthority("ROLE_" + userRole))
            );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (SecurityException | MalformedJwtException e){
            log.error("Invalid JWT Signature, Invalid JWT Token", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Signature, Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT Token", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT Token", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported JWT Token");
        } catch (Exception e) {
            log.error("Internal server error", e);
        }

    }
}
