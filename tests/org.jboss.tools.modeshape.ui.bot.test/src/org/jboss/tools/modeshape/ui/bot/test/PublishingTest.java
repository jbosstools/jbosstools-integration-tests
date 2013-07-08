package org.jboss.tools.modeshape.ui.bot.test;

import java.net.URL;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.ide.NewJavaProjectWizardDialog;
import org.jboss.reddeer.eclipse.ui.ide.NewFileCreationWizardDialog;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.modeshape.reddeer.shell.PublishedLocations;
import org.jboss.tools.modeshape.reddeer.util.ModeshapeWebdav;
import org.jboss.tools.modeshape.reddeer.view.ModeshapeExplorer;
import org.jboss.tools.modeshape.reddeer.view.ModeshapeView;
import org.jboss.tools.modeshape.ui.bot.test.suite.ModeshapeSuite;
import org.jboss.tools.modeshape.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.modeshape.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.modeshape.ui.bot.test.suite.ServerRequirement.Server;
import org.jboss.tools.modeshape.ui.bot.test.suite.ServerRequirement.State;
import org.jboss.tools.modeshape.ui.bot.test.suite.ServerRequirement.Type;
import org.junit.Test;

/**
 * Bot test for publishing/unpublishing files into/from ModeShape repository.
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@Perspective(name = "Java")
@Server(type = Type.ALL, state = State.RUNNING)
public class PublishingTest extends SWTBotTestCase {

	public static final String PROJECT_NAME = "testproject";
	public static final String FILE_NAME = "testfile.txt";
	public static final String FILE_CONTENT = "testcontent";

	public static final String SERVER_URL = "http://localhost:8080/modeshape-rest";
	public static final String USER = "admin";
	public static final String PASSWORD = "admin";

	@Test
	public void publishingTest() throws Exception {
		/* Create Java Project */
		NewJavaProjectWizardDialog projectWizard = new NewJavaProjectWizardDialog();
		projectWizard.open();
		projectWizard.getFirstPage().setProjectName(PROJECT_NAME);
		projectWizard.finish();

		/* Create Text File */
		NewFileCreationWizardDialog fileWizard = new NewFileCreationWizardDialog();
		fileWizard.open();
		fileWizard.getFirstPage().setFolderPath(PROJECT_NAME);
		fileWizard.getFirstPage().setFileName(FILE_NAME);
		fileWizard.finish();

		/* Edit Text File */
		SWTBotEditor editor = Bot.get().editorByTitle(FILE_NAME);
		editor.toTextEditor().typeText(FILE_CONTENT);
		editor.saveAndClose();

		/* Create ModeSHape Server */
		new ModeshapeView().addServer(SERVER_URL, USER, PASSWORD);

		/* Get ModeSHape Repository */
		String repository = ModeshapeSuite.getModeshapeRepository();

		/* Publish */
		new ModeshapeExplorer().publish(PROJECT_NAME).finish();
		assertTrue(new ModeshapeWebdav(repository).isFileAvailable(PROJECT_NAME + "/" + FILE_NAME, USER, PASSWORD));

		/* Published Locations */
		PublishedLocations locations = new ModeshapeExplorer().showPublishedLocations(PROJECT_NAME, FILE_NAME);
		String url = locations.getPublishedUrl();
		locations.ok();
		assertTrue(new ModeshapeWebdav(repository).isFileAvailable(new URL(url), USER, PASSWORD));

		/* Unpublish */
		new ModeshapeExplorer().unpublish(PROJECT_NAME).finish();
		assertFalse(new ModeshapeWebdav(repository).isFileAvailable(PROJECT_NAME + "/" + FILE_NAME, USER, PASSWORD));
	}

}
