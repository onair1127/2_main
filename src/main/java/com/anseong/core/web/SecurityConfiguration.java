package com.anseong.core.web;

import java.util.List;

import com.anseong.core.domain.model.user.repository.UserCache;
import com.anseong.core.web.apis.authenticate.AuthenticationFilter;
import com.anseong.core.web.apis.authenticate.JwtAuthenticationFilter;
import com.anseong.core.web.apis.authenticate.SimpleAuthenticationFailureHandler;
import com.anseong.core.web.apis.authenticate.SimpleAuthenticationSuccessHandler;
import com.anseong.core.web.apis.authenticate.SimpleLogoutSuccessHandler;
import com.anseong.core.web.apis.interceptor.RequestInterceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@EnableWebSecurity
@EnableAspectJAutoProxy(proxyTargetClass = true)
class SecurityConfiguration {

     private String profile;
     private UserCache userCache;
     private AuthenticationConfiguration authenticationConfiguration;

     SecurityConfiguration(
          @Value("${spring.profiles.active}") String profile,
          UserCache userCache,
          AuthenticationConfiguration authenticationConfiguration
     ) {
          this.profile = profile;
          this.userCache = userCache;
          this.authenticationConfiguration = authenticationConfiguration;
     }

	@Bean
     protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
          return http
               .authorizeHttpRequests((authz) -> { authz
                    // .requestMatchers(PathRequest.toH2Console()).permitAll()
                    .requestMatchers("/check/**").permitAll()
                    .requestMatchers("/api/me/guest").permitAll()
                    .requestMatchers("/api/**").authenticated()
                    .requestMatchers("/api/ceo/**").hasAuthority("ROLE_CEO")
                    .requestMatchers("/api/iamport/notification").hasAnyRole("hasIpAddress('52.78.100.19') or hasIpAddress('52.78.48.223') or hasIpAddress('52.78.5.241')");
               })
               .addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
               .addFilterAt(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
               .formLogin(form -> form.disable())
                    .logout(logout -> { logout
                         .invalidateHttpSession(true)
                         .clearAuthentication(true);
                    })
               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .httpBasic(basic -> basic.disable())
               .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
               .cors(cors -> cors.configurationSource(corsConfigurationSource()))
               .x509(x509 -> x509.disable())
               .csrf(csrf -> csrf.disable())
               .exceptionHandling(exception -> { exception
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
               })
               .build();
     }

     @Bean
     CorsConfigurationSource corsConfigurationSource() {
          CorsConfiguration config = new CorsConfiguration();
          config.setAllowedOriginPatterns(List.of("https://*.anseongapp.com", "http://172.31.*"));
          config.setAllowedOrigins(List.of("http://211.214.148.170:3000", "http://localhost:3000", "http://192.168.137.1:3000", "http://192.168.0.8:3000", "http://192.168.55.188:3000", "http://nginx", "https://anseongapp.com"));
          config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE","OPTIONS"));
          config.setExposedHeaders(List.of("Authorization", "Set-Cookie", "Issue"));
          config.setAllowedHeaders(List.of("*"));
          config.setAllowCredentials(true);

          UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
          source.registerCorsConfiguration("/**", config);
          return source;
     }

     @Bean
     WebMvcConfigurer webMvcConfigurer() {
          return new WebMvcConfigurer() {
               @Override
               public void addInterceptors(InterceptorRegistry registry) {
                    registry.addInterceptor(new RequestInterceptor())
                         .addPathPatterns("/**");
               }
          };
     }

     AuthenticationFilter authenticationFilter() throws Exception {
          AuthenticationFilter filter = new AuthenticationFilter();

          filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
          filter.setAuthenticationFailureHandler(authenticationFailureHandler());
          filter.setAuthenticationManager(this.authenticationConfiguration.getAuthenticationManager());

          return filter;
     }

     JwtAuthenticationFilter jwtAuthenticationFilter() {
          return new JwtAuthenticationFilter(profile);
     }

     @Bean
     AuthenticationSuccessHandler authenticationSuccessHandler() {
          return new SimpleAuthenticationSuccessHandler(userCache);
     }

     @Bean
     AuthenticationFailureHandler authenticationFailureHandler() {
          return new SimpleAuthenticationFailureHandler();
     }

     @Bean
     LogoutSuccessHandler logoutSuccessHandler() {
          return new SimpleLogoutSuccessHandler();
     }
}
