package org.jboss.tools.teiid.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.tools.teiid.reddeer.ModelProject;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.editor.SQLScrapbookEditor;
import org.jboss.tools.teiid.reddeer.editor.VDBEditor;
import org.jboss.tools.teiid.reddeer.perspective.DatabaseDevelopmentPerspective;
import org.jboss.tools.teiid.reddeer.view.DataSourceExplorer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorerView;
import org.jboss.tools.teiid.reddeer.view.SQLResult;
import org.jboss.tools.teiid.reddeer.wizard.CreateVDB;
import org.jboss.tools.teiid.reddeer.wizard.ImportFileWizard;
import org.jboss.tools.teiid.reddeer.wizard.ImportJDBCDatabaseWizard;
import org.jboss.tools.teiid.reddeer.wizard.ModelProjectWizard;
import org.jboss.tools.teiid.reddeer.wizard.WsdlWebImportWizard;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.Server;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.State;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.Type;
import org.junit.Test;

/**
 * 
 * @author apodhrad
 * 
 */
@Perspective(name = "Teiid Designer")
@Server(type = Type.ALL, state = State.RUNNING)
public class TopDownWsdlTest extends SWTBotTestCase {

	public static final String BUNDLE = "org.teiid.designer.ui.bot.test";
	public static final String PROJECT_NAME = "TopDownWsdlTest";
	public static final String WS_NAME = "ChkOrdSvc";
	public static final String CONNECTION_PROFILE = "TopDownWsdl SQL Profile";
	public static final String VDB_NAME = "testVDB";

	private TeiidBot teiidBot;

	public TopDownWsdlTest() {
		teiidBot = new TeiidBot();
	}

