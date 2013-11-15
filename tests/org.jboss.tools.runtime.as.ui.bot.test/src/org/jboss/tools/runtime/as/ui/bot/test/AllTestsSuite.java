package org.jboss.tools.runtime.as.ui.bot.test;

import org.jboss.tools.runtime.as.ui.bot.test.detector.RuntimeDuplications;
import org.jboss.tools.runtime.as.ui.bot.test.detector.ServerWithSeam;
import org.jboss.tools.runtime.as.ui.bot.test.detector.seam.seam22.CheckSeam22;
import org.jboss.tools.runtime.as.ui.bot.test.detector.seam.seam22.DetectSeam22;
import org.jboss.tools.runtime.as.ui.bot.test.detector.seam.seam23.CheckSeam23;
import org.jboss.tools.runtime.as.ui.bot.test.detector.seam.seam23.DetectSeam23;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap4.CheckEAP4Seam;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap4.DetectEAP4;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap4.OperateEAP4;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap51.CheckEAP51Seam;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap51.DetectEAP51;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap51.OperateEAP51;
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
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.epp4.CheckEPP4Seam;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.epp4.DetectEPP4;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.epp4.OperateEPP4;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.epp5.CheckEPP5Seam;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.epp5.DetectEPP5;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.epp5.OperateEPP5;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.ewp5.CheckEWP5Seam;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.ewp5.DetectEWP5;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.ewp5.OperateEWP5;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.jboss7.DetectJBoss7;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.jboss7.OperateJBoss7;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.jpp60.DetectJPP60;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.jpp60.OperateJPP60;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap52.CheckSOAP52Seam;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap52.DetectSOAP52;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap52.OperateSOAP52;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap52.standalone.DetectSOAPStandalone52;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap52.standalone.OperateSOAPStandalone52;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap53.CheckSOAP53Seam;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap53.DetectSOAP53;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap53.OperateSOAP53;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap53.standalone.DetectSOAPStandalone53;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap53.standalone.OperateSOAPStandalone53;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly8.DetectWildFly8;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly8.OperateWildFly8;
import org.jboss.tools.runtime.as.ui.bot.test.download.RuntimeDownload;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(JBTSuite.class)
@Suite.SuiteClasses({
		RuntimeDuplications.class,
		ServerWithSeam.class,
		RuntimeDownload.class,
		
		// Needs Java SE 1.7
		DetectWildFly8.class,
		OperateWildFly8.class,
		
		DetectJBoss7.class,
		OperateJBoss7.class,
		
		DetectEAP60.class,
		OperateEAP60.class,
		
		DetectEAP60x.class,
		OperateEAP60x.class,
		
		DetectEAP61.class,
		OperateEAP61.class,
		
		DetectEAP61x.class,
		OperateEAP61x.class,
		
		DetectEAP62.class,
		OperateEAP62.class,
		
		// Needs Java SE 1.6
		DetectEAP51.class, 
		CheckEAP51Seam.class,
		OperateEAP51.class, 
		
		DetectEAP52.class, 
		CheckEAP52Seam.class,
		OperateEAP52.class, 
		
		DetectEAP4.class, 
		CheckEAP4Seam.class,
		OperateEAP4.class,
		
		DetectEPP4.class, 
		CheckEPP4Seam.class,
		OperateEPP4.class,

		// Needs Java SE 1.6		
		DetectEPP5.class, 
		CheckEPP5Seam.class,
		OperateEPP5.class,
		
		DetectJPP60.class, 
		OperateJPP60.class,
		
		// Needs Java SE 1.6
		DetectEWP5.class, 
		CheckEWP5Seam.class,
		OperateEWP5.class,
		
		// Needs Java SE 1.6
		DetectSOAP52.class, 
		CheckSOAP52Seam.class,
		OperateSOAP52.class, 
		DetectSOAPStandalone52.class, 
		OperateSOAPStandalone52.class,
		
		DetectSOAP53.class, 
		CheckSOAP53Seam.class,
		OperateSOAP53.class, 
		DetectSOAPStandalone53.class, 
		OperateSOAPStandalone53.class,
		
		DetectSeam22.class,
		CheckSeam22.class,
		DetectSeam23.class,
		CheckSeam23.class,
})
public class AllTestsSuite {

}
