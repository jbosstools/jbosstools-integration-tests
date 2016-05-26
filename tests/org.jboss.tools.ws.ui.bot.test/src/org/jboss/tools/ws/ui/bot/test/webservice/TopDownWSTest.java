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

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceWizardPageBase.SliderLevel;
import org.jboss.tools.ws.ui.bot.test.utils.DeploymentHelper;
import org.jboss.tools.ws.ui.bot.test.utils.ServersViewHelper;
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
		setLevel(SliderLevel.DEVELOP);
		topDownWS();
	}
	
	@Test
	public void testAssembleService() {
		setLevel(SliderLevel.ASSEMBLE);

		/* There may exist WSDL and generated files created in testDefaultPkg due to using the same SliderLevel */
		prepareAssembleService();

		topDownWS();

		/* If there were WSDL file than it was also used in web.xml */
		confirmWebServiceNameOverwrite();
	}
	
	@Test
	public void testDeployService() {
		setLevel(SliderLevel.DEPLOY);
		topDownWS();
	}
	
	@Test
	public void testInstallService() {
		setLevel(SliderLevel.INSTALL);
		topDownWS();
	}
	
	/**
	 * Fails due to JBIDE-16066
	 * @see https://issues.jboss.org/browse/JBIDE-16066
	 */
	@Test
	public void testStartService() {
		setLevel(SliderLevel.START);
		topDownWS();
	}
	
	/**
	 * Fails due to JBIDE-16066
	 * @see https://issues.jboss.org/browse/JBIDE-16066
	 */
	@Test
	public void testTestService() {
		setLevel(SliderLevel.TEST);
		topDownWS();
	}
	
	@Test
	public void testDefaultPkg() {
		setLevel(SliderLevel.ASSEMBLE);
		
		prepareAssembleService();
		
		topDownWS(null);
	}

	private void confirmWebServiceNameOverwrite() {
		// look up shell
		try {
			new DefaultShell("Confirm Web Service Name Overwrite");
			new OkButton().click();
		} catch(SWTLayerException e) {
			LOGGER.log(Level.SEVERE, "No \"Confirm Web Service Name Overwrite\" dialog found!", e);
			return;
		}
	}

	private void removeWsdlFileFromProject(String wsdlFileName) {
		ProjectItem item = null;
		try {
			item = new ProjectExplorer().getProject(getWsProjectName())
						.getProjectItem("Java Resources", "src", wsdlFileName);
		} catch(EclipseLayerException e) {
			return;
		}

		item.delete();

		ShellWithTextIsAvailable condition = new ShellWithTextIsAvailable(new RegexMatcher("Delete"));
		try {
			new WaitUntil(condition);
		} catch(WaitTimeoutExpiredException e) {
			return;
		}
		new OkButton().click();
		new WaitWhile(condition);
	}
	
	private void topDownWS() {
		topDownWS("ws." + getWsName().toLowerCase());
	}
	
	protected void topDownWS(String pkg) {
		topDownWS(
				TopDownWSTest.class.getResourceAsStream("/resources/jbossws/ClassB.wsdl"),
				WebServiceRuntime.JBOSS_WS, pkg, true);

		
		/* If there were WSDL file than it was also used in web.xml */
		confirmWebServiceNameOverwrite();
		
		switch (getLevel()) {
		case DEVELOP:
		case ASSEMBLE:
			
		/*workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=377624
		 choosing 'Deploy' should normally deploy the project automatically*/
		case DEPLOY:
			ServersViewHelper.runProjectOnServer(getEarProjectName());
			
		default:
			ServersViewHelper.waitForDeployment(getEarProjectName(), getConfiguredServerName());
			break;
		}
		DeploymentHelper.assertServiceDeployed(DeploymentHelper.getWSDLUrl(getWsProjectName(), getWsName()), 10000);
	}

	private void prepareAssembleService() {
		/* There exists WSDL file created in testAssembleService due to using the same SliderLevel */
		removeWsdlFileFromProject(getWsName() + ".wsdl");
	}
}
