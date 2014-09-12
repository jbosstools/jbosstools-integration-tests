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

package org.jboss.tools.ws.ui.bot.test.webservice.eap;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceWizardPageBase.SliderLevel;
import org.jboss.tools.ws.ui.bot.test.WSAllBotTests;
import org.jboss.tools.ws.ui.bot.test.uiutils.RunConfigurationsDialog;
import org.jboss.tools.ws.ui.bot.test.webservice.TopDownWSTest;
import org.jboss.tools.ws.ui.bot.test.webservice.WebServiceRuntime;
import org.jboss.tools.ws.ui.bot.test.webservice.WebServiceTestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test operates on creating non-trivial EAP project from wsdl file
 * @author jlukas
 *
 */
@SuiteClasses({ WSAllBotTests.class, EAPCompAllTests.class })
@OpenPerspective(JavaEEPerspective.class)
@JBossServer()
public class EAPFromWSDLTest extends WebServiceTestBase {

	private static boolean servicePassed = false;

	@Before
	@Override
	public void setup() {
		if (!projectExists(getWsProjectName())) {
			projectHelper.createProject(getWsProjectName());
		}
		if (!projectExists(getWsClientProjectName())) {
			projectHelper.createProject(getWsClientProjectName());
		}
	}

	@Override
	protected String getWsProjectName() {
		return "AreaWSProject";
	}

	@Override
	protected String getEarProjectName() {
		return getWsProjectName() + "EAR";
	}

	protected String getWsClientProjectName() {
		return "AreaWSClientProject";
	}

	protected String getWsClientPackage() {
		return "org.jboss.wsclient";
	}
	protected String getClientEarProjectName() {
		return getWsClientProjectName() + "EAR";
	}

	@Override
	protected String getWsPackage() {
		return "org.jboss.ws";
	}

	@Override
	protected String getWsName() {
		return "AreaService";
	}

	@Override
	protected SliderLevel getLevel() {
		return SliderLevel.DEPLOY;
	}

	@Test
	public void testEAPFromWSDL() {
		testService();
		testClient();
	}

	private void testService() {
		topDownWS(TopDownWSTest.class.getResourceAsStream("/resources/jbossws/AreaService.wsdl"),
				WebServiceRuntime.JBOSS_WS, getWsPackage());

		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(getWsProjectName());
		IFile f = project.getFile("src/" + getWsPackage().replace(".", "/")
				+ "/impl" + "/AreaServiceImpl.java");
		/*
		 * workaround when package is not typed
		 */
		if (f == null) {
			f = project.getFile("src/" + "org.tempuri.areaservice"
					+ "/AreaServiceImpl.java");
		}
		String content = resourceHelper.readFile(f);
		Assert.assertNotNull(content);
		Assert.assertTrue(content
				.contains("public class AreaServiceImpl implements AreaService {"));
		Assert.assertTrue(content
				.contains("public float calculateRectArea(Dimensions parameters)"));
		replaceContent(f, "/resources/jbossws/AreaWS.java.ws");

		f = project.getFile("WebContent/WEB-INF/web.xml");
		content = resourceHelper.readFile(f);
		Assert.assertNotNull(content);
		Assert.assertTrue(content
				.contains("<servlet-class>org.jboss.ws.impl.AreaServiceImpl</servlet-class>"));
		Assert.assertTrue(content
				.contains("<url-pattern>/AreaService</url-pattern>"));
		
		/* workaround problems with generated code that require JAX-WS API 2.2 */
		jaxWsApi22RequirementWorkaround(getWsProjectName(), getWsPackage());
		
		serversViewHelper.runProjectOnServer(getEarProjectName());
		deploymentHelper.assertServiceDeployed(deploymentHelper.getWSDLUrl(getWsProjectName(), getWsName()), 10000);
		servicePassed = true;
	}

	private void testClient() {
		Assert.assertTrue("service must exist", servicePassed);
		clientHelper.createClient(getConfiguredServerName(),
				deploymentHelper.getWSDLUrl(getWsProjectName(), getWsName()),
				WebServiceRuntime.JBOSS_WS, getWsClientProjectName(),
				getEarProjectName(), SliderLevel.DEVELOP, getWsClientPackage());
		IProject p = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(getWsClientProjectName());
		String pkg = "org/jboss/wsclient";
		String cls = "src/" + pkg + "/AreaService.java";
		Assert.assertTrue(p.getFile(cls).exists());
		cls = "src/" + pkg + "/clientsample/ClientSample.java";
		IFile f = p.getFile(cls);
		Assert.assertTrue(f.exists());
		replaceContent(f, "/resources/jbossws/clientsample.java.ws");

		/* workaround problems with generated code that require JAX-WS API 2.2 */
		jaxWsApi22RequirementWorkaround(getWsClientProjectName(), getWsClientPackage());

		runJavaApplication();
		
		// wait until the client ends (prints the last line)
		new WaitUntil(new ConsoleHasText("Call Over!"), TimePeriod.NORMAL);

		ConsoleView cw = new ConsoleView();
		String consoleOutput = cw.getConsoleText();
		LOGGER.info("Console output: " + consoleOutput);
		Assert.assertTrue(consoleOutput, consoleOutput.contains("Server said: 37.5"));
		Assert.assertTrue(consoleOutput.contains("Server said: 3512.3699"));
	}

	private void replaceContent(IFile f, String content) {
		try {
			f.delete(true, new NullProgressMonitor());
		} catch (CoreException ce) {
			LOGGER.log(Level.WARNING, ce.getMessage(), ce);
		}
		InputStream is = null;
		try {
			is = EAPFromWSDLTest.class.getResourceAsStream(content);
			f.create(is, true, new NullProgressMonitor());
		} catch (CoreException ce) {
			LOGGER.log(Level.WARNING, ce.getMessage(), ce);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ioe) {
					// ignore
				}
			}
		}
		try {
			ResourcesPlugin
					.getWorkspace()
					.getRoot()
					.refreshLocal(IWorkspaceRoot.DEPTH_INFINITE,
							new NullProgressMonitor());
		} catch (CoreException e) {
			LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
	}

	/**
	 * @see https://community.jboss.org/message/732539#732539
	 */
	private void jaxWsApi22RequirementWorkaround(String projectName, String pkgName) {
		final String fileName = "AreaService_Service.java";
		openJavaFile(projectName, pkgName, fileName);
		TextEditor editor = new TextEditor(fileName);
		
		String text = editor.getText();
		boolean putComment = false;
		StringBuilder output = new StringBuilder();
		for(String line : text.split(System.getProperty("line.separator")) ) {
			if(line.contains("This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2")) {
				putComment = true;
			}
			if(putComment) {
				output.append("//");
				if( line.contains("}") ) {
					putComment = false;
				}
			}
			output.append(line);
			output.append(System.getProperty("line.separator"));
		}
		editor.setText(output.toString());
		editor.save();
		editor.close();
	}

	private void runJavaApplication() {
		RunConfigurationsDialog dialog = new RunConfigurationsDialog();
		dialog.open();
		dialog.createNewConfiguration("Java Application");
		dialog.setProjectName(getWsClientProjectName());
		dialog.setClassName("org.jboss.wsclient.clientsample.ClientSample");
		dialog.run();
	}
}
