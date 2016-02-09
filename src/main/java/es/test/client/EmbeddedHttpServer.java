package es.test.client;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class EmbeddedHttpServer {

	private Server jettyServer;

	public void start() throws Exception {
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		this.jettyServer = new Server(8080);
		this.jettyServer.setHandler(context);

		ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);
		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
				BeerRatingService.class.getCanonicalName());

		try {
			this.jettyServer.start();
			this.jettyServer.join();
		} finally {
			this.jettyServer.destroy();
		}
	}

	public void stop() throws Exception {
		this.jettyServer.stop();
	}

}
