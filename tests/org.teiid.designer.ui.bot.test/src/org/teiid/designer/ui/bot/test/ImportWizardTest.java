package org.teiid.designer.ui.bot.test;

import java.util.Arrays;

import org.jboss.reddeer.eclipse.datatools.ui.FlatFileProfile;
import org.jboss.tools.teiid.reddeer.ModelProject;
import org.jboss.tools.teiid.reddeer.wizard.DDLImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.FlatImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.MetadataImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.MetadataImportWizard.ImportType;
import org.jboss.tools.teiid.reddeer.wizard.TeiidImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.WsdlImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.WsdlProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.XMLImportWizard;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.BeforeClass;
import org.junit.Test;
import org.teiid.designer.ui.bot.ext.teiid.TeiidBot;
import org.teiid.designer.ui.bot.ext.teiid.editor.ModelEditor;
import org.teiid.designer.ui.bot.ext.teiid.wizard.ImportJDBCDatabaseWizard;

/**
 * Tests for importing relational models from various sources
 * 
 * @author apodhrad
 * 
 */
@Require(perspective = "Teiid Designer")
public class ImportWizardTest extends SWTTestExt {

	public static final String MODEL_PROJECT = "importTest";

	private static TeiidBot teiidBot = new TeiidBot();

	@BeforeClass
	public static void createModelProject() {
		eclipse.maximizeActiveShell();
		teiidBot.createModelProject(MODEL_PROJECT);
	}

	@Test
	public void jdbcImportTest() {
		String jdbcProfile = "HSQLDB Profile";
		String empModel = "Emp.xmi";

		teiidBot.createDatabaseProfile(jdbcProfile, "resources/db/hsqldb.properties");

		ImportJDBCDatabaseWizard wizard = new ImportJDBCDatabaseWizard();
		wizard.setConnectionProfile(jdbcProfile);
		wizard.setProjectName(MODEL_PROJECT);
		wizard.setModelName(empModel);
		wizard.addItem("PUBLIC/PUBLIC/TABLE/CUSTOMER");
		wizard.addItem("PUBLIC/PUBLIC/TABLE/ORDER");
		wizard.execute();

		checkResource(empModel);
		checkDiagram(empModel, "ORDER");
		checkDiagram(empModel, "CUSTOMER");
	}

	@Test
	public void flatImportTest() {
		String flatProfile = "Flat Profile";

		FlatFileProfile flatFileProfile = teiidBot.createFlatFileProfile(flatProfile,
				"resources/flat");

		FlatImportWizard importWizard = new FlatImportWizard();
		importWizard.setProfile(flatFileProfile.getName());
		importWizard.setName("Item");
		importWizard.setFile("items.csv");

		importModel(importWizard);

		checkResource("ItemSource.xmi");
		checkResource("ItemView.xmi");
		checkDiagram("ItemSource.xmi", "getTextFiles");
		checkDiagram("ItemSource.xmi", "Result");
		checkDiagram("ItemView.xmi", "ItemTable");
	}

	@Test
	public void xmlImportTest() {
		String xmlProfile = "XML Local Profile";
		teiidBot.createXmlProfile(xmlProfile, "resources/flat/accounts.xml");

		XMLImportWizard importWizard = new XMLImportWizard();
		importWizard.setLocal(true);
		importWizard.setName("Account");
		importWizard.setProfileName(xmlProfile);
		importWizard.setRootPath("/accounts/account");
		importWizard.addElement("accounts/account/nick");
		importWizard.addElement("accounts/account/balance");

		importModel(importWizard);

		checkResource("AccountSource.xmi");
		checkResource("AccountView.xmi");
		checkDiagram("AccountSource.xmi", "getTextFiles");
		checkDiagram("AccountSource.xmi", "Result");
		checkDiagram("AccountView.xmi", "AccountTable");
	}

	@Test
	public void wsdlImportTest() {
		String profile = "Hello Service";
		String wsdl = teiidBot.toAbsolutePath("resources/wsdl/Hello.wsdl");

		// Create wsdl profile
		WsdlProfileWizard profileWizard = new WsdlProfileWizard();
		profileWizard.setName(profile);
		profileWizard.setWsdl("file:" + wsdl);
		profileWizard.setEndPoint("HelloPort");
		profileWizard.execute();

		// Import wsdl as model
		WsdlImportWizard importWizard = new WsdlImportWizard();
		importWizard.setProfile(profile);
		importWizard.addRequestElement("sayHello/sequence/arg0");
		importWizard.addResponseElement("sayHelloResponse/sequence/return");

		importModel(importWizard);

		checkResource("HelloService.xmi");
		checkResource("HelloServiceView.xmi");
		checkDiagram("HelloService.xmi", "invoke");
		checkDiagram("HelloServiceView.xmi", "sayHello");
		checkDiagram("HelloServiceView.xmi", "sayHello_request");
		checkDiagram("HelloServiceView.xmi", "sayHello_response");
	}

