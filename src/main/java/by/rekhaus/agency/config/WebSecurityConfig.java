package by.rekhaus.agency.config;
//Класс WebSecurityConfig используется для конфигурации защиты для приложения.
// Он аннотирован (annotate) с помощью @Configuration, Данная аннотация говорит
// Spring что он является классом конфигурации, поэтому он будет анализирован с
// помощью Spring во время запуска данного приложения.

import javax.sql.DataSource;


import by.rekhaus.agency.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {


        // Настройка сервиса для поиска пользователя в базе данных.
        // И Установка Кодировщика Паролей

        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        // Страницы не требуют входа в систему
        http.authorizeRequests().antMatchers("/registration", "/login", "/logout").permitAll();

        // /На странице userInfo требуется войти в систему как ROLE_USER или ROLE_ADMIN.
        // Если входа в систему нет, он будет перенаправлен на страницу /входа в систему.
        http.authorizeRequests().antMatchers("/userInfo", "/price").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')");

        // Только для админа
        http.authorizeRequests().antMatchers("/admin", "/blog/add", "/blog/{id}/edit").access("hasRole('ROLE_ADMIN')");
        http.authorizeRequests().antMatchers( "/blog/{id}/delete" ).access("hasRole('ROLE_ADMIN')");
        // Когда пользователь вошел в систему под именем XX.
        // Но при доступе к странице, для которой требуется роль YY,
        // будет вызвано исключение AccessDeniedException
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

        // Конфигурация для формы входа в систему
        http.authorizeRequests().and().formLogin()
                // Отправь URL-адрес страницы входа в систему.
                .loginProcessingUrl("/j_spring_security_check") // Submit URL
                .loginPage("/login")//
                .defaultSuccessUrl("/userAccountInfo")//
                .failureUrl("/login?error=true")//
                .usernameParameter("username")//
                .passwordParameter("password")
                // Конфигурация для страницы выхода из системы
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/logoutSuccessful");

        // Config Remember Me.
        http.authorizeRequests().and() //
                .rememberMe().tokenRepository(this.persistentTokenRepository()) //
                .tokenValiditySeconds(1 * 24 * 60 * 60); // 24h

    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }


}
