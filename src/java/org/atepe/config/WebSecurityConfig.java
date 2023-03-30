package org.atepe.config;

import org.atepe.service.UsuarioDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;




@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

@Autowired	
private BCryptPasswordEncoder bEncoder;	

@Autowired
private UsuarioDetailService userService;



@Bean
 public BCryptPasswordEncoder passwordEncoder(){
	   BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	   return bCryptPasswordEncoder;
}


@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
  auth.userDetailsService(userService).passwordEncoder(bEncoder);
}




	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http.authorizeRequests()
		   .anyRequest().authenticated()
	.and().formLogin().loginPage("/login").permitAll()
	.and()
		 .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		 .permitAll()
			.and()
			.rememberMe()
			.userDetailsService(userService);
	//.anyRequest()
	//.authenticated()


	//loginPage("/login").permitAll();
	//	.and()
//	.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
			
		
	/*	http.
		authorizeRequests()
			.antMatchers("/resources/**").permitAll()
			.anyRequest()
			.authenticated()
		.and()
		.formLogin()
			.loginPage("/login")
			.permitAll()
		.and()
		.logout()
			.logoutSuccessUrl("/login?logout")
			.permitAll()
		.and()
		.rememberMe()
			.userDetailsService(userService);
    
	 http.csrf().disable().authorizeRequests()
		     .antMatchers(HttpMethod.GET,"/").permitAll()
		     .antMatchers(HttpMethod.POST,"/").permitAll()
			    		    .anyRequest()
				.authenticated()
			.and()
     	     	.formLogin()
			     .loginPage("/login")
				.permitAll()
			.and()
			.logout()				
			.logoutRequestMatcher(new AntPathRequestMatcher("/login?logout"));
			 .permitAll()
			.and()
			.rememberMe()
			.userDetailsService(userService); */
		
	}
	
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	web.ignoring().antMatchers("/materialize/**","/style/**", "/js/**","/report/**");
		
	}


}