package org.jboss.tools.portlet.ui.bot.test.tmp;

import org.jboss.tools.portlet.ui.bot.test.testcase.SWTTaskBasedTestCase;
import org.jboss.tools.ui.bot.ext.config.Annotations.DB;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.Test;

@Require(db=@DB)
public class TmpTest extends SWTTaskBasedTestCase {

	@Test
	public void testTmp() throws InterruptedException{
		System.out.println();
		Thread.sleep(60 * 60 * 1000);
	}
}
