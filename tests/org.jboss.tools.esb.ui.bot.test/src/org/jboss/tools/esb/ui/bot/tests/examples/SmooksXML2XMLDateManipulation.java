package org.jboss.tools.esb.ui.bot.tests.examples;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

@Require(server=@Server(type=ServerType.SOA,state=ServerState.Running))
public class SmooksXML2XMLDateManipulation extends ESBExampleTest {
	@Override
	public String getExampleName() {
		return "JBoss ESB Smooks XML->XML date-manipulation Example";
	}
	public String[] getProjectNames() {
		return new String[] {"transform_XML2XML_date_manipulation","transform_XML2XML_date_manipulation_client"};
	}
	@Override
	protected void executeExample() {
		super.executeExample();	
		String text = executeClientGetServerOutput(getExampleClientProjectName(),"src","org.jboss.soa.esb.samples.quickstart.transformxml2xmldatemanipulation.test","SendJMSMessage.java");
		bot.sleep(Timing.time3S());
		SWTTestExt.servers.removeAllProjectsFromServer();
		assertNotNull("Calling Send message failed, nothing appened to server log",text);	
		assertTrue("Calling Send message failed, unexpected server output :"+text,text.contains("OrderDate"));
	}
}
