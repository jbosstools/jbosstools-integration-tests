package org.jboss.tools.openshift.ui.bot.test.application.advanced;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsEnabled;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID414CreateApplicationFromExistingProjectTest;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.condition.v2.ApplicationIsDeployedSuccessfully;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.wizard.v2.OpenShift2ApplicationWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test capabilities of deploying already built application. War archive is added to git
 * index and pushed to OpenShift where mvn build is disabled.
 *  
 * @author mlabuda@redhat.com
 *
 */
public class ID904DeployApplicationWARArchiveTest {

	private String applicationName = "eap" + System.currentTimeMillis();
	
	private String eapApplicationXhtml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> "
			+ " <ui:composition xmlns=\"http://www.w3.org/1999/xhtml\""
			+ " xmlns:ui=\"http://java.sun.com/jsf/facelets\""
			+ "xmlns:f=\"http://java.sun.com/jsf/core\""
			+ "xmlns:h=\"http://java.sun.com/jsf/html\""
			+ "template=\"/WEB-INF/templates/default.xhtml\">"
			+ "<ui:define name=\"content\">"
			+ "<h1>Welcome to OpSh</h1>"
			+ "</ui:define></ui:composition>";
	
	@Before
	public void createJavaEEApp() {
		ID414CreateApplicationFromExistingProjectTest.createJavaEEProject(applicationName);
	}
	
	@Test
	public void testDeployApplicationWarArchive() {
		deployJavaEEApplication();
		
		addAndPushDisableMvnBuildMarker();
		modifyApplication();
		createArchive();
		publishApplication();
	}
	
	private void deployJavaEEApplication() {
		OpenShift2ApplicationWizard wizard = new OpenShift2ApplicationWizard(DatastoreOS2.USERNAME, DatastoreOS2.SERVER,
				DatastoreOS2.DOMAIN);
		wizard.openWizardFromExplorer();
		wizard.createNewApplicationOnBasicCartridge(OpenShiftLabel.Cartridge.JBOSS_EAP,
				applicationName, false, true, false, false, null, null, true, applicationName, 
				null, "openshift2", (String[]) null);
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.IMPORT_APPLICATION_WIZARD),
				TimePeriod.VERY_LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.IMPORT_APPLICATION_WIZARD);
		new OkButton().click();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.ACCEPT_HOST_KEY), TimePeriod.VERY_LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.ACCEPT_HOST_KEY);
		new YesButton().click();

		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
				TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	private void addAndPushDisableMvnBuildMarker() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		projectExplorer.getProject(applicationName).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.CONFIGURE_MARKERS).select();
		
		new WaitUntil(new ShellWithTextIsAvailable("Configure OpenShift Markers for project " + applicationName),
				TimePeriod.LONG);
		
		new DefaultShell("Configure OpenShift Markers for project " + applicationName);

		// Also hot deploy marker for faster workarounds
		new DefaultTable().getItem("Skip Maven Build").setChecked(true);
		new DefaultTable().getItem("Hot Deploy").setChecked(true);
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable("Configure OpenShift Markers for project " + applicationName),
				TimePeriod.LONG);
		
		// Publish
		ServersView servers = new ServersView();
		servers.open();

		TreeViewerHandler.getInstance().getTreeItem(new DefaultTree(),
				applicationName + " at OpenShift").select();
		new ContextMenu("Publish").select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.PUBLISH_CHANGES), 
				TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.PUBLISH_CHANGES);
		
		assertTrue("If there is no commit message, button should be Push Only without "
				+ "pushing uncommited changes.",
				new ButtonWithTextIsEnabled(new PushButton("Publish Only")).test());
		
		new DefaultStyledText(0).setText("Commit message");
		
		try {
			new WaitUntil(new ButtonWithTextIsEnabled(new PushButton(
					OpenShiftLabel.Button.COMMIT_PUBLISH)), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Button to push commited changes has not been enabled.");
		}
		
		new PushButton(OpenShiftLabel.Button.COMMIT_PUBLISH).click();

		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);		
	}
	
	private void modifyApplication() {
		Project project = new ProjectExplorer().getProject(applicationName);
		project.select();
		project.getProjectItem("src", "main", "webapp", "index.html").open();
		
		TextEditor editor = new TextEditor("index.html");
		editor.setText(eapApplicationXhtml);
		editor.save();
		editor.close();
	}
	
	// return true if context menu was opened successfully, number is prefix
	private boolean openMavenBuildContextMenu(int number) {
		try {
			new ContextMenu("Run As", number + " Maven build...").select();
			return true;
		} catch (RedDeerException ex) {
			return false;
		}
	}
	
	private void createArchive() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		
		Project project = projectExplorer.getProject(applicationName);
		project.select();
		
		int number = 1;
		while (!openMavenBuildContextMenu(number)) {
			number++;
		}
		
		new WaitUntil(new ShellWithTextIsAvailable("Edit Configuration"), TimePeriod.LONG);
		
		new DefaultShell("Edit Configuration");
		
		new LabeledText("Goals:").setText("clean package");
		new LabeledText("Profiles:").setText("openshift");
		new CheckBox("Skip Tests").click();
		
		new PushButton("Apply").click();
		new PushButton("Run").click();	
		
		new WaitWhile(new ShellWithTextIsAvailable("Edit Configuration"), TimePeriod.LONG);
		
		ConsoleView console = new ConsoleView();
		console.open();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WaitUntil(new ConsoleHasText("[INFO] BUILD SUCCESS"), TimePeriod.LONG);
	}
	
	private void publishApplication() {
		new ProjectExplorer().getProject(applicationName).select();
		new ContextMenu("Refresh").select();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		ServersView servers = new ServersView();
		servers.open();

		TreeViewerHandler.getInstance().getTreeItem(new DefaultTree(),
				applicationName + " at OpenShift").select();
		new ContextMenu("Publish").select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.PUBLISH_CHANGES), 
				TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.PUBLISH_CHANGES);
		
		new DefaultStyledText().setText("Commit 2");
		for (TreeItem item: new DefaultTree().getItems()) {
			item.setChecked(true);
		}
		
		new PushButton(OpenShiftLabel.Button.COMMIT_PUBLISH).click();

		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		// It takes some time to sucessfully deploy application
		AbstractWait.sleep(TimePeriod.getCustom(15));
		
		try {
			new WaitUntil(new ApplicationIsDeployedSuccessfully(DatastoreOS2.USERNAME, DatastoreOS2.SERVER,
				DatastoreOS2.DOMAIN, applicationName, "OpSh"), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application has not been successfully.");
		}
	}

	@After
	public void deleteApplication() {
		new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, applicationName,
				applicationName).perform();
	}
}
