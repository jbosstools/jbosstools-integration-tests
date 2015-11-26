package org.jboss.tools.openshift.ui.bot.test.application.v3.advanced;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.condition.ViewWithTitleIsActive;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.openshift.reddeer.condition.AmountOfResourcesExists;
import org.jboss.tools.openshift.reddeer.condition.ResourceExists;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.enums.ResourceState;
import org.jboss.tools.openshift.reddeer.requirement.OpenShiftCommandLineToolsRequirement.OCBinary;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS3;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.TestUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.OpenShiftResource;
import org.jboss.tools.openshift.ui.bot.test.application.v3.create.AbstractCreateApplicationTest;
import org.junit.BeforeClass;
import org.junit.Test;

@OCBinary
public class LogsTest extends AbstractCreateApplicationTest {
	
	@BeforeClass
	public static void waitForApplication() {
		new WaitUntil(new ResourceExists(Resource.POD, "eap-app-1-build", ResourceState.SUCCEEDED), 
				TimePeriod.getCustom(600), true, TimePeriod.getCustom(8));
		
		new WaitUntil(new AmountOfResourcesExists(Resource.POD, 2), TimePeriod.VERY_LONG, true,
				TimePeriod.getCustom(5));
	}
	
	@Test
	public void testPodLogOfApplicationPod() {
		TestUtils.setUpOcBinary();
		
		OpenShiftResource pod = PortForwardingTest.getPodOfEAPApplication();
		pod.select();
		String podName = pod.getName();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.POD_LOG).select();
		
		ConsoleView consoleView = new ConsoleView();
		
		new WaitUntil(new ViewWithTitleIsActive("Console"), TimePeriod.NORMAL, false);
	
		assertTrue("Console label is incorrect, it should contains project name and pod name.\n"
						+ "but label is: " + consoleView.getConsoleLabel(), consoleView.getConsoleLabel().contains(
						DatastoreOS3.PROJECT1 + "\\" + podName));
		assertTrue("Console text should contain output from EAP runtime",
				consoleView.getConsoleText().contains("Admin console listening on"));
	}
	
	@Test
	public void testPodLogOfBuildPod() {
		TestUtils.setUpOcBinary();
		
		new WaitUntil(new ResourceExists(Resource.BUILD, "eap-app-1", ResourceState.COMPLETE), 
				TimePeriod.getCustom(600), true, TimePeriod.getCustom(8));
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		
		explorer.activate();
		explorer.getOpenShift3Connection().
				getProject().getOpenShiftResources(Resource.BUILD).get(0).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.BUILD_LOG).select();
		
		new WaitUntil(new ViewWithTitleIsActive("Console"), TimePeriod.NORMAL, false);
		
		ConsoleView consoleView = new ConsoleView();
		assertTrue("Console label is incorrect, it should contains project name and name of build pod.\n"
				+ "but label is: " + consoleView.getConsoleLabel(), consoleView.getConsoleLabel().contains(
				DatastoreOS3.PROJECT1 + "\\eap-app-1-build"));
		assertTrue("There should be info in log about successfull push of image.", 
				consoleView.getConsoleText().contains("Successfully pushed"));
	}
	
	@Test
	public void testPodLogWithoutSetOCBinary() {
		TestUtils.cleanUpOCBinary();
		
		OpenShiftResource pod = PortForwardingTest.getPodOfEAPApplication();
		pod.select();
		new ContextMenu(OpenShiftLabel.ContextMenu.POD_LOG).select();
		
		assertUnknownBinaryLocationShellIsShown();
	}
	
	@Test
	public void testBuildLogWithoutSetOCBinary() {
		TestUtils.cleanUpOCBinary();
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		
		explorer.activate();
		explorer.getOpenShift3Connection().
			getProject().getOpenShiftResources(Resource.BUILD).get(0).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.BUILD_LOG).select();
		
		assertUnknownBinaryLocationShellIsShown();
	}
	
	private void assertUnknownBinaryLocationShellIsShown() {
		try {
			new DefaultShell(OpenShiftLabel.Shell.BINARY_LOCATION_UNKNOWN);
			new OkButton().click();
			
			new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.BINARY_LOCATION_UNKNOWN));
		} catch (RedDeerException ex) {
			fail("There should an error dialog about unknown oc binary location.");
		}
	}
}
