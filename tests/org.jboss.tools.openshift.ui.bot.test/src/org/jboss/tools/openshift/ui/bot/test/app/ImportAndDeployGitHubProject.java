package org.jboss.tools.openshift.ui.bot.test.app;

import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.jboss.tools.openshift.ui.bot.util.TestUtils;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.test.OpenShiftBotTest;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This test class consists of 2 test cases:
 * - existing project can be deployed to OpenShift
 * - openShift Maven profile is added to pom.xml when enabling OpenShift deployment
 * 
 * @author mlabuda
 *
 */
public class ImportAndDeployGitHubProject {

	@Before
	public void importApplication() {
		TestUtils.cleanupGitFolder("jboss-eap-application");
		new ShellMenu("File", "Import...").select();
		
		new WaitUntil(new ShellWithTextIsAvailable("Import"), TimePeriod.LONG);
		
		new DefaultShell("Import").setFocus();
		new DefaultTreeItem("Git", "Projects from Git").select();
		
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.NEXT)), TimePeriod.NORMAL);
		new PushButton(OpenShiftLabel.Button.NEXT).click();;
		
		new DefaultTree().selectItems(new DefaultTreeItem("Clone URI")); 
		
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.NEXT)), TimePeriod.NORMAL);
		new PushButton(OpenShiftLabel.Button.NEXT).click();;
		
		new LabeledText("URI:").setText(
				"https://github.com/mlabuda/jboss-eap-application.git");
		
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.NEXT)), TimePeriod.NORMAL);
		new PushButton(OpenShiftLabel.Button.NEXT).click();;
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.NEXT)), TimePeriod.NORMAL);
		new PushButton(OpenShiftLabel.Button.NEXT).click();;
		
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.NEXT)), TimePeriod.NORMAL);
		new PushButton(OpenShiftLabel.Button.NEXT).click();;
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.NEXT)), TimePeriod.NORMAL);
		new PushButton(OpenShiftLabel.Button.NEXT).click();;
		
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.FINISH)), TimePeriod.LONG);
		new PushButton(OpenShiftLabel.Button.FINISH).click();;
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	@Test
	public void testDeployGitApp() {
		deployGitApp(OpenShiftLabel.AppType.JBOSS_EAP);

		checkOpenShiftMavenProfile();
	}
		
	private void deployGitApp(String appType) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		DefaultTree connection = new DefaultTree(0);
		connection.setFocus();
		connection.getItems().get(0).expand();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		connection.getItems().get(0).select();
		new ContextMenu("New", "Application...").select();
		
		new WaitUntil(new ShellWithTextIsAvailable("New OpenShift Application"), TimePeriod.LONG);
		
		new DefaultShell("New OpenShift Application").setFocus();
		if (!(new RadioButton(1).isSelected())) {
			new RadioButton(1).click();
		}
		
		Iterator<TreeItem> iterator = new DefaultTree().getAllItems().iterator();
		while(iterator.hasNext()) {
			TreeItem cartridgeItem = iterator.next();
			if (cartridgeItem.getText().equals(appType)) {
				cartridgeItem.select();
				break;
			}
		}
		
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(
				OpenShiftLabel.Button.NEXT)), TimePeriod.LONG);
		
		new PushButton(OpenShiftLabel.Button.NEXT).click();
		
		// bcs there is no running job it is required to verify this way
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(
				OpenShiftLabel.Button.BACK)), TimePeriod.LONG);

		new LabeledText("Name:").setText("jbosseapapp");
		
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.NEXT)), TimePeriod.NORMAL);
		new PushButton(OpenShiftLabel.Button.NEXT).click();
		
		new CheckBox(0).click();
		new WaitUntil(new ButtonWithTextIsActive(new PushButton("Browse...")), TimePeriod.NORMAL);
		new PushButton("Browse...").click();
		
		new WaitUntil(new ShellWithTextIsAvailable("Select Existing Project"), TimePeriod.NORMAL);
		new DefaultShell("Select Existing Project").setFocus();
		
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.OK)), TimePeriod.NORMAL);
		new PushButton(OpenShiftLabel.Button.OK).click();
		
		new WaitUntil(new ShellWithTextIsAvailable("New OpenShift Application"), TimePeriod.LONG);
		
		new DefaultShell("New OpenShift Application").setFocus();
		
		new PushButton(OpenShiftLabel.Button.FINISH).click();;

		new WaitUntil(new ShellWithTextIsAvailable("Import OpenShift Application "), TimePeriod.VERY_LONG);
		
		new DefaultShell("Import OpenShift Application ").setFocus();
		new PushButton(OpenShiftLabel.Button.OK).click();
		
		new WaitUntil(new ShellWithTextIsAvailable("Question"), TimePeriod.VERY_LONG);
		
		DefaultShell question = new DefaultShell("Question");
		question.setFocus();
		new PushButton(OpenShiftLabel.Button.YES).click();
		
		new WaitUntil(new ShellWithTextIsAvailable("Publish " + "jboss-javaee6-webapp" + "?"), TimePeriod.VERY_LONG);
		
		new DefaultShell("Publish " + "jboss-javaee6-webapp" + "?").setFocus();
		new PushButton(OpenShiftLabel.Button.YES).click();

		new WaitUntil(new ShellWithTextIsAvailable("Attempt push force ?"), TimePeriod.VERY_LONG);
		
		new DefaultShell("Attempt push force ?").setFocus();
		new PushButton(OpenShiftLabel.Button.YES).click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	private void checkOpenShiftMavenProfile() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		Project project = projectExplorer.getProject("jboss-javaee6-webapp");
		
		TreeItem projectItem = project.getTreeItem();
		projectItem.select();
		projectItem.expand();
		projectItem.collapse();
		projectItem.expand();
		
		project.getProjectItem("pom.xml").open();
		
		new DefaultCTabItem("pom.xml").activate();

		String pomText = new DefaultStyledText().getText();
		
		assertTrue("Maven profile has not been added into pom.xml", pomText.contains("<id>openshift</id>"));
	}
	
	@After
	public void deleteApplication() { 
		OpenShiftBotTest.deleteOpenShiftApplication("jbosseapapp", OpenShiftLabel.AppType.JBOSS_EAP_TREE);
	}
	
}
