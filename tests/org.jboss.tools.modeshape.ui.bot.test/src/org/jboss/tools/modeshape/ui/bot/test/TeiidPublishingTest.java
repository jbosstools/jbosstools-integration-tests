package org.jboss.tools.modeshape.ui.bot.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.tools.modeshape.reddeer.util.ModeshapeWebdav;
import org.jboss.tools.modeshape.reddeer.util.TeiidDriver;
import org.jboss.tools.modeshape.reddeer.view.ModeshapeExplorer;
import org.jboss.tools.modeshape.reddeer.view.ModeshapeView;
import org.jboss.tools.modeshape.reddeer.wizard.ImportProjectWizard;
import org.jboss.tools.modeshape.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.modeshape.ui.bot.test.suite.ModeshapeSuite;
import org.jboss.tools.modeshape.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.modeshape.ui.bot.test.suite.ServerRequirement.Server;
import org.jboss.tools.modeshape.ui.bot.test.suite.ServerRequirement.State;
import org.jboss.tools.modeshape.ui.bot.test.suite.ServerRequirement.Type;
import org.junit.Test;

/**
 * Bot test for publishing Teiid files into ModeShape repository.
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@Perspective(name = "Teiid Designer")
@Server(type = Type.SOA, state = State.RUNNING)
public class TeiidPublishingTest extends SWTBotTestCase {

	public static final String SERVER_URL = "http://localhost:8080/modeshape-rest";
	public static final String USER = "admin";
	public static final String PASSWORD = "admin";

	@Test
	public void publishingTest() throws Exception {
		/* Create ModeShape Server */
		new ModeshapeView().addServer(SERVER_URL, USER, PASSWORD);

		new ImportProjectWizard("resources/projects/ModeShapeGoodies.zip").execute();
		new ModeshapeExplorer().publish("ModeShapeGoodies").finish();
		checkPublishedFile("/ModeShapeGoodies/BookDatatypes.xsd");
		checkPublishedFile("/ModeShapeGoodies/Books.xsd");
		checkPublishedFile("/ModeShapeGoodies/BooksDoc.xmi");
		checkPublishedFile("/ModeShapeGoodies/BooksMode.vdb");
		checkPublishedFile("/ModeShapeGoodies/Parts_Metadata.txt");
		checkPublishedFile("/ModeShapeGoodies/PartsData.csv");
		checkPublishedFile("/ModeShapeGoodies/RelModels/Books_Oracle.xmi");
		checkPublishedFile("/ModeShapeGoodies/RelModels/BooksInfo.xmi");

		/* Test ModeShape VDB on Teiid server */
		String path = ModeshapeSuite.getServerPath();
		DriverManager.registerDriver(new TeiidDriver(path + "/client/teiid-client.jar"));
		Connection conn = DriverManager.getConnection("jdbc:teiid:ModeShape@mm://localhost:31000", "user", "user");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM ModeShape.xmi_model");
		List<String> result = new ArrayList<String>();
		while (rs.next()) {
			result.add(rs.getString("jcr_name"));
		}
		conn.close();

		assertTrue("Model 'Books_Oracle' isn't involved in ModeShape VDB", result.contains("Books_Oracle"));
		assertTrue("Model 'BooksInfo' isn't involved in ModeShape VDB", result.contains("BooksInfo"));
	}

	private void checkPublishedFile(String path) throws IOException {
		String repository = ModeshapeSuite.getModeshapeRepository();
		boolean result = new ModeshapeWebdav(repository).isFileAvailable(path, USER, PASSWORD);
		assertTrue("File '" + path + "' is not published!", result);
	}

}
