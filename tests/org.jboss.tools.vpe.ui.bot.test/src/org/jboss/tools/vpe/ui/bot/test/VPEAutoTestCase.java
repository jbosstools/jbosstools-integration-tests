/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

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
import org.eclipse.swt.SWTException;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.test.TestProperties;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.condition.ShellIsActiveCondition;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.JBossToolsWebJSFJSFProject;
import org.jboss.tools.ui.bot.ext.helper.BuildPathHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.test.JBTSWTBotTestCase;
import org.jboss.tools.ui.bot.test.SWTBotJSPMultiPageEditor;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.xpl.CustomSashForm;
import org.junit.After;
import org.junit.Before;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

@Require(server = @Server(state = ServerState.Present),
		clearProjects=false,
		clearWorkspace=false,
		runOnce=true,
		perspective="Web Development"
		)
public abstract class VPEAutoTestCase extends JBTSWTBotTestCase {

  protected static Properties projectProperties;
  protected static String PROJECT_PROPERTIES = "projectProperties.properties";
  protected static final String TEST_PAGE = "inputUserName.jsp"; //$NON-NLS-1$
  protected static final String FACELETS_TEST_PAGE = "inputname.xhtml"; //$NON-NLS-1$
  protected static String JSF2_TEST_PAGE = "inputname.xhtml"; //$NON-NLS-1$

  protected final static String JBT_TEST_PROJECT_NAME = "JBIDETestProject"; //$NON-NLS-1$
  protected final static String FACELETS_TEST_PROJECT_NAME = "FaceletsTestProject"; //$NON-NLS-1$
  protected final static String JSF2_TEST_PROJECT_NAME = "JSF2TestProject"; //$NON-NLS-1$
  protected final static String RICH_FACES_UI_JAR_LOCATION;

