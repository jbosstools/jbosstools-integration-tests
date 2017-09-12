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

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.api.Shell;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.FinishButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.jst.reddeer.palette.JQueryMobilePalette;
import org.jboss.tools.vpe.bot.test.VPETestBase;
import org.jboss.tools.vpe.reddeer.condition.VPVBackIsEnabled;
import org.jboss.tools.vpe.reddeer.condition.VPVForwardIsEnabled;
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
		new WaitUntil(new JQueryIsReady(vpvEditor));
		assertFalse(vpvEditor.isBackEnabled());
		assertFalse(vpvEditor.isForwardEnabled());
		String page0 = "page-1";
		String page1 = "page1";
		new WaitUntil(new SiteHasTitle(vpvEditor, page0, page1), TimePeriod.LONG);
		new WaitUntil(new ElementIsFound(vpvEditor, "pageButton"));
		//we still have to wait because Windows browser sucks
		AbstractWait.sleep(TimePeriod.DEFAULT);
		boolean executed = vpvEditor.executeScript("$(\"#pageButton\").click()");
		assertTrue(executed);
		new WaitUntil(new JQueryIsReady(vpvEditor));
		new WaitUntil(new VPVBackIsEnabled(vpvEditor));
		new WaitWhile(new VPVForwardIsEnabled(vpvEditor));
		assertTrue(vpvEditor.isBackEnabled());
		assertFalse(vpvEditor.isForwardEnabled());
		new WaitUntil(new SiteHasTitle(vpvEditor, page1, page0), TimePeriod.LONG);
		vpvEditor.back();
		new WaitUntil(new JQueryIsReady(vpvEditor));
		new WaitUntil(new SiteHasTitle(vpvEditor, page0, page1), TimePeriod.LONG);
		new WaitWhile(new VPVBackIsEnabled(vpvEditor));
		new WaitUntil(new VPVForwardIsEnabled(vpvEditor));
		assertTrue(vpvEditor.isForwardEnabled());
		assertFalse(vpvEditor.isBackEnabled());
		vpvEditor.forward();
		new WaitUntil(new JQueryIsReady(vpvEditor));
		new WaitUntil(new SiteHasTitle(vpvEditor, page1, page0), TimePeriod.LONG);
	}
	
	private void createMultiPage(){
		VPVEditor vpvEditor = new VPVEditor();
		JQueryMobilePalette jqp = vpvEditor.getPalette();
		jqp.activateTool("Page", "jQuery Mobile");
		//TODO implement this wizard for o.j.t.common.reddeer
		Shell s = new DefaultShell("Insert Tag");
		new FinishButton().click();
		new WaitWhile(new ShellIsAvailable(s));
		jqp.activateTool("Button", "jQuery Mobile");
		s = new DefaultShell("Insert Tag");
		new LabeledText("URL (href):").setText("#page1");
		new DefaultText(new TextTooltipMatcher("Generate")).setText("pageButton");
		new FinishButton().click();
		new WaitWhile(new ShellIsAvailable(s));
	}
	
	private class ElementIsFound extends AbstractWaitCondition {
		
		private VPVEditor editor;
		private String script;
		
		public ElementIsFound(VPVEditor editor, String elementId) {
			this.editor = editor;
			this.script = "return !!document.getElementById('" +elementId+ "')";
		}

		@Override
		public boolean test() {
			return (Boolean) editor.evaluateScript(script);
		}
		
	}
	
	private class SiteHasTitle extends AbstractWaitCondition {
		
		private VPVEditor editor;
		private String visiblePage;
		private String hiddenPage;
		
		public SiteHasTitle(VPVEditor editor, String visiblePage, String hiddenPage) {
			this.editor = editor;
			this.visiblePage = visiblePage;
			this.hiddenPage = hiddenPage;
		}
		
		@Override
		public boolean test(){
			Boolean visible = (Boolean)editor.evaluateScript("return $(\"#"+visiblePage+"\").is(\":visible\")");
			Boolean hidden = (Boolean)editor.evaluateScript("return $(\"#"+hiddenPage+"\").is(\":visible\")");
			return visible && !hidden;
		}
	}
	
	private class JQueryIsReady extends AbstractWaitCondition {
		
		private VPVEditor editor;
		
		public JQueryIsReady(VPVEditor editor) {
			this.editor = editor;
		}
		
		@Override
		public boolean test(){
			return (Boolean)editor.evaluateScript("return jQuery.isReady");
		}
		
	}
	

}
