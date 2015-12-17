/******************************************************************************* 
 * Copyright (c) 2012 - 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.test.TestProperties;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.ui.bot.ext.helper.BuildPathHelper;
import org.jboss.reddeer.gef.view.PaletteView;
import org.jboss.tools.ui.bot.test.JBTSWTBotTestCase;
import org.jboss.tools.ui.bot.test.JSPMultiPageEditorExt;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.reddeer.utils.WebEngineSwitchingManager;
import org.junit.After;
import org.junit.Before;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.jboss.tools.jst.reddeer.ui.perspectives.WebDevelopmentPerspective;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectFirstPage;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectWizard;
import org.jboss.reddeer.core.lookup.WidgetLookup;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.tools.jst.reddeer.jsp.ui.wizard.NewJSPFileWizardDialog;
import org.jboss.tools.jst.reddeer.jsp.ui.wizard.NewJSPFileWizardJSPPage;
import org.jboss.tools.jst.reddeer.jsp.ui.wizard.NewJSPFileWizardJSPTemplatePage;
import org.jboss.tools.jst.reddeer.web.ui.NewXHTMLWizard;
import org.jboss.tools.jst.reddeer.web.ui.NewXHTMLFileWizardPage;
import org.jboss.tools.jst.reddeer.web.ui.NewXHTMLFileWizardXHTMLTemplatePage;
import org.jboss.tools.jst.reddeer.wst.html.ui.wizard.NewHTMLFileWizardDialog;
import org.jboss.tools.jst.reddeer.wst.html.ui.wizard.NewHTMLFileWizardHTMLPage;

import org.jboss.tools.jsf.reddeer.ui.JSFNewProjectWizard;
import org.jboss.tools.jsf.reddeer.ui.JSFNewProjectFirstPage;
import org.jboss.tools.jsf.reddeer.ui.JSFNewProjectSecondPage;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;

@CleanWorkspace
@OpenPerspective(WebDevelopmentPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
public abstract class VPEAutoTestCase extends JBTSWTBotTestCase {
  protected static Properties projectProperties;
  protected static String PROJECT_PROPERTIES = "projectProperties.properties";
  protected static final String TEST_PAGE = "inputUserName.jsp"; //$NON-NLS-1$
  protected static final String FACELETS_TEST_PAGE = "inputname.xhtml"; //$NON-NLS-1$
  protected static String JSF2_TEST_PAGE = "inputname.xhtml"; //$NON-NLS-1$

  protected final static String JBT_TEST_PROJECT_NAME = "JBIDETestProject"; //$NON-NLS-1$
  protected final static String FACELETS_TEST_PROJECT_NAME = "FaceletsTestProject"; //$NON-NLS-1$
  protected final static String JSF2_TEST_PROJECT_NAME = "JSF2TestProject"; //$NON-NLS-1$
  protected final static String DYNAMIC_WEB_TEST_PROJECT_NAME = "DynWebTestProject"; //$NON-NLS-1$
  protected final static String RICH_FACES_UI_JAR_LOCATION;

  protected static final String DEFAULT_TEST_PAGE_TEXT = "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" +
  	  "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
		  "<f:loadBundle var=\"Message\" basename=\"demo.Messages\"/>\n" +
		  "<html>\n" +
		  "  <head>\n" +
		  "    <title>Input User Name Page</title>\n" +
		  "  </head>\n" +
		  "  <body>\n" +
		  "    <f:view>\n" +
		  "      <h1><h:outputText value=\"#{Message.header}\"/></h1>\n" +
		  "		 <h:messages style=\"color: red\"/>\n" +
		  "      <h:form id=\"greetingForm\">\n" +
		  "        <h:outputText value=\"#{Message.prompt_message}\"/>\n" +
		  "        <h:inputText value=\"#{user.name}\" required=\"true\">\n" +
	      "          <f:validateLength maximum=\"30\" minimum=\"3\"/>\n" +
	      "        </h:inputText>\n" +
	      "        <h:commandButton action=\"hello\" value=\"Say Hello!\" />\n" +
	      "      </h:form>\n" +
	      "   </f:view>\n" +
	      " </body>\n" +
	      "</html>";
  
  private String projectName = null;
  private HashMap<String,String> addedVariableRichfacesUiLocation = new HashMap<String,String>();
  @InjectRequirement
  protected ServerRequirement serverRequirement;
  
  static {
    try {
      InputStream inputStream = VPEAutoTestCase.class
          .getResourceAsStream("/" + PROJECT_PROPERTIES); //$NON-NLS-1$
      projectProperties = new TestProperties();
      projectProperties.load(inputStream);
      inputStream.close();
    } catch (IOException e) {
      IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
          "Can't load properties from " + PROJECT_PROPERTIES + " file", e); //$NON-NLS-1$ //$NON-NLS-2$
      Activator.getDefault().getLog().log(status);
      e.printStackTrace();
    } catch (IllegalStateException e) {
      IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
          "Property file " + PROJECT_PROPERTIES + " was not found", e); //$NON-NLS-1$ //$NON-NLS-2$
      Activator.getDefault().getLog().log(status);
      e.printStackTrace();
    }
    // Get richfaces-ui.jar location
    // System property has priority
    String richFacesUiLocation = System.getProperty("org.jboss.tools.vpe.ui.bot.test.richfaces.ui.jar.location","");
    // Read property from Properties File
    if (richFacesUiLocation.length() == 0){
      if (projectProperties.containsKey("RichFacesUiJarLocation")){
        richFacesUiLocation = projectProperties.getProperty("RichFacesUiJarLocation");
      }
    }
    RICH_FACES_UI_JAR_LOCATION = richFacesUiLocation;
  }	  
  protected void setProjectName(String projectName) {
    this.projectName = projectName;
  }

	/**
	 * @see #clearWorkbench()
	 * @see #createJSFProject(String)
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		WebEngineSwitchingManager.checkDoNotShowBrowserEngineDialogProperty();
		/*
		 * Test JSF project
		 */
		createJSFProject(JBT_TEST_PROJECT_NAME);
		/*
		 * Test Facelets project
		 */
		createFaceletsProject(FACELETS_TEST_PROJECT_NAME);
	}

	/**
	 * Tears down the fixture. Verify Error Log. Close all dialogs which may be
	 * not closed after test executing.
	 * 
	 * @see #clearWorkbench()
	 */

	@After
	public void tearDown() throws Exception {
		new SWTJBTExt(bot)
		  .removeProjectFromServers((projectName != null && projectName.length() > 0) ? projectName : JBT_TEST_PROJECT_NAME);
//		setProjectName(null);
		super.tearDown();
	}

	/**
	 * Create JSF Project with <b>jsfProjectName</b>
	 * 
	 * @param jsfProjectName
	 *            - name of created project
	 */
	protected void createJSFProject(String jsfProjectName) {
		packageExplorer.open();
		try {
			packageExplorer.getProject(jsfProjectName);
		} catch (EclipseLayerException ele) {
			JSFNewProjectWizard jsfNewProjectWizard = new JSFNewProjectWizard();
			jsfNewProjectWizard.open();
			JSFNewProjectFirstPage jsfNewProjectFirstPage = new JSFNewProjectFirstPage();
			jsfNewProjectFirstPage.setProjectName(jsfProjectName);
			jsfNewProjectFirstPage.setProjectTemplate("JSFKickStartWithoutLibs");
			jsfNewProjectWizard.next();
			new JSFNewProjectSecondPage()
					.setRuntime(serverRequirement.getRuntimeNameLabelText(serverRequirement.getConfig()));
			jsfNewProjectWizard.finish();
		}
	}

	/**
	 * Create Facelets Project with <b>faceletsProjectName</b>
	 * 
	 * @param faceletsProjectName
	 *            - name of created project
	 */
	protected void createFaceletsProject(String faceletsProjectName) {
		packageExplorer.open();
		if (!packageExplorer.containsProject(faceletsProjectName)){
			JSFNewProjectWizard jsfNewProjectWizard = new JSFNewProjectWizard();
			jsfNewProjectWizard.open();
			JSFNewProjectFirstPage jsfNewProjectFirstPage = new JSFNewProjectFirstPage();
			jsfNewProjectFirstPage.setProjectName(faceletsProjectName);
			jsfNewProjectFirstPage.setJSFEnvironment("JSF 1.2 with Facelets");
			jsfNewProjectFirstPage.setProjectTemplate("FaceletsKickStartWithoutLibs");
			jsfNewProjectWizard.next();
			new JSFNewProjectSecondPage()
					.setRuntime(serverRequirement.getRuntimeNameLabelText(serverRequirement.getConfig()));
			jsfNewProjectWizard.finish();
		}
	}

	/**
	 * Test content of elements from <b>editor</b> by IDs.
	 * <p>
	 * Tested elements from source editor should have id's attributes that
	 * correspond to expected one from <b>expectedVPEContentFile</b>.
	 * 
	 * @param expectedVPEContentFile
	 *            - file name, for example, <i>"ShowNonVisualTags.xml"</i> with
	 *            expected VPE DOM Elements and id's attributes correspond to
	 *            source <b>editor</b> element
	 * @param editor
	 *            - {@link JSPMultiPageEditor} that contains source code with
	 *            tested elements and current id.
	 * @throws Throwable
	 * @see JSPMultiPageEditorExt
	 * @see Throwable
	 */
	@Deprecated
	protected void performContentTestByIDs(String expectedVPEContentFile,
			JSPMultiPageEditorExt editor) throws Throwable {

		JSPMultiPageEditor multiPageEditor = editor.getJspMultiPageEditor();
		assertNotNull(multiPageEditor);

		VpeController controller = TestUtil.getVpeController(multiPageEditor);

		String expectedVPEContentFilePath = getPathToResources(expectedVPEContentFile);

		File xmlTestFile = new File(expectedVPEContentFilePath);

		Document xmlTestDocument = TestDomUtil.getDocument(xmlTestFile);
		assertNotNull(
				"Can't get test file, possibly file not exists " + xmlTestFile, xmlTestDocument); //$NON-NLS-1$

		List<String> ids = TestDomUtil.getTestIds(xmlTestDocument);

		for (String id : ids) {

			compareElements(controller, xmlTestDocument, id, id);
		}

		if (getException() != null) {
			throw getException();
		}

	}

	private void compareElements(VpeController controller,
			Document xmlTestDocument, String elementId, String xmlTestId)
			throws ComparisonException {

		// get element by id
		nsIDOMElement vpeElement = findElementById(controller, elementId);
		assertNotNull("Cann't find element with id=" + elementId, vpeElement); //$NON-NLS-1$

		// get test element by id - get <test id="..." > element and get his
		// first child
		Element xmlModelElement = TestDomUtil.getFirstChildElement(TestDomUtil
				.getElemenById(xmlTestDocument, xmlTestId));

		assertNotNull(xmlModelElement);

		// compare DOMs
		try {
			TestDomUtil.compareNodes(vpeElement, xmlModelElement);
		} catch (ComparisonException e) {
			fail(e.getMessage());
		}

	}

	private nsIDOMElement findElementById(VpeController controller,
			String elementId) {

		Element sourceElement = findSourceElementById(controller, elementId);

		VpeNodeMapping nodeMapping = controller.getDomMapping().getNodeMapping(
				sourceElement);

		if (nodeMapping == null)
			return null;

		return (nsIDOMElement) nodeMapping.getVisualNode();
	}

	private Element findSourceElementById(VpeController controller,
			String elementId) {

		return getSourceDocument(controller).getElementById(elementId);
	}

	private Document getSourceDocument(VpeController controller) {
		return controller.getSourceBuilder().getSourceDocument();
	}

	protected String getPathToResources(String pagePathNextToPluginBundlePath) throws IOException {
		String pluginBundlePath = FileLocator.toFileURL(
				Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile(); //$NON-NLS-1$
		String filePath = pluginBundlePath + "resources/" + pagePathNextToPluginBundlePath; //$NON-NLS-1$
		File file = new File(filePath);
		if (!file.exists()) {
			filePath = pluginBundlePath + pagePathNextToPluginBundlePath;
		}
		return filePath;
	}

	@Override
	protected void activePerspective() {
		if (!bot.perspectiveByLabel("Web Development").isActive()) { //$NON-NLS-1$
			bot.perspectiveByLabel("Web Development").activate(); //$NON-NLS-1$
		}
	}

	protected void openPalette() {
	  new PaletteView().open();
	}

	/**
	 * Test content for elements from all VPE DOM that are nested with
	 * <i>BODY</i> descriptor
	 * 
	 * @param expectedVPEContentFile
	 *            - file name, for example,
	 *            <i>"VerificationOfNameSpaces.xml"</i> with expected VPE DOM
	 *            Elements that are nested with <i>BODY</i> descriptor
	 * @param editor
	 *            - {@link JSPMultiPageEditor} that contains source code of
	 *            currently tested page.
	 * @throws Throwable
	 */

	protected void performContentTestByDocument(String expectedVPEContentFile,
			JSPMultiPageEditorExt editor) throws Throwable {
		JSPMultiPageEditor multiPageEditor = editor.getJspMultiPageEditor();
		assertNotNull(multiPageEditor);

		nsIDOMDocument visualDocument = ((VpeEditorPart) multiPageEditor
				.getVisualEditor()).getVisualEditor().getDomDocument();

		String expectedVPEContentFilePath = getPathToResources(expectedVPEContentFile);

		File xmlTestFile = new File(expectedVPEContentFilePath);

		Document xmlTestDocument = TestDomUtil.getDocument(xmlTestFile);
		assertNotNull(
				"Can't get test file, possibly file not exists " + xmlTestFile, xmlTestDocument); //$NON-NLS-1$

		compareDocuments(visualDocument, xmlTestDocument);

	}

	private void compareDocuments(nsIDOMDocument visualDocument,
			Document xmlTestDocument) throws ComparisonException {
		nsIDOMNode visualBodyNode = visualDocument
				.getElementsByTagName("BODY").item(0); //$NON-NLS-1$
		Node testBodyNode = xmlTestDocument
				.getElementsByTagName("BODY").item(0); //$NON-NLS-1$
		TestDomUtil.compareNodes(visualBodyNode, testBodyNode);
	}

	protected void openPage(String pageName) {
		openPage(pageName, VPEAutoTestCase.JBT_TEST_PROJECT_NAME);
	}

	/**
	 * Opens page pageName from projectName
	 * 
	 * @param pageName
	 * @param projectName
	 */
	protected void openPage(String pageName, String projectName) {
		packageExplorer.open();
		packageExplorer.getProject(projectName).getProjectItem("WebContent", "pages", pageName).open();
	}

	/**
	 * Opens Test Page
	 */
	protected void openPage() {
		openPage(VPEAutoTestCase.TEST_PAGE,
				VPEAutoTestCase.JBT_TEST_PROJECT_NAME);
	}

	/**
	 * Creates new empty JSP page within test project
	 * 
	 * @param pageName
	 * @param subDirs
	 *            - complete path to page location within workspace
	 */
	protected void createJspPage(String pageName, String... subDirs) {
		packageExplorer.open();
		try {
			if (subDirs == null || subDirs.length == 0) {
				if(projectName == null){
					setProjectName(VPEAutoTestCase.JBT_TEST_PROJECT_NAME);
				}
				packageExplorer.getProject(projectName).getProjectItem("WebContent", "pages", pageName).select();
			} else {
				packageExplorer.getProject(subDirs[0])
						.getProjectItem(Arrays.copyOfRange(subDirs, 1, subDirs.length)).getProjectItem(pageName)
						.select();
			}
		} catch (EclipseLayerException ele) {
			NewJSPFileWizardDialog newJSPDialog = new NewJSPFileWizardDialog ();
			newJSPDialog.open();
			NewJSPFileWizardJSPPage newJSPFileWizardJSPPage = new NewJSPFileWizardJSPPage();
			newJSPFileWizardJSPPage.setFileName(pageName);
			if (subDirs != null && subDirs.length > 0){
				newJSPFileWizardJSPPage.selectParentFolder(subDirs);
			}
			else{
				newJSPFileWizardJSPPage.selectParentFolder(projectName,"WebContent", "pages");
			}
			newJSPDialog.next();
			new NewJSPFileWizardJSPTemplatePage().setTemplate("New JSP File (html)");
			newJSPDialog.finish();
		}
	}

	/**
	 * Maximize Source Pane
	 * 
	 * @param pageName
	 */
	public void maximizeSourcePane(String pageName) {
		DefaultEditor editor = new DefaultEditor(pageName);
		editor.activate();
		new DefaultCTabItem( "Visual/Source").activate();

		final org.jboss.tools.vpe.editor.xpl.CustomSashForm csf =
			WidgetLookup.getInstance().activeWidget(null, org.jboss.tools.vpe.editor.xpl.CustomSashForm.class,0);
		
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				csf.maxDown();
			}
		});
	}

	/**
	 * Maximize Visual Pane
	 * 
	 * @param pageName
	 */
	public void maximizeVisualPane(String pageName) {
		DefaultEditor editor = new DefaultEditor(pageName);
		editor.activate();
		new DefaultCTabItem( "Visual/Source").activate();

		final org.jboss.tools.vpe.editor.xpl.CustomSashForm csf =
			WidgetLookup.getInstance().activeWidget(null, org.jboss.tools.vpe.editor.xpl.CustomSashForm.class,0);
		
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				csf.maxUp();
			}
		});
	}

	/**
	 * Restore Source Pane
	 * 
	 * @param pageName
	 */
	public void restoreSourcePane(String pageName) {
		DefaultEditor editor = new DefaultEditor(pageName);
		editor.activate();
		new DefaultCTabItem( "Visual/Source").activate();

		final org.jboss.tools.vpe.editor.xpl.CustomSashForm csf =
			WidgetLookup.getInstance().activeWidget(null, org.jboss.tools.vpe.editor.xpl.CustomSashForm.class,0);
		
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				csf.downClicked();
			}
		});
	}

	/**
	 * Restore Visual Pane
	 * 
	 * @param pageName
	 */
	public void restoreVisualPane(String pageName) {
		DefaultEditor editor = new DefaultEditor(pageName);
		editor.activate();
		new DefaultCTabItem( "Visual/Source").activate();

		final org.jboss.tools.vpe.editor.xpl.CustomSashForm csf =
			WidgetLookup.getInstance().activeWidget(null, org.jboss.tools.vpe.editor.xpl.CustomSashForm.class,0);
		
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				csf.upClicked();
			}
		});
	}

	/**
	 * Creates new empty xhtml page within test project
	 * 
	 * @param pageName
	 * @param subDirs
	 *            - complete path to page location within workspace
	 */
	protected void createXhtmlPage(String pageName, String... subDirs) {
		packageExplorer.open();
		try {
			if (subDirs == null || subDirs.length == 0) {
				if(projectName == null){
					setProjectName(VPEAutoTestCase.JBT_TEST_PROJECT_NAME);
				}
				packageExplorer.getProject(projectName).getProjectItem("WebContent", "pages", pageName).select();
			} else {
				packageExplorer.getProject(subDirs[0])
						.getProjectItem(Arrays.copyOfRange(subDirs, 1, subDirs.length)).getProjectItem(pageName)
						.select();
			}
		} catch (EclipseLayerException ele) {
			NewXHTMLWizard newXHTMLDialog = new NewXHTMLWizard ();
			newXHTMLDialog.open();
			NewXHTMLFileWizardPage newXHTMLFileWizardPage = new NewXHTMLFileWizardPage();
			newXHTMLFileWizardPage.setFileName(pageName);
			if (subDirs != null && subDirs.length > 0){
				newXHTMLFileWizardPage.selectParentFolder(subDirs);
			}
			newXHTMLDialog.next();
			new NewXHTMLFileWizardXHTMLTemplatePage().setUseXHTMLTemplate(false);
			newXHTMLDialog.finish();
		}
	}

	/**
	 * Create JSF2 Project with <b>jsf2ProjectName</b>
	 * 
	 * @param jsf2ProjectName
	 *            - name of created project
	 */
	protected void createJSF2Project(String jsf2ProjectName) {
		packageExplorer.open();
		if (!packageExplorer.containsProject(jsf2ProjectName)){
			JSFNewProjectWizard jsfNewProjectWizard = new JSFNewProjectWizard();
			jsfNewProjectWizard.open();
			JSFNewProjectFirstPage jsfNewProjectFirstPage = new JSFNewProjectFirstPage();
			jsfNewProjectFirstPage.setProjectName(jsf2ProjectName);
			jsfNewProjectFirstPage.setJSFEnvironment("JSF 2.0");
			jsfNewProjectFirstPage.setProjectTemplate("JSFKickStartWithoutLibs");
			jsfNewProjectWizard.next();
			new JSFNewProjectSecondPage()
					.setRuntime(serverRequirement.getRuntimeNameLabelText(serverRequirement.getConfig()));
			jsfNewProjectWizard.finish();
		}
	}
  /**
   * Creates new empty HTML page within test project
   * 
   * @param pageName
   * @param subDirs
   *            - complete path to page location within workspace
   */
	protected void createHtmlPage(String pageName, String... subDirs) {
		packageExplorer.open();
		try {
			if (subDirs == null || subDirs.length == 0) {
				if(projectName == null){
					setProjectName(VPEAutoTestCase.JBT_TEST_PROJECT_NAME);
				}
				packageExplorer.getProject(projectName).getProjectItem("WebContent", "pages", pageName).select();
			} else {
				packageExplorer.getProject(subDirs[0])
						.getProjectItem(Arrays.copyOfRange(subDirs, 1, subDirs.length)).getProjectItem(pageName)
						.select();
			}
		} catch (EclipseLayerException ele) {
			NewHTMLFileWizardDialog newHTMLDialog = new NewHTMLFileWizardDialog();
			newHTMLDialog.open();
			NewHTMLFileWizardHTMLPage newHTMLFileWizardHTMLPage = new NewHTMLFileWizardHTMLPage();
			new NewHTMLFileWizardHTMLPage().setFileName(pageName);
			if (subDirs != null && subDirs.length > 0){
				newHTMLFileWizardHTMLPage.selectParentFolder(subDirs);
			}
			else{
				newHTMLFileWizardHTMLPage.selectParentFolder(projectName,"WebContent", "pages");
			}
			newHTMLDialog.next();
			newHTMLDialog.finish();
		}
	}
  
  /**
   * Add RichFaces library to project classpath
   * @param projectName
   */
  protected void addRichFacesToProjectClassPath(String projectName){
    addedVariableRichfacesUiLocation.put(projectName,
    	BuildPathHelper.addExternalJar(VPEAutoTestCase.RICH_FACES_UI_JAR_LOCATION,projectName));
  }
  /**
   * Remove previously added RichFaces library from project classpath
   * @param projectName
   */
  protected void removeRichFacesFromProjectClassPath(String projectName){
    if (addedVariableRichfacesUiLocation.containsKey(projectName)){
      BuildPathHelper.removeVariable(projectName, addedVariableRichfacesUiLocation.get(projectName), true);
      addedVariableRichfacesUiLocation.remove(projectName);
      eclipse.cleanAllProjects();
    }
  }

	/**
	 * Create Dynamic Web Project with <b>jsfProjectName</b>
	 * 
	 * @param dynamicWebfProjectName
	 *            - name of created project
	 */
	protected void createDynamicWebProject(String dynamicWebfProjectName) {
		packageExplorer.open();
		if (!packageExplorer.containsProject(dynamicWebfProjectName)) {
			WebProjectWizard webProjectWizard = new WebProjectWizard();
			webProjectWizard.open();
			WebProjectFirstPage webProjectFirstPage = new WebProjectFirstPage();
			webProjectFirstPage.setProjectName(dynamicWebfProjectName);
			webProjectWizard.finish();
		}
	}
}
