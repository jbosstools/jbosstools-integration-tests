/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.bot.test.html5;

import static org.junit.Assert.*;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.vpe.bot.test.VPETestBase;
import org.jboss.tools.vpe.reddeer.condition.VPEditorHasTextSelected;
import org.jboss.tools.vpe.reddeer.preview.editor.VPVEditor;
import org.junit.BeforeClass;
import org.junit.Test;

public class HTML5EditorHighlight extends VPETestBase{
	
	private static String pageName;
	
	@BeforeClass
	public static void prepareWorkspace(){
		createWebProject();
		pageName = createHTMLPageWithJS();
		preparePage();
	}
	@Test
	public void testHighlighting(){
		testEditorToBrowserHighlighting();
		testBrowserToEditorHighlighting();
	}
	
	private void testEditorToBrowserHighlighting(){
		TextEditor te = new TextEditor(pageName);
		assertNull(new VPVEditor().getSelectedTextInBrowser());	
		
		selectAndCheckHighlight(te, "This is body of the page", "This is body of the page abc def custom");
		selectAndCheckHighlight(te, "abc", "abc");
		selectAndCheckHighlight(te, "def", "def");
		selectAndCheckHighlight(te, "table", "abc def");
		selectAndCheckHighlight(te, "custom", "custom");

		
		
		//check highlight by cursor position
		setCursorAndCheckHighlight(te, te.getPositionOfText("abc"), "abc");
		setCursorAndCheckHighlight(te, te.getPositionOfText("def")+1, "def");
		setCursorAndCheckHighlight(te, te.getPositionOfText("This is body"), "This is body of the page abc def custom");
		setCursorAndCheckHighlight(te, te.getPositionOfText("table")+4, "abc def");
		setCursorAndCheckHighlight(te, te.getPositionOfText("custom"), "custom");
		
	}
	
	private void testBrowserToEditorHighlighting(){
		VPVEditor vpvEditor = new VPVEditor();
		AbstractWait.sleep(TimePeriod.SHORT); //we have to wait for browser 
		vpvEditor.clickInBrowser("abc");
		new WaitUntil(new VPEditorHasTextSelected(vpvEditor, "abc"));
		assertEquals("<td>abc</td>",vpvEditor.getSelectedTextInEditor());
		vpvEditor.clickInBrowser("custom");
		new WaitUntil(new VPEditorHasTextSelected(vpvEditor, "custom"));
		assertEquals("<cTag>custom</cTag>",vpvEditor.getSelectedTextInEditor());
	}
	
	private static void preparePage(){
		String bodyText = "This is body of the page<table><tr><td>abc</td><td>def</td></tr></table><cTag>custom</cTag>";
		TextEditor te = new TextEditor(pageName);
		te.insertLine(8, bodyText);
	}
	
	private void selectAndCheckHighlight(TextEditor te, String textToSelect, String highlight){
		te.selectText(textToSelect);
		VPVEditor editor = new VPVEditor();
		new WaitUntil(new VPEditorHasTextSelected(editor, highlight));
		assertEquals(highlight,editor.getSelectedTextInBrowser());
	}
	
	private void setCursorAndCheckHighlight(TextEditor te, int cursorPosition, String highlight){
		te.setCursorPosition(cursorPosition);
		VPVEditor editor = new VPVEditor();
		new WaitUntil(new VPEditorHasTextSelected(editor,highlight));
		assertEquals(highlight,editor.getSelectedTextInBrowser());
	}
	
}
