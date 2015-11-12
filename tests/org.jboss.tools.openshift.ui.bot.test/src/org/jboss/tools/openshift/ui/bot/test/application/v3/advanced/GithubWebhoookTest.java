package org.jboss.tools.openshift.ui.bot.test.application.v3.advanced;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.part.MultiPageEditor;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.openshift.reddeer.condition.EditorWithTitleIsAvailable;
import org.jboss.tools.openshift.reddeer.condition.ResourceExists;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.TestUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.wizard.v3.TemplateParameter;
import org.jboss.tools.openshift.reddeer.wizard.v3.TemplatesCreator;
import org.jboss.tools.openshift.ui.bot.test.application.v3.create.AbstractCreateApplicationTest;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS3;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class GithubWebhoookTest {
	
	private static final String GIT_SECRET = "nosecret";
	private static final String gitURI = "https://github.com/openshift-tools-testing-account/jboss-eap-quickstarts";
	
	private String randomString = "random" + System.currentTimeMillis();
	private String webPageContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n"
			+ "<ui:composition xmlns=\"http://www.w3.org/1999/xhtml\"" + "\n"
			+ "xmlns:ui=\"http://java.sun.com/jsf/facelets\"" + "\n"
			+ "xmlns:f=\"http://java.sun.com/jsf/core\"" + "\n"
			+ "xmlns:h=\"http://java.sun.com/jsf/html\"" + "\n"
			+ "template=\"/WEB-INF/templates/default.xhtml\">" + "\n"
			+ "<ui:define name=\"content\">" + "\n"
			+ "<h1>Welcome to JBoss! " + randomString + "</h1>" + "\n"
			+ "</ui:define>" + "\n"
			+ "</ui:composition>";
	
	@BeforeClass
	public static void createApplication() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		
		if (projectExplorer.containsProject(AbstractCreateApplicationTest.projectName)) {
			projectExplorer.getProject(AbstractCreateApplicationTest.projectName).delete(true);
		}
		
		TestUtils.cleanupGitFolder(AbstractCreateApplicationTest.gitFolder);
		TestUtils.setVisualEditorToUseHTML5();
		
		new TemplatesCreator(DatastoreOS3.SERVER, DatastoreOS3.USERNAME, DatastoreOS3.PROJECT1_DISPLAYED_NAME).
			createOpenShiftApplicationBasedOnServerTemplate(OpenShiftLabel.Others.EAP_TEMPLATE, 
					new TemplateParameter("GITHUB_TRIGGER_SECRET", GIT_SECRET),
					new TemplateParameter("GIT_URI", gitURI));
	}
	
	@Test
	public void testTriggerBuildViaGithubWebhook() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		new WaitUntil(new ResourceExists(DatastoreOS3.SERVER, DatastoreOS3.USERNAME, DatastoreOS3.PROJECT1_DISPLAYED_NAME,
				Resource.BUILD), TimePeriod.VERY_LONG);
		
		explorer.getOpenShift3Connection(DatastoreOS3.USERNAME, DatastoreOS3.SERVER).getProject(
				DatastoreOS3.PROJECT1_DISPLAYED_NAME).getOpenShiftResources(Resource.BUILD).get(0).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.DELETE_RESOURCE).select();
		
		new DefaultShell(OpenShiftLabel.Shell.DELETE_RESOURCE);
		new YesButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.DELETE_RESOURCE));
		
		new WaitWhile(new ResourceExists(DatastoreOS3.SERVER, DatastoreOS3.USERNAME, DatastoreOS3.PROJECT1_DISPLAYED_NAME,
				Resource.BUILD), TimePeriod.VERY_LONG);
		
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		Project project = projectExplorer.getProject(AbstractCreateApplicationTest.projectName);
		project.getProjectItem("src", "main", "webapp", "index.xhtml").open();
		
		setWebPageContent();
		commitAndPush();
		
		new WaitUntil(new ResourceExists(DatastoreOS3.SERVER, DatastoreOS3.USERNAME, DatastoreOS3.PROJECT1_DISPLAYED_NAME,
				Resource.BUILD, "eap-app-2"), TimePeriod.VERY_LONG);
	}
	
	private void setWebPageContent() {
		new WaitUntil(new EditorWithTitleIsAvailable("index.xhtml"));
		new MultiPageEditor("index.xhtml").selectPage("Source");
		TextEditor editor = new TextEditor("index.xhtml");
		editor.setText(webPageContent);
		editor.save();
		editor.close();
	}
	
	private void commitAndPush() {
		ProjectExplorer explorer = new ProjectExplorer();
		explorer.activate();
		explorer.getProject(AbstractCreateApplicationTest.projectName).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.GIT_COMMIT).select();
		
		new DefaultShell(OpenShiftLabel.Shell.COMMIT);
		new DefaultStyledText().setText("Commit from IDE " + System.currentTimeMillis());
		new DefaultToolItem("Select All").click();
		
		new WaitUntil(new WidgetIsEnabled(new PushButton(OpenShiftLabel.Button.COMMIT_PUSH)));
		
		new PushButton(OpenShiftLabel.Button.COMMIT_PUSH).click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.COMMIT), TimePeriod.LONG);
		
		new DefaultShell("Login");
		new LabeledText("User").setText(DatastoreOS3.GIT_USERNAME);
		new LabeledText("Password").setText(DatastoreOS3.GIT_PASSWORD);
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable("Login"));
		
		new DefaultShell("Push Results: jboss-eap-quickstarts - origin");
		new OkButton().click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	@AfterClass
	public static void cleanUp() {
		AbstractCreateApplicationTest.tearDown();
	}
}
