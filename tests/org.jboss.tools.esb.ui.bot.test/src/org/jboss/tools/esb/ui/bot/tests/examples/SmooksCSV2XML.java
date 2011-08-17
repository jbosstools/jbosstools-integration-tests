package org.jboss.tools.esb.ui.bot.tests.examples;

import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

@Require(server=@Server(type=ServerType.SOA,state=ServerState.Running))
public class SmooksCSV2XML extends ESBExampleTest {
	@Override
	public String getExampleName() {
		return "JBoss ESB Smooks CSV->XML Example";
	}
	@Override
	public String[] getProjectNames() {
		return new String[] {"transform_CSV2XML","transform_CSV2XML_client"};
	}
	@Override
	protected void executeExample() {
		super.executeExample();	
		String text = executeClientGetServerOutput(getExampleClientProjectName(),"src","org.jboss.soa.esb.samples.quickstart.transformcsv2xml","SendJMSMessage.java");
		assertNotNull("Calling Send message failed, nothing appened to server log",text);	
		assertTrue("Calling Send message failed, unexpected server output :"+text,text.contains("<csv-set>"));
	}
}
