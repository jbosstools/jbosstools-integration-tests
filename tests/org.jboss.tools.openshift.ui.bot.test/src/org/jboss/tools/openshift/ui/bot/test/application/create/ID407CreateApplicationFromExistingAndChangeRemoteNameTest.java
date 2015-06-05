package org.jboss.tools.openshift.ui.bot.test.application.create;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.core.util.ResultRunnable;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.condition.EditorWithTitleIsActive;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.openshift.ui.condition.ApplicationIsDeployedSuccessfully;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.DeleteApplication;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.wizard.application.NewApplicationWizard;
import org.jboss.tools.openshift.ui.wizard.application.OpenNewApplicationWizard;
import org.jboss.tools.openshift.ui.wizard.application.Templates;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test capabilities of deploying existing project to OpenShift.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID407CreateApplicationFromExistingAndChangeRemoteNameTest {

	private String applicationName = "diy" + System.currentTimeMillis();
	private String secondApplicationName = "doit" + System.currentTimeMillis();
	
	public static final String HTML_TEXT = "<!doctype html> <html lang=\"en\"> <head> <meta charset=\"utf-8\"> " +
			"<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">  <title>Welcome to OpSh</title>" +
			" </head> <body>OpSh</body> </html>";
	
	@Before
	public void createProject() {
		Templates newApplicationTemplate = new Templates(Datastore.USERNAME, 
				Datastore.DOMAIN, false);
		newApplicationTemplate.createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.DIY, applicationName, false, true, true);
		
		DeleteApplication deleteApplicaiton = 
				new DeleteApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName);
		deleteApplicaiton.deleteOpenShiftApplication();
		deleteApplicaiton.deleteServerAdapter();
	}
	
	@Test
	public void testCreateApplicationFromExistingProjectAndTestRemoteName() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getDomain(Datastore.USERNAME, Datastore.DOMAIN).select();
		
		modifyAndCommitProject();
		
		OpenNewApplicationWizard.openWizardFromExplorer(Datastore.USERNAME, Datastore.DOMAIN);
		
		NewApplicationWizard wizard = new NewApplicationWizard();
		wizard.createNewApplicationOnBasicCartridge(OpenShiftLabel.Cartridge.DIY,
				Datastore.DOMAIN, secondApplicationName, false, true, false, 
				false, null, null, true, applicationName, null, "openshift2", (String[]) null);
		
		postCreateSteps(secondApplicationName);
		
		try {
			new WaitUntil(new ApplicationIsDeployedSuccessfully(Datastore.USERNAME, 
				Datastore.DOMAIN, secondApplicationName, "OpSh"), TimePeriod.VERY_LONG);
			// PASS
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application has not been deployed successfully. Browser does not "
					+ "contain text of existing project which has been deployed.");
		}
		
		verifyRemoteName();
	}
	
	private void modifyAndCommitProject() {
		// Modify
		ProjectExplorer explorer = new ProjectExplorer();
		explorer.getProject(applicationName).getProjectItem("diy", "index.html").open();
		
		TextEditor editor = new TextEditor("index.html");
		editor.activate();
		
		new WaitUntil(new EditorWithTitleIsActive("index.html"));
		editor.setText(HTML_TEXT);
		editor.save();
		editor.close();
	
		// Commit
		explorer.open();
		explorer.getProject(applicationName).select();
		new ContextMenu("Team", "Commit...").select();
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable("Identify Yourself"), TimePeriod.NORMAL);
			new DefaultShell("Identify Yourself").setFocus();
			new PushButton("OK").click();
		} catch (WaitTimeoutExpiredException ex) {}
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.COMMIT), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.COMMIT).setFocus();		
		new DefaultStyledText().setText("Commit");
		new PushButton("Commit").click();
		

		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.COMMIT),
				TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	public static void postCreateSteps(String applicationName) {
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE), 
				TimePeriod.VERY_LONG);
			
		new DefaultShell(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE);
		new OkButton().click();
		
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
		
		// PUBLISH
		ServersView servers = new ServersView();
		servers.open();

		TreeViewerHandler.getInstance().getTreeItem(new DefaultTree(),
				applicationName + " at OpenShift").select();
		new ContextMenu("Publish").select();
		
		new DefaultShell("Publish Changes");
		new PushButton("Publish Only").click();

		new DefaultShell("Attempt push force ?");
		new YesButton().click();
	
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);

		// Browser need time
		AbstractWait.sleep(TimePeriod.NORMAL);
	}
	
	private void verifyRemoteName() {
		WorkbenchView gitRepoView = new WorkbenchView("Git", "Git Repositories");
		gitRepoView.open();
		
		final TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
		// Workaround because of strange git tree items
		TreeItem item = null;
		for (TreeItem tmpItem: new DefaultTree().getItems()) {
			if (tmpItem.getText().contains(applicationName)) {
				item = tmpItem;
				break;
			}
		}
		final TreeItem finalItem = item;
		
		assertFalse("Git view does not containt item for application", item == null);
		
		try {
			gitRepoView.activate();
			Display.syncExec(new ResultRunnable<Boolean>() {
				
				@Override
				public Boolean run() {
				treeViewerHandler.getTreeItem(finalItem, "Remotes", "openshift2").select();
				return true;
				}
			});
			// PASS
		} catch (JFaceLayerException ex) {
			fail("There is no remote with name openshift2 for application " + applicationName);
		}
			
		gitRepoView.close();
	}
	
	@After
	public void deleteApplication() {
		new DeleteApplication(Datastore.USERNAME, Datastore.DOMAIN, secondApplicationName,
				applicationName).perform();
	}
}
