package org.jboss.tools.teiid.ui.bot.test;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.tools.teiid.reddeer.wizard.ModelProjectWizard;
import org.jboss.tools.teiid.reddeer.wizard.TeiidConnectionImportWizard;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.Server;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.State;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.Type;
import org.junit.Test;

/**
* Tests functionality of Teiid connection importer
* @author Lucie Fabrikova, lfabriko@redhat.com
*
*/
@Perspective(name = "Teiid Designer")
@Server(type = Type.ALL, state = State.RUNNING)
public class TeiidConnectionImportTest extends SWTBotTestCase{

	private static final String PROJECT_NAME = "TeiidConnImporter";
	private static final String SQLSERVER_PROPS = "resources/db/sqlserver_books.properties";
	private static final String SQLSERVER_DS = "sqlserverDS";//name of data source
	private static final String SQLSERVER_MODEL = "sqlserverModel";
	
	/**
	 * Create new Teiid Model Project
	 */
	@Test
	public void createProject(){
		int currentPage = 0;//currentPage of wizard must be set to 0
		new ModelProjectWizard(currentPage).create(PROJECT_NAME, true);
	}
	
	/**
	 * Create teiid connection source model
	 */
	@Test
	public void newDataSource(){
		//invoke the import
		TeiidConnectionImportWizard wizard = new TeiidConnectionImportWizard();
		wizard.open();
		//create SQL server data source
		String[] tables = {"dbo.BOOKS", "dbo.AUTHORS", "dbo.PUBLISHERS", "dbo.BOOK_AUTHORS"};
		List<String> tablesToImport = Arrays.asList(tables);  
		wizard.createNewDataSource(SQLSERVER_DS, SQLSERVER_MODEL, TeiidConnectionImportWizard.SQLSERVER_TYPE, SQLSERVER_PROPS, PROJECT_NAME, tablesToImport);
	}
	
}