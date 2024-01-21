package com.commigo.metaclass.webconfig;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/** Imposta la sicurezza del backend. */
@Configuration
public class SecurityConfig {

  /**
   * Controlla la sicurezza delle richiesta http.
   *
   * @param http parametro di httpsecurity
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests((authz) -> authz.anyRequest().permitAll())
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(withDefaults())
        .logout(
            (logout) -> // gestione automatica del logout
            logout.deleteCookies("jwtToken").invalidateHttpSession(true).clearAuthentication(true));

    return http.build();
  }
}
