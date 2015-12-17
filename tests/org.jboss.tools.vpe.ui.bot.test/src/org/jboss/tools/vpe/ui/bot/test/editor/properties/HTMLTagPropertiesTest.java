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
package org.jboss.tools.vpe.ui.bot.test.editor.properties;

import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.swt.api.TabItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.junit.Test;

/**
 * Tests HTML tag properties displayed within Properties View
 * 
 * @author vlado pakan
 *
 */
public class HTMLTagPropertiesTest extends VPEAutoTestCase {
	/**
	 * Checks properties of selected HTML tag displayed within PropertiesView
	 */
	@Test
	public void testHTMLTagProperties() {
		PropertiesView propertiesView = new PropertiesView();
		propertiesView.open();
		createDynamicWebProject(VPEAutoTestCase.DYNAMIC_WEB_TEST_PROJECT_NAME);
		final String htmlPageName = "HTMLTagPropertiesTest.html";
		createHtmlPage(htmlPageName, VPEAutoTestCase.DYNAMIC_WEB_TEST_PROJECT_NAME, "WebContent");
		TextEditor htmlPageEditor = new TextEditor(htmlPageName);
		htmlPageEditor.setText("<!DOCTYPE html>\n" + "<html>\n" + " <head>\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n"
				+ "    <link rel=\"stylesheet\" href=\"http://code.jquery.com/mobile/1.3.1/jquery.mobile-1.3.1.min.css\"/>\n"
				+ "    <script src=\"http://code.jquery.com/jquery-1.9.1.min.js\"></script>\n"
				+ "    <script src=\"http://code.jquery.com/mobile/1.3.1/jquery.mobile-1.3.1.min.js\"></script>\n"
				+ " </head>\n" + " <body>\n" + "  <div data-role=\"fieldcontain\">\n"
				+ "  <fieldset data-role=\"collapsible\" id=\"collapsible-1\">\n" + "    <legend>Header</legend>\n"
				+ "    <p>Collapsible content.</p>\n" + "  </fieldset>\n" + "  </div>\n"
				+ "  <label><input type=\"checkbox\" name=\"checkbox-1\" id=\"checkbox-1\"/>I agree</label>\n"
				+ " </body>\n" + "</html>");
		htmlPageEditor.save();
		htmlPageEditor.close();
		packageExplorer.getProject(VPEAutoTestCase.DYNAMIC_WEB_TEST_PROJECT_NAME)
				.getProjectItem("WebContent", htmlPageName).open();
		htmlPageEditor = new TextEditor(htmlPageName);
		htmlPageEditor.setCursorPosition(htmlPageEditor.getPositionOfText("<fieldset ") + 6);
		propertiesView.activate();
		TabItem tiAll = new DefaultTabItem("All");
		tiAll.activate();
		HTMLTagPropertiesTest.checkProperty(propertiesView, "id");
		HTMLTagPropertiesTest.checkProperty(propertiesView, "data-enhance");
		HTMLTagPropertiesTest.checkProperty(propertiesView, "data-ajax");
		TabItem tiHtml = new DefaultTabItem("HTML");
		tiHtml.activate();
		assertTrue("ID attribute has to have value 'collapsible-1'",
				new LabeledText("ID:").getText().equals("collapsible-1"));
		TabItem tiJQuery = new DefaultTabItem("jQuery");
		tiJQuery.activate();
		assertTrue("Data Role attribute has to have value 'collapsible'",
				new LabeledCombo("Data Role:").getText().equals("collapsible"));
		// Check properties for <input> tag
		htmlPageEditor.setCursorPosition(htmlPageEditor.getPositionOfText("<input ") + 3);
		propertiesView.activate();
		tiAll.activate();
		HTMLTagPropertiesTest.checkProperty(propertiesView, "id");
		HTMLTagPropertiesTest.checkProperty(propertiesView, "data-mini");
		HTMLTagPropertiesTest.checkProperty(propertiesView, "data-role");
		tiHtml.activate();
		assertTrue("ID attribute has to have value 'checkbox-1'",
				new LabeledText("ID:").getText().equals("checkbox-1"));
		assertTrue("Name attribute has to have value 'checkbox-1'",
				new LabeledText("Name:").getText().equals("checkbox-1"));
		assertTrue("Type attribute has to have value 'checkbox'",
				new LabeledCombo("Type:").getText().equals("checkbox"));
		tiJQuery.activate();
		assertTrue("Data Role attribute has to have empty value", new LabeledCombo("Data Role:").getText().equals(""));

	}

	private static void checkProperty(PropertiesView propertiesView, String propName) {
		try {
			new DefaultTableItem(propName);
		} catch (SWTLayerException swtle) {
			fail("Unable to find property: " + propName);
		}
	}
}
