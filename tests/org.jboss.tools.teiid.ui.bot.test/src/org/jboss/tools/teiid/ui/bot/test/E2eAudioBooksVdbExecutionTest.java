package org.jboss.tools.teiid.ui.bot.test;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.allOf;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.VDB;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.editor.CriteriaBuilder;
import org.jboss.tools.teiid.reddeer.editor.CriteriaBuilder.OperatorType;
import org.jboss.tools.teiid.reddeer.editor.CriteriaBuilder.RadioButtonType;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.editor.Reconciler;
import org.jboss.tools.teiid.reddeer.editor.Reconciler.ExpressionBuilder;
import org.jboss.tools.teiid.reddeer.editor.SQLScrapbookEditor;
import org.jboss.tools.teiid.reddeer.editor.VDBEditor;
import org.jboss.tools.teiid.reddeer.perspective.DatabaseDevelopmentPerspective;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.shell.FunctionExpressionBuilder;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorerView;
import org.jboss.tools.teiid.reddeer.view.Procedure;
import org.jboss.tools.teiid.reddeer.view.SQLResult;
import org.jboss.tools.teiid.reddeer.wizard.CreateMetadataModel;
import org.jboss.tools.teiid.reddeer.wizard.CreateMetadataModel.ModelBuilder;
import org.jboss.tools.teiid.reddeer.wizard.CreateMetadataModel.ModelClass;
import org.jboss.tools.teiid.reddeer.wizard.CreateMetadataModel.ModelType;
import org.jboss.tools.teiid.reddeer.wizard.CreateVDB;
import org.jboss.tools.teiid.reddeer.wizard.ImportJDBCDatabaseWizard;
import org.jboss.tools.teiid.reddeer.wizard.ModelProjectWizard;
import org.jboss.tools.teiid.reddeer.wizard.TeiidProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.XMLSchemaImportWizard;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.junit.Test;

/**
 * Test E2eAudioBooksVdbExecutionTestScript_Teiid7.odt
 * Prerequisites: BooksWithSOAPEncoding.xsd, BookDatatypes.xsd, soap_encoding.xsd
 * 
 * @author lfabriko
 *
 */

@Perspective(name = "Teiid Designer")//initialize tests in this perspective
//@Server(type = Type.ALL, state = State.RUNNING)//uses info about server - swtbot.properties
public class E2eAudioBooksVdbExecutionTest extends SWTBotTestCase{

	private static final String PROJECT_NAME = "AudioBooks";
	private static final String SOURCE_MODEL_1 = "Books1";
	private static final String SOURCE_MODEL_2 = "Books2";
	private static final String VIRTUAL_B_BA = "vBooks_B_BA";
	private static final String VIRTUAL_A_P = "vBooks_A_P";
	private static final String BOOKSINFO_MODEL = "BooksInfo";
	private static final String BOOKINFO_VIRTUAL_TABLE_ = "bookInfo";
	private static final String SOAPYBOOKS_MODEL = "SoapyBooks";
	private static final String BOOKSETMIXED_DOCUMENT = "bookSetMixedDocument";
	private static final String VDB_NAME = "BooksWithAudio";
	private static final String TESTSQL_1 = "SELECT * FROM Books_Oracle.BOOKS";
	private static final String TESTSQL_2 = "SELECT * FROM BooksInfo.bookInfo";
	private static final String TESTSQL_3 = "SELECT * FROM BooksInfo.bookInfo WHERE pubName = 'Que'";
	private static final String TESTSQL_4 = "SELECT * FROM  SoapyBooks.bookSetMixedDocument";
	private static final String TESTSQL_5 = "SELECT * FROM  SoapyBooks.bookSetMixedDocument WHERE bookSetMixed.hardcoverBook.title like '%mm%'";
	private static final String GET_BOOKS_EDITION = "getBooksByEdition";
	private static final String GET_XML_BOOKS_EDITION = "getXmlBooksByEdition";
	private static TeiidBot teiidBot = new TeiidBot();
	private String props1 = "resources/db/audioBooks_hsqldb.properties";
	private String jdbcProfile = "HSQLDB Profile";
	private String xsdSchemas = "resources/xsd";
	protected final Logger log = Logger.getLogger(this.getClass());
	private static final String VIRTUAL_PROCEDURE_SQL = "CREATE VIRTUAL PROCEDURE\n" + "BEGIN\n\t"
			+ "SELECT * FROM BooksInfo.bookInfo WHERE BooksInfo.bookInfo.edition = editionIn;" + "\nEND";
	private static final String VIRTUAL_PROCEDURE_SQL2 = "CREATE VIRTUAL PROCEDURE\n" +"BEGIN\n\t"+
			"SELECT * FROM SoapyBooks.bookSetMixedDocument\n\t"
			+ "WHERE bookSetMixedDocument.bookSetMixed.hardcoverBook.title IN "
			+ "(SELECT title FROM BooksInfo.bookInfo WHERE BooksInfo.bookInfo.edition = editionIn);\n"
			+ "END";
	private static final String TESTSQL_6 = "EXEC BooksInfo.getXmlBooksByEdition (3)";
	
