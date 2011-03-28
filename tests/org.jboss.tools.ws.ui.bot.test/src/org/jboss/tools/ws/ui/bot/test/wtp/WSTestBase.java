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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotScale;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.JavaEEEnterpriseApplicationProject;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.WebServicesWSDL;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.WebServicesWebServiceClient;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.WebServlet;
import org.jboss.tools.ui.bot.ext.parts.SWTBotHyperlinkExt;
import org.jboss.tools.ui.bot.ext.parts.SWTBotScaleExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ws.ui.bot.test.uiutils.actions.NewFileWizardAction;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.DynamicWebProjectWizard;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.WebServiceWizard;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.WebServiceWizard.Service_Type;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.WebServiceWizard.Slider_Level;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.Wizard;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public abstract class WSTestBase extends SWTTestExt {

	private static final String SOAP_REQUEST_TEMPLATE = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>" +
	"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
	" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
	" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">" +
	"<soap:Body>{0}</soap:Body>" +
	"</soap:Envelope>";
	
	private static final Logger L = Logger.getLogger(WSTestBase.class.getName());
	private Slider_Level level;
	
	@Before
	public void setup() {
		if (getEarProjectName() != null && !projectExists(getEarProjectName())) {
			createEARProject(getEarProjectName());
		}
		if (!projectExists(getWsProjectName())) {
			createProject(getWsProjectName());
		}
	}
	
	private boolean projectExists(String name) {
		return projectExplorer.existsResource(name);
	}
	
	@After
	public void cleanup() {
		servers.removeAllProjectsFromServer();
	}
	
	@AfterClass
	public static void cleanAll() {
		projectExplorer.deleteAllProjects();
	}
	
	protected abstract String getWsProjectName();
	
	protected String getEarProjectName() {
		return null;
	}
	
	protected abstract String getWsPackage();
	protected abstract String getWsName();
	
	protected void setLevel(Slider_Level level) {
		this.level = level;
	}
	
	protected Slider_Level getLevel() {
		return level;
	}
	
	protected String getWSDLUrl() {
		return "http://localhost:8080/" + getWsProjectName() + "/" + getWsName() + "?wsdl";
	}
	
	protected void createClient(String projectName, String servletName,String wsdlDef, int type) {
		createProject(projectName);
		SWTBot wiz = open.newObject(WebServicesWebServiceClient.LABEL);
		wiz.comboBoxWithLabel(
				WebServicesWebServiceClient.TEXT_SERVICE_DEFINITION).setText(
						wsdlDef);
		SWTBotScale slider = bot.scale();	
		slider.setValue(type);
		selectJbossWSRuntime();
		bot.sleep(TIME_1S); // wait for wizard to validate wsdl url and
		// enable Finish button		
		open.finish(wiz);
		projectExplorer.selectProject(projectName);
		// create servlet which will invoke service
		createInvokingServlet(servletName);
	}

	/**
	 * checks if 'Web Service Runtime' is set to 'JbossWS' and possibly sets it correctly
	 * @param wiz wizard page of new Web Service
	 */
	@Deprecated
	protected void selectJbossWSRuntime() {
		SWTBotHyperlinkExt link = bot.hyperlink(1); 
		String linkText = link.getText();
		if (!linkText.contains("JBossWS")) {
			link.click();
			SWTBot dBot = bot.activeShell().bot();
			dBot.tree().select("JBossWS");
			open.finish(dBot,IDELabel.Button.OK);
		}
	}
	
	protected void createService(Service_Type t, String source, Slider_Level level, String pkg, String code) {
		//create ws source - java class or wsdl
		SWTBotEditor ed = null;
		switch (t) {
		case BOTTOM_UP:
			ed = createClass(getWsPackage(), getWsName());
			break;
		case TOP_DOWN:
			ed = createWsdl(getWsName());
			break;
		}
		assertNotNull(ed);
		//replace default content of java class w/ code
        SWTBotEclipseEditor st = ed.toTextEditor();
        st.selectRange(0, 0, st.getText().length());
        st.setText(code);
        ed.saveAndClose();
        //refresh workspace - workaround for JBIDE-6731
        try {
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IWorkspaceRoot.DEPTH_INFINITE, new NullProgressMonitor());
		} catch (CoreException e) {
			L.log(Level.WARNING, e.getMessage(), e);
		}
		bot.sleep(500);
        //create a web service
		new NewFileWizardAction().run().selectTemplate("Web Services", "Web Service").next();
		WebServiceWizard wsw = new WebServiceWizard();
		wsw.setServiceType(t);
		wsw.setServiceSource(source);
		wsw.setServerRuntime(configuredState.getServer().name);
		wsw.setWebServiceRuntime("JBossWS");
		wsw.setServiceProject(getWsProjectName());
		wsw.setServiceEARProject(getEarProjectName());
		wsw.setServiceSlider(level);
		if (wsw.isClientEnabled()) {
			wsw.setClientSlider(Slider_Level.NO_CLIENT);
		}
		if (pkg != null && pkg.trim().length() > 0) {
			wsw.next();
			wsw.setPackageName(pkg);
		}
		wsw.finish();
		util.waitForNonIgnoredJobs();
		bot.sleep(1000);
		
		//let's fail if there's some error in the wizard,
		//and close error dialog and the wizard so other tests
		//can continue
		if (bot.activeShell().getText().contains("Error")) {
			SWTBotShell sh = bot.activeShell();
			String msg = sh.bot().text().getText();
			sh.bot().button(0).click();
			wsw.cancel();
			Assert.fail(msg);
		}
	}
	
	private SWTBotEditor createClass(String pkg, String cName) {
		new NewFileWizardAction().run().selectTemplate("Java", "Class").next();
		Wizard w = new Wizard();
		w.bot().textWithLabel("Package:").setText(pkg);
		w.bot().textWithLabel("Name:").setText(cName);
		w.bot().textWithLabel("Source folder:").setText(getWsProjectName() + "/src");
		w.finish();
		return bot.editorByTitle(cName + ".java");
	}
	
	private SWTBotEditor createWsdl(String s) {
		SWTBot wiz1 = open.newObject(WebServicesWSDL.LABEL);
		wiz1.textWithLabel(WebServicesWSDL.TEXT_FILE_NAME).setText(s + ".wsdl");
		wiz1.textWithLabel(WebServicesWSDL.TEXT_ENTER_OR_SELECT_THE_PARENT_FOLDER)
			.setText(getWsProjectName() + "/src");
		wiz1.button(IDELabel.Button.NEXT).click();
		open.finish(wiz1);
		return bot.editorByTitle(s + ".wsdl");
	}
	
	protected void createInvokingServlet(String servletName) {
		String PKG_NAME="jbossws";
		SWTBot wiz = open.newObject(WebServlet.LABEL);
		wiz.textWithLabel(WebServlet.TEXT_JAVA_PACKAGE).setText(
				PKG_NAME);
		wiz.textWithLabel(WebServlet.TEXT_CLASS_NAME).setText(
				servletName);
		open.finish(wiz);
		eclipse.setClassContentFromResource(bot
				.editorByTitle(servletName
						+ ".java"), true,
				org.jboss.tools.ws.ui.bot.test.Activator.PLUGIN_ID,
				PKG_NAME, servletName
						+ ".java.servlet");
	}

	protected void createProject(String name) {
		new NewFileWizardAction().run().selectTemplate("Web", "Dynamic Web Project").next();
		new DynamicWebProjectWizard().setProjectName(name).finish();
		util.waitForNonIgnoredJobs();
		assertTrue(projectExplorer.existsResource(name));
		projectExplorer.selectProject(name);
	}
	
	protected void createEARProject(String name) {
		SWTBot wiz = open.newObject(JavaEEEnterpriseApplicationProject.LABEL);
		wiz.textWithLabel(JavaEEEnterpriseApplicationProject.TEXT_PROJECT_NAME).setText(name);
		// set EAR version
		SWTBotCombo combo = wiz.comboBox(1);
		combo.setSelection(combo.itemCount()-1);
		wiz.button(IDELabel.Button.NEXT).click();
		wiz.checkBox("Generate application.xml deployment descriptor").click();
		open.finish(wiz);
		bot.sleep(5000);
		assertTrue(projectExplorer.existsResource(name));
		projectExplorer.selectProject(name);
	}

	protected void assertServiceDeployed(String wsdlURL) {
		assertServiceDeployed(wsdlURL, 5000);
	}
	
	protected void assertServiceDeployed(String wsdlURL, long timeout) {
		long t = System.currentTimeMillis();
		int rsp = -1;
		while (t + timeout > System.currentTimeMillis()) {
			HttpURLConnection connection = null;
			try {
				URL u = new URL(wsdlURL);
				connection = (HttpURLConnection) u.openConnection();
				rsp = connection.getResponseCode();
				if (rsp == HttpsURLConnection.HTTP_OK) {
					break;
				} else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						//ignore
					}
					L.info("retrying...");
				}
			} catch (MalformedURLException e1) {
				throw new RuntimeException(e1);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
			}
		}
		L.info("done after: " + (System.currentTimeMillis() - t) + "ms.");
		assertEquals("Service was not sucessfully deployed, WSDL '" + wsdlURL + "' was not found",
				HttpURLConnection.HTTP_OK, rsp);
	}
	
	protected void assertServiceNotDeployed(String wsdlURL) {
		HttpURLConnection connection = null;
		try {
			URL u = new URL(wsdlURL);
			connection = (HttpURLConnection) u.openConnection();
			assertEquals("Project was not sucessfully undeployed, WSDL '" + wsdlURL + "' is still available",
					HttpURLConnection.HTTP_NOT_FOUND, connection.getResponseCode());
		} catch (MalformedURLException e1) {
			throw new RuntimeException(e1);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	protected void assertServiceResponseToClient(String startServlet, String response) {
		InputStream is = null;
		try {
			URL u = new URL(startServlet);
			is = u.openStream();
			String rsp = readStream(is);
			assertContains(response, rsp);
		} catch (MalformedURLException e1) {
			throw new RuntimeException(e1);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					//ignore
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getSoapRequest(String body) {
		return MessageFormat.format(SOAP_REQUEST_TEMPLATE, body);
	}
	
	protected String readStream(InputStream is) {
		Reader r = null;
		Writer w = null;
		try {
			char[] buffer = new char[1024];
			r = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			w = new StringWriter();
			int n;
			while ((n = r.read(buffer)) != -1) {
				w.write(buffer, 0, n);
			}
		} catch (IOException e) {
			L.log(Level.WARNING, e.getMessage(), e);
		} finally {
			if (r != null) {
				try {
					r.close();
				} catch (IOException e) {
					//ignore
					L.log(Level.WARNING, e.getMessage(), e);
				}
			}
			if (w != null) {
				try {
					w.close();
				} catch (IOException e) {
					//ignore
					L.log(Level.WARNING, e.getMessage(), e);
				}
			}
		}
		return w != null ? w.toString() : "";
	}
	
	protected void runProject(String project) {
		open.viewOpen(ActionItem.View.ServerServers.LABEL);
		projectExplorer.runOnServer(project);
	}
	

}
