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

import java.util.logging.Level;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.WsWizardBase.Slider_Level;
import org.junit.Test;

/**
 * Test operates on Web Service Wizard with top down process of creating web service
 * @author jlukas
 *
 */
public class TopDownWSTest extends WebServiceTestBase {

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
	public void testDevelopService() {
		setLevel(Slider_Level.DEVELOP);
		topDownWS();
	}
	
	@Test
	public void testAssembleService() {
		setLevel(Slider_Level.ASSEMBLE);
		topDownWS();
	}
	
	/**
	 * Fails due to reported bug
	 * 
	 * @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=377624
	 */
	@Test
	public void testDeployService() {
		setLevel(Slider_Level.DEPLOY);
		topDownWS();
	}
	
	@Test
	public void testInstallService() {
		setLevel(Slider_Level.INSTALL);
		topDownWS();
	}
	
	/**
	 * Fails due to JBIDE-16066
	 * @see https://issues.jboss.org/browse/JBIDE-16066
	 */
	@Test
	public void testStartService() {
		setLevel(Slider_Level.START);
		topDownWS();
	}
	
	/**
	 * Fails due to JBIDE-16066
	 * @see https://issues.jboss.org/browse/JBIDE-16066
	 */
	@Test
	public void testTestService() {
		setLevel(Slider_Level.TEST);
		topDownWS();
	}
	
	@Test
	public void testDefaultPkg() {
		setLevel(Slider_Level.ASSEMBLE);
		
		/* There exists WSDL file created in testAssembleService due to using the same Slider_Level */
		removeWsdlFileFromProject(getWsName() + ".wsdl");
		
		topDownWS(null);
		
		/* If there were WSDL file than it was also used in web.xml */
		confirmWebServiceNameOverwrite();
	}

	private void confirmWebServiceNameOverwrite() {
		// look up shell
		try {
			new DefaultShell("Confirm Web Service Name Overwrite");
			new PushButton("OK").click();
		} catch(SWTLayerException e) {
			LOGGER.log(Level.SEVERE, "No \"Confirm Web Service Name Overwrite\" dialog found!", e);
			return;
		}
	}

	private void removeWsdlFileFromProject(String wsdlFileName) {
		// select WSDL file
		SWTBotTreeItem wsdlNode = null;
		try {
			SWTBotTree projectsTree = projectExplorer.bot().tree();
			wsdlNode = projectsTree
					.expandNode(getWsProjectName())
					.expandNode("Java Resources")
					.expandNode("src")
					.getNode(wsdlFileName);
		} catch(WidgetNotFoundException e) {
			LOGGER.log(Level.SEVERE, "Not found "+wsdlFileName+" in the project.", e);
			return;
		}
		// delete
		wsdlNode.select().pressShortcut(Keystrokes.DELETE);
		//TODO use ShellWithTextIsActive with new RegexMatcher(...)
		ShellWithTextIsActive condition = new ShellWithTextIsActive("Delete");
		new WaitUntil(condition);
		new PushButton("OK").click();
		new WaitWhile(condition);
	}
	
	private void topDownWS() {
		topDownWS("ws." + getWsName().toLowerCase());
	}
	
	protected void topDownWS(String pkg) {
		topDownWS(
				TopDownWSTest.class.getResourceAsStream("/resources/jbossws/ClassB.wsdl"),
				WebServiceRuntime.JBOSS_WS, pkg);
		switch (getLevel()) {
		case DEVELOP:
		case ASSEMBLE:
			deploymentHelper.runProject(getEarProjectName());
		default:
			break;
		}
		deploymentHelper.assertServiceDeployed(deploymentHelper.getWSDLUrl(getWsProjectName(), getWsName()), 10000);
		servers.removeAllProjectsFromServer(configuredState.getServer().name);
	}
}
