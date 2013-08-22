package org.jboss.tools.switchyard.ui.bot.test.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 
 * @author apodhrad
 *
 */
public class HttpClient {

	private String url;

	public HttpClient(String url) {
		this.url = url;
	}

	public String send() throws MalformedURLException, IOException {
		return send(null);
	}

	public String send(String data) throws MalformedURLException, IOException {
		StringBuffer response = new StringBuffer();
		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
		con.setRequestMethod("POST");
		if (data != null) {
			con.setDoOutput(true);
			con.getOutputStream().write(data.getBytes());
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String line = null;
		while ((line = in.readLine()) != null) {
			response.append(line);
		}
		in.close();
		return response.toString();
	}
}
