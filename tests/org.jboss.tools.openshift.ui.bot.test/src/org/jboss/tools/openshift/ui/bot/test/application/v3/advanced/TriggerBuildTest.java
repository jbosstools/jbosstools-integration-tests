package org.jboss.tools.openshift.ui.bot.test.application.v3.advanced;

import static org.junit.Assert.fail;

import java.util.List;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.openshift.reddeer.condition.AmountOfResourcesExists;
import org.jboss.tools.openshift.reddeer.condition.ResourceExists;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.OpenShiftResource;
import org.jboss.tools.openshift.ui.bot.test.application.v3.create.AbstractCreateApplicationTest;
import org.junit.Test;

public class TriggerBuildTest extends AbstractCreateApplicationTest {

	@Test
	public void testCreateNewBuildFromBuildConfig() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.reopen();
		
		new WaitUntil(new ResourceExists(Resource.BUILD_CONFIG), 
				TimePeriod.getCustom(120), true, TimePeriod.getCustom(7));
		
		new WaitUntil(new ResourceExists(Resource.BUILD, "eap-app-1"), 
				TimePeriod.LONG, true, TimePeriod.getCustom(7));
		
		List<OpenShiftResource> builds = explorer.getOpenShift3Connection().getProject().
				getOpenShiftResources(Resource.BUILD);
		int oldAmountOfBuilds = builds.size();
		
		explorer.getOpenShift3Connection().getProject().getOpenShiftResources(Resource.BUILD_CONFIG).get(0).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.START_BUILD).select();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		try {
			new WaitUntil(new AmountOfResourcesExists(Resource.BUILD, oldAmountOfBuilds + 1), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("New build was not triggered altough it should be.");
		}
	}
	
	@Test
	public void testCloneExistingBuild() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.reopen();
		
		new WaitUntil(new ResourceExists(Resource.BUILD), 
					TimePeriod.getCustom(240), true, TimePeriod.getCustom(7));
		
		List<OpenShiftResource> builds = explorer.getOpenShift3Connection().getProject().
				getOpenShiftResources(Resource.BUILD);
		builds.get(0).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.CLONE_BUILD).select();

		int oldAmountOfBuilds = builds.size();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		try {
			new WaitUntil(new AmountOfResourcesExists(Resource.BUILD, oldAmountOfBuilds + 1),
					TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("New build was not triggered altough it should be.");
		}
	}
}
