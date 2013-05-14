package org.jboss.tools.teiid.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.wizard.CreateMetadataModel;
import org.jboss.tools.teiid.reddeer.wizard.ModelProjectWizard;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * Test for the creation of new models using Teiid Designer wizard.
 * 
 * @author psrna
 * 
 */
@Perspective(name = "Teiid Designer")
public class ModelWizardTest extends SWTBotTestCase {

	private static final String PROJECT_NAME = "ModelWizardTestProject";

	public static final String RELATIONAL_SOURCE_MODEL_NAME = "relational_source";

	public static final String RELATIONAL_VIEW_MODEL_NAME = "relational_view";

	public static final String XML_VIEW_MODEL_NAME = "xml_view";

	public static final String XSD_DATATYPE_MODEL_NAME = "xsd_datatype";

	public static final String WEBSERVICE_MODEL_NAME = "webservice_view";

	public static final String MODELEXT_MODEL_NAME = "modelext";

	public static final String FUNCTION_MODEL_NAME = "function_userdef";

	@BeforeClass
	public static void beforeClass() {
		/* Create new project */
		new ModelProjectWizard().create(PROJECT_NAME);
	}

	@AfterClass
	public static void saveAllFiles() {
		try {
			new ShellMenu("File", "Save All").select();
			Bot.get().sleep(1000);
		} catch (SWTLayerException swte) {
			// ok, everything was saved
		}
	}

	@Test
	public void relationalSourceModel() {
		CreateMetadataModel createModel = new CreateMetadataModel();
		createModel.setLocation(PROJECT_NAME);
		createModel.setName(RELATIONAL_SOURCE_MODEL_NAME);
		createModel.setClass(CreateMetadataModel.ModelClass.RELATIONAL);
		createModel.setType(CreateMetadataModel.ModelType.SOURCE);
		createModel.execute();

		assertTrue(getProject().containsItem(RELATIONAL_SOURCE_MODEL_NAME + ".xmi"));

		assertTrue(new ModelEditor(RELATIONAL_SOURCE_MODEL_NAME + ".xmi").isActive());
	}

	@Test
	public void relationalViewModel() {
		CreateMetadataModel createModel = new CreateMetadataModel();
		createModel.setLocation(PROJECT_NAME);
		createModel.setName(RELATIONAL_VIEW_MODEL_NAME);
		createModel.setClass(CreateMetadataModel.ModelClass.RELATIONAL);
		createModel.setType(CreateMetadataModel.ModelType.VIEW);
		createModel.execute();

		assertTrue(getProject().containsItem(RELATIONAL_VIEW_MODEL_NAME + ".xmi"));

		assertTrue(new ModelEditor(RELATIONAL_VIEW_MODEL_NAME + ".xmi").isActive());
	}

	@Test
	public void xmlViewModel() {
		CreateMetadataModel createModel = new CreateMetadataModel();
		createModel.setLocation(PROJECT_NAME);
		createModel.setName(XML_VIEW_MODEL_NAME);
		createModel.setClass(CreateMetadataModel.ModelClass.XML);
		createModel.setType(CreateMetadataModel.ModelType.VIEW);
		createModel.execute();

		assertTrue(getProject().containsItem(XML_VIEW_MODEL_NAME + ".xmi"));

		assertTrue(new ModelEditor(XML_VIEW_MODEL_NAME + ".xmi").isActive());
	}

	@Test
	public void xsdDatatypeModel() {
		CreateMetadataModel createModel = new CreateMetadataModel();
		createModel.setLocation(PROJECT_NAME);
		createModel.setName(XSD_DATATYPE_MODEL_NAME);
		createModel.setClass(CreateMetadataModel.ModelClass.XSD);
		createModel.setType(CreateMetadataModel.ModelType.DATATYPE);
		createModel.execute();

		assertTrue(getProject().containsItem(XSD_DATATYPE_MODEL_NAME + ".xsd"));

		assertTrue(new ModelEditor(XSD_DATATYPE_MODEL_NAME + ".xsd").isActive());
	}

	@Test
	public void webserviceModel() {
		CreateMetadataModel createModel = new CreateMetadataModel();
		createModel.setLocation(PROJECT_NAME);
		createModel.setName(WEBSERVICE_MODEL_NAME);
		createModel.setClass(CreateMetadataModel.ModelClass.WEBSERVICE);
		createModel.setType(CreateMetadataModel.ModelType.VIEW);
		createModel.execute();

		assertTrue(getProject().containsItem(WEBSERVICE_MODEL_NAME + ".xmi"));

		assertTrue(new ModelEditor(WEBSERVICE_MODEL_NAME + ".xmi").isActive());
	}

	@Test
	public void modelExtensionModel() {
		CreateMetadataModel createModel = new CreateMetadataModel();
		createModel.setLocation(PROJECT_NAME);
		createModel.setName(MODELEXT_MODEL_NAME);
		createModel.setClass(CreateMetadataModel.ModelClass.MODEL_EXTENSION);
		createModel.setType(CreateMetadataModel.ModelType.EXTENSION);
		createModel.execute();

		assertTrue(getProject().containsItem(MODELEXT_MODEL_NAME + ".xmi"));

		assertTrue(new ModelEditor(MODELEXT_MODEL_NAME + ".xmi").isActive());
	}

	@Test
	public void functionModel() {
		CreateMetadataModel createModel = new CreateMetadataModel();
		createModel.setLocation(PROJECT_NAME);
		createModel.setName(FUNCTION_MODEL_NAME);
		createModel.setClass(CreateMetadataModel.ModelClass.FUNCTION);
		createModel.setType(CreateMetadataModel.ModelType.FUNCTION);
		createModel.execute();

		assertTrue(getProject().containsItem(FUNCTION_MODEL_NAME + ".xmi"));

		assertTrue(new ModelEditor(FUNCTION_MODEL_NAME + ".xmi").isActive());
	}

	private static Project getProject() {
		Project project = new ProjectExplorer().getProject(PROJECT_NAME);
		return project;
	}
}
