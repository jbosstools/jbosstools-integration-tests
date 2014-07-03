package org.jboss.tools.openshift.ui.bot.test.application;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.test.condition.DeployedApplicationContainsText;
import org.jboss.tools.openshift.ui.bot.test.customizedexplorer.CustomizedNavigator;
import org.jboss.tools.openshift.ui.bot.test.customizedexplorer.CustomizedProject;
import org.jboss.tools.openshift.ui.bot.test.customizedexplorer.CustomizedProjectExplorer;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test binary deployment capability. Binary deployment consist of:
 * - create JBoss EAP application
 * - set up OpenShift marker for skip_maven_build
 * - modify and build application
 * - binary deployment (.war deployment)
 * 
 * This is not typical binary deployment - specific OpenShift binary deployment 
 * using OpenShift packaging. Just binary deployment of java application.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class DeployApplicationBinary {

	private static String APP_NAME = "eapbin" + System.currentTimeMillis();
	// temp workaround for getting project bcs. of getName is not quite ok
	
	@Before
	public void createJBossApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.JBOSS_EAP, APP_NAME, false, true, true);
	}
	
	@Test
	public void binaryDeployment() {
		setOpenShiftMarker();
		
		publishOpenShiftMarker();
		
		modifyApplication();
		
		mavenBuild();
		
		addWARToIndex();
		
		deployApp();
		
		verifyApp();
	}
	
	private void setOpenShiftMarker() {
		CustomizedProjectExplorer projectExplorer = new CustomizedProjectExplorer();
		projectExplorer.open();
		projectExplorer.getProject(APP_NAME).select();
		
		new ContextMenu("OpenShift", "Configure Markers...").select();
		
		new DefaultShell("Configure OpenShift Markers for project " + APP_NAME).setFocus();
		
		TableItem skipBuildMarker = new DefaultTable().getItem("Skip Maven Build");
		skipBuildMarker.setChecked(true);
		
		// Also hot deploy markers for faster workarounds
		new DefaultTable().getItem("Hot Deploy").setChecked(true);
		
		new PushButton(OpenShiftLabel.Button.OK).click();

		// verify in navigator
		CustomizedNavigator navigator = new CustomizedNavigator();
		navigator.open();
		CustomizedProject app = navigator.getProject(APP_NAME);
		boolean exists = 
				app.customizedProjectItemExists(".openshift", "markers", "skip_maven_build");

		projectExplorer.open();
		
		assertTrue("Marker has not been created", exists);
	}
	
	private void publishOpenShiftMarker() {
		publish();
	}
	
	private void expand(TreeItem item) {
		item.expand();
		item.collapse();
		item.expand();
	}
	
	private void modifyApplication() {
		CustomizedProjectExplorer projectExplorer = new CustomizedProjectExplorer();
		CustomizedProject project = projectExplorer.getProject(APP_NAME);
		project.select();
		project.getCustomizedProjectItem("src", "main", "webapp", "index.html").doubleClick();
		
		TextEditor editor = new TextEditor("index.html");
		editor.setText(RepublishApplication.TEXT);
		editor.save();
		editor.close();
	}
	
	private void mavenBuild() {
		CustomizedProjectExplorer projectExplorer = new CustomizedProjectExplorer();
		projectExplorer.open();
		
		CustomizedProject project = projectExplorer.getProject(APP_NAME);
		project.select();
		
		new ContextMenu("Run As", "5 Maven build...").select();
		
		new DefaultShell("Edit Configuration").setFocus();
		
		new LabeledText("Goals:").setText("clean package");
		new LabeledText("Profiles:").setText("openshift");
		new CheckBox("Skip Tests").click();
		
		new PushButton("Apply").click();
		new PushButton("Run").click();		
		
		AbstractWait.sleep(TimePeriod.getCustom(30));
	}
	
	private void addWARToIndex() {
		CustomizedProjectExplorer projectExplorer = new CustomizedProjectExplorer();
		projectExplorer.reopen();
		
		CustomizedProject project = projectExplorer.getProject(APP_NAME);
		project.select();

		new ContextMenu("Refresh").select();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
		
		project.select();
		
		expand(project.getTreeItem());
	
		TreeItem item = project.getCustomizedProjectItem("deployments");
		expand(item);
		item.select();
					
		// hard to say when is mvn build complete
		while (item.getItems().size() == 0) {
			new ContextMenu("Refresh").select();
			new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
		}
				
		item.getItem("ROOT.war").select();
		
		new ContextMenu("Team", "Add to Index").select();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	private void deployApp() {
		publish();
	}
	
	private void verifyApp() {
		try {
			new WaitUntil(new DeployedApplicationContainsText(APP_NAME, "OpSh"), 
					TimePeriod.LONG);
			// pass
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application was not successfully shown in browser");
		}
	}
	
	private void publish() {
		ServersView serverView = new ServersView();
		serverView.open();
		
		List<TreeItem> servers = new DefaultTree().getItems();
		TreeItem adapter = null;
		for (TreeItem server: servers) {
			if (server.getText().split(" ")[0].equals(APP_NAME)) {
				adapter = server;
				break;
			}
		}
		
		adapter.select();
		
		new ContextMenu("Publish").select();
		
		new DefaultShell("Commit Changes");
		new DefaultStyledText(0).setText("commit to openshift");
		Iterator<TreeItem> iterator = new DefaultTree(0).getItems().iterator();
		while (iterator.hasNext()) {
			TreeItem item = iterator.next();
			if (!item.isChecked()) {
				item.setChecked(true);
			}
		}
		
		new WaitUntil(new ButtonWithTextIsActive(new PushButton("Commit and Publish")), TimePeriod.NORMAL);
		new PushButton("Commit and Publish").click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
		
	@After
	public void deleteApp() {
		new DeleteApplication(APP_NAME).perform();
	}
}

