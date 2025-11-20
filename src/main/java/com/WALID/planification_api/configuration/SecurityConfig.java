package com.WALID.planification_api.configuration;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.WALID.planification_api.exceptions.GlobalExceptionHandler;

import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JWTAuthenticationFilter jwtFilter;

	@Bean
	public  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http
			.cors(cors -> cors.configurationSource(request -> {
		            CorsConfiguration config = new CorsConfiguration();
		            config.setAllowCredentials(false);
		            config.addAllowedOrigin("*");
		            config.addAllowedHeader("*");
		            config.addAllowedMethod("*");
		            config.addExposedHeader("Authorization");
		            return config;
	        	}))
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(request -> request
					.requestMatchers("/api/auth/**").permitAll()
					.requestMatchers("/api/users/**").hasAnyRole("ADMIN", "SUPER ADMIN")
					.requestMatchers("/api/roles/**").hasAnyRole("ADMIN", "SUPER ADMIN")
					.requestMatchers("/api/planification/**").hasAnyRole("USER", "ADMIN", "SUPER ADMIN")
					.requestMatchers("/api/salles/**").hasAnyRole("USER", "ADMIN", "SUPER ADMIN")
					.requestMatchers("/api/classes/**").hasAnyRole("USER", "ADMIN", "SUPER ADMIN")
					.requestMatchers("/api/cumpus/**").hasAnyRole("USER", "ADMIN", "SUPER ADMIN")
					.requestMatchers("/api/professeurs/**").hasAnyRole("USER", "ADMIN", "SUPER ADMIN")
					.requestMatchers("/api/villes/**").hasAnyRole("USER", "ADMIN", "SUPER ADMIN")
					.anyRequest().authenticated())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		 return http.build();
	}


	@Bean
    public StrictHttpFirewall allowDoubleSlashesHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedDoubleSlash(true);
        return firewall;
    }

	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}


	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public GlobalExceptionHandler globalSecurityExceptionHandler() {
	    return new GlobalExceptionHandler();
	}
}
