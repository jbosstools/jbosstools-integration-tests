/*******************************************************************************
 * Copyright (c) 2016-2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.livereload.ui.bot.test;

import static org.junit.Assert.*;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.eclipse.ui.browser.WebBrowserView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.livereload.reddeer.Livereload;
import org.jboss.tools.livereload.reddeer.condition.BrowserContainsText;
import org.jboss.tools.livereload.reddeer.requirement.LivereloadServerRequirement.LivereloadServer;
import org.junit.Test;

@JBossServer(state=ServerReqState.RUNNING, type=ServerReqType.WILDFLY10x)
@LivereloadServer(name="Livereload")
public class LivereloadTest extends LivereloadBaseTest{
	
	@InjectRequirement
    private ServerRequirement sr;
	
	private String browserText = "livereload test abcd";
	private String browserTextNew = "livereload reload test";
	
	@Test
	public void baseTest(){
		String pageName = createProjectWithPage();
		
		TextEditor te = new TextEditor(pageName);
		te.insertText(8, 0, browserText);
		te.save();
		
		deployProjectToServer(sr.getServerNameLabelText(), PROJECT_NAME);
		injectLivereload(sr.getServerNameLabelText(), PROJECT_NAME);

		WebBrowserView bw = new WebBrowserView();
		bw.open();
		bw.openPageURL(Livereload.getLivereloadURL(PROJECT_NAME, pageName));
		InternalBrowser ib = new InternalBrowser();
		String txt =ib.getText();
		assertTrue(txt.contains(browserText));
		te.activate();
		String newText = te.getText().replaceAll(browserText, browserTextNew);
		te.setText(newText);
		te.save();
		new WaitWhile(new BrowserContainsText(ib, browserText), TimePeriod.LONG);
		assertTrue(ib.getText().contains(browserTextNew));		
	}

}