	private String authorAttribute = "author_authors";//by default; reset in one method
	private static final String TEIID_CONNECTION_PROFILE = "MyTeiidProfile";
	
	/**
	 * Create new Teiid Model Project
	 */
	@Test
	public void test01(){
		int currentPage = 0;//currentPage of wizard must be set to 0
		new ModelProjectWizard(currentPage).create(PROJECT_NAME, true, "sources", "views", "schemas");
	}
	
	/**
	 * Create HSQL connection profile
	 */
	@Test
	public void test02(){
		teiidBot.createHsqlProfile(props1, jdbcProfile, true, true);
	}
	
	/**
	 * Create relational source models with different tables
	 */
	@Test
	public void test03(){
		//1. relational source model
		String[] tables1 = {"PUBLIC/PUBLIC/TABLE/AUTHORS", "PUBLIC/PUBLIC/TABLE/BOOKS", "PUBLIC/PUBLIC/TABLE/PUBLISHERS"};
		List<String> tablesToImport1 = Arrays.asList(tables1);
		importTables(jdbcProfile, SOURCE_MODEL_1, tablesToImport1);
		
		//2. relational source model
		String[] tables2 = {"PUBLIC/PUBLIC/TABLE/AUTHORS", "PUBLIC/PUBLIC/TABLE/BOOKS", "PUBLIC/PUBLIC/TABLE/BOOK_AUTHORS"};
		List<String> tablesToImport2 = Arrays.asList(tables2);
		importTables(jdbcProfile, SOURCE_MODEL_2, tablesToImport2);//import the same tables
		
		//delete the offending FK in BOOKS - FK_PUBLISHER
		//set focus to tree
		String[] path2 = {PROJECT_NAME, "Books2.xmi", "BOOKS", "FK_PUBLISHER"};
		//System.out.println(new SWTWorkbenchBot().getFocusedWidget().getClass());//class org.eclipse.draw2d.FigureCanvas
		Matcher matcher2 = allOf(widgetOfType(Tree.class));
		new SWTBotTree((Tree) new SWTBot().widget(matcher2, 0), matcher2).setFocus();
		new DefaultTreeItem(0, PROJECT_NAME, SOURCE_MODEL_2+".xmi", "BOOKS", "FK_PUBLISHER").select();
		new ContextMenu("Delete").select();
		
		//save all
		teiidBot.saveAll();
	}
	
	/**
	 * Create a relational view model (vBooks_B_BA) using the Transform from an existing Model optional builder
	 */
	@Test
	public void test04(){
		CreateMetadataModel newModel = new CreateMetadataModel();
		newModel.setLocation(PROJECT_NAME);
		newModel.setName(VIRTUAL_B_BA );
		newModel.setClass(CreateMetadataModel.ModelClass.RELATIONAL);
		newModel.setType(CreateMetadataModel.ModelType.VIEW);
		newModel.execute(ModelBuilder.TRANSFORM_EXISTING, PROJECT_NAME, SOURCE_MODEL_2+".xmi");
		teiidBot.saveAll();
		
		//delete AUTHORS table
		deleteFromContextTree(PROJECT_NAME, VIRTUAL_B_BA+".xmi", "AUTHORS");
		
		teiidBot.saveAll();
		//delete offending FK_ISBN from BOOK_AUTHORS
		deleteFromContextTree(PROJECT_NAME, VIRTUAL_B_BA+".xmi", "BOOK_AUTHORS", "FK_ISBN");
		teiidBot.saveAll();
		//delete FK_AUTHOR
		deleteFromContextTree(PROJECT_NAME, VIRTUAL_B_BA+".xmi", "BOOK_AUTHORS", "FK_AUTHOR");
		teiidBot.saveAll();
	}
	
