package org.jboss.tools.openshift.ui.bot.test.customizedexplorer;



public class CustomizedProjectExplorer extends CustomizedExplorer {

	public CustomizedProjectExplorer() {
		super("Project Explorer");
		open();
	}
		
	public void reopen() {
		close();
		open();
	}
}
