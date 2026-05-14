package com.example.ecommerce.filter;

import com.example.ecommerce.service.UserDetailsServiceImpl;
import com.example.ecommerce.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {         // OncePerRequestFilter ensures this runs exactly once per request

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // This method runs on every incoming request before it reaches your controller
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Read the Authorization header from the incoming request
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Check header exists and follows "Bearer <token>" format
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);                   // strip "Bearer " (7 chars) to get raw token
            username = jwtUtil.extractUsername(token);         // decode token and get username from it
        }

        // Proceed only if username was found and user is not already authenticated in this request
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user from DB to make sure they still exist
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate token — checks username match and expiry
            if (jwtUtil.validateToken(token, userDetails)) {

                // Create Spring Security auth object to mark this user as authenticated
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Attach request details (IP, session, etc) to the auth token
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Store authentication in security context — Spring now knows user is logged in for this request
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Pass request forward to next filter or controller — without this the request stops here
        filterChain.doFilter(request, response);
    }
}