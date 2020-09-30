import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.context.annotation.Bean;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource;
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("email@gmail.com").password("12345").authorities("ROLE_ADMIN");
		auth.jdbcAuthentication()
		    .dataSource(dataSource)
		    .usersByUsernameQuery("SELECT email, password, status FROM account WHERE email=?")// check password
		    .authoritiesByUsernameQuery("SELECT email, type FROM account WHERE email=?");	
	}
    
    protected void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable()
    	    .formLogin()
    	         .loginPage("/Login")
    	    .and()
    	    .authorizeRequests()
    	    .antMatchers("").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")// 这里页面的权限需要和前端确认
    	    .antMatchers("/admin*/**").hasAuthority("ROLE_ADMIN")
    	    .anyRequest().permitAll()
    	    .and()
    	    .logout()
    	       .logoutUrl("/logout");
    }
    @SuppressWarnings("deprecation")
   	@Bean
   	// 暂时密码是明文
   	public static NoOpPasswordEncoder passwordEncoder() {
   		return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
   	}



}
