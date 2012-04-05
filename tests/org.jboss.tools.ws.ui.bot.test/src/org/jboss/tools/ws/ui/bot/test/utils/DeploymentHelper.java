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

import static org.eclipse.swtbot.swt.finder.SWTBotAssert.assertContains;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Logger;

import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTOpenExt;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.view.ProjectExplorer;

public class DeploymentHelper {

	private final Logger LOGGER = Logger
			.getLogger(DeploymentHelper.class.getName());

	private final SWTBotExt bot = new SWTBotExt();
	
	private final ProjectExplorer projectExplorer = new ProjectExplorer();
	
	private final SWTOpenExt open = new SWTOpenExt(bot);
	
	/**
	 * Method runs project on configured server
	 * @param project
	 */
	public void runProject(String project) {
		open.viewOpen(ActionItem.View.ServerServers.LABEL);
		projectExplorer.runOnServer(project);
	}
	
	/**
	 * Method checks if service is deployed by checking http header code
	 * response of entered wsdURL
	 * @param wsdlURL
	 */
	public void assertServiceDeployed(String wsdlURL) {
		assertServiceDeployed(wsdlURL, 5000);		
	}

	/**
	 * Method checks if service is deployed by checking http header code
	 * response of entered wsdURL and timeout for this operation
	 * @param wsdlURL
	 * @param timeout
	 */
	public void assertServiceDeployed(String wsdlURL, long timeout) {
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
	 * Method checks if service is not deployed by checking http header code
	 * response of entered wsdURL
	 * @param wsdlURL
	 */
	public void assertServiceNotDeployed(String wsdlURL) {
		HttpURLConnection connection = null;
		try {
			URL u = new URL(wsdlURL);
			connection = (HttpURLConnection) u.openConnection();
			assertEquals("Project was not sucessfully undeployed, WSDL '"
					+ wsdlURL + "' is still available",
					HttpURLConnection.HTTP_NOT_FOUND,
					connection.getResponseCode());
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

	/**
	 * Method checks if service is not deployed by checking http header code
	 * response of entered wsdURL and timeout for this operation
	 * @param startServlet
	 * @param response
	 */
	public void assertServiceResponseToClient(String startServlet,
			String response) {
		assertContains(response, getPage(startServlet, 15000));
	}
	
	/**
	 * Method gets http page for entered url of page and timeout for
	 * this operation
	 * @param url
	 * @param timeout
	 * @return
	 */
	public String getPage(String url, long timeout) {
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
					page = new Scanner(connection.getInputStream())
							.useDelimiter("\\A").next();
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
		assertEquals("cannot connect to '" + url + "'",
				HttpURLConnection.HTTP_OK, rsp);
		return page;
	}
	
	/**
	 * Method gets wsdl determined by deployed project and web service name 
	 * @param projectName
	 * @param wsName
	 * @return
	 */
	public String getWSDLUrl(String projectName, String wsName) {
		return "http://localhost:8080/" + projectName + "/"
				+ wsName + "?wsdl";
	}
	
}
