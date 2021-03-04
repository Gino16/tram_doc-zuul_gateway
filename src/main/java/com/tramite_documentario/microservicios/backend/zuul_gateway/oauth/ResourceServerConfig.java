package com.tramite_documentario.microservicios.backend.zuul_gateway.oauth;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/api/security/**", "/api/usuarios/forgot_password", "/api/usuarios/cambiar_contrasenia", "/api/solicitudes/exportar/{id}/", "/api/archivos/ver-archivo/{id}/", "/swagger-ui/index.html**").permitAll()
                .antMatchers(HttpMethod.GET, "/verificarSesion").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.PUT,  "/api/solicitudes/actualizar-estado/{idSolicitud}/estado/{idEstado}").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/api/solicitudes", "/api/solicitudes/{id}", "/api/archivos/{id}", "/api/archivos/ver-archivo/{id}", "/api/solicitudes/listar-estados/{idSolicitud}").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/api/archivos/enviar-archivo/{id}", "localhost:8090/api/solicitudes/firmar/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/api/personas", "/api/personas/puestos", "/api/archivos", "/api/archivos/tipoArchivos", "/api/archivos/archivos-sin-solicitud").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/personas/{id}", "/api/personas/buscar-por-codEstudiante/{codigo}", "/api/personas/buscar-por-email/{correo}", "/api/personas/buscar-por-dni/{dniRuc}", "/api/personas/buscar-por-nombre/{nombre}", "/api/personas/buscar-por-apellido/{apellido}", "/api/solicitudes/exportar/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/solicitudes", "/api/personas", "/api/archivos", "/api/archivos/crear-con-file").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/solicitudes/{id}", "/api/archivos/{id}", "/api/personas/{id}", "/api/archivos/editar-con-file/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/archivos/{id}", "/api/personas/").hasRole("ADMIN")
                .anyRequest().denyAll()
                .and()
                .cors().configurationSource(corsConfigurationSource());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        tokenConverter.setSigningKey("alguna_clave_secreta_12345");
        return tokenConverter;
    }
}
