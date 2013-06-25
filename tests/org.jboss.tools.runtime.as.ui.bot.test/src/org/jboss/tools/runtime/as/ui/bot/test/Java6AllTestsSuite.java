package org.jboss.tools.runtime.as.ui.bot.test;

import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap51.CheckEAP51Seam;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap51.DetectEAP51;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap51.OperateEAP51;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.epp5.CheckEPP5Seam;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.epp5.DetectEPP5;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.epp5.OperateEPP5;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.ewp5.CheckEWP5Seam;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.ewp5.DetectEWP5;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.ewp5.OperateEWP5;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap52.CheckSOAP52Seam;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap52.DetectSOAP52;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap52.OperateSOAP52;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap52.standalone.DetectSOAPStandalone52;
import org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap52.standalone.OperateSOAPStandalone52;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Tests with Java SE 1.6 requirement only
 * 
 * @author Petr Suchy
 *
 */
@RunWith(JBTSuite.class)
@Suite.SuiteClasses({
	
		DetectEAP51.class, 
		CheckEAP51Seam.class,
		OperateEAP51.class, 
		
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
	
})
public class Java6AllTestsSuite {

}
