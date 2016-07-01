package org.jboss.tools.openshift.ui.bot.test.application.v3.adapter;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.openshift.reddeer.condition.BrowserContainsText;
import org.jboss.tools.openshift.reddeer.condition.ResourceExists;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.enums.ResourceState;
import org.jboss.tools.openshift.reddeer.exception.OpenShiftToolsException;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.ServerAdapter;
import org.jboss.tools.openshift.reddeer.view.resources.ServerAdapter.Version;
import org.jboss.tools.openshift.ui.bot.test.application.v3.advanced.GithubWebhoookTest;
import org.jboss.tools.openshift.ui.bot.test.application.v3.create.AbstractCreateApplicationTest;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class PublishChangesTest extends AbstractCreateApplicationTest {

	@BeforeClass	
	public static void waitForRunningApplication() {
		new WaitUntil(new ResourceExists(Resource.BUILD, "eap-app-1", ResourceState.COMPLETE),
				TimePeriod.getCustom(500), false);
	}
	
	@Test
	public void testAutomaticPublish() {
		createServerAdapter();
		changeProjectAndVerifyAutoPublish();
		verifyChangesTookEffect();
	}
	
	private void createServerAdapter() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getOpenShift3Connection().getProject().getService("eap-app").select();
		new ContextMenu(OpenShiftLabel.ContextMenu.NEW_ADAPTER_FROM_EXPLORER).select();
		
		new DefaultShell("");
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.ADAPTER));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG, false);
	}
	
	private void changeProjectAndVerifyAutoPublish() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.getProject(projectName).getProjectItem("src", "main", "webapp", "index.xhtml").select();

		new ContextMenu("Open With", "Text Editor").select();
		TextEditor textEditor = new TextEditor("index.xhtml");
		textEditor.setText(GithubWebhoookTest.webPageContent);
		textEditor.close(true);
		
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
		
		try {
			new WaitUntil(new ConsoleHasText("ROOT.war/index.xhtml"), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			Assert.fail("Local changes performed to project have not been autopublished, or at least rsync "
					+ "output in console view does not contain information about sending incremental list of "
					+ "ROOT.war/index.xhtml to OpenShift");
		}
	}
	
	private void verifyChangesTookEffect() {
		new ServerAdapter(Version.OPENSHIFT3, "eap-app").select();
		new ContextMenu(OpenShiftLabel.ContextMenu.SHOW_IN_WEB_BROWSER).select();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		try {
			new WaitUntil(new BrowserContainsText("OpenShift3"), TimePeriod.VERY_LONG);			
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application was not deployed successfully because it is not shown in web browser properly.");
		}
	}
	
	@AfterClass
	public static void removeAdapterAndApplication() {
		try {
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			new ServerAdapter(Version.OPENSHIFT3, buildConfigName).delete();
		} catch (OpenShiftToolsException ex) {
			// do nothing, adapter does not exists
		}
	}
}
