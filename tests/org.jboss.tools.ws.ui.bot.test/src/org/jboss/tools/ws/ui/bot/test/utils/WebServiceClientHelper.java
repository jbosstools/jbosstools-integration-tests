package org.jboss.tools.ws.ui.bot.test.utils;

import static org.junit.Assert.fail;

import org.eclipse.swt.SWTException;
import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.exception.CoreLayerException;
import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.eclipse.exception.EclipseLayerException;
import org.eclipse.reddeer.eclipse.ui.console.ConsoleView;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersView2;
import org.eclipse.reddeer.jface.wizard.WizardDialog;
import org.eclipse.reddeer.swt.api.Label;
import org.eclipse.reddeer.swt.api.Shell;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.label.DefaultLabel;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
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

		WebServiceClientWizardPage page = new WebServiceClientWizardPage(wizard);
		new WaitWhile(new JobIsRunning(), TimePeriod.DEFAULT, false);
		
		page.setSource(wsdl);
		new WaitUntil(new WebServiceClientPageIsValidated(page), TimePeriod.getCustom(2), true);

		page.setClientSlider(level);
		page.setServerRuntime(serverName);
		page.setWebServiceRuntime(runtime.getName());
		
		if (targetProject != null)
			page.setClientProject(targetProject);
		if (earProject != null)
			page.setClientEARProject(earProject);

		if (pkg != null && pkg.trim().length()>0) {
			wizard.next();
			new WaitWhile(new ShellIsAvailable("Progress Information"));
			new DefaultShell("Web Service Client");
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
		if (!consoleView.isOpen()) {
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
			if (!consoleName.getText().contains(serverName) && !consoleName.getText().contains("Apache CXF")) {
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
			ServersView2 sw = new ServersView2();
			sw.getServer(serverName).getModule(projectName);
			return true;
		} catch(EclipseLayerException e) {
			return false;
		}
	}

	private static class WebServiceClientPageIsValidated extends AbstractWaitCondition {
		
		private WebServiceClientWizardPage page;
		private static final String REQUIRED_TEXT = "Select a service definition and move the slider to set the level of client generation.";
		private String infoText;
		
		public WebServiceClientPageIsValidated(WebServiceClientWizardPage page) {
			this.page = page;
		}
		
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