  private String projectName = null;
  private HashMap<String,String> addedVariableRichfacesUiLocation = new HashMap<String,String>();
	
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
		clearWorkbench();
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
		clearWorkbench();
		new SWTJBTExt(bot)
		  .removeProjectFromServers((projectName != null && projectName.length() > 0) ? projectName : JBT_TEST_PROJECT_NAME);
		setProjectName(null);
		super.tearDown();
	}

	/**
	 * Create JSF Project with <b>jsfProjectName</b>
	 * 
	 * @param jsfProjectName
	 *            - name of created project
	 */
	protected void createJSFProject(String jsfProjectName) {
	  SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
    SWTBotTree innerTree = innerBot.tree();
    try {
      innerTree.getTreeItem(jsfProjectName);
    } catch (WidgetNotFoundException wnfe) {
      SWTBot wiz = open.newObject(JBossToolsWebJSFJSFProject.LABEL);
      wiz.textWithLabel("Project Name*").setText(jsfProjectName); //$NON-NLS-1$
      wiz.comboBoxWithLabel("Template*").setSelection("JSFKickStartWithoutLibs"); //$NON-NLS-1$ //$NON-NLS-2$
      wiz.button("Next >").click(); //$NON-NLS-1$
      wiz.comboBoxWithLabel("Runtime:*").setSelection(SWTTestExt.configuredState.getServer().name); //$NON-NLS-1$ //$NON-NLS-2$
      open.finish(wiz);
      waitForBlockingJobsAcomplished(60 * 1000L, BUILDING_WS);
      setException(null);
    }
	}

	/**
	 * Create Facelets Project with <b>faceletsProjectName</b>
	 * 
	 * @param faceletsProjectName
	 *            - name of created project
	 */
	protected void createFaceletsProject(String faceletsProjectName) {
	  SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
    SWTBotTree innerTree = innerBot.tree();
    try {
      innerTree.getTreeItem(faceletsProjectName);
    } catch (WidgetNotFoundException wnfe) {
      bot.menu("File").menu("New").menu("Other...").click(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      bot.shell("New").activate(); //$NON-NLS-1$
      SWTBotTree tree = bot.tree();
      delay();
      tree.expandNode("JBoss Tools Web").expandNode("JSF").select("JSF Project"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      bot.button("Next >").click(); //$NON-NLS-1$
      bot.textWithLabel("Project Name*").setText(faceletsProjectName); //$NON-NLS-1$
      bot.comboBoxWithLabel("JSF Environment*").setSelection("JSF 1.2 with Facelets"); //$NON-NLS-1$ //$NON-NLS-2$
      bot.comboBoxWithLabel("Template*").setSelection("FaceletsKickStartWithoutLibs"); //$NON-NLS-1$ //$NON-NLS-2$
      bot.button("Next >").click(); //$NON-NLS-1$

      bot.comboBoxWithLabel("Runtime:*").setSelection(SWTTestExt.configuredState.getServer().name); //$NON-NLS-1$ //$NON-NLS-2$
      delay();
      bot.button("Finish").click(); //$NON-NLS-1$
      try {
        bot.button("Yes").click(); //$NON-NLS-1$
        openErrorLog();
        openPackageExplorer();
      } catch (WidgetNotFoundException e) {
      }

      waitForBlockingJobsAcomplished(60 * 1000L, BUILDING_WS);
      setException(null);
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
	 * @see SWTBotJSPMultiPageEditor
	 * @see Throwable
	 */
	@Deprecated
	protected void performContentTestByIDs(String expectedVPEContentFile,
			SWTBotJSPMultiPageEditor editor) throws Throwable {

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
		try {
			bot.viewByTitle(WidgetVariables.PALETTE);
		} catch (WidgetNotFoundException e) {
			bot.menu("Window").menu("Show View").menu("Other...").click(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			SWTBotTree viewTree = bot.tree();
			delay();
			viewTree.expandNode("JBoss Tools Web").expandNode( //$NON-NLS-1$
					WidgetVariables.PALETTE).select();
			bot.button("OK").click(); //$NON-NLS-1$
		}
	}

	/**
	 * Close all dialogs and editors, which may be not closed after test
	 * executing.
	 * 
	 * @see #isUnuseDialogOpened()
	 * @see #closeUnuseDialogs()
	 */

	protected void clearWorkbench() {
		while (isUnuseDialogOpened()) {
			closeUnuseDialogs();
		}
		bot.closeAllEditors();
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
			SWTBotJSPMultiPageEditor editor) throws Throwable {
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

	/**
	 * Try to close all unnecessary dialogs, that could prevent next tests fails
	 */

	protected abstract void closeUnuseDialogs();

	/**
	 * Verify if any dialog that should be closed is opened
	 */

	protected abstract boolean isUnuseDialogOpened();

	/**
	 * Opens page pageName
	 * 
	 * @param pageName
	 */
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
	  SWTBot innerBot = packageExplorer.show().bot();
		SWTBotTree tree = innerBot.tree();
		tree.expandNode(projectName).expandNode("WebContent") //$NON-NLS-1$
				.expandNode("pages").getNode(pageName).doubleClick(); //$NON-NLS-1$
		bot.sleep(Timing.time3S());
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
		SWTBotTreeItem tiPageParent = null;
		if (subDirs == null || subDirs.length == 0) {
			tiPageParent = packageExplorer.selectTreeItem("pages",
					new String[] { VPEAutoTestCase.JBT_TEST_PROJECT_NAME,
							"WebContent" });
		} else {
			String[] subPath = Arrays.copyOfRange(subDirs, 0,
					subDirs.length - 1);
			tiPageParent = packageExplorer.selectTreeItem(
					subDirs[subDirs.length - 1], subPath);
		}
		tiPageParent.expand();
		try {
			tiPageParent.getNode(pageName).doubleClick();
		} catch (WidgetNotFoundException e) {
			SWTBotShell currentShell = bot.activeShell();
			open.newObject(ActionItem.NewObject.WebJSP.LABEL);
			bot.shell(IDELabel.Shell.NEW_JSP_FILE).activate();
			bot.textWithLabel(ActionItem.NewObject.WebJSP.TEXT_FILE_NAME)
					.setText(pageName);
			bot.button(IDELabel.Button.NEXT).click();
			bot.table().select(IDELabel.NewJSPFileDialog.JSP_TEMPLATE);
			bot.button(IDELabel.Button.FINISH).click();
			bot.waitUntil(new ShellIsActiveCondition(currentShell), Timing.time10S());
		}
		bot.sleep(Timing.time2S());

	}

	/**
	 * Deletes page pageName
	 * 
	 * @param pageName
	 */
	protected void deletePage(String pageName) {
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER)
				.bot();
		innerBot.tree().expandNode(JBT_TEST_PROJECT_NAME)
				.expandNode("WebContent") //$NON-NLS-1$
				.expandNode("pages").getNode("testPage.jsp").select(); //$NON-NLS-1$//$NON-NLS-2$
		bot.menu("Edit").menu("Delete").click(); //$NON-NLS-1$ //$NON-NLS-2$
		bot.shell("Confirm Delete").activate(); //$NON-NLS-1$
		bot.button("OK").click(); //$NON-NLS-1$
	}

	/**
	 * Maximize Source Pane
	 * 
	 * @param botExt
	 * @param pageName
	 */
	public void maximizeSourcePane(SWTBotExt botExt, String pageName) {
		botExt.swtBotEditorExtByTitle(pageName).selectPage(
				IDELabel.VisualPageEditor.VISUAL_SOURCE_TAB_LABEL);

		final org.jboss.tools.vpe.editor.xpl.CustomSashForm csf = bot.widgets(
				widgetOfType(CustomSashForm.class)).get(0);
		UIThreadRunnable.syncExec(new VoidResult() {
			@Override
			public void run() {
				csf.maxDown();
			}
		});
	}

	/**
	 * Maximize Visual Pane
	 * 
	 * @param botExt
	 * @param pageName
	 */
	public void maximizeVisualPane(SWTBotExt botExt, String pageName) {
		botExt.swtBotEditorExtByTitle(pageName).selectPage(
				IDELabel.VisualPageEditor.VISUAL_SOURCE_TAB_LABEL);

		final org.jboss.tools.vpe.editor.xpl.CustomSashForm csf = bot.widgets(
				widgetOfType(CustomSashForm.class)).get(0);
		UIThreadRunnable.syncExec(new VoidResult() {
			@Override
			public void run() {
				csf.maxUp();
			}
		});
	}

	/**
	 * Restore Source Pane
	 * 
	 * @param botExt
	 * @param pageName
	 */
	public void restoreSourcePane(SWTBotExt botExt, String pageName) {
		botExt.swtBotEditorExtByTitle(pageName).selectPage(
				IDELabel.VisualPageEditor.VISUAL_SOURCE_TAB_LABEL);

		final org.jboss.tools.vpe.editor.xpl.CustomSashForm csf = bot.widgets(
				widgetOfType(CustomSashForm.class)).get(0);
		UIThreadRunnable.syncExec(new VoidResult() {
			@Override
			public void run() {
				csf.downClicked();
			}
		});
	}

	/**
	 * Restore Visual Pane
	 * 
	 * @param botExt
	 * @param pageName
	 */
	public void restoreVisualPane(SWTBotExt botExt, String pageName) {
		botExt.swtBotEditorExtByTitle(pageName).selectPage(
				IDELabel.VisualPageEditor.VISUAL_SOURCE_TAB_LABEL);

		final org.jboss.tools.vpe.editor.xpl.CustomSashForm csf = bot.widgets(
				widgetOfType(CustomSashForm.class)).get(0);
		UIThreadRunnable.syncExec(new VoidResult() {
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
		SWTBotTreeItem tiPageParent = null;
		if (subDirs == null || subDirs.length == 0) {
			tiPageParent = packageExplorer.selectTreeItem("pages",
					new String[] { VPEAutoTestCase.JBT_TEST_PROJECT_NAME,
							"WebContent" });
		} else {
			String[] subPath = Arrays.copyOfRange(subDirs, 0,
					subDirs.length - 1);
			tiPageParent = packageExplorer.selectTreeItem(
					subDirs[subDirs.length - 1], subPath);
		}
		tiPageParent.expand();
		try {
			tiPageParent.getNode(pageName).doubleClick();
		} catch (WidgetNotFoundException e) {
			SWTBotShell currentShell = bot.activeShell();
			open.newObject(ActionItem.NewObject.JBossToolsWebXHTMLFile.LABEL);
			bot.shell(IDELabel.Shell.NEW_XHTML_FILE).activate();
			bot.textWithLabel(
					ActionItem.NewObject.JBossToolsWebXHTMLFile.TEXT_FILE_NAME)
					.setText(pageName);
			bot.button(IDELabel.Button.NEXT).click();
			SWTBotCheckBox cbUseTemplate = bot
					.checkBox(IDELabel.NewXHTMLFileDialog.USE_XHTML_TEMPLATE_CHECK_BOX);
			if (cbUseTemplate.isChecked()) {
				cbUseTemplate.deselect();
			}
			bot.button(IDELabel.Button.FINISH).click();
			bot.waitUntil(new ShellIsActiveCondition(currentShell), Timing.time10S());
		}
		bot.sleep(Timing.time2S());

	}

	/**
	 * Create JSF2 Project with <b>jsf2ProjectName</b>
	 * 
	 * @param jsf2ProjectName
	 *            - name of created project
	 */
  protected void createJSF2Project(String jsf2ProjectName) {
    SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
    SWTBotTree tree = innerBot.tree();
    try {
      tree.getTreeItem(jsf2ProjectName);
    } catch (WidgetNotFoundException wnfe) {
      SWTBot wiz = open
          .newObject(ActionItem.NewObject.JBossToolsWebJSFJSFProject.LABEL);
      wiz.textWithLabel(IDELabel.NewJsfProjectDialog.PROJECT_NAME_LABEL)
          .setText(jsf2ProjectName);
      wiz.comboBoxWithLabel(IDELabel.NewJsfProjectDialog.JSF_ENVIRONMENT_LABEL)
          .setSelection("JSF 2.0");//$NON-NLS-1$
      wiz.comboBoxWithLabel(IDELabel.NewJsfProjectDialog.TEMPLATE_LABEL)
          .setSelection("JSFKickStartWithoutLibs");//$NON-NLS-1$
      wiz.button(IDELabel.Button.NEXT).click();
      wiz.comboBoxWithLabel("Runtime:*").setSelection(SWTTestExt.configuredState.getServer().name); //$NON-NLS-1$
      // Check if there is problem to create JSF 2 project using configured Server Runtime
      if (!wiz.button(IDELabel.Button.FINISH).isEnabled()){
        String errorText = wiz.text(1).getText();
        wiz.button(IDELabel.Button.CANCEL).click();
        assertTrue("Unable to create JSF 2 Project with Server Runtime: "
            + SWTTestExt.configuredState.getServer().name
            + "\nError: "
            + errorText,
          false);
      }
      else{
        open.finish(wiz);
        waitForBlockingJobsAcomplished(60 * 1000L, BUILDING_WS);
        setException(null);
      }  
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
    SWTBotTreeItem tiPageParent = null;
    if (subDirs == null || subDirs.length == 0) {
      tiPageParent = packageExplorer.selectTreeItem("pages",
          new String[] { VPEAutoTestCase.JBT_TEST_PROJECT_NAME,
              "WebContent" });
    } else {
      String[] subPath = Arrays.copyOfRange(subDirs, 0,
          subDirs.length - 1);
      tiPageParent = packageExplorer.selectTreeItem(
          subDirs[subDirs.length - 1], subPath);
    }
    tiPageParent.expand();
    try {
      tiPageParent.getNode(pageName).doubleClick();
    } catch (WidgetNotFoundException e) {
      SWTBotShell currentShell = bot.activeShell();
      open.newObject(ActionItem.NewObject.WebHTMLPage.LABEL);
      bot.shell(IDELabel.Shell.NEW_HTML_FILE).activate();
      bot.textWithLabel(ActionItem.NewObject.WebHTMLPage.TEXT_FILE_NAME)
          .setText(pageName);
      bot.button(IDELabel.Button.NEXT).click();
      bot.button(IDELabel.Button.FINISH).click();
      bot.waitUntil(new ShellIsActiveCondition(currentShell), Timing.time10S());
    }
    bot.sleep(Timing.time2S());

  }
  
  /**
   * Add RichFaces library to project classpath
   * @param projectName
   */
  protected void addRichFacesToProjectClassPath(String projectName){
    Throwable exceptionBeforeCall = getException();
    addedVariableRichfacesUiLocation.put(projectName,
    	BuildPathHelper.addExternalJar(VPEAutoTestCase.RICH_FACES_UI_JAR_LOCATION,projectName));
    if (exceptionBeforeCall == null 
        && getException() != null
        && getException() instanceof SWTException){
      setException(null);
    }
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
}
