package org.jboss.tools.openshift.ui.bot.test.application.v3.create;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.tools.openshift.reddeer.condition.OpenShiftProjectExists;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.TestUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShift3Connection;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.wizard.v3.TemplatesCreator;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class AbstractCreateApplicationTest {

	public static String gitFolder = "jboss-eap-quickstarts";
	public static String projectName = "jboss-kitchensink";
	
	protected static TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
	
	@BeforeClass
	public static void setUp() {
		TestUtils.cleanupGitFolder(gitFolder);
		new TemplatesCreator().createOpenShiftApplicationBasedOnServerTemplate(OpenShiftLabel.Others.EAP_TEMPLATE);
	}
	
	@AfterClass
	public static void tearDown() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.reopen();
		
		OpenShift3Connection connection  = explorer.getOpenShift3Connection();
		connection.getProject().delete();
		
		try {
			new WaitWhile(new OpenShiftProjectExists());
		} catch (WaitTimeoutExpiredException ex) {
			connection.refresh();
		
			new WaitWhile(new OpenShiftProjectExists(), TimePeriod.getCustom(5));
		}
		
		connection.createNewProject();
		
		ProjectExplorer projectExplorer = new ProjectExplorer();
		if (projectExplorer.containsProject(projectName)) {
			projectExplorer.getProject(projectName).delete(true);
		}
	}
}
