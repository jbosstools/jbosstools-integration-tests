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
public abstract class RichFacesTagsTest extends VPEEditorTestCase {
  
  protected static final String TEST_PAGE_NAME_JSP = "RichFacesTagsTest.jsp";
  protected static final String TEST_PAGE_NAME_XHTML = "RichFacesTagsTest.xhtml";
  
  protected SWTBotEditorExt jspEditor;
  protected SWTBotEditorExt xhtmlEditor;
  protected SWTBotWebBrowser jspWebBrowser;
  protected SWTBotWebBrowser xhtmlWebBrowser;
  protected SWTBotExt botExt;
  
	public RichFacesTagsTest() {
		super();
		botExt = new SWTBotExt();
	}
	@Override
	protected void setUp() throws Exception {
	  super.setUp();
    eclipse.maximizeActiveShell();
    createJspPage(RichFacesTagsTest.TEST_PAGE_NAME_JSP);
    jspEditor = botExt.swtBotEditorExtByTitle(RichFacesTagsTest.TEST_PAGE_NAME_JSP);
    jspWebBrowser = new SWTBotWebBrowser(RichFacesTagsTest.TEST_PAGE_NAME_JSP,botExt);
    createXhtmlPage(RichFacesTagsTest.TEST_PAGE_NAME_XHTML);
    xhtmlEditor = botExt.swtBotEditorExtByTitle(RichFacesTagsTest.TEST_PAGE_NAME_XHTML);
    xhtmlWebBrowser = new SWTBotWebBrowser(RichFacesTagsTest.TEST_PAGE_NAME_XHTML,botExt);

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
    xhtmlEditor.close();
    super.tearDown();
  }
  /**
   * Runs Tag Testing
   */
  public void testTag (){
    initPageContent ();
    savePageContent ();
    verifyTag();
  }
  /**
   * Initialize proper page content
   */
  protected abstract void initPageContent ();
  /**
   * Verify tag
   */
  protected abstract void verifyTag ();
  /**
   * Saves Page Content if it was changed and shows changed editor
   */
  protected void savePageContent(){
    boolean wasSaved = false;
    if (jspEditor.isDirty()){
      jspEditor.save();
      jspEditor.show();
      wasSaved = true;
    }
    if (xhtmlEditor.isDirty()){
      xhtmlEditor.save();
      xhtmlEditor.show();
      wasSaved = true;
    }
    if (wasSaved){
      bot.sleep(Timing.time3S());
    }
  }
}
