package org.jboss.tools.openshift.ui.bot.test.application;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.test.customizedexplorer.CustomizedProject;
import org.jboss.tools.openshift.ui.bot.test.customizedexplorer.CustomizedProjectExplorer;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * Test republishing of an application. Simple DIY application is created, then
 * web site text is changed and pushed to OpenShift. Finally it is verified
 * whether, whether changes has been successfully published.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class RepublishApplication {

	private final String DIY_APP = "diyapp" + new Date().getTime();
	
	
	public static final String TEXT = "<!doctype html> <html lang=\"en\"> <head> <meta charset=\"utf-8\"> " +
			"<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">  <title>Welcome to OpSh</title>" +
			" </head> <body>OpSh</body> </html>";
	
	@Before
	public void createDYIApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.DIY, DIY_APP, false, true, true);
	}
	
	@Test
	public void canModifyAndRepublishApp() {
		CustomizedProjectExplorer projectExplorer = new CustomizedProjectExplorer();
		projectExplorer.open();
		
		CustomizedProject project = projectExplorer.getProject(DIY_APP);
		project.select();
		project.openFile("diy", "index.html");
		
		TextEditor editor = new TextEditor("index.html");
		editor.setText(TEXT);
		editor.save();
		editor.close();
		
		projectExplorer.open();
		projectExplorer.getProject(DIY_APP).select();
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
		
		new WaitUntil(new ShellWithTextIsAvailable("Push Results: " + DIY_APP + " - origin"));
		new DefaultShell("Push Results: " + DIY_APP + " - origin").setFocus();
		new PushButton("OK").click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();;
		explorer.getConnection().select();
		
		new ContextMenu("Refresh").select();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		assertTrue("Verification failed, browser does not contain required text", 
				explorer.verifyApplicationInBrowser(DIY_APP, "OpSh"));
	}

	@After
	public void deleteDIYApp() {
		new DeleteApplication(DIY_APP).perform();
	}
	
}
