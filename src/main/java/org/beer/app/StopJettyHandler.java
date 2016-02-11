package org.beer.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StopJettyHandler extends AbstractHandler {
	protected static final transient Logger LOG = LoggerFactory.getLogger(StopJettyHandler.class);
	protected Server server;

	public StopJettyHandler(Server server) {
		this.server = server;
	}

	private boolean stopServer(HttpServletResponse response) throws IOException {
		LOG.debug("Stopping Jetty...");
		response.setStatus(202);
		response.setContentType("text/plain");
		try (ServletOutputStream os = response.getOutputStream()) {
			os.println("Shutting down.");
		}
		response.flushBuffer();
		try {
			new Thread() {
				@Override
				public void run() {
					try {
						StopJettyHandler.this.server.stop();
						LOG.debug("Jetty stopped.");
					} catch (Exception e) {
						LOG.error("Stop Jetty failed, exception: ", e);
					}
				}
			}.start();
		} catch (Exception e) {
			LOG.error("Stop Jetty failed, exception: ", e);
			return false;
		}
		return true;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String pathInfo = request.getPathInfo();
		System.out.println(request.getRequestURI());
		System.out.println(pathInfo);
		if ("/stop".equals(pathInfo)) {
			stopServer(response);
			return;
		}
		// response.sendRedirect("http://ratebeer.com");
	}

}
