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
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap6.DetectEAP6;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap6.OperateEAP6;
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
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(JBTSuite.class)
@Suite.SuiteClasses({
		RuntimeDuplications.class,
		ServerWithSeam.class,
		DetectJBoss7.class,
		OperateJBoss7.class,
		DetectEAP6.class,
		OperateEAP6.class,
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
		DetectEPP5.class, 
		CheckEPP5Seam.class,
		OperateEPP5.class,
		DetectEWP5.class, 
		CheckEWP5Seam.class,
		OperateEWP5.class,
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
		CheckSeam23.class
})
public class AllTestsSuite {

}
