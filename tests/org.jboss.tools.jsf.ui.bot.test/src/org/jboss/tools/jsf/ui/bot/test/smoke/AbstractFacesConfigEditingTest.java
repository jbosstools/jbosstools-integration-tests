/*******************************************************************************
 * Copyright (c) 2007-2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jsf.ui.bot.test.smoke;

import java.util.List;

import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsDescriptionMatcher;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsResourceMatcher;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.tools.jsf.reddeer.ProjectType;
import org.jboss.tools.jsf.reddeer.ui.editor.FacesConfigEditor;
import org.jboss.tools.jsf.ui.bot.test.JSFAutoTestCase;
import org.jboss.tools.ui.bot.ext.Assertions;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;
/** Parent test for editing of faces-config.xml file
 * @author Vladimir Pakan
 *
 */
public abstract class AbstractFacesConfigEditingTest extends JSFAutoTestCase{
  
  protected static final String FACES_CONFIG_FILE_NAME = "faces-config.xml";
  private String originalContent;
  private String testProjectName = null;
  private FacesConfigEditor facesConfigEditor;
  @Override
  public void setUp() throws Exception {
    super.setUp();
    EditorHandler.getInstance().closeAll(true);
    intializeTestProject();
    facesConfigEditor = getFacesConfigEditor();
    testProjectName = getTestProjectName();
    originalContent = facesConfigEditor.getFacesConfigSourceEditor().getText();
  }
  
  @Override
  public void tearDown() throws Exception {
	//TODO: delete objects added via diagram
    if (facesConfigEditor != null) {
      facesConfigEditor.getFacesConfigSourceEditor().setText(originalContent);
      facesConfigEditor.save();
      facesConfigEditor.close();
      new WaitWhile(new JobIsRunning());
    }
    super.tearDown();
  }

	/**
	 * Test Managed Bean editing
	 */
	@Test
	public void testManagedBean() {
		final String managedBeanName = "TestBean";
		final String managedBeanClass = "TestBeanClass";
		facesConfigEditor.addManagedBean(getTestProjectType(), AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME,
				managedBeanName, managedBeanClass);
		facesConfigEditor.save();
		assertFacesConfigXmlHasNoErrors();
		facesConfigEditor.activateTreeTab();
		assertTrue("Created mbean has to be selected within Tree but is not",
				new DefaultTreeItem(AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME, "Managed Beans",
						managedBeanName).isSelected());
		Assertions.assertFileExistsInWorkspace(managedBeanClass + ".java", testProjectName, "JavaSource");
		Assertions.assertSourceEditorContains(
				stripXMLSourceText(facesConfigEditor.getFacesConfigSourceEditor().getText()),
				"<managed-bean><managed-bean-name>" + managedBeanName + "</managed-bean-name>" + "<managed-bean-class>"
						+ managedBeanClass + "</managed-bean-class>"
						+ "<managed-bean-scope>request</managed-bean-scope></managed-bean>",
				AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME);
		// Modify Managed Bean
		facesConfigEditor.activateTreeTab();
		new DefaultText(1).setText(managedBeanClass + "xxqq");
		facesConfigEditor.save();
		new WaitWhile(new JobIsRunning());
		if (getCheckForExistingManagedBeanClass()) {
			assertFacesConfigXmlHasValidationProblems();
		}
		facesConfigEditor.activateTreeTab();
		new DefaultText(1).setText(managedBeanClass);
		facesConfigEditor.save();
		new WaitWhile(new JobIsRunning());
		// Delete Managed Bean and add it back via New Managed Bean Form
		new DefaultTreeItem(AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME, "Managed Beans").select();
		new DefaultTableItem(managedBeanName).select();
		new PushButton("Remove...").click();
		new DefaultShell("Confirmation");
		new CheckBox("Delete Java Source").toggle(false);
		new OkButton().click();
		new PushButton("Add...").click();
		new DefaultShell(FacesConfigEditor.getAddManagedBeanDialogTitle(getTestProjectType()));
		new PushButton("Browse...").click();
		new DefaultShell("Select Class");
		new DefaultText().setText(managedBeanClass);
		final String selectedClassLabel = new DefaultTable().getSelectetItems().get(0).getText();
		assertTrue("Selected item in table has to start with " + managedBeanClass + "\n but is:\n" + selectedClassLabel,
				selectedClassLabel.startsWith(managedBeanClass));
		new OkButton().click();
		new DefaultShell(FacesConfigEditor.getAddManagedBeanDialogTitle(getTestProjectType()));
		new DefaultText(1).setText(managedBeanName);
		new FinishButton().click();
		facesConfigEditor.save();
		Assertions.assertSourceEditorContains(
				stripXMLSourceText(facesConfigEditor.getFacesConfigSourceEditor().getText()),
				"<managed-bean><managed-bean-name>" + managedBeanName + "</managed-bean-name>" + "<managed-bean-class>"
						+ managedBeanClass + "</managed-bean-class>"
						+ "<managed-bean-scope>request</managed-bean-scope></managed-bean>",
				AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME);
		// Delete Managed Bean
		facesConfigEditor.activateTreeTab();
		new DefaultTreeItem(AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME, "Managed Beans").select();
		new DefaultTableItem(managedBeanName).select();
		new PushButton("Remove...").click();
		new DefaultShell("Confirmation");
		new CheckBox("Delete Java Source").toggle(true);
		new OkButton().click();
		boolean managedBeanWasDeleted = false;
		try {
			new DefaultTableItem(managedBeanName);
		} catch (CoreLayerException cle) {
			managedBeanWasDeleted = true;
		}
		assertTrue("Managed bean " + managedBeanName + " was not deleted properly.", managedBeanWasDeleted);
		Assertions.assertFileNotExistsInWorkspace(managedBeanClass + ".java", testProjectName, "JavaSource");
		Assertions.assertSourceEditorNotContain(facesConfigEditor.getFacesConfigSourceEditor().getText(),
				"<managed-bean-name>" + managedBeanName + "</managed-bean-name>",
				AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME);
		Assertions.assertSourceEditorNotContain(facesConfigEditor.getFacesConfigSourceEditor().getText(),
				"<managed-bean-class>" + managedBeanClass + "</managed-bean-class>",
				AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME);
	}

