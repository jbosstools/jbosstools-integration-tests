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
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceWizardPageBase.SliderLevel;
import org.jboss.tools.ws.ui.bot.test.WSAllBotTests;
import org.jboss.tools.ws.ui.bot.test.webservice.TopDownWSTest;
import org.jboss.tools.ws.ui.bot.test.webservice.WebServiceRuntime;
import org.jboss.tools.ws.ui.bot.test.webservice.WebServiceTestBase;
import org.junit.After;
import org.junit.AfterClass;
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
@Require(perspective="Java EE", 
		server=@Server)
@OpenPerspective(JavaEEPerspective.class)
@JBossServer()
//		(type=ServerType.EAP, 
//		version = "5.1", operator = ">="))
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

	@After
	@Override
	public void cleanup() {
		LOGGER.info("overridden");
	}

	@AfterClass
	public static void x() {
		LOGGER.info("x");
		servers.removeAllProjectsFromServer();
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
		
		deploymentHelper.runProject(getEarProjectName());
		deploymentHelper.assertServiceDeployed(deploymentHelper.getWSDLUrl(getWsProjectName(), getWsName()), 10000);
		servicePassed = true;
	}

	private void testClient() {
		Assert.assertTrue("service must exist", servicePassed);
		clientHelper.createClient(deploymentHelper.getWSDLUrl(getWsProjectName(), getWsName()),
				WebServiceRuntime.JBOSS_WS, getWsClientProjectName(), getEarProjectName(), 
				SliderLevel.DEVELOP, getWsClientPackage());
		IProject p = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(getWsClientProjectName());
		String pkg = "org/jboss/wsclient";
		String cls = "src/" + pkg + "/AreaService.java";
		Assert.assertTrue(p.getFile(cls).exists());
		cls = "src/" + pkg + "/clientsample/ClientSample.java";
		IFile f = p.getFile(cls);
		Assert.assertTrue(f.exists());
		replaceContent(f, "/resources/jbossws/clientsample.java.ws");

		/*
		 * workaround for https://issues.jboss.org/browse/JBIDE-9817
		 */
		projectExplorer.selectProject(getWsClientProjectName());
		SWTBotTree tree = projectExplorer.bot().tree();
		SWTBotTreeItem item = tree.getTreeItem(getWsClientProjectName());
		item.expand();
		removeRuntimeLibrary(tree, item, bot, util);

		/* workaround problems with generated code that require JAX-WS API 2.2 */
		jaxWsApi22RequirementWorkaround(getWsClientProjectName(), getWsClientPackage());
		
		eclipse.runJavaApplication(getWsClientProjectName(),
				"org.jboss.wsclient.clientsample.ClientSample", null);
		util.waitForNonIgnoredJobs();
		
		// wait until the client ends (prints the last line)
		new WaitUntil(new ConsoleHasText("Call Over!"), TimePeriod.NORMAL);

		ConsoleView cw = new ConsoleView();
		String consoleOutput = cw.getConsoleText();
		LOGGER.info(consoleOutput);
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
		util.waitForNonIgnoredJobs();		
	}

	private void removeRuntimeLibrary(final SWTBotTree tree,
			SWTBotTreeItem item, SWTBotExt bot, SWTUtilExt util) {
		nodeContextMenu(tree, item, "Build Path", "Configure Build Path...")
				.click();
		bot.activeShell().activate();
		bot.tabItem("Libraries").activate();
		assertTrue(!bot.button("Remove").isEnabled());
		try {
			findSelectEnterpriseRuntimeLibrary(bot);
			assertTrue(bot.button("Remove").isEnabled());
			bot.button("Remove").click();
			bot.button("OK").click();			
			util.waitForNonIgnoredJobs();
		} catch (Exception e) {
			e.printStackTrace();
			bot.button("Cancel").click();
		}

	}

	private void findSelectEnterpriseRuntimeLibrary(SWTBotExt bot)
			throws Exception {
		SWTBotTree libraryTree = bot.tree(1);
		boolean libraryFound = false;
		for (SWTBotTreeItem libraryItem : libraryTree.getAllItems()) {
			if (libraryItem.getText().contains(
					"JBoss Enterprise Application Platform")) {
				libraryTree.select(libraryItem);
				libraryFound = true;
				break;
			}
		}
		if (!libraryFound)
			throw new RuntimeException("No runtime library has been found");
	}

	private SWTBotMenu nodeContextMenu(final SWTBotTree tree,
			SWTBotTreeItem item, final String... menu) {
		assert menu.length > 0;
		ContextMenuHelper.prepareTreeItemForContextMenu(tree, item);
		return UIThreadRunnable.syncExec(new Result<SWTBotMenu>() {

			public SWTBotMenu run() {
				SWTBotMenu m = new SWTBotMenu(ContextMenuHelper.getContextMenu(
						tree, menu[0], false));
				for (int i = 1; i < menu.length; i++) {
					m = m.menu(menu[i]);
				}
				return m;
			}
		});
	}
	
	/**
	 * @see https://community.jboss.org/message/732539#732539
	 * 
	 * @param projectName
	 * @param pkgName
	 */
	private void jaxWsApi22RequirementWorkaround(String projectName, String pkgName) {
		SWTBotEclipseEditor editor = packageExplorer
				.openFile(projectName, "src", pkgName, "AreaService_Service.java")
				.toTextEditor();
		
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
		editor.saveAndClose();
	}
}
