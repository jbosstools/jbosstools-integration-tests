/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.wizard;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.osgi.framework.Bundle;

public class ImportUnknownTagsWizardTest extends VPEAutoTestCase {

	private final String STORED_TAGS_PATH = "storedTags.xml"; //$NON-NLS-1$
	
	public ImportUnknownTagsWizardTest() {
	}

	@Override
	protected void closeUnuseDialogs() {

	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}

	public void _testImportWizard() throws Throwable {
		/*
		 * Open wizard page
		 */
		bot.menu("File").menu("Import...").click(); //$NON-NLS-1$ //$NON-NLS-2$
		bot.shell("Import").activate(); //$NON-NLS-1$
		SWTBotTree importTree = bot.tree();
		importTree.expandNode("Other").select("Unknown tags templates"); //$NON-NLS-1$ //$NON-NLS-2$
		bot.button(WidgetVariables.NEXT_BUTTON).click();
		/*
		 * Load stored tags
		 */
		Bundle bundle = Platform.getBundle("org.jboss.tools.vpe.ui.bot.test"); //$NON-NLS-1$
		URL url = bundle.getEntry("/resources"); //$NON-NLS-1$
		url = FileLocator.resolve(url);
		IPath path = new Path(url.getPath());
		File file = path.append(STORED_TAGS_PATH).toFile();
        assertTrue("File '" + file.getAbsolutePath() +"' does not exist.", file.exists()); //$NON-NLS-1$ //$NON-NLS-2$
        bot.text().setText(file.getAbsolutePath());
        /*
         * Check table values 
         */
        String taglib = bot.table().cell(0, 0);
        assertEquals("Wrong table value.", "lib:tag", taglib); //$NON-NLS-1$ //$NON-NLS-2$
        taglib = bot.table().cell(1, 0);
        assertEquals("Wrong table value.", "taglibName:tagName", taglib); //$NON-NLS-1$  //$NON-NLS-2$
        /*
         * Check that finish button is enabled and press it.
         */
        assertTrue("Finish button should be enabled.", //$NON-NLS-1$ 
        bot.button(WidgetVariables.FINISH_BUTTON).isEnabled());
        bot.button(WidgetVariables.FINISH_BUTTON).click();
        /*
         * Check that templates have been added to the preference page 
         */
        bot.menu("Window").menu("Preferences").click(); //$NON-NLS-1$ //$NON-NLS-2$
		bot.shell("Preferences").activate(); //$NON-NLS-1$
		importTree = bot.tree();
		importTree.expandNode("JBoss Tools") //$NON-NLS-1$d
				.expandNode("Web") //$NON-NLS-1$
				.expandNode("Editors") //$NON-NLS-1$
				.select("Visual Page Editor"); //$NON-NLS-1$
		bot.tabItem("Templates").activate(); //$NON-NLS-1$
		/*
         * Check table values on the preferences page
         */
        taglib = bot.table().cell(0, 0);
        assertEquals("Wrong table value.", "taglibName:tagName", taglib); //$NON-NLS-1$  //$NON-NLS-2$
        taglib = bot.table().cell(1, 0);
        assertEquals("Wrong table value.", "lib:tag", taglib); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
}
