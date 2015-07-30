package org.jboss.tools.runtime.as.ui.bot.test;

import org.jboss.tools.runtime.as.ui.bot.test.detector.RuntimeDuplications;
import org.jboss.tools.runtime.as.ui.bot.test.detector.ServerWithSeam;
import org.jboss.tools.runtime.as.ui.bot.test.detector.seam.seam22.CheckSeam22;
import org.jboss.tools.runtime.as.ui.bot.test.detector.seam.seam22.DetectSeam22;
import org.jboss.tools.runtime.as.ui.bot.test.detector.seam.seam23x.CheckSeam23x;
import org.jboss.tools.runtime.as.ui.bot.test.detector.seam.seam23x.DetectSeam23x;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap4.CheckEAP4Seam;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap4.DetectEAP4;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap4.OperateEAP4;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap52.CheckEAP52Seam;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap52.DetectEAP52;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap52.OperateEAP52;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap60.DetectEAP60;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap60.OperateEAP60;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap60x.DetectEAP60x;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap60x.OperateEAP60x;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap61.DetectEAP61;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap61.OperateEAP61;
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
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.epp4.CheckEPP4Seam;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.epp4.DetectEPP4;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.epp4.OperateEPP4;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.fsw60.DetectFSW60;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.fsw60.OperateFSW60;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.jboss7.DetectJBoss7;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.jboss7.OperateJBoss7;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.jpp60.DetectJPP60;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.jpp60.OperateJPP60;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.jpp61.DetectJPP61;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.jpp61.OperateJPP61;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.jpp61x.DetectJPP61x;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.jpp61x.OperateJPP61x;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap53.CheckSOAP53Seam;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap53.DetectSOAP53;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap53.OperateSOAP53;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap53.standalone.DetectSOAPStandalone53;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap53.standalone.OperateSOAPStandalone53;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly8.DetectWildFly8;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly8.OperateWildFly8;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly81.DetectWildFly81;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly81.OperateWildFly81;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly82.DetectWildFly82;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly82.OperateWildFly82;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly90.DetectWildFly90;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly90.OperateWildFly90;
import org.jboss.tools.runtime.as.ui.bot.test.download.ProductRuntimeDownload;
import org.jboss.tools.runtime.as.ui.bot.test.download.ProjectRuntimeDownload;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Tests with Java SE 1.7 requirement
 * 
 * @author Petr Suchy
 * @author Radoslav Rabara
 */
@RunWith(JBTSuite.class)
@Suite.SuiteClasses({

		RuntimeDuplications.class,
		ServerWithSeam.class,
		ProjectRuntimeDownload.class,
		ProductRuntimeDownload.class,
		
		DetectWildFly90.class,
		OperateWildFly90.class,
		
		DetectWildFly81.class,
		OperateWildFly81.class,
		
		DetectWildFly82.class,
		OperateWildFly82.class,
		
		DetectWildFly8.class,
		OperateWildFly8.class,
		
		DetectJBoss7.class,
		OperateJBoss7.class,
		
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
		
		DetectEAP61.class,
		OperateEAP61.class,
		
		DetectEAP60x.class,
		OperateEAP60x.class,
		
		DetectEAP60.class,
		OperateEAP60.class,
		
		DetectEAP52.class, 
		CheckEAP52Seam.class,
		OperateEAP52.class, 
		
		DetectEAP4.class,
		CheckEAP4Seam.class,
		OperateEAP4.class,
		
		DetectEPP4.class, 
		CheckEPP4Seam.class,
		OperateEPP4.class,
		
		DetectJPP61x.class,
		OperateJPP61x.class,
		
		DetectJPP61.class,
		OperateJPP61.class,
		
		DetectJPP60.class,
		OperateJPP60.class,
		
		DetectSOAP53.class, 
		CheckSOAP53Seam.class,
		OperateSOAP53.class, 
		DetectSOAPStandalone53.class, 
		OperateSOAPStandalone53.class,
		
		DetectSeam23x.class,
		CheckSeam23x.class,
		
		DetectSeam22.class,
		CheckSeam22.class,
		
		DetectFSW60.class,
		OperateFSW60.class
})
public class Java7AllTestsSuite {

}
