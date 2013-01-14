package org.jboss.tools.modeshape.rest.ui.bot.tests;

import java.io.IOException;
import java.net.URL;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.jboss.tools.modeshape.rest.ui.bot.ext.ModeshapeBot;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.GeneralFile;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.GeneralProject;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotNewObjectWizard;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author apodhrad
 * 
 */
@Require(server = @Server(type = ServerType.ALL, state = ServerState.Running), perspective = "Java EE")
public class Publishing extends SWTTestExt {

	public static final String PROJECT_NAME = "testproject";
	public static final String FILE_NAME = "testfile.txt";
	public static final String FILE_CONTENT = "testcontent";

	private ModeshapeBot modeshapeBot;

	public Publishing() {
		super();
		modeshapeBot = new ModeshapeBot();
	}

	@BeforeClass
	public static void createTestFile() {
		// Create a general project
		SWTBotNewObjectWizard projectWizard = new SWTBotNewObjectWizard();
		projectWizard.open(GeneralProject.LABEL);
		projectWizard.bot().textWithLabel(GeneralProject.TEXT_PROJECT_NAME).setText(PROJECT_NAME);
		projectWizard.finishWithWait();
		// Create a file
		SWTBotNewObjectWizard fileWizard = new SWTBotNewObjectWizard();
		fileWizard.open(GeneralFile.LABEL);
		fileWizard.bot().textWithLabel(GeneralFile.TEXT_FILE_NAME).setText(FILE_NAME);
		fileWizard.bot().tree().select(PROJECT_NAME);
		fileWizard.finishWithWait();
		// Edit the file
		SWTBotEclipseEditor fileEditor = projectExplorer.openFile(PROJECT_NAME, FILE_NAME)
				.toTextEditor();
		fileEditor.setText(FILE_CONTENT);
		fileEditor.saveAndClose();

		assertTrue("The test file wasn't created!",
				projectExplorer.isFilePresent(PROJECT_NAME, FILE_NAME));
	}

	@Test
	public void publishingTest() throws IOException {
		// Create modeshape server
		modeshapeBot.createModeShapeServer();

		// Publish file
		modeshapeBot.publishFile(PROJECT_NAME);
		checkPublishedFile(PROJECT_NAME);
		checkPublishedFile(PROJECT_NAME + "/" + FILE_NAME);

		// Publish location
		String publishLocation = modeshapeBot.getPublishLocation(PROJECT_NAME, FILE_NAME);
		assertTrue("Wrong publish location", modeshapeBot.isFilePublished(new URL(publishLocation)));

		// Unpublish file
		modeshapeBot.unpublishFile(PROJECT_NAME);
		checkUnublishedFile(PROJECT_NAME);
		
		// Delete modeshape server
		modeshapeBot.deleteModeShapeServer();
	}

	private void checkPublishedFile(String path) throws IOException {
		assertTrue("'" + path + "' is not published!", modeshapeBot.isFilePublished(path));
	}

	private void checkUnublishedFile(String path) throws IOException {
		assertTrue("'" + path + "' is still published!", modeshapeBot.isFilePublished(path));
	}

}