	/**
	 * Create a relational view model (vBooks_A_P) using the Transform from an existing Model optional builder
	 */
	@Test
	public void test05(){
		CreateMetadataModel newModel = new CreateMetadataModel();
		newModel.setLocation(PROJECT_NAME);
		newModel.setName(VIRTUAL_A_P );
		newModel.setClass(CreateMetadataModel.ModelClass.RELATIONAL);
		newModel.setType(CreateMetadataModel.ModelType.VIEW);
		newModel.execute(ModelBuilder.TRANSFORM_EXISTING, PROJECT_NAME, SOURCE_MODEL_1+".xmi");

		//delete BOOKS table
		deleteFromContextTree(PROJECT_NAME, VIRTUAL_A_P+".xmi", "BOOKS");
				
		teiidBot.saveAll();
		//teiidBot.closeActiveShell();
	}
	
	/**
	 * Create a relational view model (BooksInfo)
	 */
	@Test
	public void test06(){
		CreateMetadataModel newModel = new CreateMetadataModel();
		newModel.setLocation(PROJECT_NAME);
		newModel.setName(BOOKSINFO_MODEL);
		newModel.setClass(CreateMetadataModel.ModelClass.RELATIONAL);
		newModel.setType(CreateMetadataModel.ModelType.VIEW);
		newModel.execute();

		teiidBot.saveAll();//waiting
		teiidBot.closeActiveShell();
		
		// create table bookInfo
		ModelExplorerView modelView = TeiidPerspective.getInstance()
				.getModelExplorerView();
		modelView.newBaseTable(PROJECT_NAME, BOOKSINFO_MODEL+".xmi",
				BOOKINFO_VIRTUAL_TABLE_, false);
		
		//open transformation diagram of bookInfo
		modelView.openTransformationDiagram(PROJECT_NAME, BOOKSINFO_MODEL+".xmi",
				BOOKINFO_VIRTUAL_TABLE_);
		
		//transformation - add vBooks_B_BA.BOOKS and vBooks_A_P.PUBLISHERS
		modelView.addTransformationSource(PROJECT_NAME, VIRTUAL_B_BA+".xmi",
				"BOOKS");
		modelView.addTransformationSource(PROJECT_NAME, VIRTUAL_A_P+".xmi",
				"PUBLISHERS");
		
		//criteria builder
		ModelEditor editor = new ModelEditor(BOOKSINFO_MODEL+".xmi");
		editor.show();
		editor.showTransformation();
		//vBooks_B_BA.BOOKS.PUBLISHER = vBooks_A_P.PUBLISHERS.PUBLISHER_ID
		CriteriaBuilder criteriaBuilder = editor.criteriaBuilder();
		criteriaBuilder.selectRightAttribute(
				VIRTUAL_B_BA+".BOOKS", "PUBLISHER");
		criteriaBuilder.selectLeftAttribute(
				VIRTUAL_A_P+".PUBLISHERS", "PUBLISHER_ID");
		criteriaBuilder.apply();
		criteriaBuilder.finish();
		editor.save();
		
		//Start the Reconciler
		bot.toolbarButtonWithTooltip("Reconcile Transformation SQL with Target Columns").click();
		SWTBotShell shell = bot.shell("Reconcile Virtual Target Columns");
		shell.activate();

		//Delete the PUBLISHER virtual target
		new DefaultTable().select("PUBLISHER : integer");
		new PushButton("Delete").click();
		
		//Rename NAME to pubName
		SWTBotTable t =  new SWTWorkbenchBot().table();
		t.click(7, 0);
		new DefaultText("NAME").setText("pubName");
		
		//Move EDITION above PUBLISH_YEAR
		new DefaultTable().select("EDITION : integer");
		new PushButton("Up").click();
		
		//Remove the unmatched SQL symbol
		new PushButton("Remove").click();
		
		//close the reconciler
		new PushButton("OK").click();
		teiidBot.saveAll();
	}
	
