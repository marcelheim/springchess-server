package m.heim.server.config;

import m.heim.server.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    TokenService tokenService;

    @Autowired
    public SecurityConfig(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().disable();
        http
                .authorizeRequests()
                .antMatchers("/", "/api/login/*", "/h2-console/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(new JWTAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);

    }
}
