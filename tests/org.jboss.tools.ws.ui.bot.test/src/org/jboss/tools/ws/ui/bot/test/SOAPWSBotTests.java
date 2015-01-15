package org.jboss.tools.ws.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.ws.ui.bot.test.cxf.CxfWsClientTest;
import org.jboss.tools.ws.ui.bot.test.integration.SOAPWSToolingIntegrationTest;
import org.jboss.tools.ws.ui.bot.test.sample.test.SampleSoapWebServiceTest;
import org.jboss.tools.ws.ui.bot.test.sample.test.SimpleWebServiceTest;
import org.jboss.tools.ws.ui.bot.test.webservice.BottomUpWSTest;
import org.jboss.tools.ws.ui.bot.test.webservice.TopDownWSTest;
import org.jboss.tools.ws.ui.bot.test.webservice.eap.EAPFromJavaTest;
import org.jboss.tools.ws.ui.bot.test.webservice.eap.EAPFromWSDLTest;
import org.jboss.tools.ws.ui.bot.test.wsclient.WsClientTest;
import org.jboss.tools.ws.ui.bot.test.wstester.WsTesterTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(RedDeerSuite.class)
@SuiteClasses({
	SampleSoapWebServiceTest.class,
	SimpleWebServiceTest.class,
	SOAPWSToolingIntegrationTest.class,
	BottomUpWSTest.class,
	TopDownWSTest.class, 
	WsClientTest.class, 
	WsTesterTest.class, 
	EAPFromJavaTest.class,
	EAPFromWSDLTest.class,
	
	CxfWsClientTest.class
})
public class SOAPWSBotTests extends AbstractTestSuite {

}
