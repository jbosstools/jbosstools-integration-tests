package org.jboss.tools.openshift.ui.bot.test.application.v3.create;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.TestUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShift3Connection;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.wizard.v3.TemplatesCreator;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS3;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class AbstractCreateApplicationTest {

	public static String gitFolder = "jboss-eap-quickstarts";
	public static String projectName = "jboss-kitchensink";
	
	protected TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
	
	@BeforeClass
	public static void setUp() {
		TestUtils.cleanupGitFolder(gitFolder);
		new TemplatesCreator(DatastoreOS3.SERVER, DatastoreOS3.USERNAME, DatastoreOS3.PROJECT1_DISPLAYED_NAME).
				createOpenShiftApplicationBasedOnServerTemplate(OpenShiftLabel.Others.EAP_TEMPLATE);
	}
	
	@AfterClass
	public static void tearDown() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.reopen();
		
		OpenShift3Connection connection  = explorer.getOpenShift3Connection(DatastoreOS3.USERNAME);
		connection.getProject(DatastoreOS3.PROJECT1_DISPLAYED_NAME).delete();
		connection.createNewProject(DatastoreOS3.PROJECT1, DatastoreOS3.PROJECT1_DISPLAYED_NAME);
		
		ProjectExplorer projectExplorer = new ProjectExplorer();
		if (projectExplorer.containsProject(projectName)) {
			projectExplorer.getProject(projectName).delete(true);
		}
	}
}
