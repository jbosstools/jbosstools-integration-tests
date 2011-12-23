/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.webservice;

import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.WsWizardBase.Slider_Level;
import org.junit.Test;

/**
 * Test operates on Web Service Wizard with bottom up process of creating web service
 * @author jjankovi
 *
 */
public class BottomUpWSTest extends WebServiceTestBase {

		//http://localhost:8080/BottomUpWS/ClassA?wsdl
		/*
 <soap:Body>
<method xmlns = "http://jbossws/">
</method>
</soap:Body>

<soap:Body>
<ns2:methodResponse xmlns:ns2="http://jbossws/">
<return>1234567890</return>
</ns2:methodResponse>
</soap:Body>
		 */
	
	@Override
	protected String getWsPackage() {
		return "jbossws." + getLevel().toString().toLowerCase();
	}

	@Override
	protected String getWsName() {
		return "JavaWs_" + getLevel();
	}
	
	protected String getWsProjectName() {
		return "BottomUpWS-web";
	}
	
	@Override
	protected String getEarProjectName() {
		return "BottomUpWS-ear";
	}

	@Test
	public void testDeployService() {
		setLevel(Slider_Level.DEPLOY);
		bottomUpJbossWebService();
	}
	
	@Test
	public void testAssembleService() {
		setLevel(Slider_Level.ASSEMBLE);
		bottomUpJbossWebService();
	}
	
	@Test
	public void testDevelopService() {
		setLevel(Slider_Level.DEVELOP);
		bottomUpJbossWebService();
	}
	
	@Test
	public void testInstallService() {
		setLevel(Slider_Level.INSTALL);
		bottomUpJbossWebService();
	}
	
	@Test
	public void testStartService() {
		setLevel(Slider_Level.START);
		bottomUpJbossWebService();
	}
	
	@Test
	public void testTestService() {
		setLevel(Slider_Level.TEST);
		bottomUpJbossWebService();
	}

	protected void bottomUpJbossWebService() {
		bottomUpJbossWebService(BottomUpWSTest.class.getResourceAsStream("/resources/jbossws/ClassA.java.ws"));
		switch (getLevel()) {
		case DEVELOP:
		case ASSEMBLE:
		case DEPLOY:
			deploymentHelper.runProject(getEarProjectName());
			break;
		}
		deploymentHelper.assertServiceDeployed(deploymentHelper.getWSDLUrl(getWsProjectName(), getWsName()), 10000);
//		checkService(getWSDLUrl(svcName.substring(svcName.lastIndexOf(".") + 1)), QName service, QName port, String msg, String rsp)
//		servers.removeAllProjectsFromServer(configuredState.getServer().name);
	}
}
