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
package org.jboss.tools.vpe.ui.bot.test.editor.selectionbar;

import java.awt.event.KeyEvent;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.KeyboardHelper;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

/**
 * The Class SelectionBarTest.
 */
public class SelectionBarTest extends VPEAutoTestCase {

	private static final String SELECTED_TEXT = "<h:inputText value=\"#{user.name}\" required=\"true\">    <f:validateLength maximum=\"30\" minimum=\"3\"/>    </h:inputText>"; //$NON-NLS-1$
	private static final String SELECTED_TEXT2 = "<f:validateLength maximum=\"30\" minimum=\"3\"/>"; //$NON-NLS-1$
	
  private SWTBotExt botExt = null;
  private String sashStatus = "restored";
  
	public SelectionBarTest() {
		super();
		botExt = new SWTBotExt();
	}

	@Override
	protected void closeUnuseDialogs() {
		/*
		 * Nothing to close
		 */
	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}

	public void testSelectionBarContent () {
		SWTBotEditor editor = SWTTestExt.packageExplorer.openFile(JBT_TEST_PROJECT_NAME,
				"WebContent", "pages", VPEAutoTestCase.TEST_PAGE); //$NON-NLS-1$ //$NON-NLS-2$
		editor.setFocus();
		/*
		 * Navigate to '<h:inputText value="#{user.name}" required="true">'
		 */
		editor.toTextEditor().navigateTo(18, 10);
		bot.sleep(Timing.time3S());
		String errorMessage = checkSelectionBarContent();
		assertNull(errorMessage,errorMessage);
		maximizeVisualPane(botExt, VPEAutoTestCase.TEST_PAGE);
		sashStatus = "VisualPageMaximized";
		errorMessage = checkSelectionBarContent();
    assertNull(errorMessage,errorMessage);
    maximizeSourcePane(botExt, VPEAutoTestCase.TEST_PAGE);
    sashStatus = "SourcePageMaximized";
    errorMessage = checkSelectionBarContent();
    assertNull(errorMessage,errorMessage);
    restoreVisualPane(botExt, VPEAutoTestCase.TEST_PAGE);
    sashStatus = "restored";

	}
	
	 public void testSelectionBarButtonSelection () {
	    SWTBotEditor editor = SWTTestExt.packageExplorer.openFile(JBT_TEST_PROJECT_NAME,
	        "WebContent", "pages", TEST_PAGE); //$NON-NLS-1$ //$NON-NLS-2$
	    editor.setFocus();
	    /*
	     * Navigate to '<h:inputText value="#{user.name}" required="true">'
	     */
	    editor.toTextEditor().navigateTo(18, 10);
	    /*
	     * Send key press event to fire VPE listeners
	     */
	    KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_LEFT);
	    bot.sleep(Timing.time3S());
	    /*
	     * Click on the tag in the selection bar 
	     */
	    bot.toolbarDropDownButton("h:inputText").click(); //$NON-NLS-1$
	    
	    String line = editor.toTextEditor().getSelection();
	    line = line.replaceAll("\n", ""); //$NON-NLS-1$ //$NON-NLS-2$
	    line = line.replaceAll("\r", ""); //$NON-NLS-1$ //$NON-NLS-2$
	    line = line.replaceAll("\t", ""); //$NON-NLS-1$ //$NON-NLS-2$
	    assertEquals("<h:inputText> should be selected", SELECTED_TEXT, line); //$NON-NLS-1$
	    
	    bot.toolbarDropDownButton("h:inputText").menuItem("f:validateLength").click(); //$NON-NLS-1$ //$NON-NLS-2$
	    line = editor.toTextEditor().getSelection();
	    assertEquals("<f:validateLength> should be selected", SELECTED_TEXT2, line); //$NON-NLS-1$
	  }
    /**
     * Checks if Selection Bar has proper buttons
     * @return error message when Selection Bar has wrong content 
     */
	  private String checkSelectionBarContent () {
	    String errorMessage = null;
	    String buttonLabel = "html";
	    try {
	      bot.toolbarDropDownButton(buttonLabel);
	      buttonLabel = "body";
	      bot.toolbarDropDownButton(buttonLabel);
	      buttonLabel = "f:view";
	      bot.toolbarDropDownButton(buttonLabel);
	      buttonLabel = "h:inputText";
	      bot.toolbarDropDownButton(buttonLabel);
	    } catch (WidgetNotFoundException wnfe){
	      errorMessage = "Selection Bar has to contain Drop Down Button with label " 
	        + buttonLabel
	        + " but it doesn't.";
	    }
	    
	    return errorMessage;
	  }
	  
	  @Override
	  public void tearDown() throws Exception {
	    if (sashStatus.equals("VisualPageMaximized")){
	      restoreSourcePane(botExt, VPEAutoTestCase.TEST_PAGE);
	    } else if (sashStatus.equals("SourcePageMaximized")){
	      restoreVisualPane(botExt, VPEAutoTestCase.TEST_PAGE);
	    }
	    super.tearDown();
	  }
}
