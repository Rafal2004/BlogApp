package com.learningBlog.blog;

        import jakarta.servlet.http.HttpServletRequest;
        import jakarta.servlet.http.HttpServletResponse;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
        import org.springframework.security.config.annotation.web.builders.HttpSecurity;
        import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
        import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
        import org.springframework.security.core.session.SessionRegistry;
        import org.springframework.security.core.session.SessionRegistryImpl;
        import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
        import org.springframework.security.crypto.password.PasswordEncoder;
        import org.springframework.security.web.SecurityFilterChain;
        import org.springframework.security.web.header.HeaderWriter;
        import org.springframework.security.web.header.writers.DelegatingRequestMatcherHeaderWriter;
        import org.springframework.security.web.header.writers.StaticHeadersWriter;
        import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
        import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
        import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
        import org.springframework.security.web.util.matcher.RequestMatcher;
        import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


        import java.util.Arrays;
        import java.util.List;

        import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
        import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }




    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,  HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers(mvcMatcherBuilder.pattern("/")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/register")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/error")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/register/save")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/user/all")).hasAnyRole("MODERATOR", "ADMIN")
                                .requestMatchers(mvcMatcherBuilder.pattern("/add-post")).hasRole("ADMIN")
                                .requestMatchers(mvcMatcherBuilder.pattern("/show-post/**")).hasAnyRole("USER","MODERATOR", "ADMIN")
                                .requestMatchers(mvcMatcherBuilder.pattern("/delete-post/**")).hasAnyRole("MODERATOR", "ADMIN")
                                .requestMatchers(mvcMatcherBuilder.pattern("/update-post/**")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/add-comment/**")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/delete-comment/**")).hasAnyRole("USER","MODERATOR", "ADMIN")
                                .requestMatchers(mvcMatcherBuilder.pattern("/user/**")).hasAnyRole("USER","MODERATOR", "ADMIN")
                             .requestMatchers(mvcMatcherBuilder.pattern("/save-post")).hasAnyRole("USER","MODERATOR", "ADMIN")
                                .requestMatchers(mvcMatcherBuilder.pattern("/delete-saved-post")).hasAnyRole("USER","MODERATOR", "ADMIN")
                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/")
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .logoutSuccessUrl("/login")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .permitAll()
                );


        return http.build();
    }

}
