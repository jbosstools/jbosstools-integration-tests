package org.jboss.tools.jbpm.ui.bot.test.suite;

import org.jboss.tools.ui.bot.ext.SWTTestExt;

public class JBPMTest extends SWTTestExt {

	public static void prepare() {
		log.info("jBPM All Test started...");
				
		eclipse.maximizeActiveShell();
	}

	public static void clean() {
		util.waitForNonIgnoredJobs();
		bot.sleep(TIME_5S, "jBPM All Tests Finished!");
	}

}
