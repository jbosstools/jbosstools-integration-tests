package org.teiid.designer.ui.bot.test;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.tools.teiid.reddeer.VDB;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.condition.ProgressInformationShellIsActiveCondition;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.junit.AfterClass;
import org.junit.Test;
import org.teiid.designer.ui.bot.ext.teiid.TeiidBot;
import org.teiid.designer.ui.bot.ext.teiid.editor.CriteriaBuilder;
import org.teiid.designer.ui.bot.ext.teiid.editor.ModelEditor;
import org.teiid.designer.ui.bot.ext.teiid.editor.SQLScrapbookEditor;
import org.teiid.designer.ui.bot.ext.teiid.editor.VDBEditor;
import org.teiid.designer.ui.bot.ext.teiid.perspective.DatabaseDevelopmentPerspective;
import org.teiid.designer.ui.bot.ext.teiid.perspective.TeiidPerspective;
import org.teiid.designer.ui.bot.ext.teiid.view.ModelExplorerView;
import org.teiid.designer.ui.bot.ext.teiid.view.Procedure;
import org.teiid.designer.ui.bot.ext.teiid.view.SQLResult;
import org.teiid.designer.ui.bot.ext.teiid.wizard.CreateMetadataModel;
import org.teiid.designer.ui.bot.ext.teiid.wizard.CreateVDB;
import org.teiid.designer.ui.bot.ext.teiid.wizard.ImportJDBCDatabaseWizard;
import org.teiid.designer.ui.bot.test.suite.TeiidDesignerTestCase;


@Require(server = @Server(state = ServerState.Running), secureStorage = true, perspective = "Teiid Designer")
public class VirtualGroupTutorialTest extends TeiidDesignerTestCase {

	private static final String PROJECT_NAME = "MyFirstProject";

	private static final String ORACLE_MODEL_NAME = "PartsSupplier_Oracle.xmi";

	private static final String ORACLE_CONNPROFILE_NAME = "PartsSupplier Oracle";

	private static final String SQLSERVER_MODEL_NAME = "PartsSupplier_SQLServer.xmi";

	private static final String SQLSERVER_CONNPROFILE_NAME = "PartsSupplier SQL Server";

	private static final String VIRTUAL_MODEL_NAME = "PartsVirtual.xmi";
	
	private static final String VDB_NAME = "MyFirstVDB";
	
	private static final String VDB_FILE_NAME = VDB_NAME + ".vdb";

	private static final String TRANSFORMATION_SQL = "SELECT\n\t\t"           +
		
	         "PartsSupplier_Oracle.SUPPLIER.SUPPLIER_ID, "      +
	         "PartsSupplier_Oracle.SUPPLIER.SUPPLIER_NAME, "    +
	         "PartsSupplier_Oracle.SUPPLIER.SUPPLIER_STATUS, "  +
	         "PartsSupplier_Oracle.SUPPLIER.SUPPLIER_CITY, "    + 
	         "PartsSupplier_Oracle.SUPPLIER.SUPPLIER_STATE, "   +
	               "PartsSupplier_SQLServer.SUPPLIER_PARTS."    + 
	                                       "SUPPLIER_ID AS "    + 
	                                       "SUPPLIER_ID_1, "    +
	"PartsSupplier_SQLServer.SUPPLIER_PARTS.PART_ID, "          +
	"PartsSupplier_SQLServer.SUPPLIER_PARTS.QUANTITY, "         +
	"PartsSupplier_SQLServer.SUPPLIER_PARTS.SHIPPER_ID\n\t"     +
	                             
	                                       "FROM\n\t\t"         +
	                                                            
	                  "PartsSupplier_Oracle.SUPPLIER, "         +
	               "PartsSupplier_SQLServer.SUPPLIER_PARTS\n\t" +
	                              
	                                       "WHERE\n\t\t"        +
	                                                            
	               "PartsSupplier_SQLServer.SUPPLIER_PARTS."    + 
	                                       "SUPPLIER_ID = "     + 
	                  "PartsSupplier_Oracle.SUPPLIER."          +
	                                       "SUPPLIER_ID";

