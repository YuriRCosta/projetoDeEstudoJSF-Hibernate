package br.com.filter;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import br.com.entity.Person;
import br.com.jpautil.JPAUtil;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = {"/*"})
public class FilterAuthentication implements Filter, Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private JPAUtil jpaUtil;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		jpaUtil.getEntityManager();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		
		Person userLogged = (Person) session.getAttribute("userLogged");
		
		String url = req.getServletPath();
		
		if(!url.equalsIgnoreCase("index.jsf") && userLogged == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsf");
			dispatcher.forward(request, response);
			return;
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		
	}

	
	
}
