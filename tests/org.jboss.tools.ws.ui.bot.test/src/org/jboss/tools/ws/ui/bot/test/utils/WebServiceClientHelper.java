package org.jboss.tools.ws.ui.bot.test.utils;

import static org.junit.Assert.fail;

import org.eclipse.swt.SWTException;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.Label;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceClientWizard;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceClientWizardPage;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceWizardPageBase.SliderLevel;
import org.jboss.tools.ws.ui.bot.test.webservice.WebServiceRuntime;
import org.junit.Assert;

public class WebServiceClientHelper {

	private WebServiceClientHelper() {};
	
	/**
	 * Method creates Web Service Client for entered wsdl file, web project,
	 * level of creation and name of package for client
	 * @param wsdl
	 * @param targetProject
	 * @param level
	 * @param pkg
	 */
	public static void createClient(String serverName, String wsdl,
			WebServiceRuntime runtime, String targetProject,
			String earProject, SliderLevel level, String pkg) {
		WebServiceClientWizard wizard = new WebServiceClientWizard();
		wizard.open();

		WebServiceClientWizardPage page = new WebServiceClientWizardPage();
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL, false);
		
		page.setSource(wsdl);
		new WaitUntil(new WebServiceClientPageIsValidated(), TimePeriod.getCustom(2), true);

		page.setClientSlider(level);
		page.setServerRuntime(serverName);
		page.setWebServiceRuntime(runtime.getName());
		
		if (targetProject != null)
			page.setClientProject(targetProject);
		if (earProject != null)
			page.setClientEARProject(earProject);

		if (pkg != null && pkg.trim().length()>0) {
			wizard.next();
			new WaitWhile(new ShellWithTextIsAvailable("Progress Information"));
			page.setPackageName(pkg);
		}

		wizard.finish();

		checkErrorDialog(wizard);

		//check if there is any error in console output
		checkErrorInConsoleOutput(serverName, earProject);
	}

	/**
	 * let's fail if there's some error in the wizard,
	 * and close error dialog and the wizard so other tests
	 * can continue
	 * 
	 * @param wsWizard if error dialog appeared the parent wizard will be closed
	 */
	private static void checkErrorDialog(WizardDialog wsWizard) {
		Shell shell = new DefaultShell();
		String text = shell.getText();
		if (text.contains("Error")) {
			new PushButton(0).click();
			wsWizard.cancel();
			Assert.fail(text);
		}
	}

	private static void checkErrorInConsoleOutput(String serverName, String projectName) {
		ConsoleView consoleView = new ConsoleView();
		if (!consoleView.isOpened()) {
			consoleView.open();
		}
		selectServerConsole(serverName, consoleView);
		
		String consoleText = null;
		try {
			consoleText = consoleView.getConsoleText();
		} catch (CoreLayerException ex) {
			consoleView.activate();
			consoleText = consoleView.getConsoleText();
		}
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

	private static void selectServerConsole(String serverName, ConsoleView view) {
		view.activate();
		Label consoleName = null;
		try {
			consoleName = new DefaultLabel();
		} catch (CoreLayerException ex) {
			view.activate();
			consoleName = new DefaultLabel();
		}
		if (!consoleName.getText().contains(serverName)) {
			new DefaultToolItem("Display Selected Console").click();
			consoleName = new DefaultLabel();
			if (!consoleName.getText().contains(serverName)) {
				fail("Console of configured server was not found.");
			}
		}
	}

	/**
	 *  
	 * @param serverName
	 * @param projectName
	 * @return
	 * @throws SWTException
	 */
	public static boolean projectIsDeployed(String serverName, String projectName) {
		try {
			ServersView sw = new ServersView();
			sw.getServer(serverName).getModule(projectName);
			return true;
		} catch(EclipseLayerException e) {
			return false;
		}
	}

	private static class WebServiceClientPageIsValidated extends AbstractWaitCondition {

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
