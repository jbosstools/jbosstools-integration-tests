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
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
/**
 * Tests Core HTML Tags behavior 
 * @author vlado pakan
 *
 */
public class CoreHTMLTagsTest extends VPEEditorTestCase {
  
  private static final String PAGE_TEXT = "<html>\n" +
      "  <body style=\"color:red; text-align:center; background-color:green\">Body tag test</body>\n" +
      "</html>";
      
  private static final String TEST_PAGE_NAME = "CoreHTMLTagsTest.jsp";
  
  private SWTBotEditorExt jspEditor;
  private SWTBotWebBrowser webBrowser;
  private SWTBotExt botExt;
  
	public CoreHTMLTagsTest() {
		super();
		botExt = new SWTBotExt();
	}
	@Override
	protected void setUp() throws Exception {
	  super.setUp();
    eclipse.maximizeActiveShell();
    createJspPage(CoreHTMLTagsTest.TEST_PAGE_NAME);
    jspEditor = botExt.swtBotEditorExtByTitle(CoreHTMLTagsTest.TEST_PAGE_NAME);
    webBrowser = new SWTBotWebBrowser(CoreHTMLTagsTest.TEST_PAGE_NAME,botExt);
	}
	/**
   * Tests Body Tag
   */
  public void testBodyTag(){
    
    jspEditor.setText(CoreHTMLTagsTest.PAGE_TEXT);
    jspEditor.save();
    bot.sleep(Timing.time3S());
    assertVisualEditorContains(webBrowser,
        "BODY", 
        new String[]{"style"},
        new String[]{"color: red; text-align: center; background-color: green;"},
        CoreHTMLTagsTest.TEST_PAGE_NAME);
    // check after refresh
    bot.toolbarButtonWithTooltip(IDELabel.ToolbarButton.REFRESH).click();
    assertVisualEditorContains(webBrowser,
        "BODY", 
        new String[]{"style"},
        new String[]{"color: red; text-align: center; background-color: green;"},
        CoreHTMLTagsTest.TEST_PAGE_NAME);

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
}
