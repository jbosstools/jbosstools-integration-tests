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
import org.jboss.tools.ws.ui.bot.test.WSTestBase;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.WsWizardBase.Slider_Level;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test operates on Web Service Client Wizard
 * @author jlukas
 *
 */
public class WsClientTest extends WSTestBase {

	@Override
	protected String getWsProjectName() {
		return "client";
	}

	@Override
	protected String getWsPackage() {
		return "client." + getLevel().toString().toLowerCase();
	}

	@Test
	public void testDeployClient() {
		setLevel(Slider_Level.DEPLOY);
		clientTest(getWsPackage());
	}
	
	@Test
	public void testAssembleClient() {
		setLevel(Slider_Level.ASSEMBLE);
		clientTest(getWsPackage());
	}
	
	@Test
	public void testDevelopClient() {
		setLevel(Slider_Level.DEVELOP);
		clientTest(getWsPackage());
	}
	
	@Test
	public void testInstallClient() {
		setLevel(Slider_Level.INSTALL);
		clientTest(getWsPackage());
	}
	
	@Test
	public void testStartClient() {
		setLevel(Slider_Level.START);
		clientTest(getWsPackage());
	}
	
	@Test
	public void testTestClient() {
		setLevel(Slider_Level.TEST);
		clientTest(getWsPackage());
	}
	
	@Test
	public void testDefaultPkg() {
		setLevel(Slider_Level.ASSEMBLE);
		clientTest(null);
	}

	private void clientTest(String targetPkg) {
		clientHelper.createClient("http://footballpool.dataaccess.eu/data/info.wso?WSDL", getWsProjectName(), getLevel(), targetPkg);
		IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject(getWsProjectName());
		String pkg = (targetPkg != null && !"".equals(targetPkg.trim())) ? getWsPackage() : "eu.dataaccess.footballpool";
		String cls = "src/" + pkg.replace('.', '/') + "/Info.java";
		Assert.assertTrue(p.getFile(cls).exists());
		cls = "src/" + pkg.replace('.', '/') + "/clientsample/ClientSample.java";
		Assert.assertTrue(p.getFile(cls).exists());
	}
}
