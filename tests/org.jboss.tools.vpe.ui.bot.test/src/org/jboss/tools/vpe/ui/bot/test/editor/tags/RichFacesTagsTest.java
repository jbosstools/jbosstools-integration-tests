/*******************************************************************************

 * Copyright (c) 2007-2011 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor.tags;

import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
/**
 * Tests Rich Faces Tags behavior 
 * @author vlado pakan
 *
 */
public class RichFacesTagsTest extends VPEEditorTestCase {
  
  private static final String TEST_PAGE_NAME = "RichFacesTagsTest.jsp";
  
  private SWTBotEditorExt jspEditor;
  private SWTBotWebBrowser webBrowser;
  private SWTBotExt botExt;
  
	public RichFacesTagsTest() {
		super();
		botExt = new SWTBotExt();
	}
	@Override
	protected void setUp() throws Exception {
	  super.setUp();
    eclipse.maximizeActiveShell();
    createJspPage(RichFacesTagsTest.TEST_PAGE_NAME);
    jspEditor = botExt.swtBotEditorExtByTitle(RichFacesTagsTest.TEST_PAGE_NAME);
    webBrowser = new SWTBotWebBrowser(RichFacesTagsTest.TEST_PAGE_NAME,botExt);
	}

  @Override
	protected void closeUnuseDialogs() {

	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}
  @Override
  protected void tearDown() throws Exception {
    jspEditor.close();
    super.tearDown();
  }
  /**
   * Tests rich:comboBox Tag
   */
  public void testComboBoxTag(){
    final String defaultLabel = "DefaultLabel";
    jspEditor.setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
        "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" +
        "<%@ taglib uri=\"http://richfaces.org/rich\" prefix=\"rich\" %>\n" +
        "<html>\n" +
        "  <head>\n" +
        "  </head>\n" +
        "  <body>\n" +
        "    <f:view>\n" +
        "      <h:form>\n" +
        "        <rich:comboBox id=\"comboBox\" defaultLabel=\"" + defaultLabel + "\">\n" +
        "          <f:selectItem itemValue=\"item 1\"/>\n" +
        "          <f:selectItem itemValue=\"item 2\"/>\n" +
        "        </rich:comboBox>\n" +
        "      </h:form>\n" +
        "    </f:view>\n" +
        "  </body>\n" + 
        "</html>");
    jspEditor.save();
    bot.sleep(Timing.time3S());
    assertVisualEditorContains(webBrowser,
        "INPUT", 
        new String[]{"type","class","value"},
        new String[]{"text","rich-combobox-font-disabled rich-combobox-input-inactive",defaultLabel},
        RichFacesTagsTest.TEST_PAGE_NAME);
    assertVisualEditorContains(webBrowser,
        "INPUT", 
        new String[]{"type","class"},
        new String[]{"text","rich-combobox-font-inactive rich-combobox-button-background rich-combobox-button-inactive"},
        RichFacesTagsTest.TEST_PAGE_NAME);
    assertVisualEditorContains(webBrowser,
        "INPUT", 
        new String[]{"type","class"},
        new String[]{"text","rich-combobox-font-inactive rich-combobox-button-icon-inactive rich-combobox-button-inactive"},
        RichFacesTagsTest.TEST_PAGE_NAME);
    assertVisualEditorContains(webBrowser,
        "DIV", 
        new String[]{"class"},
        new String[]{"rich-combobox-strut rich-combobox-font"},
        RichFacesTagsTest.TEST_PAGE_NAME);    
    // check tag selection
    webBrowser.selectDomNode(webBrowser.getDomNodeByTagName("INPUT"), 0);
    bot.sleep(Timing.time3S());
    String selectedText = jspEditor.getSelection();
    final String hasToStartWith = "<rich:comboBox";
    assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" +
        "\nbut it is '" + selectedText + "'",
        selectedText.trim().startsWith(hasToStartWith));
    final String hasToEndWith = "</rich:comboBox>";
    assertTrue("Selected text in Source Pane has to end with '" + hasToEndWith + "'" +
        "\nbut it is '" + selectedText + "'",
        selectedText.trim().endsWith(hasToEndWith));

  }
  /**
   * Tests rich:comboBox Tag
   */
  public void testInplaceInputTag(){
    final String defaultLabel = "DefaultLabel";
    jspEditor.setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
        "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" +
        "<%@ taglib uri=\"http://richfaces.org/rich\" prefix=\"rich\" %>\n" +
        "<html>\n" +
        "  <head>\n" +
        "  </head>\n" +
        "  <body>\n" +
        "    <f:view>\n" +
        "      <rich:inplaceInput defaultLabel=\"" + defaultLabel+ "\"/>\n" +
        "    </f:view>\n" +
        "  </body>\n" + 
        "</html>");
    jspEditor.save();
    bot.sleep(Timing.time3S());
    assertVisualEditorContains(webBrowser,
      "SPAN", 
      new String[]{"vpe-user-toggle-id","title","class"},
      new String[]{"false","rich:inplaceInput defaultLabel: DefaultLabel","rich-inplace rich-inplace-view"},
      RichFacesTagsTest.TEST_PAGE_NAME);
    assertVisualEditorContainsNodeWithValue(webBrowser,
        defaultLabel, 
        RichFacesTagsTest.TEST_PAGE_NAME);
    // check tag selection
    webBrowser.selectDomNode(webBrowser.getDomNodeByTagName("SPAN",2), 0);
    bot.sleep(Timing.time3S());
    String selectedText = jspEditor.getSelection();
    final String expectedSelectedText = "<rich:inplaceInput defaultLabel=\"" + defaultLabel+ "\"/>";
    assertTrue("Selected text in Source Pane has to be '" + expectedSelectedText + "'" +
        "\nbut it is '" + selectedText + "'",
        selectedText.trim().equals(expectedSelectedText));
    webBrowser.mouseClickOnNode(webBrowser.getDomNodeByTagName("SPAN",2));
    bot.sleep(Timing.time3S());
    selectedText = jspEditor.getSelection();
    assertVisualEditorContains(webBrowser,
        "SPAN", 
        new String[]{"vpe-user-toggle-id","class"},
        new String[]{"true","rich-inplace rich-inplace-edit"},
        RichFacesTagsTest.TEST_PAGE_NAME);
    assertVisualEditorContains(webBrowser,
        "INPUT", 
        new String[]{"type","class","value"},
        new String[]{"text","rich-inplace-field",defaultLabel},
        RichFacesTagsTest.TEST_PAGE_NAME);
    assertTrue("Selected text in Source Pane has to be '" + expectedSelectedText + "'" +
        "\nbut it is '" + selectedText + "'",
        selectedText.trim().equals(expectedSelectedText));
  
  }
}
