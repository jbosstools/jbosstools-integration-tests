package org.jboss.tools.ws.ui.bot.test.utils;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.eclipse.swt.SWTException;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.api.Label;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ws.ui.bot.test.uiutils.actions.NewFileWizardAction;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.WebServiceClientWizard;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.Wizard;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.WsWizardBase.Slider_Level;
import org.jboss.tools.ws.ui.bot.test.webservice.WebServiceRuntime;
import org.junit.Assert;

public class WebServiceClientHelper extends SWTTestExt {

	private static final Logger log = Logger.getLogger(WebServiceClientHelper.class.getName());
	
	/**
	 * Method creates Web Service Client for entered wsdl file, web project,
	 * level of creation and name of package for client
	 * @param wsdl
	 * @param targetProject
	 * @param level
	 * @param pkg
	 */
	public void createClient(String wsdl, WebServiceRuntime runtime, String targetProject,
			String earProject, Slider_Level level, String pkg) {
		
		final String SERVER_NAME = configuredState.getServer().name;
		
		new NewFileWizardAction().run()
				.selectTemplate("Web Services", "Web Service Client").next();
		WebServiceClientWizard w = new WebServiceClientWizard();
		w.setSource(wsdl);
		util.waitForNonIgnoredJobs();
		bot.sleep(1000);
		w.setSlider(level, 0);
		w.setServerRuntime(SERVER_NAME);
		w.setWebServiceRuntime(runtime.getName());
		w.setClientProject(targetProject);
		w.setClientEARProject(earProject);
		if (pkg != null && pkg.trim().length()>0) {
			w.next();
			w.setPackageName(pkg);
		}
		w.finish();
		util.waitForNonIgnoredJobs();
		bot.sleep(1000);

		checkErrorDialog(w);
		
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
	private void checkErrorDialog(Wizard wsWizard) {
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
			try {
				if(projectIsDeployed(serverName, projectName)) {
					deploymentInfoMessage += "deployed";
				} else {
					deploymentInfoMessage += "NOT DEPLOYED";
				}
				deploymentInfoMessage += "]";
			} catch(SWTException e) {
				deploymentInfoMessage = ""; //there is no information about deployment status
			}
			
			fail("Console contains error"
					+ deploymentInfoMessage
					+ "\n" + consoleText);
		}
	}
	
	private void selectServerConsole(String serverName) {
		Label consoleName = new DefaultLabel();
		if (!consoleName.getText().startsWith(serverName)) {
			new DefaultToolItem("Display Selected Console").click();
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
		} catch(SWTException e) {
			log.log(Priority.ERROR, "Server#getModule(String) is still not fixed! "
					+ "Can't verify if the specified module '" + projectName + "' is deployed.");
			throw e;
		}
	}
}
