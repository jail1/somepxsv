package ro.activemall.photoxserver.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class SimpleCORSFilter implements Filter {
	private static Logger log = Logger.getLogger(SimpleCORSFilter.class);

	@SuppressWarnings("unused")
	private void dummy() {
		log.info("Dummy");
	}

	@Override
	public void destroy() {
		// log.info("Doing DESTROY CORS filter");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse result = (HttpServletResponse) response;
		result.setHeader("Access-Control-Allow-Origin", "*");
		result.setHeader("Access-Control-Allow-Methods",
				"POST, GET, OPTIONS, DELETE");
		result.setHeader("Access-Control-Max-Age", "3600");
		result.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// log.info("Doing INIT CORS filter");
	}

}