	private static final String PROCEDURE_SQL = "CREATE VIRTUAL PROCEDURE\n"                +
	   "BEGIN\n\t"                                 +
	   "SELECT * FROM PartsVirtual.OnHand "        +
	   "WHERE PartsVirtual.OnHand.QUANTITY = "     +
	   "PartsVirtual.getOnHandByQuantity.qtyIn;\n" +
	   "END";

	private static final String TESTSQL_1 = "SELECT * FROM PartsSupplier_Oracle.PARTS";

	private static final String TESTSQL_2 = "SELECT * FROM PartsVirtual.OnHand";

	private static final String TESTSQL_3 = "SELECT * FROM PartsVirtual.OnHand WHERE QUANTITY > 200"; //should return 126 rows

	private static final String TESTSQL_4 = "SELECT " + 
													"O.SUPPLIER_NAME, "                  + 
													"O.PART_ID, "                        +
													"P.PART_NAME "                       + 
													
											"FROM "                                      + 
													"PartsSupplier_Oracle.PARTS AS P, "  + 
													"PartsVirtual.OnHand AS O "          + 
											"WHERE "                                     + 
													"(P.PART_ID = O.PART_ID) and "       + 
													"(O.SUPPLIER_NAME LIKE '%la%') "     + 
													"ORDER BY PART_NAME"; //it should return 30 rows

	private static final String TESTSQL_5 = "EXEC PartsVirtual.getOnHandByQuantity( 200 )"; //it should return 30 rows

	private static final String VIRTUAL_TABLE_NAME = "OnHand";
	
	private static final String PROCEDURE_NAME = "getOnHandByQuantity";
	
	private TeiidBot teiidBot;

	public VirtualGroupTutorialTest() {
		teiidBot = new TeiidBot();
	}

	@Test
	public void virtualGroupTutorialTest() {
		teiidBot.showTeiidView();
		if (configuredState.getServer().isRunning) {
			teiidBot.setSecureStoragePassword();
		}
		
		createProject(PROJECT_NAME);
		
		teiidBot.createDatabaseProfile(ORACLE_CONNPROFILE_NAME, "resources/db/oracle_parts.properties");
		teiidBot.createDatabaseProfile(SQLSERVER_CONNPROFILE_NAME, "resources/db/sqlserver_parts.properties");
		
		eclipse.maximizeActiveShell();
		
		createOracleModel();
		createSQLServerModel();
		createViewModel();
		createTransformation();
		createVDB();
		executeVDB();
		executeSqlQueries();
		createProcedure();
		updateVDB();
		deployUpdatedVDB();
		executeProcedureQuery();
		
	}


	public void createOracleModel(){
		ImportJDBCDatabaseWizard wizard = new ImportJDBCDatabaseWizard();
		wizard.setConnectionProfile(ORACLE_CONNPROFILE_NAME);
		wizard.setProjectName(PROJECT_NAME);
		wizard.setModelName(ORACLE_MODEL_NAME);
		wizard.execute();

		assertTrue(ORACLE_MODEL_NAME + " not created!", projectExplorer.existsResource(PROJECT_NAME, ORACLE_MODEL_NAME));
	}


	public void createSQLServerModel(){
		ImportJDBCDatabaseWizard wizard = new ImportJDBCDatabaseWizard();
		wizard.setConnectionProfile(SQLSERVER_CONNPROFILE_NAME);
		wizard.setProjectName(PROJECT_NAME);
		wizard.setModelName(SQLSERVER_MODEL_NAME);
		wizard.execute();

		assertTrue(SQLSERVER_MODEL_NAME + " not created!", projectExplorer.existsResource(PROJECT_NAME, SQLSERVER_MODEL_NAME));
	}


