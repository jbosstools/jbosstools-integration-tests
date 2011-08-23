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
package org.jboss.tools.ws.ui.bot.test.wtp;

import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ws.ui.bot.test.WSAllBotTests;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.WsWizardBase.Slider_Level;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@Require(server=@Server(),perspective="Java EE")
@RunWith(RequirementAwareSuite.class)
@SuiteClasses({ WSAllBotTests.class})
public class TopDownWSTest extends WSTestBase {

	@Override
	protected String getEarProjectName() {
		return "TopDownWS-ear";
	}

	@Override
	protected String getWsPackage() {
		return "jbossws.td." + getLevel().toString().toLowerCase();
	}

	@Override
	protected String getWsName() {
		return "WsdlWs" + getLevel();
	}
	
	@Override
	protected String getWsProjectName() {
		return "TopDownWS-web";
	}

		//http://localhost:8080/TopDownJbossWS/jbossws.ClassB?wsdl
		/*
<soap:Body>
<method xmlns = "http://jbossws/">
</method>
</soap:Body>

<soap:Body>
<ns2:methodResponse xmlns:ns2="http://jbossws/">
<return>0</return>
</ns2:methodResponse>
</soap:Body>
		 */
	
	@Test
	public void testDeployService() {
		setLevel(Slider_Level.DEPLOY);
		topDownWS();
	}
	
	@Test
	public void testDevelopService() {
		setLevel(Slider_Level.DEVELOP);
		topDownWS();
	}
	
	@Test
	public void testAssembleService() {
		setLevel(Slider_Level.ASSEMBLE);
		topDownWS();
	}
	
	@Test
	public void testInstallService() {
		setLevel(Slider_Level.INSTALL);
		topDownWS();
	}
	
	@Test
	public void testStartService() {
		setLevel(Slider_Level.START);
		topDownWS();
	}
	
	@Test
	public void testTestService() {
		setLevel(Slider_Level.TEST);
		topDownWS();
	}
	
//	@Test
//	public void testDefaultPkg() {
//		setLevel(Slider_Level.ASSEMBLE);
//		topDownWS(null);
//	}

	private void topDownWS() {
		topDownWS("ws." + getWsName().toLowerCase());
	}
	
	protected void topDownWS(String pkg) {
		topDownWS(TopDownWSTest.class.getResourceAsStream("/resources/jbossws/ClassB.wsdl"), pkg);
		switch (getLevel()) {
			case DEVELOP:
			case ASSEMBLE:
			case DEPLOY:
				runProject(getEarProjectName());
				break;
		}
		assertServiceDeployed(getWSDLUrl(), 10000);
//		servers.removeAllProjectsFromServer(configuredState.getServer().name);
	}
}
