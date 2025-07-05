package com.example.ApiGatewayService.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ApiGatewayService.util.JwtUtil;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	@Autowired
	private RouteValidator routeValidator;
	
	@Autowired
	private WebClient webClient;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	public AuthenticationFilter() {
		super(Config.class);
	}
	
	public static class Config{
		
	}

	
	@Override
	public GatewayFilter apply(Config config) {
	    return (exchange, chain) -> {
	        if (routeValidator.isSecured.test(exchange.getRequest())) {
	            // Check header
	            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
	                throw new RuntimeException("Missing Authorization header");
	            }

	            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

	            if (authHeader != null && authHeader.startsWith("Bearer ")) {
	                authHeader = authHeader.substring(7);
	            }

	            try {
	            	ResponseEntity<Boolean> resIsValid = jwtUtil.validateToken(authHeader);
	            	Boolean isValid = resIsValid.getBody();
	                if (isValid == null || !isValid) {
	                    throw new RuntimeException("Unauthorized: Token validation failed");
	                }

	            } catch (Exception e) {
	                throw new RuntimeException("Error while contacting UserManagementApi", e);
	            }
	        }
	        return chain.filter(exchange);
	    };
	}

}