	@Test
	public void ddlImportTest() {
		String ddl = teiidBot.toAbsolutePath("resources/ddl/hsqldb.ddl");

		DDLImportWizard importWizard = new DDLImportWizard();
		importWizard.setDdlPath(ddl);
		importWizard.setModelName("CustomerHsqldb");

		importModel(importWizard);

		checkResource("CustomerHsqldb.xmi");
		checkDiagram("CustomerHsqldb.xmi", "USER");
		checkDiagram("CustomerHsqldb.xmi", "ADDRESS");
	}

	@Test
	public void metadataModelImportTest() {
		String source = teiidBot.toAbsolutePath("resources/dtf/relationalModel.xml");
		String target = "RelationalModel.xmi";

		MetadataImportWizard importWizard = new MetadataImportWizard();
		importWizard.setImportType(ImportType.RELATIONAL_MODEL);
		importWizard.setSource(source);
		importWizard.setTarget(target);

		importModel(importWizard);

		checkResource(target);
		checkDiagram(target, "ProductSymbols");
		checkDiagram(target, "ProductData");
		checkDiagram(target, "getProductInfo");
		checkDiagram(target, "ProductIDIndex");
	}

	@Test
	public void metadataTableImportTest() {
		String source = teiidBot.toAbsolutePath("resources/dtf/relationalTable.csv");
		String target = "RelationalTable.xmi";

		MetadataImportWizard importWizard = new MetadataImportWizard();
		importWizard.setImportType(ImportType.RELATIONAL_TABLE);
		importWizard.setSource(source);
		importWizard.setTarget(target);

		importModel(importWizard);

		checkResource(target);
		checkDiagram(target, "USER");
		checkDiagram(target, "ADDRESS");
	}

	@Test
	public void metadataVirtualTableImportTest() {
		String source = teiidBot.toAbsolutePath("resources/dtf/relationalVirtualTable.csv");
		String target = "RelationalVirtualTable.xmi";

		MetadataImportWizard importWizard = new MetadataImportWizard();
		importWizard.setImportType(ImportType.RELATIONAL_VIRTUAL_TABLE);
		importWizard.setSource(source);
		importWizard.setTarget(target);

		importModel(importWizard);

		checkResource(target);
		checkDiagram(target, "VTable1");
		checkDiagram(target, "VTable2");
		checkDiagram(target, "VTable3");
		checkDiagram(target, "VTable4");
		checkDiagram(target, "VTable5");
		checkDiagram(target, "VTable6");
		checkDiagram(target, "VTable7");
		checkDiagram(target, "VTable8");
		checkDiagram(target, "VTable9");
		checkDiagram(target, "VTable10");
		checkDiagram(target, "VProc1");
		checkDiagram(target, "NewProcedureResult");
	}

	// TEIIDDES-1588
	// @Test
	public void metadataDatatypeImportTest() {
		String source = teiidBot.toAbsolutePath("resources/dtf/relationalDatatype.csv");
		String target = "RelationalDatatype.xmi";

		MetadataImportWizard importWizard = new MetadataImportWizard();
		importWizard.setImportType(ImportType.RELATIONAL_DATATYPE);
		importWizard.setSource(source);
		importWizard.setTarget(target);

		importModel(importWizard);

		checkResource(target);
	}

	private static void importModel(TeiidImportWizard importWizard) {
		ModelProject modelProject = teiidBot.modelExplorer().getModelProject(MODEL_PROJECT);
		modelProject.importModel(importWizard);
	}

	private static void checkResource(String... path) {
		ModelProject modelproject = teiidBot.modelExplorer().getModelProject(MODEL_PROJECT);
		assertTrue(Arrays.toString(path) + " not created!", modelproject.containsItem(path));
	}

	private static void checkDiagram(String file, String label) {
		packageExplorer.openFile(MODEL_PROJECT, file);
		ModelEditor modelEditor = teiidBot.modelEditor(file);
		assertNotNull(file + " is not opened!", modelEditor);
		assertNotNull("Diagram '" + label + "' not found!", modelEditor.getModeDiagram(label));
	}
}
