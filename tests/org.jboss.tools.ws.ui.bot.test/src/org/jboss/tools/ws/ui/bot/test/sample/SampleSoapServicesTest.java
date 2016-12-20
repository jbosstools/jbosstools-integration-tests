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

package org.jboss.tools.ws.ui.bot.test.sample;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.logging.Level;

import javax.xml.namespace.QName;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ws.reddeer.ui.wizards.ws.ui.SampleWebServiceWizard;
import org.jboss.tools.ws.reddeer.ui.wizards.ws.ui.SimpleWebServiceWizard;
import org.jboss.tools.ws.ui.bot.test.soap.SOAPTestBase;
import org.jboss.tools.ws.ui.bot.test.utils.Asserts;
import org.jboss.tools.ws.ui.bot.test.utils.ResourceHelper;
import org.jboss.tools.ws.ui.bot.test.utils.ServersViewHelper;
import org.jboss.tools.ws.ui.bot.test.wsclient.WSClient;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for Sample/Simple Web Service wizard
 * 
 * @author jjankovi
 * @author Jan Richter
 *
 */
@RunWith(RedDeerSuite.class)
public class SampleSoapServicesTest extends SOAPTestBase {

	private static final String SOAP_REQUEST = getSoapRequest(
			"<ns1:sayHello xmlns:ns1=\"http://{0}/\"><arg0>{1}</arg0></ns1:sayHello>");
	private static final String SERVER_URL = "localhost:8080";

	@Override
	protected String getWsProjectName() {
		return "SampleSOAPWS";
	}

	@Test
	public void testSampleSoapWS() {
		createSampleService(getWsProjectName(), "HelloServiceSample", "sample", "SampleService");
		checkSoapService(getWsProjectName(), "HelloServiceSample", "sample", "SampleService", "You");
	}

	@Test
	public void testSimpleSoapWS() {
		createSimpleService(getWsProjectName(), "HelloServiceSimple", "sample", "SimpleService");
		checkSoapService(getWsProjectName(), "HelloServiceSimple", "sample", "SimpleService", "You");
	}

	private IProject getProject(String project) {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(project);
	}

	private IFile getDeploymentDescriptor(String project) {
		return getProject(project).getFile("WebContent/WEB-INF/web.xml");
	}

	private void createSampleService(String project, String name, String pkg, String cls) {
		SampleWebServiceWizard w = new SampleWebServiceWizard();
		w.open();
		w.setProjectName(project);
		w.setServiceName(name);
		w.setPackageName(pkg);
		w.setClassName(cls);
		w.finish();
	}

	private void createSimpleService(String project, String name, String pkg, String cls) {
		SimpleWebServiceWizard w = new SimpleWebServiceWizard();
		w.open();
		w.setProjectName(project);
		w.setServiceName(name);
		w.setPackageName(pkg);
		w.setClassName(cls);
		w.finish();
	}

	private void checkSoapService(String project, String svcName, String svcPkg, String svcClass, String msgContent) {
		TextEditor editor = new TextEditor(svcClass + ".java");
		editor.activate();
		String code = editor.getText();
		Asserts.assertContain(code, "package " + svcPkg + ";");
		String dd = ResourceHelper.readFile(getDeploymentDescriptor(project));
		Asserts.assertContain(dd, "<servlet-name>" + svcName + "</servlet-name>");
		ServersViewHelper.removeProjectFromServer(project, getConfiguredServerName());
		ServersViewHelper.runProjectOnServer(project);
		ServersViewHelper.waitForDeployment(project, getConfiguredServerName());
		try {
			WSClient c = new WSClient(new URL("http://" + SERVER_URL + "/" + project + "/" + svcName),
					new QName("http://" + svcPkg + "/", svcClass + "Service"),
					new QName("http://" + svcPkg + "/", svcClass + "Port"));
			Asserts.assertContain(c.callService(MessageFormat.format(SOAP_REQUEST, svcPkg, msgContent)),
					"Hello " + msgContent + "!");
			new WaitUntil(new ConsoleHasText("Hello: " + msgContent));
		} catch (MalformedURLException e) {
			LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
	}

	@Override
	protected String getEarProjectName() {
		return null;
	}
}
