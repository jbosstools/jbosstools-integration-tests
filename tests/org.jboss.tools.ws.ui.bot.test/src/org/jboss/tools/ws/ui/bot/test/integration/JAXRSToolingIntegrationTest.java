/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.tools.ws.reddeer.jaxrs.core.RESTfulWebServicesNode;
import org.jboss.tools.ws.reddeer.ui.tester.views.WsTesterView;
import org.jboss.tools.ws.reddeer.ui.tester.views.WsTesterView.RequestType;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.jboss.tools.ws.ui.bot.test.utils.ServersViewHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author jjankovi
 *
 */
@RunWith(RedDeerSuite.class)
@JBossServer(state=ServerRequirementState.RUNNING, cleanup=false)
@OpenPerspective(JavaEEPerspective.class)
public class JAXRSToolingIntegrationTest extends RESTfulTestBase {

	private static String projectName = "integration1"; 
	private String localhostUrl = "http://localhost:8080/";
	private String serviceUrl = localhostUrl + projectName + "/rest/"; 
	private WsTesterView wsTesterView;

	@Override
	public void setup() {
		if (!projectExists(getWsProjectName())) {
			importWSTestProject(getWsProjectName());
		}
		ServersViewHelper.runProjectOnServer(getWsProjectName());
		ServersViewHelper.waitForDeployment(getWsProjectName(), getConfiguredServerName());
		wsTesterView = new WsTesterView();
		wsTesterView.open();
	}
	
	@Override
	public void cleanup() {
		/* close web service tester */
		wsTesterView.close();
		ServersViewHelper.removeAllProjectsFromServer(getConfiguredServerName());
	}

	@Override
	protected String getWsProjectName() {
		return projectName;
	}

	@Test
	public void testGetMethod() {
		testRequestType(RequestType.GET);
	}

	@Test
	public void testPostMethod() {
		testRequestType(RequestType.POST);
	}

	@Test
	public void testPutMethod() {
		testRequestType(RequestType.PUT);
	}

	@Test
	public void testDeleteMethod() {
		testRequestType(RequestType.DELETE);
	}

	@Test
	public void testUnavailableServiceMethod() {
		restWebServicesNode = new RESTfulWebServicesNode(projectName);

		/* run on server - web service tester should be shown */
		runRestServiceOnServer(restWebServicesNode.getWebServiceByMethod("GET").get(0));
		wsTesterView.open();

		/* test generated url and response after invoking */
		assertEquals(serviceUrl + "get", wsTesterView.getServiceURL());

		invokeMethodInWSTester(wsTesterView, RequestType.POST);
		wsTesterView.activate();
		assertFalse(wsTesterView.getResponseBody().equals("GET method"));

		assertTrue("There is no header", wsTesterView.getResponseHeaders().length > 0);
		assertEquals("[HTTP/1.1 405 Method Not Allowed]", wsTesterView.getResponseHeaders()[0]);
	}
	
	private void testRequestType(RequestType type) {
		restWebServicesNode = new RESTfulWebServicesNode(projectName);

		/* run on server - web service tester should be shown */
		runRestServiceOnServer(type.name());
		
		//workaround for RedDeer that won't allow to close view until it the open() method was called
		wsTesterView.open();

		/* test generated url and response after invoking */
		assertEquals(serviceUrl + type.name().toLowerCase(), wsTesterView.getServiceURL());
		
		invokeMethodInWSTester(wsTesterView, type);
		assertEquals(type.name() + " method", wsTesterView.getResponseBody());
		assertEquals("[HTTP/1.1 200 OK]", wsTesterView.getResponseHeaders()[0]);
	}

}
