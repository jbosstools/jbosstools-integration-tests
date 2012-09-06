/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor.pagedesign;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;

public class AddSubstitutedELExpressionFolderScopeTest extends SubstitutedELTestCase{
	
	private static final String TEST_PAGE_FOR_FOLDER = "testPage"; //$NON-NLS-1$
	private static final String TEST_FOLDER = "testFolder"; //$NON-NLS-1$

	//Do not edit this variable as test will fail
	private static final String EL_VALUE = "Any Name"; //$NON-NLS-1$
	
	public void testAddSubstitutedELExpressionFolderScope() throws Throwable{
		
		//Test open page
		openPage();
		setEditor(bot.editorByTitle(TEST_PAGE).toTextEditor());
		setEditorText(getEditor().getText());
		
		//Test create new folder
		
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
		innerBot.tree().expandNode(JBT_TEST_PROJECT_NAME).expandNode("WebContent") //$NON-NLS-1$
		.select();
		
		bot.menu("File").menu("New").menu("Folder").click(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		bot.shell("New Folder").activate(); //$NON-NLS-1$
		bot.textWithLabel("Folder name:").setText(TEST_FOLDER); //$NON-NLS-1$
		bot.button("Finish").click(); //$NON-NLS-1$
		
		//Test create page in new folder
		
		innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
		innerBot.tree().expandNode(JBT_TEST_PROJECT_NAME).expandNode("WebContent") //$NON-NLS-1$
		.getNode(TEST_FOLDER).select();
		
    open.newObject(ActionItem.NewObject.WebJSP.LABEL);
		
		bot.shell(IDELabel.Shell.NEW_JSP_FILE).activate(); //$NON-NLS-1$
		bot.textWithLabel(ActionItem.NewObject.WebJSP.TEXT_FILE_NAME).setText(TEST_PAGE_FOR_FOLDER); //$NON-NLS-1$
		bot.button(IDELabel.Button.FINISH).click(); //$NON-NLS-1$
		delay();
		SWTBotEclipseEditor editorForTestPage = bot.editorByTitle(TEST_PAGE_FOR_FOLDER+".jsp").toTextEditor(); //$NON-NLS-1$
		editorForTestPage.setText(getEditorText());
		editorForTestPage.save();
		editorForTestPage.close();
		bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).setFocus();
		bot.editorByTitle(TEST_PAGE).setFocus();
		
		//Test open Page Design Options
		
		bot.toolbarButtonWithTooltip(PAGE_DESIGN).click();
		bot.shell(PAGE_DESIGN).activate();
		
		//Test choose Substituted EL tab
		
		bot.tabItem(SUBSTITUTED_EL).activate();
		
		//Clear EL table
		clearELTable(bot.table());
		
		//Test add EL with folder scope to list

		bot.button("Add").click(); //$NON-NLS-1$
		delay();
		bot.shell(ADD_EL).activate();
		bot.textWithLabel("El Name*").setText("user.name"); //$NON-NLS-1$ //$NON-NLS-2$
		bot.textWithLabel("Value").setText(EL_VALUE); //$NON-NLS-1$
		bot.radio("Folder: Any Page at the Same Folder").click(); //$NON-NLS-1$
		bot.button("Finish").click(); //$NON-NLS-1$
		
		//Test check table with ELs
		
		bot.shell(PAGE_DESIGN).activate();
		SWTBotTable table = bot.table();
		String elName = table.cell(0, "El Expression"); //$NON-NLS-1$
		String scope = table.cell(0, "Scope"); //$NON-NLS-1$
		String elValue = table.cell(0, "Value"); //$NON-NLS-1$
		assertEquals("user.name",elName); //$NON-NLS-1$
		assertEquals("Folder",scope); //$NON-NLS-1$
		assertEquals(EL_VALUE, elValue);
		
		//Test close Design Options
		
		bot.button("OK").click(); //$NON-NLS-1$
	//	waitForBlockingJobsAcomplished(VISUAL_REFRESH);
		
		//Check page content
		openPage();
		SWTBotExt botExt = new SWTBotExt();
		assertVisualEditorContains(new SWTBotWebBrowser(TEST_PAGE,botExt),
        "INPUT",
        new String[]{"value"},
        new String[]{EL_VALUE},
        TEST_PAGE);
		openPage("hello.jsp");
		assertVisualEditorContainsNodeWithValue(new SWTBotWebBrowser("hello.jsp",botExt),
        EL_VALUE,
        "hello.jsp");
		packageExplorer.openFile(JBT_TEST_PROJECT_NAME,
		    "WebContent",
		    TEST_FOLDER,
		    TEST_PAGE_FOR_FOLDER+".jsp");
		assertVisualEditorContains(new SWTBotWebBrowser(TEST_PAGE_FOR_FOLDER+".jsp",botExt),
        "INPUT",
        new String[]{"value"},
        new String[]{"#{user.name}"},
        TEST_PAGE_FOR_FOLDER+".jsp");
		openPage();
		bot.toolbarButtonWithTooltip(PAGE_DESIGN).click();
		bot.shell(PAGE_DESIGN).activate();
		
		//Test choose Substituted EL tab
		
		bot.tabItem(SUBSTITUTED_EL).activate();
		
		//Delete item
		
		bot.table().select(0);
		bot.button("Remove").click(); //$NON-NLS-1$
		bot.button("OK").click(); //$NON-NLS-1$
		
		//Check VPE content
		assertVisualEditorContains(new SWTBotWebBrowser(TEST_PAGE,botExt),
        "INPUT",
        new String[]{"value"},
        new String[]{"#{user.name}"},
        TEST_PAGE);
		
		assertVisualEditorContainsNodeWithValue(new SWTBotWebBrowser("hello.jsp",botExt),
		    "#{user.name}",
        "hello.jsp");
		
		assertVisualEditorContains(new SWTBotWebBrowser(TEST_PAGE_FOR_FOLDER+".jsp",botExt),
        "INPUT",
        new String[]{"value"},
        new String[]{"#{user.name}"},
        TEST_PAGE_FOR_FOLDER+".jsp");
	}
	
	@Override
	public void tearDown() throws Exception {
		bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).setFocus();
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
		innerBot.tree().expandNode(JBT_TEST_PROJECT_NAME).expandNode("WebContent") //$NON-NLS-1$
		.getNode(TEST_FOLDER).select();
		bot.menu("Edit").menu("Delete").click(); //$NON-NLS-1$ //$NON-NLS-2$
		bot.shell("Confirm Delete").activate(); //$NON-NLS-1$
		bot.button("OK").click(); //$NON-NLS-1$
		delay();
		super.tearDown();
	}
		
}
