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

package org.jboss.tools.ws.ui.bot.test.webservice;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.logging.Level;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.ws.ui.bot.test.WSTestBase;
import org.jboss.tools.ws.ui.bot.test.uiutils.actions.NewFileWizardAction;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.WebServiceWizard;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.WebServiceWizard.Service_Type;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.WsWizardBase.Slider_Level;
import org.junit.Assert;

/**
 * 
 * @author jjankovi
 *
 */
public class WebServiceTestBase extends WSTestBase {

	/**
	 * 
	 * @param javasrc
	 */
	protected void bottomUpJbossWebService(InputStream javasrc) {
		String s = resourceHelper.readStream(javasrc);
		String src = MessageFormat.format(s, getWsPackage(), getWsName());
		createService(Service_Type.BOTTOM_UP, getWsPackage() + "."
				+ getWsName(), getLevel(), null, src);
	}

	/**
	 * 
	 * @param input
	 * @param pkg
	 */
	protected void topDownWS(InputStream input, String pkg) {
		String s = resourceHelper.readStream(input);
		String[] tns = getWsPackage().split("\\.");
		StringBuilder sb = new StringBuilder();
		for (int i = tns.length - 1; i > 0; i--) {
			sb.append(tns[i]);
			sb.append(".");
		}
		sb.append(tns[0]);
		String src = MessageFormat.format(s, sb.toString(), getWsName());
		createService(Service_Type.TOP_DOWN, "/" + getWsProjectName() + "/src/"
				+ getWsName() + ".wsdl", getLevel(), pkg, src);
	}

	/**
	 * 
	 * @param t
	 * @param source
	 * @param level
	 * @param pkg
	 * @param code
	 */
	private void createService(Service_Type t, String source,
			Slider_Level level, String pkg, String code) {
		// create ws source - java class or wsdl
		SWTBotEditor ed = null;
		switch (t) {
		case BOTTOM_UP:
			ed = projectHelper.createClass(getWsProjectName(), getWsPackage(), getWsName());
			break;
		case TOP_DOWN:
			ed = projectHelper.createWsdl(getWsProjectName(),getWsName());
			break;
		}
		assertNotNull(ed);
		// replace default content of java class w/ code
		SWTBotEclipseEditor st = ed.toTextEditor();
		st.selectRange(0, 0, st.getText().length());
		st.setText(code);
		ed.saveAndClose();
		// refresh workspace - workaround for JBIDE-6731
		try {
			ResourcesPlugin
					.getWorkspace()
					.getRoot()
					.refreshLocal(IWorkspaceRoot.DEPTH_INFINITE,
							new NullProgressMonitor());
		} catch (CoreException e) {
			LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
		bot.sleep(500);
		// create a web service
		new NewFileWizardAction().run()
				.selectTemplate("Web Services", "Web Service").next();
		WebServiceWizard wsw = new WebServiceWizard();
		wsw.setServiceType(t);
		wsw.setSource(source);
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
		wsw.next();
		wsw.finish();
		util.waitForNonIgnoredJobs();
		bot.sleep(2*TIME_1S);

		// let's fail if there's some error in the wizard,
		// and close error dialog and the wizard so other tests
		// can continue
		if (bot.activeShell().getText().contains("Error")) {
			SWTBotShell sh = bot.activeShell();
			String msg = sh.bot().text().getText();
			sh.bot().button(0).click();
			wsw.cancel();
			Assert.fail(msg);
		}
	}

}
