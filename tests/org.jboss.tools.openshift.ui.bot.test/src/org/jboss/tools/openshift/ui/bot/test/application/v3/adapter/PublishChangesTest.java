package org.jboss.tools.openshift.ui.bot.test.application.v3.adapter;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.eclipse.condition.ConsoleHasNoChange;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.core.condition.JobIsKilled;
import org.jboss.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.openshift.reddeer.condition.ApplicationPodIsRunning;
import org.jboss.tools.openshift.reddeer.condition.BrowserContainsText;
import org.jboss.tools.openshift.reddeer.condition.OpenShiftResourceExists;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.enums.ResourceState;
import org.jboss.tools.openshift.reddeer.exception.OpenShiftToolsException;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.ServerAdapter;
import org.jboss.tools.openshift.reddeer.view.resources.ServerAdapter.Version;
import org.jboss.tools.openshift.ui.bot.test.application.v3.create.AbstractCreateApplicationTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class PublishChangesTest extends AbstractCreateApplicationTest {

	public static String PUBLISHED_CODE = "package org.jboss.as.quickstarts.helloworld;\n"
			+ "public class HelloService {\n"
			+ "String createHelloMessage(String name) { return \"Hello OpenShift \" + name + \"!\"; }"
			+ "}";
	
	private String changedClass= "ROOT.war/WEB-INF/classes/org/jboss/as/quickstarts/helloworld/HelloService.class";
	
	@BeforeClass	
	public static void waitForRunningApplication() {
		new WaitUntil(new OpenShiftResourceExists(Resource.BUILD, "eap-app-1", ResourceState.COMPLETE),
				TimePeriod.getCustom(1000));
		
		new WaitUntil(new ApplicationPodIsRunning(), TimePeriod.LONG);
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
		
		new DefaultShell(OpenShiftLabel.Shell.SERVER_ADAPTER_SETTINGS);
		new FinishButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.SERVER_ADAPTER_SETTINGS));
		new WaitUntil(new JobIsKilled("Refreshing server adapter list"), TimePeriod.LONG, false);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	private void changeProjectAndVerifyAutoPublish() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		ProjectItem projectItem = projectExplorer.getProject(PROJECT_NAME).getProjectItem("Java Resources", "src/main/java",
				"org.jboss.as.quickstarts.helloworld", "HelloService.java");
		projectItem.select();
		projectItem.open();
		
		TextEditor textEditor = new TextEditor("HelloService.java");
		textEditor.setText(PUBLISHED_CODE);
		textEditor.close(true);
		
		new WaitWhile(new JobIsRunning());
		new WaitUntil(new ConsoleHasNoChange(), TimePeriod.LONG);
	
		assertTrue("Local changes performed to project have not been autopublished, or at least rsync "
					+ "output in console view does not contain information about sending incremental list of changes,"
					+ "specifically with changed class " + changedClass,
				new ConsoleView().getConsoleText().contains(changedClass));
	}
	
	private void verifyChangesTookEffect() {
		new ServerAdapter(Version.OPENSHIFT3, "eap-app", "Service").select();
		new ContextMenu(OpenShiftLabel.ContextMenu.SHOW_IN_WEB_BROWSER).select();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		try {
			new WaitUntil(new BrowserContainsText("Hello OpenShift"), TimePeriod.VERY_LONG);			
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application was not deployed successfully because it is not shown in web browser properly.");
		}
	}
	
	@AfterClass
	public static void removeAdapterAndApplication() {
		try {
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			new ServerAdapter(Version.OPENSHIFT3, BUILD_CONFIG).delete();
		} catch (OpenShiftToolsException ex) {
			// do nothing, adapter does not exists
		}
	}
}
