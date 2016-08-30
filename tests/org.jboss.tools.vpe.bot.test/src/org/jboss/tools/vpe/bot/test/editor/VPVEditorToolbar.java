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
package org.jboss.tools.vpe.bot.test.editor;

import static org.junit.Assert.*;

import org.jboss.reddeer.workbench.ui.dialogs.FilteredPreferenceDialog;
import org.jboss.tools.vpe.bot.test.VPETestBase;
import org.jboss.tools.vpe.reddeer.preview.editor.VPVEditor;
import org.jboss.tools.vpe.reddeer.resref.core.VpvResourcesDialog;
import org.junit.BeforeClass;
import org.junit.Test;


public class VPVEditorToolbar extends VPETestBase{
	
	private static String pageName; 
	
	@BeforeClass
	public static void createProject(){
		createWebProject();
		pageName = createHTMLPageWithJS();
	}
	
	@Test
	public void openPreferences(){
		FilteredPreferenceDialog fd = new VPVEditor().openPreferences();
		assertTrue(fd.isOpen());
		assertEquals("Visual Page Editor", fd.getPageName());
		fd.ok();
	}
	
	public void pageDesignOptions(){
		VpvResourcesDialog rDialog = new VPVEditor().openPageDesignOptions();
	}
	
	

}
