/*******************************************************************************

 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.palette;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotBrowser;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotStyledText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;

/**
 * Tests of jQuery Mobile Palette  
 * @author vlado pakan
 *
 */
public class MobilePaletteTest extends VPEAutoTestCase {
  private SWTBotExt botExt = null;
  private static final String JQUERY_MOBILE_PALLETE = "jQuery Mobile";
  
  public MobilePaletteTest() {
    super();
    botExt = new SWTBotExt();
  }
  /**
   * Checks if Mobile Palette is not present for JSF project 
   */
	public void xxtestMobilePaletteNotPresentForJSFProject(){
	  openPage();
    openPalette();	
    
    assertFalse("Palette should not contain item: " + MobilePaletteTest.JQUERY_MOBILE_PALLETE, SWTBotWebBrowser.paletteContainsRootPaletteCotnainer(botExt,MobilePaletteTest.JQUERY_MOBILE_PALLETE));
	}
	/**
   * Checks if Mobile Palette is present for HTML project and inserting tags works 
   */
  public void testMobilePalette(){
    createDynamicWebProject(VPEAutoTestCase.DYNAMIC_WEB_TEST_PROJECT_NAME);
    final String htmlPageName = "MobilePaletteTestpage.html";
    createHtmlPage(htmlPageName, VPEAutoTestCase.DYNAMIC_WEB_TEST_PROJECT_NAME,"WebContent");
    SWTBotEclipseEditor htmlPage = eclipse.openFile(VPEAutoTestCase.DYNAMIC_WEB_TEST_PROJECT_NAME, 
        "WebContent",
        htmlPageName).toTextEditor();
    htmlPage.selectLine(0);
    htmlPage.insertText("<!DOCTYPE html> ");
    htmlPage.save();
    htmlPage.close();
    eclipse.openFile(VPEAutoTestCase.DYNAMIC_WEB_TEST_PROJECT_NAME, 
        "WebContent",
        htmlPageName);
    openPalette();  
    
    assertTrue("Palette has to contain item: " + MobilePaletteTest.JQUERY_MOBILE_PALLETE, SWTBotWebBrowser.paletteContainsRootPaletteCotnainer(botExt,MobilePaletteTest.JQUERY_MOBILE_PALLETE));
    // insert tag
    SWTJBTExt.selectTextInSourcePane(botExt,
        htmlPageName,
        "<body>",
        6, 
        0,
        0);
    SWTBotWebBrowser.activatePaletteTool(botExt, "Checkbox");
    bot.shell(IDELabel.Shell.INSERT_TAG).activate();
    // check init values
    SWTBotText txLabel = bot.textWithLabel("Label:");
    assertText("I agree",txLabel);
    SWTBotStyledText stCode = bot.styledText("<label><input type=\"checkbox\" name=\"checkbox-1\" id=\"checkbox-1\"/>I \nagree</label>\n");
    SWTBotCheckBox chbMini = bot.checkBoxWithLabel("Mini:");
    assertFalse("Checkbox Mini is checked", chbMini.isChecked());
    SWTBotCombo cmbTheme = bot.comboBoxWithLabel("Theme:");
    assertText("",cmbTheme);
    SWTBotBrowser brPreview = bot.browser();
    assertTextContains("<input type=\"checkbox\" id=\"checkbox-1\" name=\"checkbox-1\" />", brPreview);
    // make changes to attributes
    final String labelValue="##NEW_LABEL##";
    txLabel.setText(labelValue);
    chbMini.click();
    cmbTheme.setText("a");
    bot.sleep(Timing.time3S());
    assertTextContains("<input type=\"checkbox\" data-theme=\"a\" data-mini=\"true\" class=\"custom\" id=\"checkbox-1\" name=\"checkbox-1\" />", brPreview);
    assertEquals("<inputtype=\"checkbox\"name=\"checkbox-1\"id=\"checkbox-1\"class=\"custom\"data-mini=\"true\"data-theme=\"a\"/><labelfor=\"checkbox-1\">"
        + labelValue + "</label>",
        stCode.getText().replaceAll("\n", "").replaceAll(" ", ""));
    bot.button("Hide Preview").click();
    bot.sleep(Timing.time3S());
    assertFalse(brPreview.isVisible());
    assertFalse(stCode.isVisible());
    bot.button(IDELabel.Button.FINISH).click();
    bot.sleep(Timing.time3S());
    String htmlSourceText = bot.editorByTitle(htmlPageName).toTextEditor().getText();
    assertContains("<body><input type=\"checkbox\" name=\"checkbox-1\" id=\"checkbox-1\" class=\"custom\" data-mini=\"true\" data-theme=\"a\"/>", htmlSourceText);
    assertContains("<label for=\"checkbox-1\">" + labelValue + "</label>", htmlSourceText);
  }
	@Override
	protected void closeUnuseDialogs() {
    bot.button(IDELabel.Button.CANCEL).click();
	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return bot.activeShell().getText().equals(IDELabel.Shell.INSERT_TAG);
	}
	
}
