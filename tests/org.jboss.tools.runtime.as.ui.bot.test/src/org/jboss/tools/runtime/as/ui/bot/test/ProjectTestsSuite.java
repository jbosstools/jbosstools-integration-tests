package org.jboss.tools.runtime.as.ui.bot.test;

import org.jboss.tools.runtime.as.ui.bot.test.detector.seam.seam22.CheckSeam22;
import org.jboss.tools.runtime.as.ui.bot.test.detector.seam.seam22.DetectSeam22;
import org.jboss.tools.runtime.as.ui.bot.test.detector.seam.seam23.CheckSeam23;
import org.jboss.tools.runtime.as.ui.bot.test.detector.seam.seam23.DetectSeam23;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.jboss7.DetectJBoss7;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.jboss7.OperateJBoss7;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly8.DetectWildFly8;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly8.OperateWildFly8;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly81.DetectWildFly81;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly81.OperateWildFly81;
import org.jboss.tools.runtime.as.ui.bot.test.download.RuntimeDownload;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Projects tests suite<br/>
 * 
 * Testing Projects runtimes.
 * 
 * @author Petr Suchy
 * @author Radoslav Rabara
 */
@RunWith(JBTSuite.class)
@SuiteClasses({
		RuntimeDownload.class,
		
		DetectWildFly81.class,
		OperateWildFly81.class,
		
		DetectWildFly8.class,
		OperateWildFly8.class,
		
		DetectJBoss7.class,
		OperateJBoss7.class,
		
		DetectSeam23.class,
		CheckSeam23.class,
		
		DetectSeam22.class,
		CheckSeam22.class
})
public class ProjectTestsSuite {

}
