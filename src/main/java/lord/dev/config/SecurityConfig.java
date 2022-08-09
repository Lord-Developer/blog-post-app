package lord.dev.config;


import lord.dev.security.jwt.JWTokenEntryPoint;
import lord.dev.security.jwt.JWTokenFilter;
import lord.dev.sevice.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JWTokenFilter jwTokenFilter;
    private final JWTokenEntryPoint jwTokenEntryPoint;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, JWTokenFilter jwTokenFilter, JWTokenEntryPoint jwTokenEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.jwTokenFilter = jwTokenFilter;
        this.jwTokenEntryPoint = jwTokenEntryPoint;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(new JWTokenEntryPoint())
                .and()
                .addFilterBefore(jwTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/api/auth/verifyEmail").permitAll()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/swagger-ui.html**", "/swagger-resources/**",
                        "/v2/api-docs**", "/webjars/**"   , "/swagger-ui/**").permitAll()
                .anyRequest()
                .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
