package org.jboss.tools.openshift.ui.bot.test.application;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.jboss.tools.openshift.ui.bot.util.TestUtils;
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

	private static final String APP_NAME = "jbosseapapp" + System.currentTimeMillis();
	
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
		new NewApplicationTemplates(false).deployExistingProject(
				OpenShiftLabel.AppType.JBOSS_EAP, APP_NAME, "jboss-javaee6-webapp", null);

		checkOpenShiftMavenProfile();
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
		new DeleteApplication(APP_NAME, OpenShiftLabel.AppType.JBOSS_EAP_TREE).perform();
	}
	
}
