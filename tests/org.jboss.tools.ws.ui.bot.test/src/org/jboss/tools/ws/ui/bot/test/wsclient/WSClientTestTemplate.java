/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.wsclient;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceWizardPageBase.SliderLevel;
import org.jboss.tools.ws.ui.bot.test.WSTestBase;
import org.jboss.tools.ws.ui.bot.test.webservice.WebServiceRuntime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test template operates on Web Service Client Wizard
 * 
 * @author jlukas
 * @author Radoslav Rabara
 */
public class WSClientTestTemplate extends WSTestBase {
	
	protected final WebServiceRuntime serviceRuntime;
	
	public WSClientTestTemplate(WebServiceRuntime serviceRuntime) {
		this.serviceRuntime = serviceRuntime;
	}

	@Override
	protected String getWsProjectName() {
		return "client";
	}

	@Override
	protected String getWsPackage() {
		return "client." + getLevel().toString().toLowerCase();
	}

	@Override
	protected String getEarProjectName() {
		return "clientEAR";
	}
	
	protected String getSampleClientFileName() {
		return "clientsample/ClientSample.java";
	}
	
	/**
	 * Fails because the created client is not deployed to the server
	 * 
	 * @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=428982
	 */
	@Test
	public void testDeployClient() {
		setLevel(SliderLevel.DEPLOY);
		clientTest(getWsPackage());
	}

	@Test
	public void testAssembleClient() {
		setLevel(SliderLevel.ASSEMBLE);
		clientTest(getWsPackage());
	}

	@Test
	public void testDevelopClient() {
		setLevel(SliderLevel.DEVELOP);
		clientTest(getWsPackage());
	}

	@Test
	public void testInstallClient() {
		setLevel(SliderLevel.INSTALL);
		clientTest(getWsPackage());
	}

	@Test
	public void testStartClient() {
		setLevel(SliderLevel.START);
		clientTest(getWsPackage());
	}

	@Test
	public void testTestClient() {
		setLevel(SliderLevel.TEST);
		clientTest(getWsPackage());
	}

	@Test
	public void testDefaultPkg() {
		setLevel(SliderLevel.ASSEMBLE);
		clientTest(null);
	}

	@After
	public void cleanup() {
		super.cleanup();
		//XXX remove all packages ??
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		Project p = pe.getProject(getWsProjectName());
		ProjectItem src = p.getProjectItem("src");
		for(ProjectItem pkg: src.getChildren()) {
			System.err.println(pkg.getText());
			pkg.delete();
		}
	}

	protected void clientTest(String targetPkg) {
		clientHelper.createClient(
				 "http://soaptest.parasoft.com/calculator.wsdl",
				serviceRuntime,
				getWsProjectName(),
				getEarProjectName(),
				getLevel(),
				targetPkg);
		
		assertThatExpectedFilesExists(targetPkg);
		
		assertThatEARProjectIsDeployed();
	}
	
	private void assertThatExpectedFilesExists(String targetPkg) {
		IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject(getWsProjectName());
		String pkg = (targetPkg != null && !"".equals(targetPkg.trim())) ? getWsPackage() :
			"com.parasoft.wsdl.calculator";
		String src = "src/" + pkg.replace('.', '/') + "/";
		String[] expectedFiles = {
				src + "ICalculator.java",
				src + "Add.java",
				src + "AddResponse.java",
				src + "Divide.java",
				src + "DivideResponse.java",
				src + "Multiply.java",
				src + "MultiplyResponse.java",
				src + "Subtract.java",
				src + "SubtractResponse.java",
				src + getSampleClientFileName()};
		for(String file : expectedFiles) {
			Assert.assertTrue("File " + file + " was not created", p.getFile(file).exists());
		}
	}
	
	private void assertThatEARProjectIsDeployed() {
		switch (getLevel()) {
		case TEST:
		case START:
		case INSTALL:
		case DEPLOY:
			if(!clientHelper.projectIsDeployed(configuredState.getServer().name, getEarProjectName())) {
				fail("Project was not found on the server.");
			}
		default:
			break;
		}
	}
}

