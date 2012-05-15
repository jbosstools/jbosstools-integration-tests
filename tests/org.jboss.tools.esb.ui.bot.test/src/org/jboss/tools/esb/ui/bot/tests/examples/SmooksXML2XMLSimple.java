package org.jboss.tools.esb.ui.bot.tests.examples;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

@Require(server=@Server(type=ServerType.SOA,state=ServerState.Running))
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
		SWTTestExt.servers.removeAllProjectsFromServer();
		super.executeExample();	
		String text = executeClientGetServerOutput(getExampleClientProjectName(),"src","org.jboss.soa.esb.samples.quickstart.transformxml2xmlsimple.test","SendJMSMessage.java");
		bot.sleep(Timing.time3S());
		assertFalse ("Test fails due to ESB deployment error: NNNN", text.contains("ERROR [org.apache.juddi.v3.client.transport.wrapper.RequestHandler]"));
		assertNotNull("Calling Send message failed, nothing appened to server log",text);	
		assertTrue("Calling Send message failed, unexpected server output :"+text,text.contains("<Order"));
		
		SWTTestExt.servers.removeAllProjectsFromServer();		

		/* Close the open, but empty "Quick Fix" dialog - https://issues.jboss.org/browse/JBIDE-11781 */
		try {
			SWTTestExt.bot.shell("Quick Fix").close();
		}
		catch (Exception E) {
			System.out.println("Condition from https://issues.jboss.org/browse/JBIDE-11781 not found " + E.getMessage());
		}
		//SWTTestExt.bot.closeAllShells(); // https://issues.jboss.org/browse/JBIDE-11781
	}
}
