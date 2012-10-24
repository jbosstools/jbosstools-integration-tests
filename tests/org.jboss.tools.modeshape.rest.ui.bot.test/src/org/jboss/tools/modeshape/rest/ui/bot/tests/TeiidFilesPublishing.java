package org.jboss.tools.modeshape.rest.ui.bot.tests;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.jboss.tools.modeshape.rest.ui.bot.ext.ModeshapeBot;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.gen.IView;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.Import.GeneralExistingProjectsintoWorkspace;
import org.jboss.tools.ui.bot.ext.helper.ResourceHelper;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotImportWizard;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This test publishes the whole Teiid project via Model Explorer. The published
 * files are checked via WebDav.
 * 
 * @author apodhrad
 * 
 */
@Require(server = @Server(type = ServerType.ALL, state = ServerState.Running), perspective = "Teiid Designer")
public class TeiidFilesPublishing extends SWTTestExt {

	public static final String BUNDLE = "org.jboss.tools.modeshape.rest.ui.bot.test";

	private ModeshapeBot modeshapeBot;

	public TeiidFilesPublishing() {
		super();
		modeshapeBot = new ModeshapeBot();
		modeshapeBot.setModeshapeExplorer(new IView() {
			public String getName() {
				return "Model Explorer";
			}

			public List<String> getGroupPath() {
				List<String> l = new Vector<String>();
				l.add("Teiid Designer");
				return l;
			}
		});
	}

	@BeforeClass
	public static void importProject() {
		String path = ResourceHelper.getResourceAbsolutePath(BUNDLE, "resources",
				"ModeShapeGoodies.zip");
		SWTBotImportWizard wizard = new SWTBotImportWizard();
		wizard.open(GeneralExistingProjectsintoWorkspace.LABEL);
		wizard.bot().radio(1).click();
		wizard.bot().text(1).setText(path);
		wizard.bot().button("Refresh").click();
		wizard.finishWithWait();
	}

	@Test
	public void testTeiidPublishing() throws Exception {
		modeshapeBot.createModeShapeServer();
		modeshapeBot.publishFile("ModeShapeGoodies");
		checkPublishedFile("/ModeShapeGoodies/BookDatatypes.xsd");
		checkPublishedFile("/ModeShapeGoodies/Books.xsd");
		checkPublishedFile("/ModeShapeGoodies/BooksDoc.xmi");
		checkPublishedFile("/ModeShapeGoodies/BooksMode.vdb");
		checkPublishedFile("/ModeShapeGoodies/Parts_Metadata.txt");
		checkPublishedFile("/ModeShapeGoodies/PartsData.csv");
		checkPublishedFile("/ModeShapeGoodies/RelModels/Books_Oracle.xmi");
		checkPublishedFile("/ModeShapeGoodies/RelModels/BooksInfo.xmi");
	}

	private void checkPublishedFile(String path) throws IOException {
		assertTrue("File '" + path + "' is not published!", modeshapeBot.isFilePublished(path));
	}

}
