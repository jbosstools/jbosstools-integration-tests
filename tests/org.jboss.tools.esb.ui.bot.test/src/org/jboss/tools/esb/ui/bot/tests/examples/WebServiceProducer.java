package org.jboss.tools.esb.ui.bot.tests.examples;

import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

@SWTBotTestRequires(server=@Server(type=ServerType.SOA,state=ServerState.Running),perspective="Java")
public class WebServiceProducer extends ESBExampleTest {
	@Override
	public String getExampleName() {
		return "JBoss ESB Web Service producer Example";
	}
	@Override
	public String getExampleProjectName() {
		return "webservice_producer";
	}
	@Override
	public String getExampleClientProjectName() {
		return "webservice_producer_client";
	}
	
	@Override
	protected void executeExample() {
		super.executeExample();	
		String text = executeClientGetServerOutput("org.jboss.soa.esb.samples.quickstart.webserviceproducer.test.SendMessage","jms");
		assertNotNull("Calling Send message failed, nothing appened to server log",text);	
		assertTrue("Calling Send message failed, unexpected server output :"+text,text.contains("Goodbye!!"));
		fail("testing method http not yet implemented");
	}
}
