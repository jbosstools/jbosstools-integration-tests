package org.jboss.tools.openshift.ui.bot.test.application.v3.create;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.BackButton;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.NoButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.TestUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShift3Connection;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.OpenShiftProject;
import org.jboss.tools.openshift.reddeer.view.OpenShiftResource;
import org.jboss.tools.openshift.reddeer.wizard.v3.NewOpenShift3ApplicationWizard;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS3;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateApplicationFromTemplateTest {
	
	private String gitFolder = "jboss-eap-quickstarts";
	private String projectName = "jboss-kitchensink";
	
	@Before
	public void setUp() {
		TestUtils.cleanupGitFolder(gitFolder);
	}
	
	@Test
	public void testCreateApplicationFromTemplate() {
		String genericWebhookURL;
		String githubWebhookURL;
		
		new NewOpenShift3ApplicationWizard(DatastoreOS3.SERVER, DatastoreOS3.USERNAME,
				DatastoreOS3.PROJECT1_DISPLAYED_NAME).openWizardFromExplorer();
		new DefaultTree().selectItems(new DefaultTreeItem(OpenShiftLabel.Others.EAP_TEMPLATE));
		
		new WaitUntil(new WidgetIsEnabled(new NextButton()), TimePeriod.NORMAL);
		
		new NextButton().click();
		
		new WaitUntil(new WidgetIsEnabled(new BackButton()), TimePeriod.LONG);
		
		String gitRef = new DefaultTable().getItem("GIT_REF").getText(1);
		String gitURI = new DefaultTable().getItem("GIT_URI").getText(1);
		String gitContext = new DefaultTable().getItem("GIT_CONTEXT_DIR").getText(1);
		String eapRelease = new DefaultTable().getItem("EAP_RELEASE").getText(1);
		String applicationName = new DefaultTable().getItem("APPLICATION_NAME").getText(1);
		new NextButton().click();
		
		new WaitWhile(new WidgetIsEnabled(new NextButton()), TimePeriod.LONG);
		
		new FinishButton().click();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.APPLICATION_SUMMARY), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.APPLICATION_SUMMARY);
		
		assertTrue("GIT_REF is not same as the one shown in New OpenShift Application wizard.", 
				new DefaultTable().getItem("GIT_REF").getText(1).equals(gitRef));
		assertTrue("GIT_URI is not same as the one shown in New OpenShift Application wizard.", 
				new DefaultTable().getItem("GIT_URI").getText(1).equals(gitURI));
		assertTrue("GIT_CONTEXT_DIR is not same as the one shown in New OpenShift Application wizard.", 
				new DefaultTable().getItem("GIT_CONTEXT_DIR").getText(1).equals(gitContext));
		assertTrue("EAP_RELEASE is not same as the one shown in New OpenShift Application wizard.", 
				new DefaultTable().getItem("EAP_RELEASE").getText(1).equals(eapRelease));
		assertTrue("APPLICATION_NAME is not same as the one shown in New OpenShift Application wizard.", 
				new DefaultTable().getItem("APPLICATION_NAME").getText(1).equals(applicationName));
		assertFalse("GENERIC_TRIGGER_SECRET is empty, altough it should be generated and non-empty.",
				new DefaultTable().getItem("GENERIC_TRIGGER_SECRET").getText(1).isEmpty());
		assertFalse("GITHUB_TRIGGER_SECRET is empty, altough it should be generated and non-empty.", 
				new DefaultTable().getItem("GITHUB_TRIGGER_SECRET").getText(1).isEmpty());
		
		new DefaultLink("Click here to display the webhooks available to automatically trigger builds.").click();
		
		new DefaultShell(OpenShiftLabel.Shell.WEBHOOK_TRIGGERS);
		genericWebhookURL = new DefaultText(0).getText();
		githubWebhookURL = new DefaultText(1).getText();
		
		assertFalse("Generic webhook URL should not be empty.", genericWebhookURL.isEmpty());
		assertFalse("GitHub webhook URL should not be empty.", githubWebhookURL.isEmpty());
		
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.WEBHOOK_TRIGGERS));
		
		new DefaultShell(OpenShiftLabel.Shell.APPLICATION_SUMMARY);
		new OkButton().click();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.IMPORT_APPLICATION));
		
		new DefaultShell(OpenShiftLabel.Shell.IMPORT_APPLICATION);
		new FinishButton().click();
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CHEATSHEET), TimePeriod.LONG);
			
			new DefaultShell(OpenShiftLabel.Shell.CHEATSHEET);
			new CheckBox(0).click();
			new NoButton().click();
			
			new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CHEATSHEET));
		} catch (WaitTimeoutExpiredException ex) {
			// do nothing if cheat sheet is not provided
		}
		
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		
		assertTrue("Project Explorer should contain imported project kitchensink",
				projectExplorer.containsProject(projectName));
		
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		OpenShiftProject project = explorer.getOpenShift3Connection(DatastoreOS3.USERNAME, DatastoreOS3.SERVER).
				getProject(DatastoreOS3.PROJECT1_DISPLAYED_NAME);
		
		List<OpenShiftResource> buildConfig = project.getOpenShiftResources(Resource.BUILD_CONFIG);
		assertTrue("There should be precisely 1 build config for created application, but there is following amount"
				+ " of build configs: " + buildConfig.size(), buildConfig.size() == 1);
		assertTrue("There should be application name and git URI in build config tree item, but they are not."
				+ "Application name is '" + applicationName + "' and git URI is '" + gitURI + "', but build "
						+ "config consists of text '" + buildConfig.get(0).getText() + "'",
				buildConfig.get(0).getText().contains(applicationName) && 
				buildConfig.get(0).getText().contains(gitURI));
		
		List<OpenShiftResource> imageStream = project.getOpenShiftResources(Resource.IMAGE_STREAM);
		assertTrue("There should be precisely 1 image stream for created application, but there is following amount"
				+ " of image streams: " + imageStream.size(), imageStream.size() == 1);
	
		List<OpenShiftResource> routes = project.getOpenShiftResources(Resource.ROUTE);
		assertTrue("There should be precisely 1 route for created application, but there is following amount"
				+ " of routes:" + routes.size(), routes.size() == 1);
		assertTrue("Generated (default) route should contain application name, but it's not contained.",
				routes.get(0).getText().contains(applicationName));
		
		List<OpenShiftResource> services = project.getOpenShiftResources(Resource.SERVICE);
		assertTrue("There should be precisely 2 services for created application, but there is following amount"
				+ " of services: " + services.size(), services.size() == 2);
		
		List<OpenShiftResource> replicationController = project.getOpenShiftResources(Resource.REPLICATION_CONTROLLER);
		assertTrue("There should be precisely 1 replication controller for created application, but there is "
				+ "following amount of replication controllers: " + replicationController.size(),
				replicationController.size() == 1);
	}
	
	@After
	public void tearDown() {
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
