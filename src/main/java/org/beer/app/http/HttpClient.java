package org.beer.app.http;

import java.io.IOException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
			HttpGet httpGet = new HttpGet(url);
			LOG.debug("HTTP GET line: " + httpGet.getRequestLine());
			LOG.debug("HTTP GET URI: " + httpGet.getURI());

			ResponseHandler<String> responseHandler = (HttpResponse response) -> {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? StringEscapeUtils.unescapeHtml4(EntityUtils.toString(entity)) : null;
				}
				throw new ClientProtocolException("Unexpected response status: " + status);
			};

			responseBody = client.execute(httpGet, responseHandler);

			LOG.debug("HTTP RESPONSE: " + responseBody);
		} catch (IOException e) {
			LOG.error("" + e);
		}
		return responseBody;
	}

	public String post(String url) {
		String responseBody = null;
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(url);
			LOG.debug("HTTP POST line: " + httpPost.getRequestLine());
			LOG.debug("HTTP POST URI: " + httpPost.getURI());

			ResponseHandler<String> responseHandler = (HttpResponse response) -> {
				logHeaders(response);
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null
							? StringEscapeUtils.unescapeHtml4(EntityUtils.toString(entity, "Windows-1252")) : null;
				}
				throw new ClientProtocolException("Unexpected response status: " + status);
			};
			responseBody = client.execute(httpPost, responseHandler);

			LOG.debug("HTTP RESPONSE: " + responseBody);
		} catch (IOException e) {
			LOG.error("" + e);
		}
		return responseBody;
	}

	private static void logHeaders(HttpResponse response) {
		if (LOG.isDebugEnabled()) {
			for (Header h : response.getAllHeaders()) {
				LOG.debug("Header: " + h);
			}
		}

	}
}
