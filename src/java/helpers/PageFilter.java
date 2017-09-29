/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Sindre
 */
@WebFilter("/faces/*")
public class PageFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        boolean isLoggedIn = session.getAttribute("user") != null;
        if (isLoggedIn) {
            if (request.getRequestURI().contains("/faces/loginPage.xhtml") || request.getRequestURI().contains("/faces/registerUser.xhtml")) {
                response.sendRedirect("userProfile.xhtml");
            }
        } else if(!isLoggedIn) {
            if (request.getRequestURI().contains("/faces/userProfile.xhtml")) {
                response.sendRedirect("loginPage");
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
