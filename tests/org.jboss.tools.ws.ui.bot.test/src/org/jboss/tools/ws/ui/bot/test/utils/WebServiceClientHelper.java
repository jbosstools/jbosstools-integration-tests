package org.jboss.tools.ws.ui.bot.test.utils;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ws.ui.bot.test.uiutils.actions.NewFileWizardAction;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.WebServiceClientWizard;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.WsWizardBase.Slider_Level;
import org.junit.Assert;

/**
 * 
 * @author jjankovi
 *
 */
public class WebServiceClientHelper extends SWTTestExt {

	/**
	 * 
	 * @param wsdl
	 * @param targetProject
	 * @param level
	 * @param pkg
	 */
	public void createClient(String wsdl, String targetProject,
			Slider_Level level, String pkg) {
		new NewFileWizardAction().run()
				.selectTemplate("Web Services", "Web Service Client").next();
		WebServiceClientWizard w = new WebServiceClientWizard();
		w.setSource(wsdl);
		util.waitForNonIgnoredJobs();
		bot.sleep(1000);
		w.setSlider(level, 0);
		w.setServerRuntime(configuredState.getServer().name);
		w.setWebServiceRuntime("JBossWS");
		w.setClientProject(targetProject);
		if (pkg != null && !"".equals(pkg.trim())) {
			w.next();
			w.setPackageName(pkg);
		}
		w.finish();
		util.waitForNonIgnoredJobs();
		bot.sleep(1000);

		// let's fail if there's some error in the wizard,
		// and close error dialog and the wizard so other tests
		// can continue
		if (bot.activeShell().getText().contains("Error")) {
			SWTBotShell sh = bot.activeShell();
			String msg = sh.bot().text().getText();
			sh.bot().button(0).click();
			w.cancel();
			Assert.fail(msg);
		}
	}
	
}
