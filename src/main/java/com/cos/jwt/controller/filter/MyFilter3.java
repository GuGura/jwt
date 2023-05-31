package com.cos.jwt.controller.filter;

import com.cos.jwt.config.jwt.JwtProperties;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
//시큐리티가 동작하기전에 걸러내야한다.
public class MyFilter3 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

//        req.setCharacterEncoding("UTF-8");
        // 토큰: cos 이걸 만들어줘야함. id,pw 정상적으로 들어와서 로그인이 완료 되면 토큰을 만들어주고 그걸 응답을 해준다.
        // 요청할 때 마다 header에 Authorization에 value값으로 토큰을 가지고 오겠죠?
        // 그때 토큰이 넘어오면 이 토큰이 내가 만든 토큰이 맞는지만 검증만 하면 됨. (RSA, HS256)
        if(req.getMethod().equals("POST")){
//            System.out.println("POST 요청됨");
            String headerAuth = req.getHeader(JwtProperties.HEADER_STRING);
//            System.out.println(headerAuth);
//            System.out.println("필터3");

            if(headerAuth.equals(JwtProperties.SECRET)){
                chain.doFilter(req,res); //체인에 넘겨주지않고 쌩으로 쓰면 쌩으로 쓴 코드가 끝나면 프로그램이 끝난다.
            }else{
                PrintWriter out = res.getWriter();
                out.println("인증안됨");
                //chain을 안이어줘서 그 이후 필터는 실행되지않는다.
            }
        }
    }
}
