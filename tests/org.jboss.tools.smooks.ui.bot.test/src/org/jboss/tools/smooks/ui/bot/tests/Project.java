package org.jboss.tools.smooks.ui.bot.tests;

import org.eclipse.swt.widgets.MessageBox;

public class Project {
	// Smooks project
	public static final String PROJECT_NAME = "smookstest1";
	public static final String SMOOKS_PATH_VAR_NAME = "jbosstools.test.smooks.1.2.4";
	public static final String SMOOKS_PATH;
	public static final String SMOOKS_LIBNAME = "smooks-1.2.4";
	
	static {
		SMOOKS_PATH = System.getProperty(SMOOKS_PATH_VAR_NAME);
		if(SMOOKS_PATH==null) {
			throw new IllegalStateException(java.text.MessageFormat.format("System property {''0''} is not defined", SMOOKS_PATH_VAR_NAME));
		}
	}
}
