package com.onnasoft.date.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    private String SECRET = "";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            if (existsJWTToken(request, response)) {
                Claims claims = validateToken(request);
                if (claims.get("authorities") != null) {
                    setUpSpringAuthentication(claims);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } else {
                SecurityContextHolder.clearContext();
            }
            chain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(401, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(500, "¡Error desconocido!");
        }
    }

    private void getSecret(HttpServletRequest request) {
        if (SECRET.equals("")) {
            final var context = request.getServletContext();
            final var ctx = WebApplicationContextUtils.getWebApplicationContext(context);
            final var env = ctx.getBean(Environment.class);
            this.SECRET = env.getProperty("secret");
        }
    }

    private Claims validateToken(HttpServletRequest request) throws Exception {
        getSecret(request);
        final var jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        final var parser = Jwts.parser().setSigningKey(SECRET.getBytes());
        final var claims = parser.parseClaimsJws(jwtToken);
        return claims.getBody();
    }

    /**
     * Metodo para autenticarnos dentro del flujo de Spring
     * 
     * @param claims
     */
    private void setUpSpringAuthentication(Claims claims) {
        @SuppressWarnings("unchecked")
        List<String> authorities = (List<String>) claims.get("authorities");

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(auth);

    }

    private boolean existsJWTToken(HttpServletRequest request, HttpServletResponse res) {
        final var authenticationHeader = request.getHeader(HEADER);
        if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
            return false;
        return true;
    }
}