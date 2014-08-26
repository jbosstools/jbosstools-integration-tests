package org.jboss.tools.ws.ui.bot.test.utils;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWTException;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.Label;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.toolbar.ViewToolItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceClientWizard;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceClientWizardPage;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceWizardPageBase.SliderLevel;
import org.jboss.tools.ws.ui.bot.test.webservice.WebServiceRuntime;
import org.junit.Assert;

public class WebServiceClientHelper extends SWTTestExt {

	/**
	 * Method creates Web Service Client for entered wsdl file, web project,
	 * level of creation and name of package for client
	 * @param wsdl
	 * @param targetProject
	 * @param level
	 * @param pkg
	 */
	public void createClient(String wsdl, WebServiceRuntime runtime, String targetProject,
			String earProject, SliderLevel level, String pkg) {
		final String SERVER_NAME = configuredState.getServer().name;

		WebServiceClientWizard wizard = new WebServiceClientWizard();
		wizard.open();

		WebServiceClientWizardPage page = new WebServiceClientWizardPage();
		
		page.setSource(wsdl);
		new WaitUntil(new WebServiceClientPageIsValidated(), TimePeriod.getCustom(2), true);

		page.setClientSlider(level);
		page.setServerRuntime(SERVER_NAME);
		page.setWebServiceRuntime(runtime.getName());
		page.setClientProject(targetProject);
		page.setClientEARProject(earProject);

		if (pkg != null && pkg.trim().length()>0) {
			wizard.next();
			page.setPackageName(pkg);
		}
		wizard.finish();

		checkErrorDialog(wizard);

		//check if there is any error in console output
		checkErrorInConsoleOutput(SERVER_NAME, earProject);
	}

	/**
	 * let's fail if there's some error in the wizard,
	 * and close error dialog and the wizard so other tests
	 * can continue
	 * 
	 * @param wsWizard if error dialog appeared the parent wizard will be closed
	 */
	private void checkErrorDialog(WizardDialog wsWizard) {
		Shell shell = new DefaultShell();
		String text = shell.getText();
		if (text.contains("Error")) {
			new PushButton(0).click();
			wsWizard.cancel();
			Assert.fail(text);
		}
	}

	private void checkErrorInConsoleOutput(String serverName, String projectName) {
		ConsoleView consoleView = new ConsoleView();
		consoleView.open();
		selectServerConsole(serverName);
		String consoleText = consoleView.getConsoleText();
		if (consoleText.contains("ERROR")) {
			consoleView.clearConsole();
			String deploymentInfoMessage = " [deployment status: ";
			if(projectIsDeployed(serverName, projectName)) {
				deploymentInfoMessage += "deployed";
			} else {
				deploymentInfoMessage += "NOT DEPLOYED";
			}
			deploymentInfoMessage += "]";
			
			fail("Console contains error"
					+ deploymentInfoMessage
					+ "\n" + consoleText);
		}
	}

	private void selectServerConsole(String serverName) {
		Label consoleName = new DefaultLabel();
		if (!consoleName.getText().startsWith(serverName)) {
			new ViewToolItem("Display Selected Console").click();
			consoleName = new DefaultLabel();
			if (!consoleName.getText().startsWith(serverName)) {
				fail("Console of configured server was not found.");
			}
		}
	}

	/**
	 * TODO: SWTException is thrown by {@link Server#getModule(String)} because
	 * it doesn't work properly. Remove throws clause when it will be fixed.
	 * 
	 * @param serverName
	 * @param projectName
	 * @return
	 * @throws SWTException
	 */
	public boolean projectIsDeployed(String serverName, String projectName) throws SWTException {
		try {
			ServersView sw = new ServersView();
			sw.getServer(serverName).getModule(projectName);
			return true;
		} catch(EclipseLayerException e) {
			return false;
		}
	}

	private class WebServiceClientPageIsValidated implements WaitCondition {

		private WebServiceClientWizardPage page = new WebServiceClientWizardPage();

		private static final String REQUIRED_TEXT = "Select a service definition and move the slider to set the level of client generation.";
		private String infoText;
		
		@Override
		public boolean test() {
			infoText = page.getInfoText();
			return infoText.equals(REQUIRED_TEXT);
		}

		@Override
		public String description() {
			return "Required text is '" + REQUIRED_TEXT + "' but there is '" + infoText + "'";
		}
		
	}
}
