package com.onnasoft.date.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.onnasoft.date.services.UserService;
import com.onnasoft.date.services.UserService.UserNotFound;
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

    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            if (existsJWTToken(request, response)) {
                Claims claims = validateToken(request);
                if (claims.get("authorities") != null) {
                    setUpSpringAuthentication(request, claims);
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
        } catch (UserNotFound e) {
            logger.warn("Posible robo de datos sensibles!");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(401, e.getMessage());
        } catch (Exception e) {
            logger.error(e);
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
     * @throws UserNotFound
     */
    private void setUpSpringAuthentication(HttpServletRequest request, Claims claims) throws UserNotFound {
        @SuppressWarnings("unchecked")
        final var authorities = (List<String>) claims.get("authorities");
        final var email = claims.getSubject();

        final var auth = new UsernamePasswordAuthenticationToken(email, null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        
        getUserService(request);
        final var user = userService.findUserByEmail(email);

        final var session = request.getSession();
        session.setAttribute("userId", user.getId());
        session.setAttribute("user", user);
    }

    private void getUserService(HttpServletRequest request) {
        if (userService == null) {
            final var context = request.getServletContext();
            final var ctx = WebApplicationContextUtils.getWebApplicationContext(context);
            userService = ctx.getBean(UserService.class);
        }
    }

    private boolean existsJWTToken(HttpServletRequest request, HttpServletResponse res) {
        final var authenticationHeader = request.getHeader(HEADER);
        if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
            return false;
        return true;
    }
}