	/**
	 * Tests Component editing
	 */
	@Test
	public void testComponent() {
		String componentType = "TestComponentType";
		String componentClass = "TestComponentClass";
		facesConfigEditor.addComponent(AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME, componentType,
				componentClass);
		facesConfigEditor.save();
		new WaitWhile(new JobIsRunning());
		checkFacesConfigNodeEditing("Components", componentType, componentClass, "component",
				"component-type", "component-class", true);
	}

	/**
	 * Tests Converter editing
	 */
	@Test
	public void testConverter() {
		String converterId = "TestConverterID";
		String converterClass = "TestConverterClass";
		facesConfigEditor.addConverter(AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME, converterId,
				converterClass);
		facesConfigEditor.save();
		new WaitWhile(new JobIsRunning());
		checkFacesConfigNodeEditing("Converters", converterId, converterClass, "converter", "converter-id",
				"converter-class", true);
	}

	/**
	 * Tests Referenced Bean editing
	 */
	@Test
	public void testReferencedBean() {
		String referencedBeanName = "TestReferencedBeanName";
		String referencedBeanClass = "TestReferencedBeanClass";
		facesConfigEditor.addReferencedBean(AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME, referencedBeanName,
				referencedBeanClass);
		facesConfigEditor.save();
		new WaitWhile(new JobIsRunning());
		checkFacesConfigNodeEditing("Referenced Beans", referencedBeanName, referencedBeanClass, "referenced-bean",
				"referenced-bean-name", "referenced-bean-class", true);
	}
	/**
	 * Tests Render Kit editing
	 */
	@Test
	public void testRenderKit() {
		String renderKitId = "TestRenderKitID";
		String renderKitClass = "TestRenderKitClass";
		facesConfigEditor.addRenderKit(getTestProjectType(), AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME,
				renderKitId, renderKitClass);
		facesConfigEditor.save();
		new WaitWhile(new JobIsRunning());
		checkFacesConfigNodeEditing("Render Kits", renderKitId, renderKitClass, "render-kit", "render-kit-id",
				"render-kit-class", false);
	}