	@Test
	public void topDownWsdlTestScript() throws Exception {
		/* Create new project */
		new ModelProjectWizard().create(PROJECT_NAME);

		/* Import wsdl */
		new ImportFileWizard().importFile("resources/wsdl", "wsdl");
		new WsdlWebImportWizard().importWsdl(WS_NAME, PROJECT_NAME, "TpcrOrderChecking.wsdl");

		/* Create DB connection profile */
		teiidBot.createDatabaseProfile(CONNECTION_PROFILE, "resources/db/sqlserver_tpcr.properties");

		/* Import a Relational Source */
		String fileName = "TPCR_S2k.xmi";

		ImportJDBCDatabaseWizard wizard = new ImportJDBCDatabaseWizard();
		wizard.setConnectionProfile(CONNECTION_PROFILE);
		wizard.setProjectName(PROJECT_NAME);
		wizard.setModelName(fileName);
		wizard.execute();

		ModelProject modelproject = teiidBot.modelExplorer().getModelProject(PROJECT_NAME);
		assertTrue(fileName + " not created!", modelproject.containsItem(fileName));

		open(WS_NAME + "Responses.xmi", "Service1Soap_CheckOrder_OCout", "Mapping Diagram");

		ModelEditor modelEditor = new ModelEditor(WS_NAME + "Responses.xmi");
		modelEditor.show();
		modelEditor.showMappingTransformation("CONTAINER");

		/* Map XML view to Relational Sources */
		String sql = "SELECT convert(O_ORDERKEY, string) AS ORDER_KEY, convert(O_ORDERDATE, date) AS ORDER_DATE, C_NAME AS CUSTOMER, convert(P_PARTKEY, string) AS PART_KEY, P_NAME AS PART_NAME, convert(L_SHIPDATE, date) AS SHIP_DATE, O_ORDERSTATUS AS ORDER_STATUS, P_COMMENT AS PART_COMMENT, C_COMMENT AS CUSTOMER_COMMENT "
				+ "FROM TPCR_S2k.ORDERS, TPCR_S2k.PART, TPCR_S2k.LINEITEM, TPCR_S2k.CUSTOMER "
				+ "WHERE (O_ORDERDATE = {ts'1993-03-31 00:00:00.0'}) AND (O_ORDERKEY = L_ORDERKEY) AND (O_CUSTKEY = C_CUSTKEY) AND (L_PARTKEY = P_PARTKEY) AND (L_SHIPDATE BETWEEN {ts'1993-04-01 00:00:00.0'} AND {ts'1993-04-15 00:00:00.0'})";

		modelEditor.setTransformation(sql);
		modelEditor.saveAndValidateSql();
		modelEditor.save();

		/* Build the WS Operation's transformation */
		String procedureSql = "CREATE VIRTUAL PROCEDURE\n"
				+ "BEGIN\n"
				+ "\tDECLARE string VARIABLES.IN_ShipDateHigh;\n"
				+ "\tVARIABLES.IN_ShipDateHigh = xpathValue(ChkOrdSvc.Service1Soap.CheckOrder.OCin, '/*:OC_Input/*:ShipDateHigh');\n"
				+ "\tDECLARE string VARIABLES.IN_ShipDateLow;\n"
				+ "\tVARIABLES.IN_ShipDateLow = xpathValue(ChkOrdSvc.Service1Soap.CheckOrder.OCin, '/*:OC_Input/*:ShipDateLow');\n"
				+ "\tDECLARE string VARIABLES.IN_OrderDate;\n"
				+ "\tVARIABLES.IN_OrderDate = xpathValue(ChkOrdSvc.Service1Soap.CheckOrder.OCin, '/*:OC_Input/*:OrderDate');\n";
		String selectSql = "SELECT * FROM ChkOrdSvcResponses.Service1Soap_CheckOrder_OCout WHERE (ChkOrdSvcResponses.Service1Soap_CheckOrder_OCout.OC_Output.CONTAINER.ORDER_DATE = parseDate(VARIABLES.IN_ORDERDATE, 'yyyy-MM-dd')) AND ((ChkOrdSvcResponses.Service1Soap_CheckOrder_OCout.OC_Output.CONTAINER.SHIP_DATE >= parseDate(VARIABLES.IN_SHIPDATELOW, 'yyyy-MM-dd')) AND (ChkOrdSvcResponses.Service1Soap_CheckOrder_OCout.OC_Output.CONTAINER.SHIP_DATE <= parseDate(VARIABLES.IN_SHIPDATEHIGH, 'yyyy-MM-dd')));";

		open(WS_NAME + ".xmi", "Service1Soap", "CheckOrder", "Transformation Diagram");

		modelEditor = new ModelEditor(WS_NAME + ".xmi");
		modelEditor.show();
		modelEditor.showTransformation();
		modelEditor.setTransformation(procedureSql + selectSql + "\nEND");
		modelEditor.saveAndValidateSql();
		modelEditor.save();

		/* Create a VDB */
		CreateVDB createVDB = new CreateVDB();
		createVDB.setFolder(PROJECT_NAME);
		createVDB.setName(VDB_NAME);
		createVDB.execute();

		VDBEditor editor = new VDBEditor("testVDB.vdb");
		editor.show();
		editor.addModel(PROJECT_NAME, WS_NAME + ".xmi");
		editor.save();

		// TODO: check the following ChkOrdSvc.xmi ChkOrdSvcResponse.xmi
		// TPCSR_S2k.xmi TpcrOrderChecking.xsd

		new ModelExplorerView().executeVDB(PROJECT_NAME, VDB_NAME + ".vdb");
	
		new DataSourceExplorer().openSQLScrapbook(VDB_NAME + ".*", true);

		SQLScrapbookEditor sqlEditor = new SQLScrapbookEditor();
		sqlEditor.show();
		sqlEditor.setDatabase(VDB_NAME);

		String testSql = "SELECT * FROM ChkOrdSvcResponses.Service1Soap_CheckOrder_OCout";
		sqlEditor.setText(testSql);
		sqlEditor.executeAll();

		SQLResult result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView()
				.getByOperation(testSql);
		assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());

		testSql = "EXEC ChkOrdSvc.Service1Soap.CheckOrder('<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<OC_Input xmlns=\"http://com.metamatrix/TPCRwsdl_VDB\">"
				+ "<OrderDate>1993-03-31</OrderDate>" + "<ShipDateLow>1993-04-01</ShipDateLow>"
				+ "<ShipDateHigh>1993-04-02</ShipDateHigh>" + "</OC_Input>')";

		sqlEditor.setText(testSql);
		sqlEditor.executeAll();
		result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation(testSql);
		assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());

		// Close the editor without saving
		sqlEditor.close();

		// Generate the WAR file
	}

	private static void open(String... path) {
		new ModelExplorer().getModelProject(PROJECT_NAME).open(path);
	}
}