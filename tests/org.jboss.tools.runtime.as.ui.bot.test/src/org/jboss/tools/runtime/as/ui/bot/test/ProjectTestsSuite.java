package org.jboss.tools.runtime.as.ui.bot.test;

import org.jboss.tools.runtime.as.ui.bot.test.detector.seam.seam22.CheckSeam22;
import org.jboss.tools.runtime.as.ui.bot.test.detector.seam.seam22.DetectSeam22;
import org.jboss.tools.runtime.as.ui.bot.test.detector.seam.seam23.CheckSeam23;
import org.jboss.tools.runtime.as.ui.bot.test.detector.seam.seam23.DetectSeam23;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap60x.DetectEAP60x;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap60x.OperateEAP60x;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap61x.DetectEAP61x;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap61x.OperateEAP61x;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap62.DetectEAP62;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap62.OperateEAP62;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.jboss7.DetectJBoss7;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.jboss7.OperateJBoss7;
import org.jboss.tools.runtime.as.ui.bot.test.download.RuntimeDownload;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(JBTSuite.class)
@SuiteClasses({
		RuntimeDownload.class,
		
		DetectJBoss7.class,
		OperateJBoss7.class,
		
		DetectEAP60x.class,
		OperateEAP60x.class,
		
		DetectEAP61x.class,
		OperateEAP61x.class,
		
		DetectEAP62.class,
		OperateEAP62.class,
		
		DetectSeam22.class,
		CheckSeam22.class,
		DetectSeam23.class,
		CheckSeam23.class
})
public class ProjectTestsSuite {

}