	/**
	 * Tests Validator editing
	 */
	@Test
	public void testValidator() {

		String validatorId = "TestValidatorID";
		String validatorClass = "TestValidatorClass";
		facesConfigEditor.addValidator(AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME, validatorId,
				validatorClass);
		facesConfigEditor.save();
		new WaitWhile(new JobIsRunning());
		checkFacesConfigNodeEditing("Validators", validatorId, validatorClass, "validator", "validator-id",
				"validator-class", false);

	}

	/**
	 * Asserts if faces-config.xml has no errors
	 */
	protected static void assertFacesConfigXmlHasNoErrors() {
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		List<Problem> errors = problemsView.getProblems(ProblemType.ERROR,
				new ProblemsResourceMatcher(AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME));
		boolean areThereNoErrors = ((errors == null) || (errors.size() == 0));
		assertTrue("There are errors in Problems view: " + (areThereNoErrors ? "" : errors.get(0).getDescription()),
				areThereNoErrors);
	}
	/**
	 * Asserts if faces-config.xml has errors
	 */
	protected static void assertFacesConfigXmlHasValidationProblems() {
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		List<Problem> problems = problemsView.getProblems(ProblemType.ERROR,
				new ProblemsResourceMatcher(AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME));
		boolean areThereProblems = ((problems != null) && (problems.size() > 0));
		if (!areThereProblems) {
			problems = problemsView.getProblems(ProblemType.WARNING,
					new ProblemsDescriptionMatcher(new RegexMatcher(".*references to non-existent class.*")),
					new ProblemsResourceMatcher(AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME));
			areThereProblems = ((problems != null) && (problems.size() > 0));
		}
		assertTrue("There are missing problems in Problems view for "
				+ AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME + " file.", areThereProblems);
	}

