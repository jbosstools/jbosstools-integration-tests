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
package org.jboss.tools.vpe.ui.bot.test.editor.preferences;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
/**
 * Prompt for Tag Attributes during Tag Insert test
 * @author vlado pakan
 *
 */
public class PromptForTagAttributesDuringTagInsertTest extends PreferencesTestCase{
  
  private static final String TEST_PAGE_NAME = "PromptForTagAttributes.jsp";
  private SWTBotEclipseEditor jspEditor;
  private SWTBotWebBrowser webBrowser;
  private SWTBotExt botExt = null;
  
  public PromptForTagAttributesDuringTagInsertTest() {
    super();
    botExt = new SWTBotExt();
  }
  @Override
  public void setUp() throws Exception {
    super.setUp();
    eclipse.maximizeActiveShell();
    createJspPage(PromptForTagAttributesDuringTagInsertTest.TEST_PAGE_NAME);
    jspEditor = botExt.editorByTitle(PromptForTagAttributesDuringTagInsertTest.TEST_PAGE_NAME).toTextEditor();
    webBrowser = new SWTBotWebBrowser(PromptForTagAttributesDuringTagInsertTest.TEST_PAGE_NAME,botExt);
  }
  /**
   * Prompt for Tag Attributes during Tag Insert test
   */
	public void testPromptForTagAttributesDuringTagInsert(){
	  jspEditor.setText("");
	  jspEditor.save();
	  bot.sleep(Timing.time2S());
	  // Check Ask for Tag Attributes during Insert
		bot.toolbarButtonWithTooltip(PREF_TOOLTIP).click();
		bot.shell(PREF_FILTER_SHELL_TITLE).activate();
		SWTBotCheckBox chbAskForAttributes = bot.checkBox(ASK_FOR_ATTRIBUTES);
	  if (!chbAskForAttributes.isChecked()) {
	    chbAskForAttributes.click();
    }
		bot.button("OK").click();
		
		webBrowser.activatePaletteTool("outputText");
    SWTBot dialogBot = null;
    try{
      dialogBot = botExt.shell(IDELabel.Shell.INSERT_TAG).activate().bot();
      dialogBot.button(IDELabel.Button.FINISH).click();
    } catch (WidgetNotFoundException wnfe){
      // do nothing
    }
    assertNotNull("Dialog asking for Tag Attributes during Insert was not opened but it has to.",dialogBot);
  	// Uncheck Ask for Tag Attributes during Insert
		bot.toolbarButtonWithTooltip(PREF_TOOLTIP).click();
    bot.shell(PREF_FILTER_SHELL_TITLE).activate();
    chbAskForAttributes = bot.checkBox(ASK_FOR_ATTRIBUTES);
    chbAskForAttributes.click();
    bot.button("OK").click();
    webBrowser.activatePaletteTool("outputText");
    dialogBot = null;
    try{
      dialogBot = botExt.shell(IDELabel.Shell.INSERT_TAG).activate().bot();
      dialogBot.button(IDELabel.Button.FINISH).click();
    } catch (WidgetNotFoundException wnfe){
      // do nothing
    }
    assertNull("Dialog asking for Tag Attributes during Insert was opened but it must not to be.",dialogBot);
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
}
