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
package org.jboss.tools.ws.ui.bot.test.wtp;

import java.text.MessageFormat;

import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.WebServiceWizard.Service_Type;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.WebServiceWizard.Slider_Level;
import org.junit.Test;

@SWTBotTestRequires(server=@Server(),perspective="Java EE")
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
	
	private void topDownWS() {
		String s = readStream(TopDownWSTest.class.getResourceAsStream("/resources/jbossws/ClassB.wsdl"));
		String[] tns = getWsPackage().split("\\.");
		StringBuilder sb = new StringBuilder();
		for (int i = tns.length - 1; i > 0; i--) {
			sb.append(tns[i]);
			sb.append(".");
		}
		sb.append(tns[0]);
		String src = MessageFormat.format(s, sb.toString(), getWsName());
		String pkg = "ws." + getWsName().toLowerCase();
		createService(Service_Type.TOP_DOWN, "/" + getWsProjectName() + "/src/" + getWsName() + ".wsdl", getLevel(), pkg, src);
		switch (getLevel()) {
		case DEVELOP:
		case ASSEMBLE:
		case DEPLOY:
			runProject(getEarProjectName());
			break;
		}
		assertServiceDeployed(getWSDLUrl());
//		servers.removeAllProjectsFromServer(configuredState.getServer().name);
	}

}
