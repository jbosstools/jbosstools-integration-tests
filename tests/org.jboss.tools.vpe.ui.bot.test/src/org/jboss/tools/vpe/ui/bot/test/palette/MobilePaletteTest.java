/*******************************************************************************
 * Copyright (c) 2013 - 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.palette;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.core.handler.WidgetHandler;
import org.jboss.reddeer.swt.api.Browser;
import org.jboss.reddeer.swt.api.StyledText;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.LabeledCheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;

/**
 * Tests of jQuery Mobile Palette
 * 
 * @author vlado pakan
 *
 */
public class MobilePaletteTest extends VPEAutoTestCase {

	private static final String JQUERY_MOBILE_PALLETE = "jQuery Mobile";

	/**
	 * Checks if Mobile Palette is not present for JSF project
	 */
	@Test
	public void testMobilePaletteNotPresentForJSFProject() {
		openPage();
		openPalette();
		assertFalse("Palette should not contain item: " + MobilePaletteTest.JQUERY_MOBILE_PALLETE,
				SWTBotWebBrowser.paletteContainsRootPaletteCotnainer(MobilePaletteTest.JQUERY_MOBILE_PALLETE));
	}

	/**
	 * Checks if Mobile Palette is present for HTML project and inserting tags
	 * works
	 */
	@Test
	public void testMobilePalette() {
		createDynamicWebProject(VPEAutoTestCase.DYNAMIC_WEB_TEST_PROJECT_NAME);
		final String htmlPageName = "MobilePaletteTestpage.html";
		createHtmlPage(htmlPageName, VPEAutoTestCase.DYNAMIC_WEB_TEST_PROJECT_NAME, "WebContent");
		packageExplorer.open();
		packageExplorer.getProject(VPEAutoTestCase.DYNAMIC_WEB_TEST_PROJECT_NAME)
				.getProjectItem("WebContent", htmlPageName).open();
		TextEditor htmlPage = new TextEditor(htmlPageName);
		htmlPage.insertText(0, "<!DOCTYPE html> \n");
		htmlPage.save();
		htmlPage.close();
		packageExplorer.getProject(VPEAutoTestCase.DYNAMIC_WEB_TEST_PROJECT_NAME)
				.getProjectItem("WebContent", htmlPageName).open();
		openPalette();

		assertTrue("Palette has to contain item: " + MobilePaletteTest.JQUERY_MOBILE_PALLETE,
				SWTBotWebBrowser.paletteContainsRootPaletteCotnainer(MobilePaletteTest.JQUERY_MOBILE_PALLETE));
		// insert tag
		htmlPage = new TextEditor(htmlPageName);
		htmlPage.setCursorPosition(htmlPage.getPositionOfText("<body>") + 6);
		SWTBotWebBrowser.activatePaletteTool("Checkbox");
		new DefaultShell("Insert Tag");
		// check init values
		Text txLabel = new LabeledText("Label:");
		assertTrue("Lable should have value 'I agree'", txLabel.getText().equals("I agree"));
		StyledText stCode = new DefaultStyledText("<ion-checkbox>I agree</ion-checkbox>\n");
		CheckBox chbChecked = new LabeledCheckBox("Checked:");
		assertFalse("Checkbox Checked is checked", chbChecked.isChecked());
		Browser brPreview = new InternalBrowser();
		assertTrue("Browser should contain '<ion-checkbox>I agree</ion-checkbox>'",
				brPreview.getText().contains("<ion-checkbox>I agree</ion-checkbox>"));
		// make changes to attributes
		final String labelValue = "##NEW_LABEL##";
		txLabel.setText(labelValue);
		chbChecked.toggle(true);
		AbstractWait.sleep(TimePeriod.getCustom(3));
		assertTrue("Browser should contain '<ion-checkbox ng-checked=\"true\">" + labelValue + "</ion-checkbox>'",
				brPreview.getText().contains("<ion-checkbox ng-checked=\"true\">" + labelValue + "</ion-checkbox>"));
		assertEquals("<ion-checkboxng-checked=\"true\">" + labelValue + "</ion-checkbox>",
				stCode.getText().replaceAll("\n", "").replaceAll(" ", ""));
		new PushButton("Hide Preview").click();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		assertFalse(WidgetHandler.getInstance().isVisible(brPreview.getSWTWidget()));
		assertFalse(WidgetHandler.getInstance().isVisible(stCode.getSWTWidget()));
		new FinishButton().click();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		String htmlSourceText = bot.editorByTitle(htmlPageName).toTextEditor().getText().replaceAll("\n", "")
				.replaceAll("\t", "").replaceAll("\r", "");
		assertContains("<ion-checkbox ng-checked=\"true\">" + labelValue + "</ion-checkbox>", htmlSourceText);
	}

}
