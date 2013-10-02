package org.jboss.tools.modeshape.reddeer.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.internal.preferences.Base64;

/**
 * This class represents a user interface through Modeshape WebDav server.
 * 
 * @author apodhrad
 * 
 */
public class ModeshapeWebdav {

	public static final String HOST = "http://localhost:8080/modeshape-webdav";
	public static final String REPOSITORY = "repository";
	public static final String WORKSPACE = "default";
	public static final String PUBLISH_AREA = "/files";

	private String host;
	private String repository;
	private String workspace;
	private String publishArea;

	public ModeshapeWebdav() {
		this(HOST, REPOSITORY, WORKSPACE, PUBLISH_AREA);
	}

	public ModeshapeWebdav(String repository) {
		this(HOST, repository, WORKSPACE, PUBLISH_AREA);
	}
	
	public ModeshapeWebdav(String repository, String publishArea) {
		this(HOST, repository, WORKSPACE, publishArea);
	}

	public ModeshapeWebdav(String host, String repository, String workspace, String publishArea) {
		this.host = host;
		this.repository = repository;
		this.workspace = workspace;
		this.publishArea = publishArea;
	}

	public String getHost() {
		return host;
	}

	public String getRepository() {
		return repository;
	}

	public String getWorkspace() {
		return workspace;
	}

	public String getPublishArea() {
		return publishArea;
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

	public boolean isFileAvailable(String path, String user, String password) throws IOException {
		URL url = computeUrl(path);
		return getConnection(url, user, password).getResponseCode() == HttpURLConnection.HTTP_OK;
	}

	private HttpURLConnection getConnection(URL url, String user, String password) throws IOException {
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

	public URL computeUrl(String path) throws MalformedURLException {
		return new URL(host + "/" + repository + "/" + workspace + "/" + publishArea + "/" + path);
	}
}
