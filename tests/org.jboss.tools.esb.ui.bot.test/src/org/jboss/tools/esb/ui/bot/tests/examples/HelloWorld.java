package org.jboss.tools.esb.ui.bot.tests.examples;

import java.io.IOException;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.junit.AfterClass;
import org.junit.Test;
//import org.jboss.tools.test.util.ScreenRecorderExt;

@Require(server=@Server(type=ServerType.SOA,state=ServerState.Running))
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
		assertFalse ("Test fails due to ESB deployment error: NNNN", text.contains("ERROR [org.apache.juddi.v3.client.transport.wrapper.RequestHandler]"));
		assertNotNull("Calling JMS Send message failed, nothing appened to server log",text);	
		assertTrue("Calling JMS Send message failed, unexpected server output :"+text,text.contains("Body: Hello World"));
		text = null;
		
//		ScreenRecorderExt screenRecorderExt = null;
//		try {
//			screenRecorderExt = new ScreenRecorderExt();
//			screenRecorderExt.start("screen_record.mov");
//		}
//		catch (Exception E) {
//			E.printStackTrace();
//		}
				
		text = executeClientGetServerOutput(getExampleClientProjectName(),"src","org.jboss.soa.esb.samples.quickstart.helloworld.test","SendEsbMessage.java");
		assertFalse ("Test fails due to ESB deployment error: NNNN", text.contains("ERROR [org.apache.juddi.v3.client.transport.wrapper.RequestHandler]"));
		assertNotNull("Calling ESB Send message failed, nothing appened to server log",text);
		assertTrue("Calling ESB Send message failed, unexpected server output :"+text,text.contains("hello world esb"));
	
		SWTTestExt.servers.removeAllProjectsFromServer();
		
//		try {
//			screenRecorderExt.stop();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
}
