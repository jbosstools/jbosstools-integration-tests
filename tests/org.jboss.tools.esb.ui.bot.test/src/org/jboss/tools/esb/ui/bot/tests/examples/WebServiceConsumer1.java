package org.jboss.tools.esb.ui.bot.tests.examples;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

@Require(server=@Server(type=ServerType.SOA,state=ServerState.Running))
public class WebServiceConsumer1 extends ESBExampleTest {
	@Override
	public String getExampleName() {
		return "JBoss ESB Web Service consumer1 Example";
	}
	public String[] getProjectNames() {
		return new String[] {"webservice_consumer1","webservice_consumer1_client"};
	}
	@Override
	protected void executeExample() {
		super.executeExample();	
		String text = executeClientGetServerOutput(getExampleClientProjectName(),"src","org.jboss.soa.esb.samples.quickstart.webservice_consumer1.test","SendJMSMessage.java");
		assertNotNull("Calling JMS Send message failed, nothing appened to server log",text);	
		assertTrue("Calling JMS Send message failed, unexpected server output :"+text,text.contains("Hello World Greeting for"));
		text = null;
		text = executeClientGetServerOutput(getExampleClientProjectName(),"src","org.jboss.soa.esb.samples.quickstart.webservice_consumer1.test","SendEsbMessage.java");
		assertNotNull("Calling ESB Send message failed, nothing appened to server log",text);	
		assertTrue("Calling ESB Send message failed, unexpected server output :"+text,text.contains("Hello World Greeting for"));
		SWTTestExt.servers.removeAllProjectsFromServer();
	}
}
