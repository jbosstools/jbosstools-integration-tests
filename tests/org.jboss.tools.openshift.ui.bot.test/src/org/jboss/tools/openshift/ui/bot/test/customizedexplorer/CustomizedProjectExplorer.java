package org.jboss.tools.openshift.ui.bot.test.customizedexplorer;


/**
 * Similar to RedDeer ProjectExplorer but works with customized projects, 
 * which allow to work with git based projects.
 * 
 * @author mlabuda@redhat.com
 *
 */
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
