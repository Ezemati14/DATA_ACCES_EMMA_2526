package org.emma.catchupperiod.secutiry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        //Aca indicamos que TODS pueden entrar a /login
                        .requestMatchers("/login").permitAll()
                        //Y aca indicamos si quiere entrar a /library, tiene que tener el rol LIBRARY
                        .requestMatchers("/library/**").hasRole("LIBRARY")
                        .requestMatchers("/editUser").hasRole("USER")
                        .requestMatchers("/books").hasRole("USER")
                        //Y aca indicamos que para otra pagina, necesita autenticarse
                        .anyRequest().authenticated()
                )
                //Esto es una configuracion de Spring Security para indicarle que queremos utilizar un formulario de login
                //vieine de la clas httpSecurity
                .userDetailsService(customUserDetailsService)
                .formLogin(form -> form
                        //Aca decimos que la pagina principal es login
                        .loginPage("/login")
                        //Aca va a procesar lo de la pagina login
                        .loginProcessingUrl("/login")
                        //Estos parametros son lo que va a procesar
                        .usernameParameter("surname")
                        .passwordParameter("code")
                        //Aca le decimos que no lleve al controlador con url /default
                        //Y ahi decimos hacia donde nos va a llevar
                        .defaultSuccessUrl("/default", true)
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }
}





/*@Bean
    public UserDetailsService userDetailsService() {
        //Con esto le indicamos si el usuario existe o no, y que rol tiene
        //InMemoryUserDetailsManager con esto guardamos el usuario en memoria
        return new InMemoryUserDetailsManager(
                //Esto lo que hace es crear un usaurio con nombre library
                //y contraseña library, y con el rol LIBRARY
                //{noop} es para indicar que no se va a encriptar la contraseña
                User.withUsername("library")
                        .password("{noop}library")
                        .roles("LIBRARY")
                        .build()
        );
    }*/
