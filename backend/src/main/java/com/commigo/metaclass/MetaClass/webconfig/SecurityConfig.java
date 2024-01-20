package com.commigo.metaclass.MetaClass.webconfig;

import com.commigo.metaclass.MetaClass.gestionestanza.controller.GestioneStanzaControl;
import com.commigo.metaclass.MetaClass.gestionestanza.service.GestioneStanzaService;
import com.commigo.metaclass.MetaClass.gestionestanza.service.GestioneStanzaServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests((authz) -> authz.anyRequest().permitAll())
        .csrf((csrf) -> csrf.disable())
        .httpBasic(withDefaults())
        .logout(
            (logout) -> // gestione automatica del logout
            logout.deleteCookies("jwtToken").invalidateHttpSession(true).clearAuthentication(true));

    return http.build();
  }
}
