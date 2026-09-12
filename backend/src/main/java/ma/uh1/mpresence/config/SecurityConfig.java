package ma.uh1.mpresence.config;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @org.springframework.beans.factory.annotation.Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(allowedOrigins.split(",")));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Back-office web (Thymeleaf) : session + formLogin classique, réservé
     * aux comptes ADMIN/FORMATEUR (le Doctorant n'a pas accès au back-office,
     * uniquement à l'app mobile).
     */
    @Bean
    @Order(1)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/backoffice/**", "/css/**", "/js/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/backoffice/login").permitAll()
                        .requestMatchers("/css/**", "/js/**").permitAll()
                        .anyRequest().hasAnyRole("ADMIN", "FORMATEUR")
                )
                .formLogin(form -> form
                        .loginPage("/backoffice/login")
                        .loginProcessingUrl("/backoffice/login")
                        .defaultSuccessUrl("/backoffice/dashboard", true)
                        .failureUrl("/backoffice/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/backoffice/logout")
                        .logoutSuccessUrl("/backoffice/login?logout")
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    /**
     * API REST (mobile app) : stateless, Bearer JWT.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // Photos de profil : publiques (une balise <img> ne peut pas envoyer de Bearer token).
                        .requestMatchers(HttpMethod.GET, "/uploads/photos/**").permitAll()

                        // Espace "moi" du Doctorant connecté (mobile) : self-service uniquement.
                        .requestMatchers("/doctorants/me/**").authenticated()

                        // Espace "moi" du Formateur connecté (mobile) : demandes d'affectation.
                        .requestMatchers("/formateurs/me/**").hasAnyRole("FORMATEUR", "ADMIN")

                        // Formations : consultables par tout le monde connecté (Doctorant inclus),
                        // seules les mutations sont réservées à l'ADMIN.
                        .requestMatchers(HttpMethod.GET, "/administration/formations/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/administration/formations").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/administration/formations/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/administration/formations/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/administration/formations/*/modules/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/administration/formations/*/modules/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/administration/formations/*/modules/**").hasRole("ADMIN")

                        // Scan QR / feuilles de présence : réservé à l'admin/formateur qui scanne.
                        .requestMatchers("/formations/participerModuleByQr/**").hasAnyRole("ADMIN", "FORMATEUR")
                        .requestMatchers("/formations/*/modules/*/presences").hasAnyRole("ADMIN", "FORMATEUR")
                        .requestMatchers("/formations/*/modules/*/roster").hasAnyRole("ADMIN", "FORMATEUR")
                        .requestMatchers("/formations/*/modules/*/doctorants/*/presence").hasAnyRole("ADMIN", "FORMATEUR")

                        // Profil admin/formateur (distinct de /doctorants/me).
                        .requestMatchers("/users/me/**").hasAnyRole("ADMIN", "FORMATEUR")

                        // Toutes les ressources de gestion back-office : réservées admin/formateur,
                        // le Doctorant n'y a jamais accès (même en lecture).
                        .requestMatchers("/doctorants/**").hasAnyRole("ADMIN", "FORMATEUR")
                        .requestMatchers("/demandes/**").hasAnyRole("ADMIN", "FORMATEUR")
                        .requestMatchers("/reinscriptions/**").hasAnyRole("ADMIN", "FORMATEUR")
                        .requestMatchers("/inscriptions/**").hasAnyRole("ADMIN", "FORMATEUR")
                        .requestMatchers("/publications/**").hasAnyRole("ADMIN", "FORMATEUR")
                        .requestMatchers("/rendezvous/**").hasAnyRole("ADMIN", "FORMATEUR")
                        .requestMatchers("/notifications/**").hasAnyRole("ADMIN", "FORMATEUR")
                        .requestMatchers("/formations-internes/**").hasAnyRole("ADMIN", "FORMATEUR")

                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) // requis pour la console H2
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