	/**
	 * Create additional folders, import xsd
	 */
	@Test
	public void test07(){
		//Under sources create a Relational folder
		createFolder("Relational", PROJECT_NAME, "sources");
		//Under views create these folders: Relational, XmlDocuments
		createFolder("Relational", PROJECT_NAME, "views");
		createFolder("XmlDocuments", PROJECT_NAME, "views");
		//import xsd
		XMLSchemaImportWizard wizard = new XMLSchemaImportWizard();
		wizard.setLocal(true);
		wizard.setRootPath(new File(xsdSchemas).getAbsolutePath());
		String[] schemas = {"BookDatatypes.xsd", "soap_encoding.xsd", "BooksWithSOAPEncoding.xsd"}; 
		wizard.setSchemas(schemas);
		wizard.setDestination("AudioBooks/schemas");
		wizard.execute();
	}
	
	/**
	 * Create SoapyBooks relational view model
	 */
	@Test
	public void test08(){
		CreateMetadataModel soapyBooks = new CreateMetadataModel();
		soapyBooks.setLocation(PROJECT_NAME+"/schemas");
		soapyBooks.setName(SOAPYBOOKS_MODEL);
		soapyBooks.setClass(ModelClass.XML);
		soapyBooks.setType(ModelType.VIEW);
		soapyBooks.setModelBuilder(ModelBuilder.BUILD_FROM_XML_SCHEMA);
		String[] pathToXmlSchema = {"AudioBooks", "schemas", "BooksWithSOAPEncoding.xsd"};
		String rootElement = "bookSetMixed : BooksNS:BookSetMixed";
		soapyBooks.execute(pathToXmlSchema, rootElement);
	}
	
	/**
	 * Edit attributes of SoapyBooks
	 */
	@Test
	public void test09(){
		//open bookSetMixed diagram
		ModelExplorerView mew = new ModelExplorerView();
		String[] filePath = {PROJECT_NAME, "schemas", SOAPYBOOKS_MODEL+".xmi", "bookSetMixedDocument"};
		mew.open(filePath);
		ModelEditor me = new ModelEditor(SOAPYBOOKS_MODEL+".xmi");
		
		//merge mapping classes starting with "author"
		List<SWTBotGefEditPart> mappingClassesAuthor = me.getMappingClasses("author");
		me.selectParts(mappingClassesAuthor);
		if (bot.toolbarButtonWithTooltip("Merge Mapping Classes").isEnabled()){
			bot.toolbarButtonWithTooltip("Merge Mapping Classes").click();
			log.info("Merge mapping classes with prefix \"author\"");
		}//otherwise they are already merged
		
		//delete attributes from author
		List<String> authorAttrs = me.namesOfAttributes("author");
		String firstAuthorRaw = authorAttrs.get(0);//retain this attribute
		log.info("Retaining author attribute: " + firstAuthorRaw);
		this.authorAttribute = firstAuthorRaw.substring(0, firstAuthorRaw.indexOf(" "));// author* : string --> author*
		for (int i = 1; i < authorAttrs.size(); i++){//delete other attributes
			me.deleteLabeledItem(authorAttrs.get(i));
		}
		
		//delete attributes from bookSetMixed
		String[] attrsToDelete = {
				"isbn_softcoverBook : ISBN",
				"title_softcoverBook : string",
				"subtitle_softcoverBook : string",
				"edition_softcoverBook : BookEdition",
				"publisher_publishingInformation : string",
				"publishDate_publishingInformation : PublicationDate",
				"isbn_audioBook : ISBN",
				"title_audioBook : string",
				"subtitle_audioBook : string",
				"edition_audioBook : BookEdition",
				"publisher_publishingInformation1 : string",
				"publishDate_publishingInformation1 : PublicationDate"};
		for (String label : attrsToDelete){
			me.deleteLabeledItem(label);
		}
		teiidBot.saveAll();
	}
	
	
	/**
	 * Edit bookSetMixed table - reconciler, review resulting SQL, transformation editor
	 */
	@Test
	public void test10(){
		ModelExplorerView mew = TeiidPerspective.getInstance()
				.getModelExplorerView();
		String[] filePath = {PROJECT_NAME, "schemas", SOAPYBOOKS_MODEL+".xmi", BOOKSETMIXED_DOCUMENT};
		mew.open(filePath);
		ModelEditor me = new ModelEditor(SOAPYBOOKS_MODEL+".xmi");
		
		//Add the BooksInfo.bookinfo group to the transformation
		me.showMappingTransformation("bookSetMixed");
		mew.addTransformationSource(PROJECT_NAME, BOOKSINFO_MODEL+".xmi", BOOKINFO_VIRTUAL_TABLE_);

		teiidBot.saveAll();
		
		//start the reconciler
		Reconciler rec = me.openReconciler();
		//bind pubName to publisher
		rec.bindAttributes("publisher : string", "pubName");
		
		//PUBLISH_YEAR to publish_date
		rec.bindAttributes("publishDate : string", "PUBLISH_YEAR");
		
		//Add Type as a New virtual target attribute
		rec.addNewVirtualTargetAttribute("TYPE");
		
		//create new constant from reader
		rec.expressionBuilderConstant("reader : string", "string", "Elmer Fudd");
		
		//Clear the remaining unmatched SQL symbols
		rec.clearRemainingUnmatchedSymbols();
		
		//type resolver
		rec.resolveTypes(ExpressionBuilder.KEEP_VIRTUAL_TARGET);
		
		rec.close();
		teiidBot.saveAll();
	}
	
