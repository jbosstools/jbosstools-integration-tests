package org.jboss.tools.openshift.ui.condition;

import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;

/**
 * 
 * Wait condition for application deployment.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ApplicationIsDeployedSuccessfully implements WaitCondition {

	private String expectedText;
	
	private OpenShiftExplorerView explorer;
	
	public ApplicationIsDeployedSuccessfully(String username, String domain, String appName, 
			String expectedText) {
		this.expectedText = expectedText;
		
		 explorer = new OpenShiftExplorerView();
		 explorer.open();
		 
		 explorer.selectApplication(username, domain, appName);
		 
		 AbstractWait.sleep(TimePeriod.getCustom(7));
		 
		 new ContextMenu(OpenShiftLabel.ContextMenu.SHOW_IN_BROWSER).select();
	}
	
	@Override
	public boolean test() {
		return new InternalBrowser().getText().contains(expectedText);
	}

	@Override
	public String description() {
		return "application is deployed successfully";
	}
}
