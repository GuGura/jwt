package com.cos.jwt.config;

import com.cos.jwt.controller.filter.MyFilter1;
import com.cos.jwt.controller.filter.MyFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
내가 직접 설정파일로 만들어준 Filter에서
setOrder를 통해 낮은 번호를 줄수록 실행이 젤 빠르다는 것을 알게되었다.
이제 내가 만든 filter와 Security 체인에 연결시킨 필터 실행 순서를 비교해주자 SecurityConfig로 가봐라
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MyFilter1> filter1(){
        FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());
        bean.addUrlPatterns("/*"); //모든 요청에서 다해라
        bean.setOrder(0); //낮은 번호가 필터중 가장먼저 실행됨
        return bean;
    }
    @Bean
    public FilterRegistrationBean<MyFilter2> filter2(){
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/*"); //모든 요청에서 다해라
        bean.setOrder(1); //낮은 번호가 필터중 가장먼저 실행됨
        return bean;
    }
}
