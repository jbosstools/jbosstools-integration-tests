package org.jboss.tools.runtime.as.ui.bot.test;

import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly82.DetectWildFly82;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly82.OperateWildFly82;
import org.jboss.tools.runtime.as.ui.bot.test.download.SmokeRuntimeDownload;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(JBTSuite.class)
@Suite.SuiteClasses({
	SmokeRuntimeDownload.class,
	
	DetectWildFly82.class,
	OperateWildFly82.class
})
public class SmokeSuite {

}
