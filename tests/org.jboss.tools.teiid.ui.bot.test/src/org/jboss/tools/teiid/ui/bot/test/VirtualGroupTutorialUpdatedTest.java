package org.jboss.tools.teiid.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.perspective.DatabaseDevelopmentPerspective;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.view.GuidesView;
import org.jboss.tools.teiid.reddeer.view.SQLResult;
import org.jboss.tools.teiid.reddeer.wizard.ImportJDBCDatabaseWizard;
import org.jboss.tools.teiid.reddeer.wizard.ModelProjectWizard;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.Server;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.State;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.Type;
import org.junit.Test;

/**
 * Test functions described in testscript E2eVirtualGroupTutorial.
 * In contrast to VirtualGroupTutorialTest, it uses Modelling action sets and creates preview.
 * @author Lucie Fabrikova, lfabriko@redhat.com
 *
 */
@Perspective(name = "Teiid Designer")//initialize tests in this perspective
@Server(type = Type.ALL, state = State.RUNNING)//uses info about server - swtbot.properties
public class VirtualGroupTutorialUpdatedTest extends SWTBotTestCase{

	private static final String PROJECT_NAME = "MyFirstProject";

	private static TeiidBot teiidBot = new TeiidBot();
	
	private String jdbcProfile = "HSQLDB Profile";
	private String jdbcProfile2 = "HSQLDB Profile 2";
	
	private String partssupModel1 = "partssupModel1.xmi";
	private String partssupModel2 = "partssupModel2.xmi";
	
	private String props1 = "resources/db/ds1.properties";
	private String props2 = "resources/db/ds2.properties";
	
	private String TESTSQL_1 = "select * from \"partssupModel1\".\"PARTS\"";
	
	/**
	 * Create new Teiid Model Project
	 */
	@Test
	public void createProject(){
		int currentPage = 0;//currentPage of wizard must be set to 0
		new ModelProjectWizard(currentPage).create(PROJECT_NAME, true);
	}
	
	/**
	 * Create connection profiles to HSQL databases
	 */
	@Test
	public void createConnProfiles(){
		teiidBot.createHsqlProfile(props1, jdbcProfile, true, true);
		teiidBot.createHsqlProfile(props2, jdbcProfile2, false, true);
	}
	
	/**
	 * Create data sources for connection profiles
	 */
	@Test
	public void createSources(){
		//datasource ds1
		importFromHsql(jdbcProfile, partssupModel1);
		
		//datasource ds2
		importFromHsql(jdbcProfile2, partssupModel2);//import the same tables
	}
	
	/**
	 * Preview data from model
	 */
	@Test
	public void previewData(){
		//wait until project is saved
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		new GuidesView().previewData(true, PROJECT_NAME, partssupModel1, "PARTS");
		
		SQLResult result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation(TESTSQL_1);
		assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());
		
		new WaitWhile(new IsInProgress(), TimePeriod.NORMAL);
		TeiidPerspective.getInstance().open();
	}
	
	
	/**
	 * Import tables from HSQL database
	 * @param connProfile name of connection profile (e.g. HSQLDB Profile)
	 * @param modelName name of model (e.g. partssupplier.xmi)
	 */
	public void importFromHsql(String connProfile, String modelName) {
		ImportJDBCDatabaseWizard wizard = new ImportJDBCDatabaseWizard();
		wizard.setConnectionProfile(connProfile);
		wizard.setProjectName(PROJECT_NAME);
		wizard.setModelName(modelName);
		// add all tables
		wizard.addItem("PUBLIC/PUBLIC/TABLE/PARTS");
		wizard.addItem("PUBLIC/PUBLIC/TABLE/SHIP_VIA");
		wizard.addItem("PUBLIC/PUBLIC/TABLE/STATUS");
		wizard.addItem("PUBLIC/PUBLIC/TABLE/SUPPLIER");
		wizard.addItem("PUBLIC/PUBLIC/TABLE/SUPPLIER_PARTS");

		wizard.execute(true);
	}
	
	
	
}
