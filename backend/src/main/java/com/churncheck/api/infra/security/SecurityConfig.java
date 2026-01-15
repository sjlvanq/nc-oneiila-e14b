package com.churncheck.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.Customizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)

 public class SecurityConfig {
     @Autowired
     private SecurityFilter securityFilter;
     
     @Autowired
     private CustomAuthenticationEntryPoint authEntryPoint;

     @Bean
     SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.cors(Customizer.withDefaults())
				.csrf(c -> c.disable())
				// .formLogin(form -> form.disable())
				.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())) // //TODO Quitar en producción -
																						// Frames de UI H2
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				.authorizeHttpRequests((request) -> request
						.requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/h2-console/**")).permitAll() // TODO:
																															// Quitar
																															// en
																															// producción
						.requestMatchers(HttpMethod.POST, "/login").permitAll()
						.requestMatchers("/api/stats").permitAll()
						.requestMatchers("/clients/high-risk").permitAll()
						.requestMatchers("/clients/**").authenticated()
						.requestMatchers("/chat/**").authenticated()  // ← Esto está protegiendo los endpoints de chat
						.requestMatchers(HttpMethod.POST, "/chat/conversations").authenticated()
						.requestMatchers(HttpMethod.POST, "/chat/conversations/**").authenticated()
						.requestMatchers("/swagger/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs",
								"/v3/api-docs/**")
						.permitAll()
						.anyRequest().authenticated())

 				.exceptionHandling(e -> e.authenticationEntryPoint(authEntryPoint))
 				.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
 				.build();
 	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
