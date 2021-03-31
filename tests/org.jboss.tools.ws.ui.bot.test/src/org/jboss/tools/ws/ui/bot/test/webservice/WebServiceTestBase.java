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

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.wizard.WizardDialog;
import org.eclipse.reddeer.requirements.autobuilding.AutoBuildingRequirement.AutoBuilding;
import org.eclipse.reddeer.swt.api.Shell;
import org.eclipse.reddeer.swt.api.StyledText;
import org.eclipse.reddeer.swt.condition.ControlIsEnabled;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.exception.SWTLayerException;
import org.eclipse.reddeer.swt.impl.button.FinishButton;
import org.eclipse.reddeer.swt.impl.button.NextButton;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceFirstWizardPage;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceFirstWizardPage.ServiceType;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceSecondWizardPage;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceWizard;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceWizardPageBase.SliderLevel;
import org.jboss.tools.ws.ui.bot.test.soap.SOAPTestBase;
import org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper;
import org.jboss.tools.ws.ui.bot.test.utils.ResourceHelper;
import org.jboss.tools.ws.ui.bot.test.utils.ServersViewHelper;
import org.junit.After;
import org.junit.Assert;

/**
 * Base class for Web Service wizard tests
 * 
 * @author jjankovi
 *
 */
@AutoBuilding(value = false, cleanup = true)
public abstract class WebServiceTestBase extends SOAPTestBase {

	protected abstract String getWsPackage();

	protected abstract String getWsName();
	
	@After
	public void undeploy() {
		ServersViewHelper.removeAllProjectsFromServer(getConfiguredServerName());
	}
	
	protected void createBottomUpWS(InputStream input, WebServiceRuntime serviceRuntime, boolean useDefaultProjects) {
		createService(ServiceType.BOTTOM_UP, getWsPackage() + "."
				+ getWsName(), getLevel(), null, input, serviceRuntime, useDefaultProjects);
	}

	protected void createTopDownWS(InputStream input, WebServiceRuntime serviceRuntime, String pkg, boolean useDefaultProjects) {
		createService(ServiceType.TOP_DOWN, "/" + getWsProjectName() + "/src/main/java/"
				+ getWsName() + ".wsdl", getLevel(), pkg, input, serviceRuntime, useDefaultProjects);
	}	

	private void createService(ServiceType type, String source,
			SliderLevel level, String pkg, InputStream input, WebServiceRuntime serviceRuntime, boolean useDefaultProjects) {
		
		createSource(type, input);
		ProjectHelper.cleanAllProjects();

		WebServiceWizard wizard = new WebServiceWizard();
		wizard.open();
		fillFirstWizardPage(wizard, type, source, level, serviceRuntime, useDefaultProjects);
		
		new WaitUntil(new ControlIsEnabled(new NextButton()), TimePeriod.getCustom(5), false);
		wizard.next();

		checkErrorDialog(wizard);

		if (pkg != null && pkg.trim().length() > 0) {
			WebServiceSecondWizardPage page2 = new WebServiceSecondWizardPage(wizard);
			page2.setPackageName(pkg);
			wizard.next();
		}

		if (pkg == null && type == ServiceType.TOP_DOWN && getLevel() == SliderLevel.ASSEMBLE) {
			new FinishButton().click();
			confirmWebServiceNameOverwrite();
			
			new WaitWhile(new ShellIsAvailable("Web Service"));
			new WaitWhile(new JobIsRunning());
		} else {
			new DefaultShell("Web Service");
			wizard.finish();
		}

		checkWizardErrors(wizard);
	}

	/**
	 * Let's fail if there's some error in the wizard,
	 * and close error dialog and the wizard so other tests can continue
	 * @param wizard
	 */
	private void checkWizardErrors(WebServiceWizard wizard) {		
		DefaultShell activeShell = new DefaultShell();
		if (activeShell != null) {
			String shellText = activeShell.getText();
			if (shellText.contains("Error")) {
				new PushButton(0).click();
				wizard.cancel();
				Assert.fail(shellText);	
			}
		}
	}

	private void fillFirstWizardPage(ReferencedComposite referencedComposite, ServiceType type, String source, SliderLevel level,
			WebServiceRuntime serviceRuntime, boolean useDefaultProjects) {
		
		WebServiceFirstWizardPage page = new WebServiceFirstWizardPage(referencedComposite);
		page.setServiceType(type);
		page.setSource(source);
		page.setServerRuntime(getConfiguredServerName());
		page.setWebServiceRuntime(serviceRuntime.getName());
		if (!useDefaultProjects) {
			page.setServiceProject(getWsProjectName());
			page.setServiceEARProject(getEarProjectName());
		}
		new DefaultShell("Web Service");
		page.setServiceSlider(level);
		if (page.isClientEnabled()) {
			page.setClientSlider(SliderLevel.NO_CLIENT);
		}
	}

	private void createSource(ServiceType type, InputStream input) {
		String source = getServiceSource(type, input);
		switch (type) {
			case BOTTOM_UP:
				TextEditor editor = ProjectHelper.createClass(getWsProjectName(), getWsPackage(), getWsName());
				assertNotNull(editor);

				// replace default content of java class w/ code
				editor.setText(source);
				editor.save();
				editor.close();
				break;
			case TOP_DOWN:
				DefaultEditor ed = ProjectHelper.createWsdl(getWsProjectName(),getWsName());
				assertNotNull(ed);
				StyledText text = new DefaultStyledText();
				assertNotNull(text);
			
				text.setText(source);
				ed.save();
				ed.close();
				break;
		}
	}
	
	private String getServiceSource(ServiceType type, InputStream input) {
		String source = ResourceHelper.readStream(input);
		switch (type) {
			case BOTTOM_UP:
				return MessageFormat.format(source, getWsPackage(), getWsName());		
			case TOP_DOWN:
				return MessageFormat.format(source, getTopDownReplacement(), getWsName());				
			default:
				throw new IllegalArgumentException("Unknown service type.");
		}
	}
	
	private String getTopDownReplacement() {
		String[] tns = getWsPackage().split("\\.");
		StringBuilder sb = new StringBuilder();
		for (int i = tns.length - 1; i > 0; i--) {
			sb.append(tns[i]);
			sb.append(".");
		}
		sb.append(tns[0]);
		return sb.toString();
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

	private void confirmWebServiceNameOverwrite() {
		// look up shell
		try {
			new DefaultShell("Confirm Web Service Name Overwrite");
			new OkButton().click();
		} catch(SWTLayerException e) {
			LOGGER.log(Level.SEVERE, "No \"Confirm Web Service Name Overwrite\" dialog found!", e);
			return;
		}
	}
}
