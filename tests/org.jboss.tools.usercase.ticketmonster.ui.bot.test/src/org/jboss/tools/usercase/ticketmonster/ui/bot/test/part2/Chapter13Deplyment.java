package org.jboss.tools.usercase.ticketmonster.ui.bot.test.part2;

import org.jboss.reddeer.eclipse.condition.BrowserHasURL;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.browser.BrowserEditor;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.junit.Before;
import org.junit.Test;

public class Chapter13Deplyment extends AbstractPart2Test{
	
	private Project ticketMonsterProject;
	public static final String H2GIT = "https://github.com/jboss-developer/jboss-eap-quickstarts.git";
	public static final String H2location = System.getProperty("h2consolelocation");
	
	@Before
	public void importProject(){
		createTicketMonsterEAP6();
		ProjectExplorer pe = new ProjectExplorer();
		ticketMonsterProject = pe.getProject(TICKET_MONSTER_NAME);
		ticketMonsterProject.select();
		ticketMonsterProject.getTreeItem().doubleClick(); //expand does not work
		createEAPServerRuntime();
	}
	
	
	@Test
	public void deployTicketMonster(){
		deployProject(ticketMonsterProject.getName(), "JBoss EAP 6.1+ Runtime Server");
		BrowserEditor browser = new BrowserEditor(new RegexMatcher("http://localhost.*"));
		new WaitUntil(new BrowserHasURL(browser, "http://localhost:8080/ticket-monster/index.jsf"), TimePeriod.LONG);
		importH2Console();
		deployH2Console();
	}
	
	public void importH2Console(){
		new ProjectExplorer().open();
		new ShellMenu("File","Import...").select();
		new DefaultShell("Import");
		new DefaultTreeItem("Web","WAR file").select();
		new PushButton("Next >").click();
		new DefaultCombo("WAR file:").setText(H2location +"h2console.war");
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive("Import"));
		new WaitUntil(new ShellWithTextIsActive("Open Associated Perspective?"));
		new PushButton("No").click();
		new WaitWhile(new ShellWithTextIsActive("Open Associated Perspective?"));
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL, false);
	}
	
	public void deployH2Console(){
		deployProject("h2console", "JBoss EAP 6.1+ Runtime Server");
		BrowserEditor browser = new BrowserEditor(new RegexMatcher("http://localhost.*"));
		new WaitUntil(new BrowserHasURL(browser, new RegexMatcher("http://localhost:8080/h2console.*")), TimePeriod.LONG);
	}
	
	

}
