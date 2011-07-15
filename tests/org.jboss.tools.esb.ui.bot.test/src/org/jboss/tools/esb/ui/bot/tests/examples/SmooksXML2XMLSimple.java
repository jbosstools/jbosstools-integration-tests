package org.jboss.tools.esb.ui.bot.tests.examples;

import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

@SWTBotTestRequires(server=@Server(type=ServerType.SOA,state=ServerState.Running))
public class SmooksXML2XMLSimple extends ESBExampleTest {
	@Override
	public String getExampleName() {
		return "JBoss ESB Smooks XML->XML Example";
	}
	public String[] getProjectNames() {
		return new String[] {"transform_XML2XML_simple","transform_XML2XML_simple_client"};
	}
	@Override
	protected void executeExample() {
		super.executeExample();	
		String text = executeClientGetServerOutput(getExampleClientProjectName(),"src","org.jboss.soa.esb.samples.quickstart.transformxml2xmlsimple.test","SendJMSMessage.java");
		bot.sleep(Timing.time3S());
		assertNotNull("Calling Send message failed, nothing appened to server log",text);	
		assertTrue("Calling Send message failed, unexpected server output :"+text,text.contains("<Order"));
	}
}
