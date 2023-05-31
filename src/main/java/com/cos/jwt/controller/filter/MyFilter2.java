package com.cos.jwt.controller.filter;

import javax.servlet.*;
import java.io.IOException;

public class MyFilter2 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터2");
        chain.doFilter(request,response); //체인에 넘겨주지않고 쌩으로 쓰면 쌩으로 쓴 코드가 끝나면 프로그램이 끝난다.
    }
}
