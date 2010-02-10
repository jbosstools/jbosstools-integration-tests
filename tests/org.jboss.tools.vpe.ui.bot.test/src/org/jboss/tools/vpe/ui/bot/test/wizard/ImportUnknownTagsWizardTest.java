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

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

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
		bot.menu("File").menu("Import...").click(); //$NON-NLS-1$ //$NON-NLS-2$
		bot.shell("Import").activate(); //$NON-NLS-1$
		SWTBotTree importTree = bot.tree();
		importTree.expandNode("Other").select("Unknown tags templates"); //$NON-NLS-1$ //$NON-NLS-2$
		bot.button(WidgetVariables.NEXT_BUTTON).click();
		bot.text().setText(STORED_TAGS_PATH);
	}
	
	protected void openPage() {
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
		SWTBotTree tree = innerBot.tree();
		tree.expandNode(JBT_TEST_PROJECT_NAME)
		.expandNode("WebContent").expandNode("pages").getNode(TEST_PAGE).doubleClick(); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
}
