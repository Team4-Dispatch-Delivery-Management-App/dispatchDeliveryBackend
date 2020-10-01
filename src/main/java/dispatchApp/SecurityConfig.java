package dispatchApp;


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
	// 连到已经创建好的db去
	@Autowired
	private DataSource dataSource;

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//在内存中看有没有能匹配的用户，有的话负值为admin
		auth.inMemoryAuthentication().withUser("zhangxueqian96@gmail.com").password("123").authorities("ROLE_ADMIN");

		// inmeory没找到回去数据库找， get user user name & authentication
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("SELECT email, password, status FROM account WHERE email=?") // 得到&check用户输入密码和数据库里的是否匹配
				.authoritiesByUsernameQuery("SELECT emailId, authorities FROM authorities WHERE emailId=?"); //看在数据库里用户有什么权限
		

	}

	// 哪些url有哪些权限
	protected void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable()
    	    .formLogin()
    	         .loginPage("/Login")
    	    .and()
    	    .authorizeRequests()
    	    //.antMatchers("/order/**").hasAuthority("ROLE_USER") // 访问order必须是登录的用户 // 和前端确认// history 也要确认
    	    //.antMatchers("/admin*/**").hasAuthority("ROLE_ADMIN")
    	    .anyRequest().permitAll()
    	    .and()
    	    .logout()
    	       .logoutUrl("/logout");
    }
	
	// encode / decode password (To Do)
	@SuppressWarnings("deprecation")
	@Bean
	// 暂时密码是明文
	public static NoOpPasswordEncoder passwordEncoder() {
		return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}


}
