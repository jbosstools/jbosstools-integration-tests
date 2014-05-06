package org.jboss.tools.openshift.ui.bot.test.customizedexplorer;

/**
 * Customized navigator. Similar to RedDeer navigator but can works with
 * git projects.
 * @author mlabuda@redhat.com
 *
 */
public class CustomizedNavigator extends CustomizedExplorer{

	public CustomizedNavigator() {
		super("Navigator");
		open();
	}

}
