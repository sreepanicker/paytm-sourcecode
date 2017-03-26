/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sree;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sreejithu.panicker
 */
public class JspFilter implements Filter {
    FilterConfig filterConfig = null;
    public JspFilter() {
    }    
   
    public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain)
            throws IOException, ServletException {

            HttpServletRequest req = (HttpServletRequest) request;
            String requestURI = req.getRequestURI();
            if (requestURI.contains("profiledetail.jsp") ){
                 request.getRequestDispatcher("/login.jsp").forward(request, response);
            }else{
                chain.doFilter(request, response); 
            }
    }

    @Override
    public void destroy() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    	
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
	this.filterConfig = filterConfig;
    }


    
}
