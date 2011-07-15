package org.jboss.tools.esb.ui.bot.tests.examples;

import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.junit.AfterClass;

@SWTBotTestRequires(server=@Server(type=ServerType.SOA,state=ServerState.Running))
public class HelloWorld extends ESBExampleTest {
	@Override
	public String getExampleName() {
		return "JBoss ESB HelloWorld Example - ESB";
	}
	@Override
	public String[] getProjectNames() {
		return new String[] {"helloworld","helloworld_testclient"};
	}
	@AfterClass
	public static void waitaminute() {
		//bot.sleep(Long.MAX_VALUE);
	}

	@Override
	protected void executeExample() {
		super.executeExample();	
		String text = executeClientGetServerOutput(getExampleClientProjectName(),"src","org.jboss.soa.esb.samples.quickstart.helloworld.test","SendJMSMessage.java");
		assertNotNull("Calling JMS Send message failed, nothing appened to server log",text);	
		assertTrue("Calling JMS Send message failed, unexpected server output :"+text,text.contains("Body: Hello World"));
		text = null;
		text = executeClientGetServerOutput(getExampleClientProjectName(),"src","org.jboss.soa.esb.samples.quickstart.helloworld.test","SendEsbMessage.java");
		assertNotNull("Calling ESB Send message failed, nothing appened to server log",text);	
		assertTrue("Calling ESB Send message failed, unexpected server output :"+text,text.contains("hello world esb"));
		
	}
}