	/**
	 * Check editing of particular tree node within Face Config Editor Tree
	 * 
	 * @param treeNodeLabel
	 * @param nameTextValue
	 * @param classTextValue
	 * @param xmlNodeName
	 * @param nameXmlNodeName
	 * @param classXmlNodeName
	 * @param checkForValdiationErrors
	 */
	protected void checkFacesConfigNodeEditing(String treeNodeLabel, String nameTextValue,
			String classTextValue, String xmlNodeName, String nameXmlNodeName,
			String classXmlNodeName, boolean checkForValdiationErrors) {

		if (checkForValdiationErrors) {
			assertFacesConfigXmlHasValidationProblems();
		}
		facesConfigEditor.activateTreeTab();
		assertTrue("Selected node has to have label '" + nameTextValue + " but it odes not",
				facesConfigEditor.getFacesConfigTreeItem(AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME,
						treeNodeLabel, nameTextValue).isSelected());
		Assertions.assertSourceEditorContains(
				stripXMLSourceText(facesConfigEditor.getFacesConfigSourceEditor().getText()),
				"<" + xmlNodeName + ">" + "<" + nameXmlNodeName + ">" + nameTextValue + "</" + nameXmlNodeName + ">"
						+ "<" + classXmlNodeName + ">" + classTextValue + "</" + classXmlNodeName + ">" + "</"
						+ xmlNodeName + ">",
				AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME);
		// Delete Node
		facesConfigEditor.activateTreeTab();
		facesConfigEditor.getFacesConfigTreeItem(AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME, treeNodeLabel)
				.select();
		new DefaultTableItem(nameTextValue).select();
		new PushButton("Remove...").click();
		new DefaultShell("Confirmation");
		new OkButton().click();
		boolean nodeWasDeleted = false;
		try {
			new DefaultTableItem(nameTextValue);
		} catch (CoreLayerException cle) {
			nodeWasDeleted = true;
		}
		assertTrue(nameTextValue + " was not deleted properly.", nodeWasDeleted);
		Assertions.assertSourceEditorNotContain(facesConfigEditor.getFacesConfigSourceEditor().getText(),
				"<" + nameXmlNodeName + ">" + nameTextValue + "</" + nameXmlNodeName + ">",
				AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME);
		Assertions.assertSourceEditorNotContain(facesConfigEditor.getFacesConfigSourceEditor().getText(),
				"<" + classXmlNodeName + ">" + classTextValue + "</" + classXmlNodeName + ">",
				AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME);
	}
  /**
   * Test editing via Diagram tab
   */
  /*
  public void testDiagramEditing(){
    final int verticalSpacing = 100;
    facesConfigEditorExt.selectPage(IDELabel.FacesConfigEditor.DIAGRAM_TAB_LABEL);
    final FacesConfigGefEditorBot gefEditorBot = new FacesConfigGefEditorBot(facesConfigEditorExt.getReference());
    gefViewer =  gefEditorBot.getViewer();
    SWTBotGefEditPart mainPart = gefViewer.mainEditPart();
    // add View to diagram via pallete tool
    gefViewer.activateTool(IDELabel.FacesConfigEditor.GEF_VIEW_TEMPLATE_TOOL);
    SWTBotGefEditPart gefObjectPart = mainPart.descendants(
        new FacesConfigGefEditorPartMatcher(AbstractFacesConfigEditingTest.getInputNamePageName(getTestProjectType()))).get(0);
    gefViewer.click(FacesConfigGefEditorUtil.getGefPartPosition(gefObjectPart).x,
        FacesConfigGefEditorUtil.getGefPartPosition(gefObjectPart).y + verticalSpacing);
    final String viewAddedViaToolName = "addedViaTool";
    handleNewViewWizard(viewAddedViaToolName + 
        AbstractFacesConfigEditingTest.getNewPagesExtension(getTestProjectType()));
    bot.sleep(Timing.time3S());
    facesConfigEditor.save();
    bot.sleep(Timing.time3S());
    assertFacesConfigXmlHasNoErrors(botExt);
    Assertions.assertFileExistsInWorkspace(viewAddedViaToolName + 
          AbstractFacesConfigEditingTest.getNewPagesExtension(getTestProjectType()),
        getTestProjectName(),
        "WebContent");
    gefViewer.activateTool(IDELabel.FacesConfigEditor.GEF_CREATE_NEW_CONNECTION_TOOL);
    gefObjectPart.click();
    bot.sleep(Timing.time1S());
    gefObjectAddedViaViewTool = mainPart.descendants(new FacesConfigGefEditorPartMatcher("/" + viewAddedViaToolName + 
        AbstractFacesConfigEditingTest.getNewPagesExtension(getTestProjectType()))).get(0);
    gefObjectAddedViaViewTool.click();
    bot.sleep(Timing.time1S());
    facesConfigEditor.save();
    bot.sleep(Timing.time3S());
    Assertions.assertSourceEditorContains(AbstractFacesConfigEditingTest.stripXMLSourceText(facesConfigEditorExt.getText()), 
        "<navigation-case><from-outcome>" + viewAddedViaToolName +
        "</from-outcome><to-view-id>/" + viewAddedViaToolName + 
        AbstractFacesConfigEditingTest.getNewPagesExtension(getTestProjectType()) +
        "</to-view-id></navigation-case>", 
        AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME);
    // add View to Diagram via D'n'D
    final String dndPageName = "testDnDPage";
    createTestPage(dndPageName + 
        AbstractFacesConfigEditingTest.getNewPagesExtension(getTestProjectType()));
    facesConfigEditor.show();
    facesConfigEditor.setFocus();
    SWTBotTreeItem tiPage = SWTEclipseExt.selectTreeLocation(open.viewOpen(ActionItem.View.JBossToolsWebWebProjects.LABEL).bot(),
        getTestProjectName(),
        "WebContent",
        "pages",
        dndPageName + AbstractFacesConfigEditingTest.getNewPagesExtension(getTestProjectType()));
    DragAndDropHelper.dnd((TreeItem)tiPage.widget, (FigureCanvas) gefEditorBot.getControl());
    facesConfigEditor.save();
    bot.sleep(Timing.time3S());
    assertFacesConfigXmlHasNoErrors(botExt);
    gefViewer.activateTool(IDELabel.FacesConfigEditor.GEF_CREATE_NEW_CONNECTION_TOOL);
    gefObjectPart.click();
    bot.sleep(Timing.time1S());
    gefObjectAddedViaDnDTool = mainPart.descendants(new FacesConfigGefEditorPartMatcher("/pages/" + dndPageName + 
        AbstractFacesConfigEditingTest.getNewPagesExtension(getTestProjectType()))).get(0);
    gefObjectAddedViaDnDTool.click();
    bot.sleep(Timing.time1S());
    gefViewer.click(FacesConfigGefEditorUtil.getGefPartPosition(gefObjectAddedViaDnDTool).x,
        FacesConfigGefEditorUtil.getGefPartPosition(gefObjectAddedViaDnDTool).y + verticalSpacing);
    gefViewer.clickContextMenu(IDELabel.Menu.AUTO_LAYOUT);
    bot.shell(IDELabel.Shell.AUTO_LAYOUT).activate();
    bot.button(IDELabel.Button.OK).click();
    facesConfigEditor.save();
    bot.sleep(Timing.time3S());
    Assertions.assertSourceEditorContains(AbstractFacesConfigEditingTest.stripXMLSourceText(facesConfigEditorExt.getText()), 
        "<navigation-case><from-outcome>" + dndPageName +
        "</from-outcome><to-view-id>/pages/" + dndPageName + 
        AbstractFacesConfigEditingTest.getNewPagesExtension(getTestProjectType()) + 
        "</to-view-id></navigation-case>", 
        AbstractFacesConfigEditingTest.FACES_CONFIG_FILE_NAME);
  }
  */
	/**
	 * Handle adding new View
	 * 
	 * @param fromViewID
	 */
	protected void handleNewViewWizard(String fromViewID) {
		new DefaultShell("New View");
		new LabeledText("From View ID:").setText(fromViewID);
		new CheckBox("Create File on Disk").toggle(true);
		new FinishButton().click();
		new WaitWhile(new ShellWithTextIsActive("New View"));
	}
  /**
   * Returns XML Source striped from spaces, tabs and EOL
   * 
   * @return String
   */
  protected static String stripXMLSourceText(String editorText) {
    return editorText.replaceAll("\n", "").replaceAll("\t", "")
        .replaceAll("\b", "").replaceAll(" ", "").replaceAll("\r", "")
        .replaceAll("\f", "");
  }
  /**
   * Confirm deletion of View from Diagram Editor
   */
  protected void confirmViewDelete(){
    bot.shell(IDELabel.Shell.CONFIRMATION).activate();
    bot.checkBox(IDELabel.FacesConfigEditor.DELETE_FILE_FROM_DISK_CHECK_BOX).select();
    bot.button(IDELabel.Button.OK).click();
  }