	/**
	 * Author transformation, Edit criteria
	 */
	@Test
	public void test11(){
		ModelExplorerView mew = TeiidPerspective.getInstance()
				.getModelExplorerView();
		String[] filePath = {PROJECT_NAME, "schemas", SOAPYBOOKS_MODEL+".xmi", BOOKSETMIXED_DOCUMENT};
		mew.open(filePath);
		ModelEditor me = new ModelEditor(SOAPYBOOKS_MODEL+".xmi");
		me.showMappingTransformation("author");
		me.openInputSetEditor();
		
		new DefaultTreeItem(1, "bookSetMixed", "isbn : ISBN").select();
		new PushButton("< New").click();

		bot.toolbarButton(1).click();//close the inputset editor
		
		// vBooks_B_BA.BOOK_AUTHORS and vBooks_A_P.AUTHORS tables to the transformation
		me.showMappingTransformation("author");
		mew.addTransformationSource(PROJECT_NAME, VIRTUAL_B_BA+".xmi", "BOOK_AUTHORS");
		mew.addTransformationSource(PROJECT_NAME, VIRTUAL_A_P+".xmi", "AUTHORS");
		
		teiidBot.saveAll();
		System.out.println();
		
		//Criteria Builder to join (vBooks_B_BA.BOOK_AUTHORS.ISBN = INPUTS.isbn) 
		//AND (vBooks_B_BA.BOOK_AUTHORS.AUTHOR_ID = vBooks_A_P.AUTHORS.AUTHOR_ID)
		CriteriaBuilder criteriaBuilder = me.criteriaBuilder();
		criteriaBuilder.selectRightAttribute(
				VIRTUAL_B_BA+".BOOK_AUTHORS", "ISBN");
		criteriaBuilder.selectLeftAttribute("INPUTS", "isbn");
		criteriaBuilder.apply();
		
		new PushButton("AND").click();
		
		criteriaBuilder.selectRightAttribute(
				VIRTUAL_B_BA+".BOOK_AUTHORS", "AUTHOR_ID");
		criteriaBuilder.selectLeftAttribute(VIRTUAL_A_P + ".AUTHORS", "AUTHOR_ID");
		criteriaBuilder.apply();
		
		criteriaBuilder.finish();
		me.save();
		
		//change select statement in sql
		String originalSql = me.getTransformation().toString();
		String newSelect = originalSql.replaceFirst("SELECT\n\t\tvBooks_B_BA.BOOK_AUTHORS.ISBN, vBooks_B_BA.BOOK_AUTHORS.AUTHOR_ID, vBooks_A_P.AUTHORS.AUTHOR_ID AS AUTHOR_ID_1, vBooks_A_P.AUTHORS.FIRSTNAME, vBooks_A_P.AUTHORS.LASTNAME, vBooks_A_P.AUTHORS.MIDDLEINIT\n\tFROM",
				"SELECT\n\t\tvBooks_A_P.AUTHORS.LASTNAME as "+this.authorAttribute+" \n\tFROM");
		me.setTransformation(newSelect);
		me.save();

	}
	
