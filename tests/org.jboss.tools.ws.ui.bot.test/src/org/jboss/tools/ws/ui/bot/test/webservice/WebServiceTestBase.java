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

import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.logging.Level;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.StyledText;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceFirstWizardPage;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceSecondWizardPage;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceWizard;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceFirstWizardPage.ServiceType;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceWizardPageBase.SliderLevel;
import org.jboss.tools.ws.ui.bot.test.WSTestBase;
import org.junit.Assert;

/**
 * 
 * @author jjankovi
 *
 */
public class WebServiceTestBase extends WSTestBase {

	protected void bottomUpJbossWebService(InputStream javasrc) {
		String s = resourceHelper.readStream(javasrc);
		String src = MessageFormat.format(s, getWsPackage(), getWsName());
		createService(ServiceType.BOTTOM_UP, getWsPackage() + "."
				+ getWsName(), getLevel(), null, src, WebServiceRuntime.JBOSS_WS);
	}

	protected void topDownWS(InputStream input, WebServiceRuntime serviceRuntime, String pkg) {
		String s = resourceHelper.readStream(input);
		String[] tns = getWsPackage().split("\\.");
		StringBuilder sb = new StringBuilder();
		for (int i = tns.length - 1; i > 0; i--) {
			sb.append(tns[i]);
			sb.append(".");
		}
		sb.append(tns[0]);
		String src = MessageFormat.format(s, sb.toString(), getWsName());
		createService(ServiceType.TOP_DOWN, "/" + getWsProjectName() + "/src/"
				+ getWsName() + ".wsdl", getLevel(), pkg, src, serviceRuntime);
	}

	private void createService(ServiceType t, String source,
			SliderLevel level, String pkg, String code, WebServiceRuntime serviceRuntime) {
		// create ws source - java class or wsdl
		switch (t) {
		case BOTTOM_UP:
			TextEditor editor = projectHelper.createClass(getWsProjectName(), getWsPackage(), getWsName());
			assertNotNull(editor);

			// replace default content of java class w/ code
			editor.setText(code);
			editor.save();
			editor.close();
			break;
		case TOP_DOWN:
			DefaultEditor ed = projectHelper.createWsdl(getWsProjectName(),getWsName());
			assertNotNull(ed);
			StyledText text = new DefaultStyledText();
			assertNotNull(text);
			
			text.setText(code);
			ed.save();
			ed.close();
			break;
		}

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

		// create a web service
		WebServiceWizard wizard = new WebServiceWizard();
		wizard.open();

		WebServiceFirstWizardPage page = new WebServiceFirstWizardPage();
		page.setServiceType(t);
		page.setSource(source);
		page.setServerRuntime(getConfiguredServerName());
		page.setWebServiceRuntime(serviceRuntime.getName());
		page.setServiceProject(getWsProjectName());
		page.setServiceEARProject(getEarProjectName());
		page.setServiceSlider(level);
		if (page.isClientEnabled()) {
			page.setClientSlider(SliderLevel.NO_CLIENT);
		}
		AbstractWait.sleep(TimePeriod.SHORT);
		wizard.next();

		checkErrorDialog(wizard);

		if (pkg != null && pkg.trim().length() > 0) {
			WebServiceSecondWizardPage page2 = new WebServiceSecondWizardPage();
			page2.setPackageName(pkg);
			Button finishButton = new PushButton(IDELabel.Button.FINISH);
			wizard.next();
			new WaitUntil(new WidgetIsEnabled(finishButton));
		}

		wizard.finish();

		// let's fail if there's some error in the wizard,
		// and close error dialog and the wizard so other tests
		// can continue
		if (new DefaultShell() != null) {
			if (new DefaultShell().getText().contains("Error")) {
				String msg = new DefaultText().getText();
				new PushButton(0).click();
				wizard.cancel();
				Assert.fail(msg);	
			}
		}
	}

	private void checkErrorDialog(WizardDialog openedWizard) {
		Shell shell = new DefaultShell();
		String text = shell.getText();
		if (text.contains("Error")) {
			String msg = "<no text>";
			try {
				msg = new DefaultText().getText();
			} catch (SWTLayerException e) {

			}
			new PushButton(0).click();
			openedWizard.cancel();
			Assert.fail(text + msg);
		}
	}

}
