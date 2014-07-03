package org.jboss.tools.openshift.ui.bot.test.condition;

import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;

public class DeployedApplicationContainsText implements WaitCondition {

	private String appName;
	private String expectedText;
	
	private OpenShiftExplorerView explorer;
	
	public DeployedApplicationContainsText(String appName, String expectedText) {
		this.appName = appName;
		this.expectedText = expectedText;
		
		 explorer = new OpenShiftExplorerView();
		 explorer.open();
		 
		 explorer.getApplication(appName).select();
		 
		 new ContextMenu("Show In", "Web Browser").select();
	}
	
	@Override
	public boolean test() {
		new InternalBrowser().refresh();
		return new InternalBrowser().getText().contains(expectedText);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
