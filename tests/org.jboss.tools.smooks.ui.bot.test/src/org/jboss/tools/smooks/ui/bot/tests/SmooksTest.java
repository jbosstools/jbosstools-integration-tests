package org.jboss.tools.smooks.ui.bot.tests;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.TestConfigurator;
import org.jboss.tools.ui.bot.ext.helper.UserLibraryHelper;


public class SmooksTest extends SWTTestExt {

	public static void prepare() {
		
		eclipse.maximizeActiveShell();
		
		// prepare smooks user library 
		String libPath = TestConfigurator.currentConfig.getProperty("LIB.SMOOKS");
		String[] jarList = UserLibraryHelper.getJarList(libPath);
		UserLibraryHelper.addUserLibrary("smooks", jarList );		
	}

	public static void clean() {
		bot.sleep(1000, "All Finished");		
	}
}
