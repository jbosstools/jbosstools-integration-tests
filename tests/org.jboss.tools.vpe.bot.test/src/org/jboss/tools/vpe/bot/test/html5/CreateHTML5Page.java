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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;

import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.wst.html.ui.wizard.NewHTMLFileWizardPage;
import org.eclipse.reddeer.eclipse.wst.html.ui.wizard.NewHTMLTemplatesWizardPage;
import org.eclipse.reddeer.eclipse.wst.html.ui.wizard.NewHTMLWizard;
import org.eclipse.reddeer.gef.view.PaletteView;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.vpe.bot.test.VPETestBase;
import org.jboss.tools.vpe.reddeer.preview.editor.VPVEditor;
import org.junit.BeforeClass;
import org.junit.Test;

public class CreateHTML5Page extends VPETestBase{
	
	@BeforeClass
	public static void prepareWorkspace(){
		createWebProject();
	}
	
	@Test
	public void createHTML5Page(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(PROJECT_NAME);
		NewHTMLWizard nw = new NewHTMLWizard();
		nw.open();
		NewHTMLFileWizardPage fp = new NewHTMLFileWizardPage(nw);
		assertEquals(PROJECT_NAME+"/WebContent",fp.getParentFolder());
		assertEquals("NewFile.html",fp.getFileName());
		assertTrue(nw.isFinishEnabled());
		nw.next();
		NewHTMLTemplatesWizardPage tp = new NewHTMLTemplatesWizardPage(nw);
		assertEquals("New HTML File (5)",tp.getHTMLTemplate());
		assertFalse(nw.isNextEnabled());
		nw.finish();
		VPVEditor htmlEditor = new VPVEditor();
		assertTrue(htmlEditor.getBrowserURL() != null);
		assertTrue(htmlEditor.getMarkers().isEmpty());
		new TextEditor().setCursorPosition(1, 1);
		new ContextMenuItem("Validate").select();
		new WaitWhile(new JobIsRunning());
		assertTrue(htmlEditor.getMarkers().isEmpty());
		assertTrue(new PaletteView().isOpen());
	}
	
	@Test
	public void deleteHTML5Page(){
		String pageName = createHTMLPageWithJS();
		new DefaultEditor(pageName);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem("WebContent",pageName).delete();
	}
	
	

}
