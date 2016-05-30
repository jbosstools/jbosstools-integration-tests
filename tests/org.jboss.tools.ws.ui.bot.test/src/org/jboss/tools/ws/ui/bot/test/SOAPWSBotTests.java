package org.jboss.tools.ws.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.ws.ui.bot.test.cxf.CxfWsClientTest;
import org.jboss.tools.ws.ui.bot.test.integration.SOAPWSToolingIntegrationTest;
import org.jboss.tools.ws.ui.bot.test.sample.SampleSoapServicesTest;
import org.jboss.tools.ws.ui.bot.test.webservice.BottomUpWSTest;
import org.jboss.tools.ws.ui.bot.test.webservice.TopDownWSTest;
import org.jboss.tools.ws.ui.bot.test.wsclient.WsClientTest;
import org.jboss.tools.ws.ui.bot.test.wstester.WsTesterTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Complete test suite for SOAP web services
 * 
 * @author Jan Richter
 *
 */
@RunWith(RedDeerSuite.class)
@SuiteClasses({
	SampleSoapServicesTest.class,
	SOAPWSToolingIntegrationTest.class,
	BottomUpWSTest.class,
	TopDownWSTest.class, 
	WsClientTest.class, 
	WsTesterTest.class,
	CxfWsClientTest.class
})
public class SOAPWSBotTests {

}
