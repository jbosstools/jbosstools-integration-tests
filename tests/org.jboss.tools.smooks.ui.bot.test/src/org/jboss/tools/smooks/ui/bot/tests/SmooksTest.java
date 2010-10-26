package org.jboss.tools.smooks.ui.bot.tests;

import org.jboss.tools.ui.bot.ext.SWTTestExt;


public class SmooksTest extends SWTTestExt {

	public static void prepare() {
		
		eclipse.maximizeActiveShell();
		bot.viewByTitle("Welcome").close();
	}

	public static void clean() {
		bot.sleep(10000, "All Finished");		
	}
}