	public void createViewModel(){
		CreateMetadataModel newModel = new CreateMetadataModel();
		newModel.setLocation(PROJECT_NAME);
		newModel.setName(VIRTUAL_MODEL_NAME);
		newModel.setClass(CreateMetadataModel.ModelClass.RELATIONAL);
		newModel.setType(CreateMetadataModel.ModelType.VIEW);
		newModel.execute();

		assertTrue(VIRTUAL_MODEL_NAME + " not created!", projectExplorer.existsResource(PROJECT_NAME, VIRTUAL_MODEL_NAME));
	}

	public void createTransformation(){
		ModelExplorerView modelView = TeiidPerspective.getInstance().getModelExplorerView();
		modelView.newBaseTable(PROJECT_NAME, VIRTUAL_MODEL_NAME, VIRTUAL_TABLE_NAME);
		modelView.openTransformationDiagram(PROJECT_NAME, VIRTUAL_MODEL_NAME, VIRTUAL_TABLE_NAME);
		modelView.addTransformationSource(PROJECT_NAME, ORACLE_MODEL_NAME, "SUPPLIER");
		modelView.addTransformationSource(PROJECT_NAME, SQLSERVER_MODEL_NAME, "SUPPLIER_PARTS");

		ModelEditor editor = modelEditor(VIRTUAL_MODEL_NAME);
		editor.show();
		editor.showTransformation();

		CriteriaBuilder criteriaBuilder = editor.criteriaBuilder();
		criteriaBuilder.selectRightAttribute("PartsSupplier_Oracle.SUPPLIER", "SUPPLIER_ID");
		criteriaBuilder.selectLeftAttribute("PartsSupplier_SQLServer.SUPPLIER_PARTS", "SUPPLIER_ID");
		criteriaBuilder.apply();
		criteriaBuilder.finish();

		editor.save();

		assertEquals("SQL Statements do not match!", TRANSFORMATION_SQL, editor.getTransformation());
	}

	public void createVDB(){
		CreateVDB createVDB = new CreateVDB();
		createVDB.setFolder(PROJECT_NAME);
		createVDB.setName(VDB_NAME);
		createVDB.execute();

		VDBEditor editor = VDBEditor.getInstance(VDB_FILE_NAME);
		editor.show();
		editor.addModel(PROJECT_NAME, VIRTUAL_MODEL_NAME);
		editor.save();

		assertEquals(ORACLE_MODEL_NAME, editor.getModel(0));
		assertEquals(SQLSERVER_MODEL_NAME, editor.getModel(1));
		assertEquals(VIRTUAL_MODEL_NAME, editor.getModel(2));
	}

	public void executeVDB() {
		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.open();
		VDB vdb = modelExplorer.getModelProject(PROJECT_NAME).getVDB(VDB_FILE_NAME);
		vdb.deployVDB();
		bot.waitWhile(new ProgressInformationShellIsActiveCondition(), TaskDuration.LONG.getTimeout());
		bot.waitWhile(new NonSystemJobRunsCondition(), TaskDuration.LONG.getTimeout());
		vdb.executeVDB();
	}
	
	public void executeSqlQueries(){
		SQLScrapbookEditor editor = new SQLScrapbookEditor("SQL Scrapbook0");
		editor.show();
		editor.setDatabase(VDB_NAME);


		// TESTSQL_1  
		editor.setText(TESTSQL_1);
		editor.executeAll();
		
		SQLResult result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation(TESTSQL_1);
		assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());

		// TESTSQL_2 
		editor.show();
		editor.setText(TESTSQL_2);
		editor.executeAll();

