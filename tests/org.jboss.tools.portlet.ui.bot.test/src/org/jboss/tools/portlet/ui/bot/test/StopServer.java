package org.jboss.tools.portlet.ui.bot.test;

import org.jboss.tools.portlet.ui.bot.test.testcase.SWTTaskBasedTestCase;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Seam;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.junit.Test;

@Require(server=@Server(version="5.1.0", state=ServerState.NotRunning), seam=@Seam)
public class StopServer extends SWTTaskBasedTestCase {

	@Test
	public void testStop(){
		System.out.println("Stopping ++++++++++++++++");
	}
}
