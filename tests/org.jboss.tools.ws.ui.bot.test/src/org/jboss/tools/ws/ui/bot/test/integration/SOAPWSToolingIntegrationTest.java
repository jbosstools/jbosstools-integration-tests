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

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.autobuilding.AutoBuildingRequirement.AutoBuilding;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.ws.reddeer.swt.condition.WsTesterNotEmptyResponseText;
import org.jboss.tools.ws.reddeer.ui.tester.views.WsTesterView;
import org.jboss.tools.ws.reddeer.ui.tester.views.WsTesterView.RequestType;
import org.jboss.tools.ws.ui.bot.test.soap.SOAPTestBase;
import org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper;
import org.jboss.tools.ws.ui.bot.test.utils.ServersViewHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class for testing SOAP integration in Web Service Tester
 * 
 * @author jjankovi
 *
 */
@RunWith(RedDeerSuite.class)
@AutoBuilding(value = false, cleanup = true)
public class SOAPWSToolingIntegrationTest extends SOAPTestBase {

	private final String request = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?> " + LINE_SEPARATOR
			+ "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" "
			+ "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
			+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> " + LINE_SEPARATOR + "<soap:Header> "
			+ LINE_SEPARATOR + "</soap:Header>" + LINE_SEPARATOR + "<soap:Body> " + LINE_SEPARATOR
			+ "<webs:sayHello xmlns:webs=\"http://webservices.samples.jboss.org/\">" + LINE_SEPARATOR
			+ "<arg0>User</arg0>" + LINE_SEPARATOR + "</webs:sayHello>" + LINE_SEPARATOR + "</soap:Body>"
			+ LINE_SEPARATOR + "</soap:Envelope>";

	@Override
	public void setup() {
		if (!ProjectHelper.projectExists(getWsProjectName())) {
			ProjectHelper.importWSTestProject(getWsProjectName(), getConfiguredRuntimeName());
			ProjectHelper.cleanAllProjects();
			ServersViewHelper.runProjectOnServer(getWsProjectName());
			ServersViewHelper.waitForDeployment(getWsProjectName(), getConfiguredServerName());
		}
	}

	@Override
	protected String getWsProjectName() {
		return "integration2";
	}

	/**
	 * Fails due to JBDS-3907
	 * 
	 * @see https://issues.jboss.org/browse/JBDS-3907
	 */
	@Test
	public void testSimpleIntegration() {
		WsTesterView wsTesterView = openWSDLFileInWSTester();
		testWSDLInWSTester(wsTesterView);
	}

	private WsTesterView openWSDLFileInWSTester() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.activate();
		Project project = projectExplorer.getProject(getWsProjectName());
		project.refresh();
		project.getProjectItem("wsdl", "HelloWorldService.wsdl").select();
		new ContextMenu("Web Services", "Test in JBoss Web Service Tester").select();

		WsTesterView tester = new WsTesterView();
		tester.open();
		return tester;
	}

	private void testWSDLInWSTester(WsTesterView wsTesterView) {
		wsTesterView.setRequestType(RequestType.JAX_WS);
		wsTesterView.invokeGetFromWSDL().ok();
		new WaitWhile(new JobIsRunning());
		wsTesterView.open();
		wsTesterView.setRequestBody(request);
		wsTesterView.invoke();
		wsTesterView.open();
		new WaitUntil(new WsTesterNotEmptyResponseText());
		String rsp = wsTesterView.getResponseBody();
		assertTrue(rsp.trim().length() > 0);
		assertTrue(rsp, rsp.contains("Hello User!"));
	}

	@Override
	protected String getEarProjectName() {
		return null;
	}

}
