/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.livereload.ui.bot.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.ui.browser.WebBrowserView;
import org.eclipse.reddeer.junit.execution.annotation.RunIf;
import org.eclipse.reddeer.swt.impl.browser.InternalBrowser;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.livereload.reddeer.Livereload;
import org.jboss.tools.livereload.reddeer.condition.BrowserContainsText;
import org.jboss.tools.livereload.reddeer.requirement.DockerWildflyRequirement.DockerWildfly;
import org.jboss.tools.livereload.reddeer.requirement.LivereloadServerRequirement.LivereloadServer;
import org.junit.Test;

//ssh enabled docker image with wildfly
@DockerWildfly(imageName = "adejonge/wildfly", homeFolder = "/lib/wildfly", userName = "root", pass = "root", name = "wf")
@LivereloadServer(name = "Livereload")
public class LivereloadRemoteServerTest extends LivereloadBaseTest {

	private String browserText = "livereload test abcd";
	private String browserTextNew = "livereload reload test";

	@Test
	@RunIf(conditionClass = SystemWithDocker.class)
	public void baseTest() {
		String pageName = createProjectWithPage();

		TextEditor te = new TextEditor(pageName);
		te.insertText(8, 0, browserText);
		te.save();

		deployProjectToRemoteServer("wf", PROJECT_NAME);
		injectLivereload("wf", PROJECT_NAME);

		WebBrowserView bw = new WebBrowserView();
		bw.open();
		bw.openPageURL(Livereload.getLivereloadURL(PROJECT_NAME, pageName));
		InternalBrowser ib = new InternalBrowser();
		String txt = ib.getText();
		assertTrue(txt.contains(browserText));
		te.activate();
		String newText = te.getText().replaceAll(browserText, browserTextNew);
		te.setText(newText);
		te.save();
		new WaitWhile(new BrowserContainsText(ib, browserText), TimePeriod.LONG);
		assertTrue(ib.getText().contains(browserTextNew));
	}

}