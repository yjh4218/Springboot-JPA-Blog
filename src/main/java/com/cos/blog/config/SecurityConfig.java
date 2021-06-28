package com.cos.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.blog.config.auth.PrincipalDetailService;

// 빈 등록 : 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것
@Configuration //빈등록(IOC관리)
@EnableWebSecurity	// 시큐리티 필터 추가 = 스프링 시큐리티가 활성화가 되어 있는데 어떤 설정을 해당 파일에서 하겠다. 시큐리티 필터가 등록이 됨
@EnableGlobalMethodSecurity(prePostEnabled = true)//특정 주소로 접근을 하면 권한 및 인증을 미리 체크하겠다는 뜻
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	//로그인 처리할때 아이디 처리
	@Autowired
	private PrincipalDetailService principalDetailService;
	
	
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManager();
	}

	@Bean //loc가 되요!(함수가 리턴하는 값을 스프링이 관리함
	public BCryptPasswordEncoder encodePWD() {
		return new BCryptPasswordEncoder();
		
	}
	
	//시큐리티가 대신 로그인해주는데, password 가로채기를 진행함
	//해당 password가 어떤 해쉬로 되어 회원가입이 되었는지 알아야
	//같은 해쉬로 암호화해서 DB에 있는 해쉬와 비교할 수 있음
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//PrincipalDetailService의 loadUserByUsername 리턴 온 데이터를 사용자가 적은 암호를 해쉬로 변경하여 db 데이터와 비교
		//비밀번호 확인 되면 PrincipalDetail로 감싸져서 스프링 시큐리티 영역에 저장됨
		auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
	}
	
	
	//접속시 /auth 경로는 모두 접근 가능 그 이외는 인증 필요함
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable() //csrf 토큰 비활성화(테스트시 걸어두는게 좋음). 시큐리티는 csrf 토큰이 없으면 접근을 막음
			.authorizeRequests()
				.antMatchers("/", "/auth/**","/js/**", "/css/**", "/image/**", "/dummy/**")
				.permitAll()
				.anyRequest()
				.authenticated()
			.and()
				.formLogin()
				.loginPage("/auth/loginForm") //인증이 필요한 모든 요청은 loginForm으로 진행
				.loginProcessingUrl("/auth/loginProc") //스프링 시큐리티가 해당 주소로 요청오는 로그인을 가로채서 대신 로그인 진행.
				//로그인을 가로채서 PrincipalDetailService의 loadUserByUsername로 전달함.
				.defaultSuccessUrl("/"); //정상적으로 로그인시 홈페이지 이동
	}

}
