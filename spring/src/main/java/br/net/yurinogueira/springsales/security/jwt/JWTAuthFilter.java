package br.net.yurinogueira.springsales.security.jwt;

import br.net.yurinogueira.springsales.domain.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserServiceImpl userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer")) {
            String token = authorization.split(" ")[1];
            boolean isValid = jwtService.isValidToken(token);

            try {
                if (isValid) {
                    String userLogin = jwtService.getSystemUserLogin(token);
                    UserDetails userDetails = userService.loadUserByUsername(userLogin);
                    UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(user);
                }
            } catch (Exception exception) {
                handleErrors(response, exception);
            }
        }

        try {
            filterChain.doFilter(request, response);
        } catch (Exception exception){
            handleErrors(response, exception);
        }
    }

    private void handleErrors(HttpServletResponse response, Exception exception) throws IOException {
        System.out.println(exception.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(exception.getMessage());
    }


}
