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

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceWizardPageBase.SliderLevel;
import org.jboss.tools.ws.ui.bot.test.utils.DeploymentHelper;
import org.jboss.tools.ws.ui.bot.test.utils.ServersViewHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test operates on Web Service Wizard with bottom up process of creating web
 * service
 * 
 * @author jlukas
 *
 */
@RunWith(RedDeerSuite.class)
public class BottomUpWSTest extends WebServiceTestBase {

	// http://localhost:8080/BottomUpWS/ClassA?wsdl
	/*
	 * <soap:Body> <method xmlns = "http://jbossws/"> </method> </soap:Body>
	 * 
	 * <soap:Body> <ns2:methodResponse xmlns:ns2="http://jbossws/">
	 * <return>1234567890</return> </ns2:methodResponse> </soap:Body>
	 */
	
	@Override
	protected String getWsPackage() {
		return "jbossws." + getLevel().toString().toLowerCase();
	}

	@Override
	protected String getWsName() {
		return "JavaWs_" + getLevel();
	}

	@Override
	protected String getWsProjectName() {
		return "BottomUpWS-web";
	}

	@Override
	protected String getEarProjectName() {
		return "BottomUpWS-ear";
	}

	@Test
	public void testDevelopService() {
		setLevel(SliderLevel.DEVELOP);
		bottomUpJbossWebService();
	}

	@Test
	public void testAssembleService() {
		setLevel(SliderLevel.ASSEMBLE);
		bottomUpJbossWebService();
	}

	@Test
	public void testDeployService() {
		setLevel(SliderLevel.DEPLOY);
		bottomUpJbossWebService();
	}

	@Test
	public void testInstallService() {
		setLevel(SliderLevel.INSTALL);
		bottomUpJbossWebService();
	}

	@Test
	public void testStartService() {
		setLevel(SliderLevel.START);
		bottomUpJbossWebService();
	}

	@Test
	public void testTestService() {
		setLevel(SliderLevel.TEST);
		bottomUpJbossWebService();
	}

	protected void bottomUpJbossWebService() {
		bottomUpWS(BottomUpWSTest.class.getResourceAsStream("/resources/jbossws/ClassA.java.ws"),
				WebServiceRuntime.JBOSS_WS, true);
		switch (getLevel()) {
			case DEVELOP:
			case ASSEMBLE:

			/*
			 * workaround for
			 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=377624 choosing
			 * 'Deploy' should normally deploy the project automatically
			 */
			case DEPLOY:
				ServersViewHelper.runProjectOnServer(getEarProjectName());

			default:
				ServersViewHelper.waitForDeployment(getEarProjectName(), getConfiguredServerName());
				break;
		}
		DeploymentHelper.assertServiceDeployed(DeploymentHelper.getWSDLUrl(getWsProjectName(), getWsName()), 10000);
	}
}
