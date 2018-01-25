package com.zero.zeros.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//import com.cloud.easyPrice.common.service.UserService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	/*
	@Autowired 
	public UserService userService; 
	*/
	@Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Override
	public void configure(WebSecurity web) throws Exception{
		//인증 open (주로 리소스)
		//web.ignoring().antMatchers("/resources/**");
		
		//ex) 모든 경로에 대한 접속 open
		//web.ignoring().antMatchers("/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		//리소스외에 페이지의 인증/비인증/인증권한등을 설정
		http
			.authorizeRequests()
			//전체 URL 인증 받도록 설정 (메인/로그인 제외)
			.antMatchers("/", "/userRegist", "/resources/**", "/h2-console/**").permitAll()
			.anyRequest().authenticated()
			//.antMatchers("/**").authenticated()
		
		    // admin 페이지가 별도로 있을 경우 권한에 따른 화면 오픈
		    //.antMatchers("/admin/**").hasAuthority("ADMIN")
			
		    // ex) 모두 open 및 특정 URL에 대한 security 적용 /** 보다 앞에 있어야함
			//.antMatchers("/test").authenticated()
			//.antMatchers("/**").permitAll();
			
		.and()
		    //h2 콘솔 접속 설정 :  disable CRSF, disable X-Frame-Options
			.csrf().disable()
		    .headers().frameOptions().disable()
		
		.and()
			// 로그인 페이지 : 컨트롤러 매핑을 하지 않으면 기본 제공되는 로그인 페이지 호출
			.formLogin()
			.loginPage("/userLogin")
			.permitAll()
			
		.and()
	        .logout()
	        .logoutUrl("/logout")
	        .logoutSuccessUrl("/userLogin");
	}
	
	/*
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
    }
    */
	
}
