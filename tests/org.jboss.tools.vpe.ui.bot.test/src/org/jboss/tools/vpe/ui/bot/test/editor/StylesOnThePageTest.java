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
package org.jboss.tools.vpe.ui.bot.test.editor;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import java.io.IOException;
import java.util.List;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
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
 * Tests functionality of styles defined within page 
 * @author vlado pakan
 *
 */
public class StylesOnThePageTest extends VPEEditorTestCase {
  
  private static final String JSP_FILE_NAME = "stylesOnThePageTest.jsp";
  
  public void testStylesOnThePage() throws IOException{
    SWTBotTree tree = packageExplorer.show().bot().tree();
    tree.expandNode(VPEAutoTestCase.JBT_TEST_PROJECT_NAME)
      .expandNode("WebContent")
      .getNode("pages")
      .select();
    open.newObject(ActionItem.NewObject.WebJSPFile.LABEL);
    bot.shell(IDELabel.Shell.NEW_JSP_FILE).activate(); //$NON-NLS-1$
    bot.textWithLabel(ActionItem.NewObject.WebJSPFile.TEXT_FILE_NAME).setText(StylesOnThePageTest.JSP_FILE_NAME); //$NON-NLS-1$
    bot.button(IDELabel.Button.FINISH).click(); //$NON-NLS-1$
    bot.sleep(Timing.time3S());
    util.waitForJobs(JobName.BUILDING_WS);
    SWTBotEditor jspEditor = bot.editorByTitle(StylesOnThePageTest.JSP_FILE_NAME);
    String oldStyle = "background: lime;color: red;";
    jspEditor.toTextEditor().setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\"%>\n" +
        "<html>\n" +
        "  <head>\n" +
        "    <style type=\"text/css\">h3 {" +
        oldStyle +
        "}</style>\n" +
        "  </head>\n" +
        "  <body>\n" +
        "    <h3>\n" +
        "      Title Level 3\n" +
        "    </h3>\n" +
        "  </body>\n" +
        "</html>");
    jspEditor.save();
    bot.sleep(Timing.time3S());
    util.waitForJobs(JobName.BUILDING_WS);
    // add CSS File Reference
    bot.sleep(Timing.time3S());
    SWTBotEditorExt botEditorExt = new SWTBotExt().swtBotEditorExtByTitle(StylesOnThePageTest.JSP_FILE_NAME);
    botEditorExt.selectPage(IDELabel.VisualPageEditor.PREVIEW_TAB_LABEL);
    Matcher<Browser> matcher = widgetOfType(Browser.class);
    @SuppressWarnings("unchecked")
    List<Browser> browsers = ((List<Browser>)botEditorExt.bot().widgets(matcher));
    Browser visualSourcePaneBrowser = browsers.get(0);
    Browser previewPaneBrowser = browsers.get(1);
    // Test if current style is applied on Visual/Source pane
    String textToContain = "<STYLE>h3 {" + oldStyle + "}</STYLE>";
    String browserText = SWTUtilExt.invokeMethod(visualSourcePaneBrowser, "getText").toLowerCase(); 
    assertTrue("Browser on Visual/>Source pane has to contain text " + textToContain +
        " but it doesn't.\n" +
        "Browser text is:\n" +
        browserText,
        browserText.contains(textToContain.toLowerCase()));
    textToContain = "<H3 style=\"-moz-user-modify:";
    assertTrue("Browser on Visual/>Source pane has to contain text " + textToContain +
        " but it doesn't.\n" +
        "Browser text is:\n" +
        browserText,
        browserText.contains(textToContain.toLowerCase()));
    //  Test if current style is applied on Preview pane
    textToContain = "<STYLE>h3 {" + oldStyle + "}</STYLE>";
    browserText = SWTUtilExt.invokeMethod(previewPaneBrowser, "getText").toLowerCase();
    assertTrue("Browser on Preview pane has to contain text " + textToContain +
        " but it doesn't.\n" +
        "Browser text is:\n" +
        browserText,
        browserText.contains(textToContain.toLowerCase()));
    textToContain = "<H3 style=\"-moz-user-modify:";
    assertTrue("Browser on Preview pane has to contain text " + textToContain +
        " but it doesn't.\n" +
        "Browser text is:\n" +
        browserText,
        browserText.contains(textToContain.toLowerCase()));
    // Apply new style
    String newStyle = "background: black;color: white;";
    botEditorExt.selectPage(IDELabel.VisualPageEditor.VISUAL_SOURCE_TAB_LABEL);
    jspEditor.toTextEditor()
      .setText(jspEditor.toTextEditor().getText().replaceFirst(oldStyle, newStyle));
    jspEditor.save();
    bot.sleep(Timing.time3S());
    botEditorExt.selectPage(IDELabel.VisualPageEditor.PREVIEW_TAB_LABEL);
    // Test if current style is applied on Visual/Source pane
    textToContain = "<STYLE>h3 {" + newStyle + "}</STYLE>";
    browserText = SWTUtilExt.invokeMethod(visualSourcePaneBrowser, "getText").toLowerCase();
    assertTrue("Browser on Visual/>Source pane has to contain text " + textToContain +
        " but it doesn't.\n" +
        "Browser text is:\n" +
        browserText,
        browserText.contains(textToContain.toLowerCase()));
    textToContain = "<H3 style=\"-moz-user-modify:";
    assertTrue("Browser on Visual/>Source pane has to contain text " + textToContain +
        " but it doesn't.\n" +
        "Browser text is:\n" +
        browserText,
        browserText.contains(textToContain.toLowerCase()));
    //  Test if current style is applied on Preview pane
    textToContain = "<STYLE>h3 {" + newStyle + "}</STYLE>";
    browserText = SWTUtilExt.invokeMethod(previewPaneBrowser, "getText").toLowerCase();
    assertTrue("Browser on Preview pane has to contain text " + textToContain +
        " but it doesn't.\n" +
        "Browser text is:\n" +
        browserText,
        browserText.contains(textToContain.toLowerCase()));
    textToContain = "<H3 style=\"-moz-user-modify:";
    assertTrue("Browser on Preview pane has to contain text " + textToContain +
        " but it doesn't.\n" +
        "Browser text is:\n" +
        browserText,
        browserText.contains(textToContain.toLowerCase()));

    botEditorExt.selectPage(IDELabel.VisualPageEditor.VISUAL_SOURCE_TAB_LABEL);
    jspEditor.close();
	}
	
	@Override
	protected void closeUnuseDialogs() {
	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}
  
}
