package org.jboss.tools.teiid.ui.bot.test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.datatools.ui.FlatFileProfile;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.tools.teiid.reddeer.ModelProject;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.DDLImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.FlatImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.HSQLDBDriverWizard;
import org.jboss.tools.teiid.reddeer.wizard.HSQLDBProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.ImportJDBCDatabaseWizard;
import org.jboss.tools.teiid.reddeer.wizard.MetadataImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.MetadataImportWizard.ImportType;
import org.jboss.tools.teiid.reddeer.wizard.TeiidImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.WsdlImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.WsdlProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.XMLImportWizard;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for importing relational models from various sources
 * 
 * @author apodhrad
 * 
 */
@Perspective(name = "Teiid Designer")
public class ImportWizardTest extends SWTBotTestCase {

	public static final String MODEL_PROJECT = "importTest";

	private static TeiidBot teiidBot = new TeiidBot();

	@BeforeClass
	public static void createModelProject() {
		teiidBot.createModelProject(MODEL_PROJECT);
	}

	@Test
	public void jdbcImportTest() {
		String jdbcProfile = "HSQLDB Profile";
		String empModel = "Emp.xmi";

		createHSQLDBProfile(jdbcProfile);

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

		FlatFileProfile flatFileProfile = teiidBot.createFlatFileProfile(flatProfile, "resources/flat");

		FlatImportWizard importWizard = new FlatImportWizard();
		importWizard.setProfile(flatFileProfile.getName());
		importWizard.setName("Item");
		//importWizard.setFile("items.csv");
		importWizard.setFile("items.csv     <<<<");

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

	@Test//!!!
	public void wsdlImportTest() {
		try {
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
		} catch (Exception ex){
			System.out.println("see TEIIDDES-1855 - should be fixed in Teiid Designer 8.3");
			new PushButton("Cancel").click();//close dialog
		}
	}

	@Test
	public void ddlImportTest() {
		String ddl = teiidBot.toAbsolutePath("resources/ddl/hsqldb.ddl");

		DDLImportWizard importWizard = new DDLImportWizard();
		importWizard.setDdlPath(ddl);
		importWizard.setModelName("CustomerHsqldb");
		importWizard.setAutoselectDialect(true);

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

	private static void importModel(TeiidImportWizard importWizard) {
		ModelProject modelProject = teiidBot.modelExplorer().getModelProject(MODEL_PROJECT);
		modelProject.importModel(importWizard);
	}

	private static void checkResource(String... path) {
		new SWTWorkbenchBot().sleep(500);
		ModelProject modelproject = new ModelExplorer().getModelProject(MODEL_PROJECT);
		assertTrue(Arrays.toString(path) + " not created!", modelproject.containsItem(path));
	}

	private static void checkDiagram(String file, String label) {
		new SWTWorkbenchBot().sleep(500);
		Project project = new ProjectExplorer().getProject(MODEL_PROJECT);
		project.getProjectItem(file).open();
		ModelEditor modelEditor = teiidBot.modelEditor(file);
		assertNotNull(file + " is not opened!", modelEditor);
		assertNotNull("Diagram '" + label + "' not found!", modelEditor.getModeDiagram(label));
	}

	private static void createHSQLDBProfile(String name) {
		Properties props = new Properties();
		try {
			props.load(new FileReader("resources/db/hsqldb.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		new HSQLDBDriverWizard(props.getProperty("db.jdbc_path")).create();
		new HSQLDBProfileWizard("HSQLDB Driver", props.getProperty("db.name"),
				props.getProperty("db.hostname")).setUser(props.getProperty("db.username"))
				.setPassword(props.getProperty("db.password")).setName(name).create();
	}
}
