/*******************************************************************************

 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor.pagedesign;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
/**
 * Tests editing of EL value  
 * @author vlado pakan
 *
 */
public class EditingELValueTest extends PageDesignTestCase {
  
  private SWTBotExt botExt = null;
  private static final String GREETING_PAGE_NAME = "greeting.xhtml";
  private static final String INPUT_NAME_PAGE_NAME = "inputname.xhtml";
  private static final String EL_VARIABLE_NAME = "request.contextPath";
  private SWTBotEclipseEditor jspGreetingPageEditor;
  private SWTBotEclipseEditor jspInputNamePageEditor;
  private String greetingPageOrigText;
  private String inputNamePageOrigText;
  
	public EditingELValueTest() {
		super();
		botExt = new SWTBotExt();
	}
	@Override
	public void setUp() throws Exception {
	  super.setUp();
    eclipse.maximizeActiveShell();
	}
	/**
	 * Tests editing of EL value 
	 */
	public void testEditingELValue(){
    openPage(EditingELValueTest.GREETING_PAGE_NAME,VPEAutoTestCase.FACELETS_TEST_PROJECT_NAME);
    openPage(EditingELValueTest.INPUT_NAME_PAGE_NAME,VPEAutoTestCase.FACELETS_TEST_PROJECT_NAME);
    jspGreetingPageEditor = botExt.editorByTitle(EditingELValueTest.GREETING_PAGE_NAME).toTextEditor();
    greetingPageOrigText = jspGreetingPageEditor.getText();
    jspInputNamePageEditor = botExt.editorByTitle(EditingELValueTest.INPUT_NAME_PAGE_NAME).toTextEditor();
    inputNamePageOrigText = jspGreetingPageEditor.getText();
    jspGreetingPageEditor.setText(jspGreetingPageEditor.getText()
        .replaceFirst("/templates/common.xhtml", "#{" + EditingELValueTest.EL_VARIABLE_NAME + "}templates/common.xhtml"));
    jspGreetingPageEditor.save();
    bot.sleep(Timing.time2S());
    assertVisualEditorContainsNodeWithValue(new SWTBotWebBrowser(EditingELValueTest.GREETING_PAGE_NAME,botExt),
        "Template file is not found: \"templates/common.xhtml\"", 
        EditingELValueTest.GREETING_PAGE_NAME);
    jspInputNamePageEditor.setText(jspInputNamePageEditor.getText()
        .replaceFirst("/templates/common.xhtml", "#{" + EditingELValueTest.EL_VARIABLE_NAME + "}templates/common.xhtml"));
    jspInputNamePageEditor.save();
    bot.sleep(Timing.time2S());
    assertVisualEditorContainsNodeWithValue(new SWTBotWebBrowser(EditingELValueTest.INPUT_NAME_PAGE_NAME,botExt),
        "Template file is not found: \"templates/common.xhtml\"", 
        EditingELValueTest.INPUT_NAME_PAGE_NAME);
    // Opens Page Design Options Dialog
    util.waitForToolbarButtonWithTooltipIsFound(PAGE_DESIGN, Timing.time10S());
    bot.toolbarButtonWithTooltip(PAGE_DESIGN).click();
    SWTBot optionsDialogBot = bot.shell(IDELabel.Shell.PAGE_DESIGN_OPTIONS).activate().bot();
    optionsDialogBot.tabItem(IDELabel.PageDesignOptionsDialog.SUBSTITUTED_EL_EXPRESSIONS_TAB).activate();
    optionsDialogBot.button(IDELabel.Button.ADD_WITHOUT_DOTS).click();
    SWTBot addELReferenceDialogBot = optionsDialogBot.shell(IDELabel.Shell.ADD_EL_REFERENCE).activate().bot();
    addELReferenceDialogBot.textWithLabel(IDELabel.PageDesignOptionsDialog.SUBSTITUTED_EL_EXPRESSIONS_VALUE)
      .setText("/");
    addELReferenceDialogBot.textWithLabel(IDELabel.PageDesignOptionsDialog.SUBSTITUTED_EL_EXPRESSIONS_EL_NAME)
      .setText(EditingELValueTest.EL_VARIABLE_NAME);
    addELReferenceDialogBot.button(IDELabel.Button.FINISH).click();
    optionsDialogBot.button(IDELabel.Button.OK).click();
    jspGreetingPageEditor.close();
    jspInputNamePageEditor.close();
    openPage(EditingELValueTest.GREETING_PAGE_NAME,VPEAutoTestCase.FACELETS_TEST_PROJECT_NAME);
    openPage(EditingELValueTest.INPUT_NAME_PAGE_NAME,VPEAutoTestCase.FACELETS_TEST_PROJECT_NAME);
    // Checks Visual Representation of Pages
    SWTBotWebBrowser webBrowserGreetingPage = new SWTBotWebBrowser(EditingELValueTest.GREETING_PAGE_NAME,botExt);
    assertVisualEditorNotContainNodeWithValue(webBrowserGreetingPage,
        " Template file is not found: \"#{" + EditingELValueTest.EL_VARIABLE_NAME + "}templates/common.xhtml\"", 
        EditingELValueTest.GREETING_PAGE_NAME);
    SWTBotWebBrowser webBrowserInputNamePage = new SWTBotWebBrowser(EditingELValueTest.INPUT_NAME_PAGE_NAME,botExt);
    assertVisualEditorNotContainNodeWithValue(webBrowserInputNamePage,
        " Template file is not found: \"#{" + EditingELValueTest.EL_VARIABLE_NAME + "}templates/common.xhtml\"", 
        EditingELValueTest.INPUT_NAME_PAGE_NAME);
    // Delete EL Variable from Pages
    jspGreetingPageEditor = botExt.editorByTitle(EditingELValueTest.GREETING_PAGE_NAME).toTextEditor();
    jspGreetingPageEditor.setText(jspGreetingPageEditor.getText()
      .replaceFirst("\\#\\{" + EditingELValueTest.EL_VARIABLE_NAME + "\\}", "/"));
    jspGreetingPageEditor.save();
    bot.sleep(Timing.time2S());
    assertVisualEditorNotContainNodeWithValue(webBrowserGreetingPage,
        " Template file is not found: \"#{" + EditingELValueTest.EL_VARIABLE_NAME + "}templates/common.xhtml\"", 
        EditingELValueTest.GREETING_PAGE_NAME);
    jspInputNamePageEditor = botExt.editorByTitle(EditingELValueTest.INPUT_NAME_PAGE_NAME).toTextEditor();
    jspInputNamePageEditor.setText(jspInputNamePageEditor.getText()
        .replaceFirst("\\#\\{" + EditingELValueTest.EL_VARIABLE_NAME + "\\}", "/"));
    jspInputNamePageEditor.save();
    bot.sleep(Timing.time2S());    
    assertVisualEditorNotContainNodeWithValue(webBrowserInputNamePage,
        " Template file is not found: \"#{" + EditingELValueTest.EL_VARIABLE_NAME + "}templates/common.xhtml\"", 
        EditingELValueTest.INPUT_NAME_PAGE_NAME);
    bot.toolbarButtonWithTooltip(PAGE_DESIGN).click();
    optionsDialogBot = bot.shell(IDELabel.Shell.PAGE_DESIGN_OPTIONS).activate().bot();
    optionsDialogBot.tabItem(IDELabel.PageDesignOptionsDialog.SUBSTITUTED_EL_EXPRESSIONS_TAB).activate();
    assertTrue ("EL Substitution for EL Name " + EditingELValueTest.EL_VARIABLE_NAME + " is not defined.",
        SWTEclipseExt.isItemInTableColumn(optionsDialogBot.table(), EditingELValueTest.EL_VARIABLE_NAME, 1));
    optionsDialogBot.button(IDELabel.Button.OK).click();
  }

	@Override
	protected void closeUnuseDialogs() {

	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}
  @Override
public void tearDown() throws Exception {
    
    if (jspGreetingPageEditor != null){
      jspGreetingPageEditor.show();
      deleteAllELSubstitutions();
      jspGreetingPageEditor.setText(greetingPageOrigText);
      jspGreetingPageEditor.saveAndClose();
    }
    if (jspInputNamePageEditor != null){
      jspInputNamePageEditor.show();
      deleteAllELSubstitutions();
      jspInputNamePageEditor.setText(inputNamePageOrigText);
      jspInputNamePageEditor.saveAndClose();  
    }
    
    super.tearDown();
  } 
}
