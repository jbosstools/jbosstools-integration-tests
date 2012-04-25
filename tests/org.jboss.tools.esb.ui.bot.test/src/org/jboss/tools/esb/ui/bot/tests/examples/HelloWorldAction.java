package org.jboss.tools.esb.ui.bot.tests.examples;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

@Require(server=@Server(type=ServerType.SOA,state=ServerState.Running))
public class HelloWorldAction extends ESBExampleTest {
	@Override
	public String getExampleName() {
		return "JBoss ESB HelloWorld Action Example - ESB";
	}
	@Override
	public String[] getProjectNames() {
		return new String[] {"helloworld_action","helloworld_action_client"};
	}

	@Override
	protected void executeExample() {
		super.executeExample();
		String text = executeClientGetServerOutput(getExampleClientProjectName(),"src","org.jboss.soa.esb.samples.quickstart.helloworldaction.test","SendJMSMessage.java");
		assertNotNull("Calling JMS Send message failed, nothing appened to server log",text);	
		assertTrue("Calling JMS Send message failed, unexpected server output :"+text,text.contains("Hello World Action ESB invoked!"));	
		SWTTestExt.servers.removeAllProjectsFromServer();	
	}
}
