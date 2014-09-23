package org.jboss.tools.usercase.ticketmonster.ui.bot.test.part2;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.jboss.reddeer.eclipse.condition.BrowserHasURL;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.browser.BrowserEditor;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
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
	
	private String H2location = null;
	
	@Before
	public void importProject(){
		try{
			System.setProperty("jsse.enableSNIExtension", "false");
			URL website = new URL("https://github.com/jboss-developer/jboss-eap-quickstarts/raw/6.3.x-develop/h2-console/h2console.war");
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream("h2console.war");
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
			rbc.close();
			H2location = new File("h2console.war").getAbsolutePath();
			System.out.println(new File("h2console.war").getAbsolutePath());
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		createTicketMonsterEAP6();
		ProjectExplorer pe = new ProjectExplorer();
		ticketMonsterProject = pe.getProject(TICKET_MONSTER_NAME);
	}
	
	
	@Test
	public void deployTicketMonster(){
		deployProject(ticketMonsterProject.getName(), EAP_61_NAME_WITHOUT_RUNTIME);
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
		new LabeledCombo("WAR file:").setText(H2location);
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive("Import"));
		new WaitUntil(new ShellWithTextIsActive("Open Associated Perspective?"));
		new PushButton("No").click();
		new WaitWhile(new ShellWithTextIsActive("Open Associated Perspective?"));
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL, false);
	}
	
	public void deployH2Console(){
		deployProject("h2console", EAP_61_NAME_WITHOUT_RUNTIME);
		BrowserEditor browser = new BrowserEditor(new RegexMatcher("http://localhost.*"));
		new WaitUntil(new BrowserHasURL(browser, new RegexMatcher("http://localhost:8080/h2console.*")), TimePeriod.LONG);
	}
	
	

}
