/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.utils;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * DeploymentHelper
 * 
 * @author jjankovi
 * @author Radoslav Rabara
 *
 */
public class DeploymentHelper {

	private static final Logger LOGGER = Logger
			.getLogger(DeploymentHelper.class.getName());

	private DeploymentHelper() {};

	/**
	 * Method checks if service is deployed by checking http header code
	 * response of entered wsdURL and timeout for this operation
	 * @param wsdlURL
	 * @param timeout
	 */
	public static void assertServiceDeployed(String wsdlURL, long timeout) {
		long t = System.currentTimeMillis();
		int rsp = -1;
		while (t + timeout > System.currentTimeMillis()) {
			HttpURLConnection connection = null;
			try {
				URL u = new URL(wsdlURL);
				connection = (HttpURLConnection) u.openConnection();
				rsp = connection.getResponseCode();
				if (rsp == HttpURLConnection.HTTP_OK) {
					break;
				} else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// ignore
					}
					LOGGER.info("retrying...");
				}
			} catch (MalformedURLException e1) {
				throw new RuntimeException(e1);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
			}
		}
		LOGGER.info("done after: " + (System.currentTimeMillis() - t) + "ms.");
		assertEquals("Service was not sucessfully deployed, WSDL '" + wsdlURL
				+ "' was not found", HttpURLConnection.HTTP_OK, rsp);
	}
	
	/**
	 * Returns http page for entered <var>url</var> of page in the specified
	 * <var>timeout</var>
	 *
	 * @param url page url
	 * @param timeout timeout of this operation
	 * @return content of the http page
	 */
	public static String getPage(String url, long timeout) {
		long t = System.currentTimeMillis();
		int rsp = -1;
		String page = null;
		while (t + timeout > System.currentTimeMillis()) {
			HttpURLConnection connection = null;
			try {
				URL u = new URL(url);
				connection = (HttpURLConnection) u.openConnection();
				rsp = connection.getResponseCode();
				if (rsp == HttpURLConnection.HTTP_OK) {
					try(Scanner scanner = new Scanner(connection.getInputStream())) {
						page = scanner.useDelimiter("\\A").next();
					}
					break;
				} else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// ignore
					}
					LOGGER.info("retrying...");
				}
			} catch (MalformedURLException e1) {
				throw new RuntimeException(e1);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
			}
		}
		LOGGER.info("done after: " + (System.currentTimeMillis() - t) + "ms.");
		assertEquals("Cannot connect to '" + url + "'",
				HttpURLConnection.HTTP_OK, rsp);
		return page;
	}

	/**
	 * Returns wsdl URL determined by deployed project and web service name
	 * 
	 * @param projectName name of the deployed project
	 * @param wsName web service name
	 * @return wsdl URL
	 */
	public static String getWSDLUrl(String projectName, String wsName) {
		return "http://localhost:8080/" + projectName + "/"
				+ wsName + "?wsdl";
	}
}
