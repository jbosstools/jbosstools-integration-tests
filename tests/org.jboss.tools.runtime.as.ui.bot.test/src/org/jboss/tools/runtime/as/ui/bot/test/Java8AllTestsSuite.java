package org.jboss.tools.runtime.as.ui.bot.test;

import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap63.DetectEAP63;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap63.OperateEAP63;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap64.DetectEAP64;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap64.OperateEAP64;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap70.DetectEAP70;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap70.OperateEAP70;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly10.DetectWildFly10;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly10.OperateWildFly10;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly10web.DetectWildFly10Web;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly10web.OperateWildFly10Web;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly81.DetectWildFly81;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly81.OperateWildFly81;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly82.DetectWildFly82;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly82.OperateWildFly82;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly90.DetectWildFly90;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly90.OperateWildFly90;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly90web.DetectWildFly90Web;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly90web.OperateWildFly90Web;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Tests with Java SE 1.8 requirement
 * 
 * @author Radoslav Rabara
 */
@RunWith(JBTSuite.class)
@Suite.SuiteClasses({
	
	DetectEAP70.class,
	OperateEAP70.class,
	
	DetectEAP64.class,
	OperateEAP64.class,
	
	DetectEAP63.class,
	OperateEAP63.class,
	
	DetectWildFly81.class,
	OperateWildFly81.class,
	
	DetectWildFly82.class,
	OperateWildFly82.class,
	
	DetectWildFly90.class,
	OperateWildFly90.class,
	
	DetectWildFly90Web.class,
	OperateWildFly90Web.class,
	
	DetectWildFly10.class,
	OperateWildFly10.class,
	
	DetectWildFly10Web.class,
	OperateWildFly10Web.class
})
public class Java8AllTestsSuite {

}
