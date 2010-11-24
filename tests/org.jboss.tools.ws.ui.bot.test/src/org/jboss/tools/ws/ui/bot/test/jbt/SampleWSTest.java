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
package org.jboss.tools.ws.ui.bot.test.jbt;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ws.ui.bot.test.uiutils.actions.NewSampleWSWizardAction;
import org.jboss.tools.ws.ui.bot.test.uiutils.actions.TreeItemAction;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.SampleWSWizard;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.SampleWSWizard.Type;
import org.jboss.tools.ws.ui.bot.test.utils.WSClient;
import org.jboss.tools.ws.ui.bot.test.wtp.WSTestBase;
import org.junit.AfterClass;
import org.junit.Test;

@SWTBotTestRequires(server=@Server(),perspective="Java EE")
public class SampleWSTest extends WSTestBase {

	private static final String SOAP_REQUEST = getSoapRequest("<ns1:sayHello xmlns:ns1=\"http://{0}/\"><arg0>{1}</arg0></ns1:sayHello>");
	private static final String SERVER_URL = "localhost:8080";
	private static final Logger L = Logger.getLogger(SampleWSTest.class.getName());
	
	@AfterClass
	public static void clean() {
		servers.removeAllProjectsFromServer();
		projectExplorer.deleteAllProjects();
	}
	
	@Override
	protected String getWsProjectName() {
		return "SampleSOAPWS";
	}

	@Override
	protected String getWsPackage() {
		return null;
	}

	@Override
	protected String getWsName() {
		return null;
	}
	
	@Test
	public void testSampleSoapWS() {
		IFile dd = getDD(getWsProjectName());
		if (!dd.exists()) {
			createDD(getWsProjectName());
		}
		assertTrue(dd.exists());
		createSampleSOAPWS(getWsProjectName(), "HelloService", "sample", "SampleService");
		checkSOAPService(getWsProjectName(), "HelloService", "sample", "SampleService", "You");

		createSampleSOAPWS(getWsProjectName(), "GreetService", "greeter", "Greeter");
		checkSOAPService(getWsProjectName(), "GreetService", "greeter", "Greeter", "Tester");
	}

	@Test
	public void testSampleRestWS() {
		if ("JBOSS_AS".equals(configuredState.getServer().type)) {
			fail("This test requires RESTEasy jars in the server");
		}
		String project = "SampleRESTWS";
		createProject(project);
		IFile dd = getDD(project);
		if (!dd.exists()) {
			createDD(project);
		}
		assertTrue(dd.exists());
		createSampleRESTWS(project, "RESTSample", "rest.sample", "Sample", "RESTApp");
		checkRESTService(project, "RESTSample", "rest.sample", "Sample", "Hello World!", "RESTApp");
	}

	private void createDD(String project) {
		SWTBotTree tree = projectExplorer.tree();
		SWTBotTreeItem ti = tree.expandNode(project);
		bot.sleep(500);
		ti = ti.getNode("Deployment Descriptor: " + project);
		new TreeItemAction(ti, "Generate Deployment Descriptor Stub").run();
		util.waitForNonIgnoredJobs();
	}

	private void createSampleSOAPWS(String project, String name, String pkg, String cls) {
		createSampleService(Type.SOAP, project, name, pkg, cls, null);
	}
	
	private void checkSOAPService(String project, String svcName, String svcPkg, String svcClass, String msgContent) { 
		checkService(Type.SOAP, project, svcName, svcPkg, svcClass, msgContent, null);
	}
	
	private void checkRESTService(String project, String svcName, String svcPkg, String svcClass, String msgContent, String appCls) { 
		checkService(Type.REST, project, svcName, svcPkg, svcClass, msgContent, appCls);
	}
	
	private void createSampleRESTWS(String project, String name, String pkg, String cls, String appCls) {
		createSampleService(Type.REST, project, name, pkg, cls, appCls);
	}
	
	private void createSampleService(Type type, String project, String name, String pkg, String cls, String appCls) {
		SampleWSWizard w = new NewSampleWSWizardAction(type).run();
		w.setProjectName(project).setServiceName(name);
		w.setPackageName(pkg).setClassName(cls);
		if (type == Type.REST) {
			w.setApplicationClassName(appCls);
		}
		w.finish();
		util.waitForNonIgnoredJobs();
	}
	
	private void checkService(Type type, String project, String svcName, String svcPkg, String svcClass, String msgContent, String appCls) {
		SWTBotEditor ed = bot.activeEditor();
		assertEquals(svcClass + ".java", ed.getTitle());
		String code = ed.toTextEditor().getText(); 
		assertContains("package " + svcPkg + ";", code);
		String dd = readFile(getDD(project));
		switch (type) {
		case REST:
			assertContains("@Path(\"/" + svcName + "\")", code);
			assertContains("@GET()", code);
			assertContains("@Produces(\"text/plain\")", code);
			assertContains("<servlet-name>Resteasy</servlet-name>", dd);
			assertContains("<param-value>" + svcPkg + "." + appCls + "</param-value>", dd);
			break;
		case SOAP:
			assertContains("<servlet-name>" + svcName + "</servlet-name>", dd);
			break;
		}
		runProject(project);
		switch (type) {
		case REST:
			try {
				URL u = new URL("http://" + SERVER_URL + "/" + project + "/" + svcName);
				String s = readStream(u.openConnection().getInputStream());
				assertEquals(msgContent, s);
			} catch (MalformedURLException e) {
				L.log(Level.WARNING, e.getMessage(), e);
			} catch (IOException e) {
				L.log(Level.WARNING, e.getMessage(), e);
			}
			break;
		case SOAP:
			try {
				WSClient c = new WSClient(new URL("http://" + SERVER_URL + "/" + project + "/" + svcName),
						new QName("http://" + svcPkg + "/", svcClass + "Service"),
						new QName("http://" + svcPkg + "/", svcClass + "Port"));
				assertContains("Hello " + msgContent + "!", c.callService(MessageFormat.format(SOAP_REQUEST, svcPkg, msgContent)));
			} catch (MalformedURLException e) {
				L.log(Level.WARNING, e.getMessage(), e);
			}
			break;
		}
	}
	
	private IProject getProject(String project) {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(project);
	}
	
	private IFile getDD(String project) {
		return getProject(project).getFile("WebContent/WEB-INF/web.xml");
	}
	
	private String readFile(IFile file) {
		try {
			return readStream(file.getContents());
		} catch (CoreException e) {
			L.log(Level.WARNING, e.getMessage(), e);
		}
		return "";
	}
}
