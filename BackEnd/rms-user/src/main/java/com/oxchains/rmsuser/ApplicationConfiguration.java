package com.oxchains.rmsuser;

import com.oxchains.rmsuser.auth.AuthError;
import com.oxchains.rmsuser.auth.JwtAuthenticationProvider;
import com.oxchains.rmsuser.auth.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

/**
 * @author ccl
 * @create 2018-01-09 10:58
 **/
@EnableWebSecurity
@Configuration
public class ApplicationConfiguration extends WebSecurityConfigurerAdapter {

    @Resource private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Resource private JwtTokenFilter jwtTokenFilter;
    @Resource private AuthError authError;

//    public UserApplicationConfiguration(@Autowired JwtTokenFilter jwtTokenFilter, @Autowired JwtAuthenticationProvider jwtAuthenticationProvider, @Autowired AuthError authError) {
//        this.jwtTokenFilter = jwtTokenFilter;
//        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
//        this.authError = authError;
//    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/user/*","/menu/**/*","/permission/**/*")
                .permitAll()
//                .antMatchers("/user/phone").authenticated()
                .antMatchers("/**/*")
                .authenticated().and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(authError)
                .accessDeniedHandler(authError);
//        http.cors().and().csrf().disable().authorizeRequests().antMatchers("/**/*").permitAll();
    }



    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    /**
     * allow cross origin requests
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE")
                        .allowedHeaders("*");
            }
        };
    }
}
