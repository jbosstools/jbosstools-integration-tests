package org.jboss.tools.runtime.as.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly10.DetectWildFly10;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly10.OperateWildFly10;
import org.jboss.tools.runtime.as.ui.bot.test.download.SmokeRuntimeDownload;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
	SmokeRuntimeDownload.class,
	
	DetectWildFly10.class,
	OperateWildFly10.class
})
public class SmokeSuite {

}