		result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation(TESTSQL_2);
		assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());

		// TESTSQL_3 
		editor.show();
		editor.setText(TESTSQL_3);
		editor.executeAll();

		result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation(TESTSQL_3);
		assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());
		assertEquals(126, result.getCount());

		// TESTSQL_4
		editor.show();
		editor.setText(TESTSQL_4);
		editor.executeAll();

		result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation(TESTSQL_4);
		assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());
		assertEquals(30, result.getCount());
	}

	public void createProcedure(){
		ModelExplorerView modelView = TeiidPerspective.getInstance().getModelExplorerView();
		Procedure procedure = modelView.newProcedure(PROJECT_NAME, VIRTUAL_MODEL_NAME, PROCEDURE_NAME);
		procedure.addParameter("qtyIn", "short : xs:int");
		modelView.openTransformationDiagram(PROJECT_NAME, VIRTUAL_MODEL_NAME, PROCEDURE_NAME);
		
		ModelEditor editor = modelEditor(VIRTUAL_MODEL_NAME);
		editor.show();
		editor.showTransformation();
		editor.setTransformationProcedureBody("SELECT * FROM PartsVirtual.OnHand;");
		editor.save();
		
		CriteriaBuilder criteriaBuilder = editor.criteriaBuilder();
		criteriaBuilder.selectLeftAttribute("PartsVirtual." + VIRTUAL_TABLE_NAME, "QUANTITY");
		criteriaBuilder.selectRightAttribute("PartsVirtual." + PROCEDURE_NAME, "qtyIn");
		criteriaBuilder.apply();
		criteriaBuilder.finish();

		editor.save();

		assertEquals(PROCEDURE_SQL, editor.getTransformation());
	}

	public void updateVDB(){
		TeiidPerspective.getInstance().getModelExplorerView().open(PROJECT_NAME, VDB_FILE_NAME);
		
		VDBEditor editor = VDBEditor.getInstance(VDB_FILE_NAME);
		editor.show();
		editor.synchronizeAll();
		editor.save();

		assertEquals(ORACLE_MODEL_NAME, editor.getModel(0));
		assertEquals(SQLSERVER_MODEL_NAME, editor.getModel(1));
		assertEquals(VIRTUAL_MODEL_NAME, editor.getModel(2));
	}

	public void deployUpdatedVDB(){
		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.open();
		VDB vdb = modelExplorer.getModelProject(PROJECT_NAME).getVDB(VDB_FILE_NAME);
		// simple execute vdb isn't enough, we have to deploy and then execute vdb
		vdb.deployVDB();
		bot.waitWhile(new ProgressInformationShellIsActiveCondition(), TaskDuration.LONG.getTimeout());
		bot.waitWhile(new NonSystemJobRunsCondition(), TaskDuration.LONG.getTimeout());
		vdb.executeVDB();
	}


	public void executeProcedureQuery(){
		SQLScrapbookEditor editor = new SQLScrapbookEditor("SQL Scrapbook1");
		editor.show();
		editor.setDatabase(VDB_NAME);
		editor.setText(TESTSQL_5);
		editor.executeAll();
		
		SQLResult result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation(TESTSQL_5);
		assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());
		// The procedure doesn't return result, is this really intended?
		// assertEquals(30, result.getCount());
	}

	@AfterClass
	public static void closeScrapbookEditor(){
		closeScrapbook();
		closeVDBEditor();
		closeModelEditor(VIRTUAL_MODEL_NAME);
		closeModelEditor(ORACLE_MODEL_NAME);
		closeModelEditor(SQLSERVER_MODEL_NAME);
		closeAutoConnectToTeiidInstance();
	}

	private static void closeScrapbook() {
		try {
			SQLScrapbookEditor editor = new SQLScrapbookEditor("SQL Scrapbook0");
			editor.show();
			editor.close();
			editor = new SQLScrapbookEditor("SQL Scrapbook1");
			editor.show();
			editor.close();
		} catch (WidgetNotFoundException e){
			
		}
	}
	
	private static void closeVDBEditor() {
		try {
			VDBEditor editor = VDBEditor.getInstance(VDB_NAME + ".vdb");
			editor.close();
		} catch (WidgetNotFoundException e){
			
		}
	}
	
	private static void closeModelEditor(String name) {
		try {
			ModelEditor editor = modelEditor(name);
			editor.close();
		} catch (WidgetNotFoundException e){
			
		}
	}
	
	private static void closeAutoConnectToTeiidInstance() {
		SWTBotExt bot = SWTBotFactory.getBot();
		
		try {
			bot.shell("Auto Connect to New Teiid Instance").close();
		} catch (WidgetNotFoundException e){
			
		}
	}
}
