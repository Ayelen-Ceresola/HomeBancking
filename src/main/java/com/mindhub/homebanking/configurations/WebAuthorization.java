package com.mindhub.homebanking.configurations;

import netscape.javascript.JSException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity

@Configuration

public class WebAuthorization  {

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {

        http.authorizeRequests()

                .antMatchers("/web/pages/index.html").permitAll()
                .antMatchers("/web/assets/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/login").permitAll()
                .antMatchers(HttpMethod.POST,"/api/clients").permitAll()
                .antMatchers(HttpMethod.POST,"/api/logout").permitAll()
                .antMatchers(HttpMethod.POST,"/clients/current/accounts","/api/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/loans").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/transaction").hasAuthority("CLIENT")
                .antMatchers("/web/pages/**","/api/clients/current/**").hasAuthority("CLIENT")
                .antMatchers("/api/loans").hasAuthority("CLIENT")
                .antMatchers("/api/accounts").hasAuthority("CLIENT")
                .antMatchers("/api/accounts/{id}").hasAuthority("CLIENT")
                .antMatchers("/api/accounts").hasAuthority("CLIENT")
                .antMatchers( "/h2-console","/rest/**","/api/clients","/web/manager.html").hasAuthority("ADMIN");






        http.formLogin()

                .usernameParameter("email")

                .passwordParameter("password")

                .loginPage("/api/login");


        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");

        //Deshabilita la protección CSRF (Cross-Site Request Forgery)
        http.csrf().disable();

        //Deshabilita las opciones para acceder a H2 console
        http.headers().frameOptions().disable();

        //Configura un punto de entrada para el manejo de autenticación y envía un código de error HTTP 401 (Unauthorized) si se produce un error de autenticación.
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // Configura un manejador de éxito de inicio de sesión que limpia los atributos de autenticación.
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        //si la sesion falla te envia una respuesta de fallo de autenticacion
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        //si el cierre de sesion es exitoso se envia una respuesta de exitosa
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        //construye y devuelve el filtro configurado
        return http.build();
    }



        private void clearAuthenticationAttributes(HttpServletRequest request) {

            HttpSession session = request.getSession(false);

            if (session != null) {

                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

            }

        }


}




