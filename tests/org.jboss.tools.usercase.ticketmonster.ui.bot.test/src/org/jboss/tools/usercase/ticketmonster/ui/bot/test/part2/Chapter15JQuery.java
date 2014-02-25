package org.jboss.tools.usercase.ticketmonster.ui.bot.test.part2;

import static org.junit.Assert.*;


import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.browser.BrowserView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.editor.DefaultEditor;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.browsersim.BrowsersimHandler;
import org.junit.Before;
import org.junit.Test;

public class Chapter15JQuery extends AbstractPart2Test{
	
	private Project ticketMonsterProject;
	
	@Before
	public void importProject(){
		createTicketMonsterEAP6();
		createEAPServerRuntime();
		deployProject(TICKET_MONSTER_NAME, "JBoss EAP 6.1+ Runtime Server");
		ProjectExplorer pe = new ProjectExplorer();
		ticketMonsterProject = pe.getProject(TICKET_MONSTER_NAME);
		ticketMonsterProject.select();
		ticketMonsterProject.getTreeItem().doubleClick(); //expand does not work
	}
	//should start server if its not runinng & deploy ticketmonster if its not
	@Test
	public void createMobileHTMLFile(){
		ticketMonsterProject.getProjectItem("src","main","webapp").select();
		new ContextMenu("New", "HTML File").select();
		new WaitUntil(new ShellWithTextIsActive("New HTML File"));
		assertEquals(TICKET_MONSTER_NAME+"/src/main/webapp",new LabeledText("Enter or select the parent folder:").getText());
		new LabeledText("File name:").setText("mobile.html");
		new PushButton("Next >").click();
		new DefaultTable().select("HTML5 jQuery Mobile Page ");
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive("New HTML File"));
		new DefaultEditor("mobile.html");
		assertEquals("<!DOCTYPE html> ",new DefaultStyledText().getText().split("\n")[0]);
		ServersView sview = new ServersView();
		sview.open();
		sview.getServers().get(0).publish();
		BrowserView browser = new BrowserView();
		browser.open();
		browser.openPageURL("http://localhost:8080/ticket-monster/mobile.html");
		BrowsersimHandler bs =new BrowsersimHandler();
		bs.openBrowsersim("http://localhost:8080/ticket-monster/mobile.html");
		String jQueryContent = fileToString("resources/browsersim/JQueryMobileContent.txt");
		assertEquals(jQueryContent, bs.getBrowserSimBrowser());
		bs.close();
	}

}
