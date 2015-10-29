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

import java.util.logging.Level;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ws.reddeer.ui.wizards.CreateNewFileWizardPage;
import org.jboss.tools.ws.reddeer.ui.wizards.jst.jsp.NewJSPFileWizard;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceWizardPageBase.SliderLevel;
import org.jboss.tools.ws.ui.bot.test.utils.DeploymentHelper;
import org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper;
import org.jboss.tools.ws.ui.bot.test.utils.ResourceHelper;
import org.jboss.tools.ws.ui.bot.test.utils.ServersViewHelper;
import org.jboss.tools.ws.ui.bot.test.utils.WebServiceClientHelper;
import org.jboss.tools.ws.ui.bot.test.webservice.WebServiceRuntime;
import org.jboss.tools.ws.ui.bot.test.webservice.WebServiceTestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test operates on creating non-trivial EAP project from Java class
 * 
 * @author jlukas
 *
 */
@OpenPerspective(JavaEEPerspective.class)
public class EAPFromJavaTest extends WebServiceTestBase {

	private static boolean servicePassed = false;

	@Before
	@Override
	public void setup() {
		if (!ProjectHelper.projectExists(getWsProjectName())) {
			ProjectHelper.createProject(getWsProjectName());
		}
		if (!ProjectHelper.projectExists(getWsClientProjectName())) {
			ProjectHelper.createProject(getWsClientProjectName());
		}
	}

	@Override
	protected String getWsProjectName() {
		return "TestWSProject";
	}

	@Override
	protected String getEarProjectName() {
		return getWsProjectName() + "EAR";
	}

	protected String getWsClientProjectName() {
		return "TestWSClientProject";
	}

	@Override
	protected String getWsPackage() {
		return "test.ws";
	}

	@Override
	protected String getWsName() {
		return "Echo";
	}

	@Override
	protected SliderLevel getLevel() {
		return SliderLevel.DEPLOY;
	}

	@Test
	public void testEAPFromJava() {
		testService();
		testClient();
	}

	private void testService() {
		// create a class representing some complex type
		TextEditor editor = ProjectHelper.createClass(getWsProjectName(), "test", "Person");
		editor.setText(ResourceHelper
				.readStream(EAPFromJavaTest.class.getResourceAsStream("/resources/jbossws/Person.java.ws")));
		editor.save();
		editor.close();

		// refresh workspace - workaround??? for JBIDE-6731
		try {
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IWorkspaceRoot.DEPTH_INFINITE,
					new NullProgressMonitor());
		} catch (CoreException e) {
			LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
		bottomUpWS(EAPFromJavaTest.class.getResourceAsStream("/resources/jbossws/Echo.java.ws"),
				WebServiceRuntime.JBOSS_WS, false);
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(getWsProjectName());
		IFile f = project.getFile("WebContent/WEB-INF/web.xml");
		String content = ResourceHelper.readFile(f);
		Assert.assertNotNull(content);
		Assert.assertTrue(content.contains("<servlet-class>test.ws.Echo</servlet-class>"));
		Assert.assertTrue(content.contains("<url-pattern>/Echo</url-pattern>"));
		ServersViewHelper.runProjectOnServer(getEarProjectName());
		ServersViewHelper.waitForDeployment(getEarProjectName(), getConfiguredServerName());
		DeploymentHelper.assertServiceDeployed(DeploymentHelper.getWSDLUrl(getWsProjectName(), getWsName()), 10000);
		servicePassed = true;
	}

	private void testClient() {
		Assert.assertTrue("service must exist", servicePassed);
		WebServiceClientHelper.createClient(getConfiguredServerName(),
				DeploymentHelper.getWSDLUrl(getWsProjectName(), getWsName()), WebServiceRuntime.JBOSS_WS,
				getWsClientProjectName(), getEarProjectName(), getLevel(), "");
		IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject(getWsClientProjectName());
		String pkg = "test/ws";
		String cls = "src/" + pkg + "/EchoService.java";
		Assert.assertTrue(p.getFile(cls).exists());
		cls = "src/" + pkg + "/clientsample/ClientSample.java";
		Assert.assertTrue(p.getFile(cls).exists());

		createJSPFile();

		/**
		 * Workaround for 4.x branch
		 * 
		 * bot.activeShell().bot().button("Skip").click(); bot.sleep(TIME_5S);
		 */
		setJSPFileContent();
		AbstractWait.sleep(TimePeriod.getCustom(2));
		ServersViewHelper.runProjectOnServer(getEarProjectName());
		ServersViewHelper.waitForDeployment(getEarProjectName(), getConfiguredServerName());
		ServersViewHelper.serverClean(getConfiguredServerName());
		String pageContent = DeploymentHelper
				.getPage("http://localhost:8080/" + getWsClientProjectName() + "/index.jsp", 15000);
		LOGGER.info(pageContent);
		Assert.assertTrue(pageContent.contains("BartSimpson(age: 12)"));
		Assert.assertTrue(pageContent.contains("Homer(age: 44)"));
	}

	private void createJSPFile() {
		NewJSPFileWizard wizard = new NewJSPFileWizard();
		wizard.open();

		CreateNewFileWizardPage page = new CreateNewFileWizardPage();
		page.setFileName("index");
		page.setParentFolder(getWsClientProjectName() + "/WebContent");

		wizard.finish();
	}

	private void setJSPFileContent() {
		TextEditor editor = new TextEditor("index.jsp");
		editor.setText(ResourceHelper
				.readStream(EAPFromJavaTest.class.getResourceAsStream("/resources/jbossws/index.jsp.ws")));
		editor.save();
		editor.close();
	}
}
