package org.telran.forum.security.service;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
public class AuthorizationConfiguration extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic();
		http.csrf().disable();//crosside request surgery - когда отпарвляется запрос с формой, в параметрах возвращается токен что с этого сайта идет все норм
		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS); //првоеряем авторизацию только по заголовку, а не по сейшн id. То есть запрещаем куки 
		http.authorizeRequests()
			.antMatchers("/account/register/**").permitAll() //проверить, что там всякую ерунду можно написать //можно не только эндпоинты но и методы
			.antMatchers("/forum/posts/**").permitAll()
			.anyRequest().authenticated();
		
	}
}