  /**
   * Returns proper Input Name Page Name
   * @param testProjectType
   * @return
   */
/*  
  private static String getInputNamePageName(ProjectType testProjectType){
    String result;
    if (testProjectType.equals(ProjectType.JSF)){
      result = "/pages/inputUserName.jsp";
    }
    else if (testProjectType.equals(ProjectType.JSF2)){
      result = "/pages/inputname.xhtml";
    }
    else {
      throw new IllegalArgumentException("Not supported TestProjectType " + testProjectType);
    }
    
    return result;
    
  }
*/  
  /**
   * Returns proper Extension for new Web Pages
   * @param testProjectType
   * @return
   */
  /*
  private static String getNewPagesExtension(ProjectType testProjectType){
    String result;
    if (testProjectType.equals(ProjectType.JSF)){
      result = ".jsp";
    }
    else if (testProjectType.equals(ProjectType.JSF2)){
      result = ".xhtml";
    }
    else {
      throw new IllegalArgumentException("Not supported TestProjectType " + testProjectType);
    }
    
    return result;
    
  }
  */
  /*
  private void createTestPage (String testPageName){
    if (testPageName.endsWith(".jsp")){
      createJspPage(testPageName, getTestProjectName(), "WebContent", "pages");
    }
    else if (testPageName.endsWith(".xhtml")){
      createXhtmlPage(testPageName, getTestProjectName(), "WebContent", "pages");
    }
    else {
      throw new IllegalArgumentException("Not supported Test Page Extension when creating page " + testPageName);
    }
  }
  */
  /**
   * Returns current SWTBotEditor
   * @return
   */
  protected abstract FacesConfigEditor getFacesConfigEditor();
  /**
   * Returns Test Project Name
   * @return
   */
  protected abstract String getTestProjectName();
  /**
   * Initializes Test Project
   */
  protected abstract void intializeTestProject();
  /**
   * Returns Test Project Type
   * @return
   */
  protected abstract ProjectType getTestProjectType();
  /**
   * Returns true when test has to check if Managed Class exists
   * @return
   */
  protected abstract boolean getCheckForExistingManagedBeanClass();
  
}
  
