package org.beer.app;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmbeddedHttpServer {

	private static final transient Logger LOG = LoggerFactory.getLogger(EmbeddedHttpServer.class);

	private Server jettyServer;

	public void start() throws Exception {
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);
		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
				BeerRatingService.class.getCanonicalName());

		StopJettyHandler stopJettyHandler = new StopJettyHandler(this.jettyServer);
		HandlerCollection handlerCollection = new HandlerCollection();
		handlerCollection.setHandlers(new Handler[] { context, stopJettyHandler });

		this.jettyServer = new Server(8080);
		// this.jettyServer.setHandler(context);
		this.jettyServer.setStopAtShutdown(true);
		this.jettyServer.setHandler(handlerCollection);

		try {
			LOG.debug("Starting Jetty...");
			this.jettyServer.start();
			this.jettyServer.join();
		} finally {
			this.jettyServer.destroy();
		}
	}

	public void stop() {
		try {
			this.jettyServer.stop();
		} catch (Exception e) {
			LOG.error("Stop jetty failed, exception: ", e);
			this.jettyServer.destroy();
		}
	}
}
