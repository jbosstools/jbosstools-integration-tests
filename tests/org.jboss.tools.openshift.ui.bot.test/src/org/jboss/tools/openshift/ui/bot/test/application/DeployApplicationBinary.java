package org.jboss.tools.openshift.ui.bot.test.application;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
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
import org.jboss.tools.openshift.ui.bot.test.customizedexplorer.CustomizedNavigator;
import org.jboss.tools.openshift.ui.bot.test.customizedexplorer.CustomizedProject;
import org.jboss.tools.openshift.ui.bot.test.customizedexplorer.CustomizedProjectExplorer;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
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
		
		new WaitUntil(new ShellWithTextIsAvailable(
				"Configure OpenShift Markers for project " + APP_NAME), TimePeriod.NORMAL);
		
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
		
		// Workaround for GTK 3 to close error shells caused by xulrunner
		doGTKworkaround();
		
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
		
		new WaitUntil(new ShellWithTextIsAvailable("Edit Configuration"),
				TimePeriod.NORMAL);
		
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
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		
		AbstractWait.sleep(TimePeriod.getCustom(20));
		
		assertTrue("Changes has not been successfully deployed", 
				explorer.verifyApplicationInBrowser(APP_NAME, "OpSh"));
	}
	
	private void doGTKworkaround() {
		new WorkbenchShell().closeAllShells(); 
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
		
		new WaitUntil(new ShellWithTextIsAvailable("Publish " + 
				APP_NAME + "?"), TimePeriod.LONG);
		
		new DefaultShell("Publish " + APP_NAME + "?").setFocus();
		
		new PushButton(OpenShiftLabel.Button.YES).click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
		
	@After
	public void deleteApp() {
		new DeleteApplication(APP_NAME).perform();
	}
}

