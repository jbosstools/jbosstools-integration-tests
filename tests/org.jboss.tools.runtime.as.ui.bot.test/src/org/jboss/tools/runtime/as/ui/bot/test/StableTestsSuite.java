package org.jboss.tools.runtime.as.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.runtime.as.ui.bot.test.detector.cdk2.DetectCDK2;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap60x.DetectEAP60x;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap60x.OperateEAP60x;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap61x.DetectEAP61x;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap61x.OperateEAP61x;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap62.DetectEAP62;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap62.OperateEAP62;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap62x.DetectEAP62x;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap62x.OperateEAP62x;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap63.DetectEAP63;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap63.OperateEAP63;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap64.DetectEAP64;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap64.OperateEAP64;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap70.DetectEAP70;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap70.OperateEAP70;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.jboss7.DetectJBoss7;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.jboss7.OperateJBoss7;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly10.DetectWildFly10;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly10.OperateWildFly10;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly10web.DetectWildFly10Web;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly10web.OperateWildFly10Web;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly8.DetectWildFly8;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly8.OperateWildFly8;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly81.DetectWildFly81;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly81.OperateWildFly81;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly90.DetectWildFly90;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly90.OperateWildFly90;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly90web.DetectWildFly90Web;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly90web.OperateWildFly90Web;
import org.jboss.tools.runtime.as.ui.bot.test.download.ProductRuntimeDownload;
import org.jboss.tools.runtime.as.ui.bot.test.download.ProjectRuntimeDownload;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Stable Tests Suite
 * 
 * Contains all mavenized tests
 * 
 * @author Radoslav Rabara
 */
@RunWith(RedDeerSuite.class)
@SuiteClasses({
		ProjectRuntimeDownload.class,
		ProductRuntimeDownload.class,
		
		DetectCDK2.class,
	
		DetectWildFly10.class,
		OperateWildFly10.class,
		
		DetectWildFly10Web.class,
		OperateWildFly10Web.class,
		
		DetectWildFly90.class,
		OperateWildFly90.class,

		DetectWildFly90Web.class,
		OperateWildFly90Web.class,
		
		DetectWildFly81.class,
		OperateWildFly81.class,
		
		DetectWildFly8.class,
		OperateWildFly8.class,
		
		DetectJBoss7.class,
		OperateJBoss7.class,
		
		DetectEAP70.class,
		OperateEAP70.class,
		
		DetectEAP64.class,
		OperateEAP64.class,
		
		DetectEAP63.class,
		OperateEAP63.class,
		
		DetectEAP62x.class,
		OperateEAP62x.class,
		
		DetectEAP62.class,
		OperateEAP62.class,
		
		DetectEAP61x.class,
		OperateEAP61x.class,
		
		DetectEAP60x.class,
		OperateEAP60x.class
})
public class StableTestsSuite {

}
