/*******************************************************************************
 * Copyright (c) 2007-2016 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.wizard;

import java.io.File;

import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.vpe.reddeer.preferences.VisualPageEditorPreferencePage;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.junit.Test;

public class ImportUnknownTagsWizardTest extends VPEAutoTestCase {

	private final String STORED_TAGS_PATH = "storedTags.xml"; //$NON-NLS-1$

	public ImportUnknownTagsWizardTest() {
		super();
	}

	@Test
	public void testImportWizard() throws Throwable {
		/*
		 * Open wizard page
		 */
		new ShellMenu("File","Import...").select();
		new DefaultShell("Import");
		new DefaultTreeItem("Other","User specified tag templates").select();
		new PushButton("Next >").click();
		/*
		 * Load stored tags
		 */
		File file = new File(getPathToResources(STORED_TAGS_PATH));
		assertTrue("File '" + file.getAbsolutePath() + "' does not exist.", file.exists()); //$NON-NLS-1$ //$NON-NLS-2$
		new DefaultText().setText(file.getAbsolutePath());
		/*
		 * Check table values
		 */
		String libTagline = new DefaultTableItem("lib:tag").getText();
		String tagLibNameLine = new DefaultTableItem("taglibName:tagName").getText();

		assertEquals("Wrong table value.", "lib:tag", libTagline); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Wrong table value.", "taglibName:tagName", tagLibNameLine); //$NON-NLS-1$ //$NON-NLS-2$
		/*
		 * Check that finish button is enabled and press it.
		 */
		new FinishButton().click();
		/*
		 * Check that templates have been added to the preference page
		 */
		VisualPageEditorPreferencePage vpePreferencePage = new VisualPageEditorPreferencePage();
		WorkbenchPreferenceDialog preferenceDialog = null;
		preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		preferenceDialog.select(vpePreferencePage);
		vpePreferencePage.activateVisualTemplatesTab();
		/*
		 * Check table values on the preferences page
		 */
		libTagline = new DefaultTableItem("lib:tag").getText();
		tagLibNameLine = new DefaultTableItem("taglibName:tagName").getText();

		new OkButton().click();
		assertEquals("Wrong table value.", "taglibName:tagName", tagLibNameLine); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Wrong table value.", "lib:tag", libTagline); //$NON-NLS-1$ //$NON-NLS-2$

	}

}