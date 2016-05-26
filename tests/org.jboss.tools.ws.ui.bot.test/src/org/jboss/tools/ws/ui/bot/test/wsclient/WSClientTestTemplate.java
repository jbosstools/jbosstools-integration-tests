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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.views.navigator.ResourceNavigator;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceWizardPageBase.SliderLevel;
import org.jboss.tools.ws.ui.bot.test.soap.SOAPTestBase;
import org.jboss.tools.ws.ui.bot.test.utils.ServersViewHelper;
import org.jboss.tools.ws.ui.bot.test.utils.WebServiceClientHelper;
import org.jboss.tools.ws.ui.bot.test.webservice.WebServiceRuntime;
import org.junit.After;
import org.junit.Test;

/**
 * Test template operates on Web Service Client Wizard
 * 
 * @author jlukas
 * @author Radoslav Rabara
 */
public class WSClientTestTemplate extends SOAPTestBase {

	protected final WebServiceRuntime serviceRuntime;

	public WSClientTestTemplate(WebServiceRuntime serviceRuntime) {
		this.serviceRuntime = serviceRuntime;
	}
	
	@After
	public void undeploy() {
		ServersViewHelper.removeAllProjectsFromServer(getConfiguredServerName());
	}

	@Override
	protected String getWsProjectName() {
		return "client";
	}

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
	@Override
	public void cleanup() {
		super.cleanup();
		deleteAllPackages();
	}

	protected void clientTest(String targetPkg) {
		WebServiceClientHelper.createClient(
				getConfiguredServerName(),
				 "http://soaptest.parasoft.com/calculator.wsdl",
				serviceRuntime,
				null,
				null,
				getLevel(),
				targetPkg);
		
		assertThatExpectedFilesExists(targetPkg);
		
		assertThatEARProjectIsDeployed();
	}
	
	private void assertThatExpectedFilesExists(String targetPkg) {
		ResourceNavigator navigator = new ResourceNavigator();
		navigator.open();
		Project project = navigator.getProject(getWsProjectName());
		project.refresh();
		
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
			assertTrue("File " + file + " was not created", project.containsItem(file.split("/")));
		}
	}
	
	private void assertThatEARProjectIsDeployed() {
		switch (getLevel()) {
		
		/*workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=428982
		 choosing 'Deploy' should normally deploy the project automatically*/
		case DEPLOY:
			ServersViewHelper.runProjectOnServer(getEarProjectName());
			
		case TEST:
		case START:
		case INSTALL:
			ServersViewHelper.waitForDeployment(getEarProjectName(), getConfiguredServerName());
			if(!WebServiceClientHelper.projectIsDeployed(getConfiguredServerName(), getEarProjectName())) {
				fail("Project was not found on the server.");
			}
		default:
			break;
		}
	}

	private void deleteAllPackages() {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		Project p = pe.getProject(getWsProjectName());
		ProjectItem src = p.getProjectItem("src");
		try {
			for(ProjectItem pkg: src.getChildren()) {
				pkg.select();
				pkg.delete();
			}
		} catch(RedDeerException e) {
			pe.open();
			src = p.getProjectItem("src");
			List<ProjectItem> pkgs = src.getChildren();
			for(ProjectItem pkg: pkgs) {
				pkg.select();
				pkg.delete();
			}
		}
		
	}
}

