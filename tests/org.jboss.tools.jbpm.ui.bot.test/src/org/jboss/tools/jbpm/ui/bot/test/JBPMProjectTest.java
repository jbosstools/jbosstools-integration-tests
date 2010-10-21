package org.jboss.tools.jbpm.ui.bot.test;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.jbpm.ui.bot.test.suite.JBPMTest;
import org.jboss.tools.jbpm.ui.bot.test.suite.Project;
import org.jboss.tools.ui.bot.ext.config.Annotations.JBPM;
import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.config.TestConfigurator;
import org.jboss.tools.ui.bot.ext.types.EntityType;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;

@SWTBotTestRequires( jbpm=@JBPM(), server=@Server(type=ServerType.SOA,state=ServerState.Present), perspective="jBPM jPDL 3")
public class JBPMProjectTest extends JBPMTest {

	@Test
	public void createProject() {
		
		// Create project
		eclipse.createNew(EntityType.JBPM3_PROJECT);
		bot.textWithLabel("Project name:").setText(Project.PROJECT_NAME);
		bot.clickButton(IDELabel.Button.NEXT);
				
		String rtName = "JBPM-"+TestConfigurator.currentConfig.getJBPM().version;
		
		// There is some bug related to undefined runtime even it's defined 
		/*
		bot.textWithLabel("Name :").setText(rtName);
		String rtHome = TestConfigurator.currentConfig.getJBPM().jbpmHome;
		bot.sleep(TIME_5S);
		
		bot.textWithLabel("Location :").setText(rtHome);			
		
		String msg3 = "Press next to continue the project creation";
				
		try {
			bot.text(msg3);
		} catch(WidgetNotFoundException e) {
			fail("Missing confirmation during jbpm runtime definition text");
		}
				
		bot.clickButton(IDELabel.Button.NEXT);
		*/
		
		bot.comboBox().setSelection(rtName);
		
		
		SWTBotShell wizard = bot.activeShell();
		bot.clickButton(IDELabel.Button.FINISH);
		eclipse.waitForClosedShell(wizard);
		util.waitForNonIgnoredJobs();
	}	
}