	/**
	 * Refactor models --- NO, will end in mess (at least 1 method uses as path project-model-table, no folders...
	 * Edit choice -- nejde, criteria builder
	 */
	@Test
	public void test12(){
		ModelExplorerView mew = TeiidPerspective.getInstance()
				.getModelExplorerView();
		String[] filePath = {PROJECT_NAME, "schemas", SOAPYBOOKS_MODEL+".xmi", BOOKSETMIXED_DOCUMENT};
		mew.open(filePath);
		bot.sleep(5000);
		//WAITING
		//click choice -> edit
		ModelEditor me = new ModelEditor(SOAPYBOOKS_MODEL+".xmi");
		//me.show();
		me.setFocus();
		
		//Matcher m = allOf(widgetOfType(Tree.class));
		//List<Tree> trees = new SWTBot().widgets(m);
		
		Matcher m2 = new Matcher(){

			@Override
			public void describeTo(Description arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void describeMismatch(Object arg0, Description arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean matches(Object arg0) {
				if (arg0 instanceof Tree){
					Tree t = (Tree)arg0;
					for (TreeItem ti : t.getItems()){
						TreeItem[] tis2 = ti.getItems();
						for (TreeItem ti3 : tis2){
							if ((ti3.getData() != null)&&(ti3.getData().toString().contains("Xml"))){//org.teiid.designer.metamodels.xml.impl.XmlChoiceImp
								new SWTBotTreeItem(ti3).click();
							}
						}
					}
					return true;
				}
				return false;
			}
			
		};
		
		List<Tree> list = new SWTBot().widgets(m2);//just to click somewhere inside the editor (but setFocus isn't enough)
		
		new DefaultTreeItem("bookSetMixed").select();
		new DefaultTreeItem("bookSetMixed", "choice [MC: bookSetMixed]").select();
		new ContextMenu("Edit").select();
		
		//1st criteria
		new DefaultTable(0).select(0);
		new SWTWorkbenchBot().toolbarButtonWithTooltip("Edit").click();
		CriteriaBuilder cb = new CriteriaBuilder(new SWTWorkbenchBot().activeShell());
		
		//COLUMN type
		cb.selectRadioButton(RadioButtonType.COLUMN, RadioButtonType.LEFT);
		cb.selectLeftAttribute("bookSetMixed", "TYPE : string", true);
		
		//=
		cb.selectOperator(OperatorType.EQUALS);
		
		//CONSTANT Hardback
		cb.selectRadioButton(RadioButtonType.CONSTANT, RadioButtonType.RIGHT);
		cb.setConstant("Hardback");
		
		cb.apply();
		cb.close();

		//2nd criteria
		new DefaultTable(0).select(1);
		new SWTWorkbenchBot().toolbarButtonWithTooltip("Edit").click();
		cb = new CriteriaBuilder(new SWTWorkbenchBot().activeShell());
		
		//function
		cb.selectRadioButton(RadioButtonType.FUNCTION, RadioButtonType.LEFT);
		new PushButton("Edit...").click();
		FunctionExpressionBuilder feb = new FunctionExpressionBuilder(new SWTWorkbenchBot().activeShell());
		feb.setCategory("STRING");
		feb.setFunction("LCASE(STRING)");
		feb.apply();
		feb.setRadioButtonType(RadioButtonType.COLUMN);
		feb.setColumn("bookSetMixed", "TYPE : string");
		feb.apply();
		feb.close();
		
		//operator LIKE
		cb.selectOperator(OperatorType.LIKE);
		
		//constant sof%
		cb = new CriteriaBuilder(new SWTWorkbenchBot().activeShell());
		cb.selectRadioButton(RadioButtonType.CONSTANT, RadioButtonType.RIGHT);
		//cb.setConstant("sof%");
		new DefaultText(1).setText("sof%");
		cb.apply();
		cb.close();
		
		//3rd criteria
		new DefaultTable(0).select(2);
		new SWTWorkbenchBot().toolbarButtonWithTooltip("Edit").click();
		cb = new CriteriaBuilder(new SWTWorkbenchBot().activeShell());
		
		//column TYPE
		cb.selectRadioButton(RadioButtonType.COLUMN, RadioButtonType.LEFT);
		cb.selectLeftAttribute("bookSetMixed", "TYPE : string", true);
		
		//IN
		cb.selectOperator(OperatorType.IN);
		cb.addConstantsToList("Books on Tape", "Audio", "Books on CD");
		cb = new CriteriaBuilder(new SWTWorkbenchBot().activeShell());
		cb.apply();
		cb.close();
		
		teiidBot.saveAll();	
		
	}
	
	/**
	 * Create VDB BooksWithAudio
	 */
	@Test
	public void test13(){
		CreateVDB createVDB = new CreateVDB();
		createVDB.setFolder(PROJECT_NAME);
		createVDB.setName(VDB_NAME);
		createVDB.execute();

		VDBEditor editor = VDBEditor.getInstance(VDB_NAME+".vdb");
		editor.show();
		editor.addModel(true, PROJECT_NAME, "schemas", "SoapyBooks.xmi");
		editor.save();
	}
	
	/**
	 * VDB - deploy, execute
	 */
	@Test
	public void test14(){
		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.open();
		VDB vdb = modelExplorer.getModelProject(PROJECT_NAME).getVDB(VDB_NAME+".xmi");
		vdb.deployVDB();
		vdb.executeVDB(true);
		
		closeActiveShell();
		
		SQLScrapbookEditor editor = new SQLScrapbookEditor("SQL Scrapbook0");
		editor.show();
		editor.setDatabase(VDB_NAME);

		// TESTSQL_1
		editor.setText(TESTSQL_1);
		editor.executeAll();

		SQLResult result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView()
				.getByOperation(TESTSQL_1);
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

		// TESTSQL_4
		editor.show();
		editor.setText(TESTSQL_4);
		editor.executeAll();

		result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation(TESTSQL_4);
		assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());
		
		// TESTSQL_5
		editor.show();
		editor.setText(TESTSQL_5);
		editor.executeAll();

		result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation(TESTSQL_5);
		assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());

	}
	
	
	/**
	 * Create procedure getBooksByEdition, preview data
	 */
	@Test
	public void test15(){
		ModelExplorerView modelView = TeiidPerspective.getInstance().getModelExplorerView();
		Procedure procedure = new Procedure(PROJECT_NAME, BOOKSINFO_MODEL+".xmi");
		procedure.create(GET_BOOKS_EDITION, true);
		procedure.addParameter(true, "editionIn");
		procedure.addParameterType("editionIn", "int : xs:long", true);
		teiidBot.saveAll();
		
		//modelView.open();*/
		modelView.openTransformationDiagram(PROJECT_NAME, BOOKSINFO_MODEL+".xmi", GET_BOOKS_EDITION);
		ModelEditor editor = new ModelEditor(BOOKSINFO_MODEL+".xmi");
		editor.show();
		
		modelView.addTransformationSource(PROJECT_NAME, BOOKSINFO_MODEL+".xmi", BOOKINFO_VIRTUAL_TABLE_);
		//click at T
		editor.showTransformation();
		editor.setFocus();
		editor.setTransformationProcedureBody(VIRTUAL_PROCEDURE_SQL, true);//WHERE BooksInfo.bookInfo.EDITION = editionIn
		editor.save();
		
		//preview data
		
	}
	
