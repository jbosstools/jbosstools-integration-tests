package org.jboss.tools.esb.ui.bot.tests.examples;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

@Require(server=@Server(type=ServerType.SOA,state=ServerState.Running),perspective="Java")
public class WebServiceProducer extends ESBExampleTest {
	@Override
	public String getExampleName() {
		return "JBoss ESB Web Service producer Example";
	}
	public String[] getProjectNames() {
		return new String[] {"webservice_producer","webservice_producer_client"};
	}
	@Override
	protected void executeExample() {
		super.executeExample();	
		
		String text = executeClientGetServerOutput("org.jboss.soa.esb.samples.quickstart.webserviceproducer.test.SendMessage","jms");
		assertNotNull("Calling Send message failed, nothing appened to server log",text);	
		assertTrue("Calling Send message failed, unexpected server output :"+text,text.contains("Goodbye!!"));	

		text = executeClientGetServerOutput("org.jboss.soa.esb.samples.quickstart.webserviceproducer.test.SendMessage","http 8765");
		assertNotNull("Calling Send message failed, nothing appened to server log",text);	
		assertTrue("Calling Send message failed, unexpected server output :"+text,text.contains("Goodbye!!"));

		text = executeClientGetServerOutput("org.jboss.soa.esb.samples.quickstart.webserviceproducer.test.SendMessage","socket 8888");
		assertNotNull("Calling Send message failed, nothing appened to server log",text);	
		assertTrue("Calling Send message failed, unexpected server output :"+text,text.contains("Goodbye!!"));
		
		SWTTestExt.servers.removeAllProjectsFromServer();
	}
}
