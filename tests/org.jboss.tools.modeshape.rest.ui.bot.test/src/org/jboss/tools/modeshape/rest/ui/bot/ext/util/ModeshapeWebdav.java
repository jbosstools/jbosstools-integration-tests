package org.jboss.tools.modeshape.rest.ui.bot.ext.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.internal.preferences.Base64;

/**
 * 
 * This class represents a user interface through Modeshape WebDav server.
 * 
 * @author apodhrad
 * 
 */
public class ModeshapeWebdav {

	public static final String DEFAULT_HOST = "http://localhost:8080/modeshape-webdav";

	private String host;

	public ModeshapeWebdav() {
		this(DEFAULT_HOST);
	}

	public ModeshapeWebdav(String host) {
		this.host = host;
	}

	public String getHost() {
		return host;
	}

	public InputStream getInputStream(URL url, String user, String password) throws IOException {
		return getConnection(url, user, password).getInputStream();
	}

	public String getTextOutput(URL url, String user, String password) throws IOException {
		InputStream input = getInputStream(url, user, password);
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		StringBuffer output = new StringBuffer();
		String line = null;
		while ((line = reader.readLine()) != null) {
			output.append(line);
		}
		return output.toString();
	}

	public boolean isFileAvailable(URL url, String user, String password) throws IOException {
		return getConnection(url, user, password).getResponseCode() == HttpURLConnection.HTTP_OK;
	}

	private HttpURLConnection getConnection(URL url, String user, String password)
			throws IOException {
		String authorization = user + ":" + password;
		String encodedAuthorization = new String(Base64.encode(authorization.getBytes()));

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setAllowUserInteraction(false);
		connection.setUseCaches(false);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
		connection.connect();

		return connection;
	}

	public URL computeUrl(String repository, String workspace, String publishArea, String path)
			throws MalformedURLException {
		return new URL(host + "/" + repository + "/" + workspace + "/" + publishArea + "/" + path);
	}
}
