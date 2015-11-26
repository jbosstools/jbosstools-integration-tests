package org.jboss.tools.openshift.ui.bot.test.application.v3.create;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.ui.browser.BrowserEditor;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.openshift.reddeer.condition.AmountOfResourcesExists;
import org.jboss.tools.openshift.reddeer.condition.BrowserContainsText;
import org.jboss.tools.openshift.reddeer.condition.ResourceExists;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.enums.ResourceState;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.junit.Test;

public class DeploymentTest extends AbstractCreateApplicationTest {
	
	@Test
	public void testDeploymentOfApplicationCreatedFromTemplate() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		
		try {
			new WaitUntil(new ResourceExists(Resource.BUILD, "eap-app-1"), 
					TimePeriod.getCustom(120), true, TimePeriod.getCustom(7));
		} catch (WaitTimeoutExpiredException ex) {
			fail("There should be build of an application, but there is not.");
		}
		
		try {
			new WaitUntil(new ResourceExists(Resource.POD, "eap-app-1-build",
					ResourceState.SUCCEEDED), TimePeriod.getCustom(800),
					true, TimePeriod.getCustom(7));
		} catch (WaitTimeoutExpiredException ex) {
			fail("There should be a succeeded pod of an application build, but there is not.");
		}
		
		try {
			new WaitUntil(new AmountOfResourcesExists(Resource.POD, 2), TimePeriod.getCustom(150),
					true, TimePeriod.getCustom(7));
		} catch (WaitTimeoutExpiredException ex) {
			fail("There should be 2 pods. One of a build another one of an application, but there are not.");
		}
		
		try {
			new WaitUntil(new ResourceExists(Resource.BUILD, "eap-app-1", ResourceState.COMPLETE),
					TimePeriod.VERY_LONG, true, TimePeriod.getCustom(7));
		} catch (WaitTimeoutExpiredException ex) {
			fail("There should be completed build of an application, but there is not.");
		}
		
		
		explorer.getOpenShift3Connection().getProject().getOpenShiftResources(Resource.ROUTE).get(0).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.SHOW_IN_WEB_BROWSER).select();
		
		try {
			new WaitUntil(new BrowserContainsText("Welcome to JBoss!"), TimePeriod.VERY_LONG);
			new BrowserEditor("kitchensink").close();
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application was not deployed successfully because it is not shown in web browser properly.");
		}
	}
}
