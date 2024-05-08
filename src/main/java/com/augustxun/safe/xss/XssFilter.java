package com.augustxun.safe.xss;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "xssFilter", urlPatterns = "/*")
public class XssFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String enctype = request.getContentType();
        String requestURI = request.getRequestURI();


        //todo 通过配置指定url来过滤不经过xss过滤

        /**
         * 如果请求为上传附件的话，则不用XssHttpServletRequestWrapper来包
         */
        if (StrUtil.isNotBlank(enctype) && enctype.contains("multipart/form-data")) {
            filterChain.doFilter(request, servletResponse);
        } else {
            try {
                XssHttpServletRequestWrapper xssRequestWapper = new XssHttpServletRequestWrapper(request);
                filterChain.doFilter(xssRequestWapper, servletResponse);
            } catch (Exception ex) {
                log.error("xssWrap包装过滤处理异常:{}", ex);
            }

        }

    }

    @Override
    public void destroy() {
    }
}