	/**
	 * Create procedure getXmlBooksByEdition
	 */
	@Test
	public void test16(){
		ModelExplorerView modelView = TeiidPerspective.getInstance().getModelExplorerView();
		Procedure procedure = new Procedure(PROJECT_NAME, BOOKSINFO_MODEL+".xmi");
		procedure.create(GET_XML_BOOKS_EDITION, true);
		procedure.addParameter(true, "editionIn");
		procedure.addParameterType("editionIn", "int : xs:long", true);
		teiidBot.saveAll();
		
		modelView.open();
		modelView.openTransformationDiagram(PROJECT_NAME, BOOKSINFO_MODEL+".xmi", GET_XML_BOOKS_EDITION);
		ModelEditor editor = new ModelEditor(BOOKSINFO_MODEL+".xmi");
		editor.show();
		//click at T
		editor.showTransformation();
		modelView.addTransformationSource(PROJECT_NAME, BOOKSINFO_MODEL+".xmi", BOOKINFO_VIRTUAL_TABLE_);
		editor.setTransformationProcedureBody(VIRTUAL_PROCEDURE_SQL2, true);//WHERE BooksInfo.bookInfo.EDITION = editionIn
		editor.save();
	}
	
	/**
	 * SoapyBooks: Change the SOAP Encoding property from NONE to DEFAULT
	 */
	@Test
	public void test17(){
		//open SoapyBooks
		ModelExplorerView mv = TeiidPerspective.getInstance().getModelExplorerView();
		mv.open(PROJECT_NAME, "schemas", SOAPYBOOKS_MODEL+".xmi", BOOKSETMIXED_DOCUMENT);
		
		//focus on model explorer!!!
		mv.open();
		
		new DefaultTreeItem(PROJECT_NAME, "schemas", SOAPYBOOKS_MODEL+".xmi", BOOKSETMIXED_DOCUMENT).select();
		
		//change soap encoding property		
		SWTBotTree tree = new SWTWorkbenchBot().tree(1);

		SWTBotTreeItem treeItem = tree.getAllItems()[1];
		SWTBotTreeItem node = treeItem.getNode(3).click(1);
		node.doubleClick();

		Matcher comboMatcher = allOf(widgetOfType(CCombo.class));
		List<CCombo> ccombo =  new SWTBot().widgets(comboMatcher);
		new SWTBotCCombo(ccombo.get(0)).setSelection("DEFAULT");
	}
	
