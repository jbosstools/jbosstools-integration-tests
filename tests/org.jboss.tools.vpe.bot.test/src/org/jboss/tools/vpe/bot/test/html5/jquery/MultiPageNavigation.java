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
package org.jboss.tools.vpe.bot.test.html5.jquery;

import static org.junit.Assert.*;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.jst.reddeer.palette.JQueryMobilePalette;
import org.jboss.tools.jst.reddeer.web.ui.editor.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.bot.test.VPETestBase;
import org.jboss.tools.vpe.reddeer.matcher.TextTooltipMatcher;
import org.jboss.tools.vpe.reddeer.preview.editor.VPVEditor;
import org.junit.BeforeClass;
import org.junit.Test;


public class MultiPageNavigation extends VPETestBase{
	
	//TODO should not depend on palette
	//TODO should have different tests for palette
	
	@BeforeClass
	public static void prepareWorkspace(){
		createWebProject();
		createHTMLPage("HTML5 jQuery Mobile Page (1.4)");
	}
	
	@Test
	public void testMultiPageNavigation(){
		createMultiPage();
		VPVEditor vpvEditor = new VPVEditor();
		assertFalse(vpvEditor.isBackEnabled());
		assertFalse(vpvEditor.isForwardEnabled());
		String page0 = ".*viewId=[0-9]";
		String page1 = ".*viewId=[0-9]#page1";
		checkCurrentPage(vpvEditor, page0);
		vpvEditor.evaluateScript("document.getElementById('pageButton').click()");
		assertTrue(vpvEditor.isBackEnabled());
		assertFalse(vpvEditor.isForwardEnabled());
		checkCurrentPage(vpvEditor, page1);
		vpvEditor.back();
		
		checkCurrentPage(vpvEditor, page0);
		assertTrue(vpvEditor.isForwardEnabled());
		assertFalse(vpvEditor.isBackEnabled());
		vpvEditor.forward();
		checkCurrentPage(vpvEditor, page1);
	}
	
	private void createMultiPage(){
		VPVEditor vpvEditor = new VPVEditor();
		JQueryMobilePalette jqp = vpvEditor.getPalette();
		jqp.activateTool("Page", "jQuery Mobile");
		new DefaultShell("Insert Tag");
		new FinishButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Insert Tag"));
		jqp.activateTool("Button", "jQuery Mobile");
		new DefaultShell("Insert Tag");
		new LabeledText("URL (href):").setText("#page1");
		new DefaultText(new TextTooltipMatcher("Generate")).setText("pageButton");
		new FinishButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Insert Tag"));
	}
	
	private void checkCurrentPage(VPVEditor vpvEditor, String expectedPage){
		String browserURL = vpvEditor.getBrowserURL();
		assertTrue("URL is "+browserURL+" but we expect it to end with "+expectedPage,browserURL.matches(expectedPage));
	
	}

}
