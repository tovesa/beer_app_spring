package org.beer.app;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClient {
	private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);
	private static final HttpClient INSTANCE = new HttpClient();

	public static HttpClient getInstance() {
		return INSTANCE;
	}

	public String get(String url) {
		String responseBody = null;
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpGet httpget = new HttpGet(url);
			LOG.debug("HTTP GET: " + httpget.getRequestLine());

			ResponseHandler<String> responseHandler = (HttpResponse response) -> {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				}
				throw new ClientProtocolException("Unexpected response status: " + status);
			};

			responseBody = client.execute(httpget, responseHandler);

			LOG.debug("HTTP RESPONSE: " + responseBody);
		} catch (ClientProtocolException e) {
			LOG.debug("ClientProtocolException: " + e.getMessage());
		} catch (IOException e) {
			LOG.debug("IOException: " + e.getMessage());
		}
		return responseBody;
	}
}
