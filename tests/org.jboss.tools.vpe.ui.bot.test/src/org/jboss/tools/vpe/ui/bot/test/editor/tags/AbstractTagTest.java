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

import java.security.InvalidParameterException;

import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
/**
 * Abstract Tag Test behavior 
 * @author vlado pakan
 *
 */
public abstract class AbstractTagTest extends VPEEditorTestCase {
  
  private static final String TEST_PAGE_NAME_JSP = "TagTest.jsp";
  private static final String TEST_PAGE_NAME_XHTML = "TagTest.xhtml";
  
  private SWTBotEditorExt sourceEditor;
  private SWTBotWebBrowser visualEditor;
  private TestPageType testPageType;
  protected SWTBotExt botExt;
  
	public AbstractTagTest() {
		super();
		botExt = new SWTBotExt();
	}
	@Override
	public void setUp() throws Exception {
	  super.setUp();
    eclipse.maximizeActiveShell();
    initTestPage ();
    savePage();
	}
  /**
   * 
   * @param testPageType
   * @param pageText
   */
	protected void initTestPage(TestPageType testPageType , String pageText){
	  this.testPageType = testPageType;
	  if (testPageType.equals(TestPageType.JSP)){
	    createJspPage(AbstractTagTest.TEST_PAGE_NAME_JSP);
	    sourceEditor = botExt.swtBotEditorExtByTitle(AbstractTagTest.TEST_PAGE_NAME_JSP);
	    visualEditor = new SWTBotWebBrowser(AbstractTagTest.TEST_PAGE_NAME_JSP,botExt);
	  } else if (testPageType.equals(TestPageType.XHTML)){
	    createXhtmlPage(AbstractTagTest.TEST_PAGE_NAME_XHTML);
	    sourceEditor = botExt.swtBotEditorExtByTitle(AbstractTagTest.TEST_PAGE_NAME_XHTML);
	    visualEditor = new SWTBotWebBrowser(AbstractTagTest.TEST_PAGE_NAME_XHTML,botExt);
	  } else {
	    throw new InvalidParameterException("Invalid value of testPageType used: " + testPageType);
	  }
	  sourceEditor.setText(pageText);
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
    sourceEditor.close();
    super.tearDown();
  }
  /**
   * Runs Tag Testing
   */
  public void testTag (){
    verifyTag();
  }
  /**
   * Initialize properly page content
   */
  protected abstract void initTestPage ();
  /**
   * Verify tag
   */
  protected abstract void verifyTag ();
  /**
   * Saves Page if it was changed and shows changed editor
   */
  protected void savePage(){
    boolean wasSaved = false;
    if (sourceEditor.isDirty()){
      sourceEditor.save();
      sourceEditor.show();
      wasSaved = true;
    }
    if (wasSaved){
      bot.sleep(Timing.time3S());
    }
  }
  /**
   * Returns actual Source part of VPE Editor
   * @return
   */
  protected SWTBotEditorExt getSourceEditor() {
    return sourceEditor;
  }
  /**
   * Returns actual Visual part of VPE Editor
   * @return
   */
  protected SWTBotWebBrowser getVisualEditor() {
    return visualEditor;
  }  
  /**
   * Returns actual Test Page File Name
   * @return
   */
  protected String getTestPageFileName() {
    String fileName = null;
    
    if (testPageType.equals(TestPageType.JSP)){
      fileName = AbstractTagTest.TEST_PAGE_NAME_JSP;  
    } else if (testPageType.equals(TestPageType.XHTML)){
      fileName = AbstractTagTest.TEST_PAGE_NAME_XHTML;  
    } else{
      throw new InvalidParameterException("Invalid value of testPageType used: " + testPageType);
    }
    
    return fileName;
  }
  
}
