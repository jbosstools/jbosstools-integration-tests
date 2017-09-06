/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.ui.bot.test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.browsersim.reddeer.condition.BrowserSimBrowserHasText;
import org.jboss.tools.livereload.reddeer.requirement.LivereloadServerRequirement.LivereloadServer;
import org.jboss.tools.vpe.reddeer.preview.editor.VPVEditor;
import org.junit.Test;

@LivereloadServer(name="Livereload")
public class LivereloadTest extends BrowsersimBaseTest{
	
	
	@Test
	public void testLiveReload() throws RemoteException{
		String page = createProjectWithPage();
		VPVEditor htmlEditor = new VPVEditor();
		TextEditor te = new TextEditor();
		te.insertText(8, 0, "browsersim-livereload test");
		te.save();
		new DefaultStyledText();
	
		launchBrowsersim(new ContextMenuItem("Open With","BrowserSim"));
		bsHandler.enableLivereload();
		new WaitUntil(new BrowserSimBrowserHasText(bsHandler, "browsersim-livereload test"));
		
		assertTrue(bsHandler.getBrowserText().contains("browsersim-livereload test"));
		
		new WorkbenchShell();
		te.setText(te.getText().replace("browsersim-livereload test", "browsersim-livereload reload"));
		te.save();
		
		new WaitUntil(new BrowserSimBrowserHasText(bsHandler, "browsersim-livereload reload"));
		
		assertTrue(bsHandler.getBrowserText().contains("browsersim-livereload reload"));
		
		
		
	}

}