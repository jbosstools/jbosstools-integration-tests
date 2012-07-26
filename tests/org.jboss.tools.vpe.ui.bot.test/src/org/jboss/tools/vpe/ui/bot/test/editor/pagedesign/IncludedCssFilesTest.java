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

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.hamcrest.Matcher;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.JobName;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

/**
 * Tests functionality of Included CSS Files tab page of Page Design Options Dialog 
 * @author vlado pakan
 *
 */
public class IncludedCssFilesTest extends PageDesignTestCase {
  
  private static final String CSS_FILE_NAME = "includedCssFileTest.css";
  private static final String HTML_FILE_NAME = "includedCssFileTest.html";
  
  private SWTBot addCssReferenceDialogBot = null;
  private SWTBot optionsDialogBot  = null;
    
  public void testIncludedCssFiles() throws IOException{
    SWTBotTree tree = packageExplorer.show().bot().tree();
    tree.expandNode(VPEAutoTestCase.JBT_TEST_PROJECT_NAME)
      .expandNode("WebContent")
      .getNode("pages")
      .select();
    // add CSS File
    open.newObject(ActionItem.NewObject.WebCSS.LABEL);
    bot.shell(IDELabel.Shell.NEW_CSS_FILE).activate(); //$NON-NLS-1$
    bot.textWithLabel(IDELabel.NewCSSWizard.FILE_NAME).setText(IncludedCssFilesTest.CSS_FILE_NAME); //$NON-NLS-1$
    bot.button(IDELabel.Button.FINISH).click(); //$NON-NLS-1$
    if (getException() != null && getException() instanceof NullPointerException){
      setException(null);
    }
    bot.sleep(Timing.time3S());
    util.waitForJobs(JobName.BUILDING_WS);
    SWTBotEditor cssEditor = bot.editorByTitle(IncludedCssFilesTest.CSS_FILE_NAME);
    cssEditor.toTextEditor().setText("h1 {\n" + 
      "color: Red\n" +
      "}");
    cssEditor.saveAndClose();
    // add HTML File
    open.newObject(ActionItem.NewObject.WebHTMLPage.LABEL);
    bot.shell(IDELabel.Shell.NEW_HTML_FILE).activate(); //$NON-NLS-1$
    bot.textWithLabel(IDELabel.NewHTMLWizard.FILE_NAME).setText(IncludedCssFilesTest.HTML_FILE_NAME); //$NON-NLS-1$
    bot.button(IDELabel.Button.FINISH).click(); //$NON-NLS-1$
    bot.sleep(Timing.time3S());
    util.waitForJobs(JobName.BUILDING_WS);
    SWTBotEditor htmlEditor = bot.editorByTitle(IncludedCssFilesTest.HTML_FILE_NAME);
    htmlEditor.toTextEditor().setText("<html>\n" +
      "  <body>\n" +
      "    <h1>Title</h1>\n" +
      "  </body>\n" +
      "</html>");
    htmlEditor.save();
    bot.sleep(Timing.time3S());
    util.waitForJobs(JobName.BUILDING_WS);
    // add CSS File Reference
    util.waitForToolbarButtonWithTooltipIsFound(PAGE_DESIGN, Timing.time10S());
    bot.toolbarButtonWithTooltip(PAGE_DESIGN).click();
    optionsDialogBot = bot.shell(IDELabel.Shell.PAGE_DESIGN_OPTIONS).activate().bot();
    optionsDialogBot.tabItem(IDELabel.PageDesignOptionsDialog.INCLUDED_CSS_FILES_TAB).activate();
    optionsDialogBot.button(IDELabel.Button.ADD_WITHOUT_DOTS).click();
    addCssReferenceDialogBot = optionsDialogBot.shell(IDELabel.Shell.ADD_CSS_REFERENCE).activate().bot();
    addCssReferenceDialogBot.textWithLabel(IDELabel.PageDesignOptionsDialog.INCLUDED_CSS_FILES_CSS_FILE_PATH)
      .setText(SWTUtilExt.getPathToProject(VPEAutoTestCase.JBT_TEST_PROJECT_NAME) + File.separator +
          "WebContent" + File.separator +
          "pages" + File.separator +
          IncludedCssFilesTest.CSS_FILE_NAME);
    addCssReferenceDialogBot.button(IDELabel.Button.FINISH).click();
    addCssReferenceDialogBot = null;
    optionsDialogBot.button(IDELabel.Button.OK).click();
    optionsDialogBot = null;
    SWTBotEditorExt botEditorExt = new SWTBotExt().swtBotEditorExtByTitle(IncludedCssFilesTest.HTML_FILE_NAME);
    botEditorExt.selectPage(IDELabel.VisualPageEditor.PREVIEW_TAB_LABEL);
    Matcher<Browser> matcher = widgetOfType(Browser.class);
    @SuppressWarnings("unchecked")
    List<Browser> browsers = ((List<Browser>)botEditorExt.bot().widgets(matcher));
    Browser browser0 = browsers.get(0);
    Browser browser1 = browsers.get(1);
    String browser0Text = SWTUtilExt.invokeMethod(browser0, "getText");
    final String textToContain = "h1 {color: Red}";
    // browser0 is editable browser displayed on page Visual/Source
    boolean firstBrowserIsEditable = browser0Text.contains("dragIcon");
    if (firstBrowserIsEditable) {
      assertTrue("Preview Browser displayed Web Page Incorretly. There is no H1 tag with Red Color",
          SWTUtilExt.invokeMethod(browser1, "getText").contains(textToContain));
    } else {
      assertTrue("Preview Browser displayed Web Page Incorretly. There is no H1 tag with Red Color",
          browser0Text.contains(textToContain));    
    }
    // Test Visual Interpretation of CSS in Visual/Source Pane
    botEditorExt.selectPage(IDELabel.VisualPageEditor.VISUAL_SOURCE_TAB_LABEL);
    if (firstBrowserIsEditable) {
      assertTrue("Preview Browser displayed Web Page Incorretly. There is no H1 tag with Red Color",
          SWTUtilExt.invokeMethod(browser1, "getText").contains(textToContain));
    } else {
      assertTrue("Preview Browser displayed Web Page Incorretly. There is no H1 tag with Red Color",
          SWTUtilExt.invokeMethod(browser0, "getText").contains(textToContain));    
    }
    bot.toolbarButtonWithTooltip(PAGE_DESIGN).click();
    optionsDialogBot = bot.shell(IDELabel.Shell.PAGE_DESIGN_OPTIONS).activate().bot();
    optionsDialogBot.tabItem(IDELabel.PageDesignOptionsDialog.INCLUDED_CSS_FILES_TAB).activate();
    optionsDialogBot.table().select(0);
    optionsDialogBot.button(IDELabel.Button.REMOVE).click();
    optionsDialogBot.button(IDELabel.Button.OK).click();
    optionsDialogBot = null;
    htmlEditor.close();
	}
	
	@Override
	protected void closeUnuseDialogs() {
    if (addCssReferenceDialogBot != null){
      addCssReferenceDialogBot.button(IDELabel.Button.CANCEL).click();
      addCssReferenceDialogBot = null;
    }
    if (optionsDialogBot != null){
      optionsDialogBot.button(IDELabel.Button.OK).click();
      optionsDialogBot = null;
    }
	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return optionsDialogBot != null
		    || addCssReferenceDialogBot != null;
	}
  
}
