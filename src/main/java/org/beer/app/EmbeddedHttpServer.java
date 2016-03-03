package org.beer.app;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.elasticsearch.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmbeddedHttpServer {

	private static final Logger LOG = LoggerFactory.getLogger(EmbeddedHttpServer.class);
	private Server jettyServer;
	private ServerConnector connector;

	public void start() throws Exception {
		HandlerList handlers = registerServlets();
		initServer(handlers);
		startServer();
	}

	private HandlerList registerServlets() throws URISyntaxException, MalformedURLException {

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		ClassLoader cl = EmbeddedHttpServer.class.getClassLoader();
		URL f = cl.getResource("static-content/index.html");
		if (f == null) {
			throw new ResourceNotFoundException("Directory static-content not found.");
		}

		try {
			URI webRootUri = f.toURI().resolve("./").normalize();
			context.setBaseResource(Resource.newResource(webRootUri));
		} catch (URISyntaxException e) {
			LOG.error("Resolve web root URI failed: " + e);
			throw e;
		} catch (MalformedURLException e) {
			LOG.error("Set base resource failed: " + e);
			throw e;
		}

		ServletHolder defaultServlet = new ServletHolder("default", DefaultServlet.class);
		defaultServlet.setInitParameter("dirAllowed", "true");
		context.addServlet(defaultServlet, "/");

		ServletContextHandler wsContext = new ServletContextHandler();
		wsContext.setContextPath("/ws");
		ServletHolder jerseyServlet = wsContext.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);
		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
				BeerRatingService.class.getCanonicalName());

		StopJettyHandler stopJettyHandler = new StopJettyHandler(this.jettyServer);

		HandlerList handlerList = new HandlerList();
		handlerList.addHandler(context);
		handlerList.addHandler(wsContext);
		handlerList.addHandler(stopJettyHandler);

		return handlerList;

	}

	private void initServer(HandlerList handlerList) {
		this.jettyServer = new Server();
		this.jettyServer.setStopAtShutdown(true);
		this.jettyServer.setHandler(handlerList);
		this.connector = new ServerConnector(this.jettyServer);
		this.connector.setPort(8080);
		this.jettyServer.addConnector(this.connector);
	}

	private void startServer() throws Exception {
		try {
			LOG.debug("Starting Jetty...");
			this.jettyServer.start();
			this.jettyServer.join();
		} finally {
			this.connector.close();
			this.jettyServer.destroy();
		}
	}
}
