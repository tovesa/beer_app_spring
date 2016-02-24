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
	protected static final Logger LOG = LoggerFactory.getLogger(StopJettyHandler.class);
	protected Server server;

	public StopJettyHandler(Server server) {
		this.server = server;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String pathInfo = request.getPathInfo();
		LOG.debug("requestUri: " + request.getRequestURI());
		LOG.debug("pathInfo: " + pathInfo);
		if ("/stop".equals(pathInfo)) {
			stopServer(response);
			return;
		}
		// response.sendRedirect("http://ratebeer.com");
	}

	private boolean stopServer(HttpServletResponse response) throws IOException {
		LOG.debug("Stopping Jetty...");
		response.setStatus(202);
		response.setContentType("text/plain");
		try (ServletOutputStream os = response.getOutputStream()) {
			os.println("Shutting down.");
		} catch (IOException e) {
			LOG.error("Write response failed: " + e);
		}
		response.flushBuffer();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		LOG.error("Try to stop now...");
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

			// Runnable stopTask = () -> {
			// try {
			// StopJettyHandler.this.server.stop();
			// LOG.debug("Jetty stopped.");
			// } catch (Exception e) {
			// LOG.error("Stop Jetty failed, exception: ", e);
			// }
			// };
			//
			// try {
			// new Thread(stopTask).start();
		} catch (Exception e) {
			LOG.error("Stop Jetty failed, exception: ", e);
			return false;
		}
		return true;
	}
}