	/**
	 * Execute VDB
	 */
	@Test
	public void test18(){
		ModelExplorerView mv = TeiidPerspective.getInstance().getModelExplorerView();
		mv.open(PROJECT_NAME, VDB_NAME+".vdb");
		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.open();
		
		//synchronize vdb
		VDBEditor vdbeditor = VDBEditor.getInstance(VDB_NAME+".vdb");
		vdbeditor.show();
		vdbeditor.synchronizeAll();
		vdbeditor.save();
		
		VDB vdb = modelExplorer.getModelProject(PROJECT_NAME).getVDB(VDB_NAME+".vdb");
		vdb.deployVDB();
		vdb.executeVDB(true);
		
		closeActiveShell();//?
		
		SQLScrapbookEditor editor = new SQLScrapbookEditor("SQL Scrapbook0");
		editor.show();
		editor.setDatabase(VDB_NAME);//profil?

		// TESTSQL_6
		editor.setText(TESTSQL_6);
		editor.executeAll();

		SQLResult result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView()
				.getByOperation(TESTSQL_6);
		assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());
	}
	
	/**
	 * Import tables from HSQL database
	 * @param connProfile name of connection profile (e.g. HSQLDB Profile)
	 * @param modelName name of model (e.g. partssupplier.xmi)
	 */
	public void importTables(String connProfile, String modelName, List<String> tables) {
		ImportJDBCDatabaseWizard wizard = new ImportJDBCDatabaseWizard();
		wizard.setConnectionProfile(connProfile);
		wizard.setProjectName(PROJECT_NAME);
		wizard.setModelName(modelName);
		// add all tables
		for (String table : tables){
			wizard.addItem(table);
		}	
		wizard.execute(true);
	}
	
	
	
	private void deleteFromContextTree(String... path){
		Matcher matcher2 = allOf(widgetOfType(Tree.class));
		new SWTBotTree((Tree) new SWTBot().widget(matcher2, 0), matcher2).setFocus();
		new DefaultTreeItem(0, path).select();
		new ContextMenu("Delete").select();
	}
	
	private void createFolder(String folderName, String... path){
		Matcher matcher2 = allOf(widgetOfType(Tree.class));
		new SWTBotTree((Tree) new SWTBot().widget(matcher2, 0), matcher2).setFocus();
		new DefaultTreeItem(0, path).select();
		new ContextMenu("New", "Folder").select();
		new DefaultText(1).setText(folderName);
		new PushButton("Finish").click();
	}
	
	/**
	 * Confirm active shell, if appears
	 */
	private void closeActiveShell(){
		try {
			new SWTWorkbenchBot().activeShell().bot().button("Yes").click();
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
	
}
