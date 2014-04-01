package org.jboss.tools.openshift.ui.bot.test.app;

import java.util.Date;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.openshift.ui.bot.test.OpenShiftBotTest;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RepublishApp extends OpenShiftBotTest {

	private final String DYI_APP = "diyapp" + new Date().getTime();
	
	
	private final String text = "<!doctype html> <html lang=\"en\"> <head> <meta charset=\"utf-8\"> " +
			"<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">  <title>Welcome to OpSh</title>" +
			" <style> </head> <body> </body> </html>";
	
	@Before
	public void createDYIApp() {
		createOpenShiftApplication(DYI_APP, OpenShiftLabel.AppType.DIY);
	}
	
	@Test
	public void canModifyAndRepublishApp() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		
		Project project = projectExplorer.getProjects().get(0);
		project.select();
		project.getProjectItem("diy", "index.html").open();
		
		TextEditor editor = new TextEditor("index.html");
		editor.setText(text);
		editor.save();
		editor.close();
		
		projectExplorer.open();
		projectExplorer.getProject(DYI_APP).select();
		new ContextMenu("Team", "Commit...").select();
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable("Identify Yourself"), TimePeriod.LONG);
			new DefaultShell("Identify Yourself").setFocus();
			new PushButton("OK").click();
		} catch (WaitTimeoutExpiredException ex) {}
		
		new WaitUntil(new ShellWithTextIsAvailable("Commit Changes"), TimePeriod.LONG);
		
		new DefaultShell("Commit Changes").setFocus();		
		new DefaultStyledText().setText("Commit");
		new PushButton("Commit and Push").click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		
		new WaitUntil(new ShellWithTextIsAvailable("Push Results: " + DYI_APP + " - origin"));
		new DefaultShell("Push Results: " + DYI_APP + " - origin").setFocus();
		new PushButton("OK").click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		TreeItem connection = new DefaultTree().getAllItems().get(0);
		connection.select();
		new ContextMenu("Refresh").select();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		connection.expand();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		connection.getItems().get(0).expand();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		connection.getItems().get(0).getItems().get(0).select();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		//new ContextMenu("Show in Web Browser").select();
		//new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		//TODO verify title with Browser Editor
	}

	@After
	public void deleteDIYApp() {
		deleteOpenShiftApplication(DYI_APP, OpenShiftLabel.AppType.DIY_TREE);
	}
	
}
