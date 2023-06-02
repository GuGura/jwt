package com.cos.jwt.config;

import com.cos.jwt.config.jwt.JwtAuthenticationFilter;
import com.cos.jwt.config.jwt.JwtAuthorizationFilter;
import com.cos.jwt.controller.filter.MyFilter1;
import com.cos.jwt.controller.filter.MyFilter3;
import com.cos.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final UserRepository userRepository;

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity>{
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsFilter) //@CrossOrigin(인증X), 시큐리티 필터에 등록 인증(O)
                    .addFilter(new JwtAuthenticationFilter(authenticationManager)) //AuthenticationManager
                    .addFilter(new JwtAuthorizationFilter(authenticationManager,userRepository));
        }
    }

    //Security를 쓰고는 있는데 Security Session을 안쓰니까 모든 페이지에 접근이 가능해짐
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
/*                .addFilterAfter(new MyFilter3(),BasicAuthenticationFilter.class)
                  FilterConfig의 주석과 이어진다. 내가 직접 설정해준 Filter와 Security의 BasicAuthenticationFilter 앞뒤에 filter3를 넣어서 실행순서를 비교해보면
                  addFilterAfter/Before 둘다 FilterConfig에서 만든 기본적인 필터보다 빠르게 실행된다는 것을 볼 수 있다.
                  고로 내가 만들 필터가 Spring Security보다 먼저 실행되게 하고 싶으면 여기서 설정해야한다는 것이다.
                  그리고 인터넷에서 시큐리티 filter에 대해 검색해봐라. 그럼 */
//                .addFilterBefore(new MyFilter3(), SecurityContextHolderFilter.class)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 사용하지않고 STATELESS 서버로 만들겠다.
                .and()
                .formLogin().disable() //일반적인 로그인 안쓰겠다.
                // Basic는 ID와 PW를 담고 Bearer는 토큰을 담는 방식이다.
                .httpBasic().disable() //기본적인 http 로그인방식 안쓰겠다.
                .apply(new MyCustomDsl())
                //--------------------------------jwt 쓸려면 위에 고정
                .and()
                //Security 처리에 HttpServletRequest 를 이용하겠다는 의미
                .authorizeRequests()
                // /api/v1/?/**에는 해당 권한이 있는 사람만 들어올 수 있게 권한을 걸겠다.
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .and().build();
    }
}
