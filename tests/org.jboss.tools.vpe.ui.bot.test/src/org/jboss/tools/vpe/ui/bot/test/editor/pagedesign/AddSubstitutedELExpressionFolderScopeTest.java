package org.jboss.tools.vpe.ui.bot.test.editor.pagedesign;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.test.WidgetVariables;

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
		
		bot.menu("File").menu("New").menu("JSP File").click(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		bot.shell("New File JSP").activate(); //$NON-NLS-1$
		bot.textWithLabel("Name*").setText(TEST_PAGE_FOR_FOLDER); //$NON-NLS-1$
		bot.button("Finish").click(); //$NON-NLS-1$
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
		
		checkVPEForTestPage("AddSubstitutedELExpressionTestPage.xml"); //$NON-NLS-1$
		checkVPEForHelloPage("AddSubstitutedELExpressionHelloPage.xml"); //$NON-NLS-1$
		checkVPEForTestPageFolder("RemoveSubstitutedELExpressionTestPage.xml"); //$NON-NLS-1$
		
		//Test check the page in the same folder
		
		
		
		//Test open Page Design Options
		
		bot.toolbarButtonWithTooltip(PAGE_DESIGN).click();
		bot.shell(PAGE_DESIGN).activate();
		
		//Test choose Substituted EL tab
		
		bot.tabItem(SUBSTITUTED_EL).activate();
		
		//Delete item
		
		bot.table().select(0);
		bot.button("Remove").click(); //$NON-NLS-1$
		bot.button("OK").click(); //$NON-NLS-1$
		
		//Check VPE content
		
		checkVPEForTestPage("RemoveSubstitutedELExpressionTestPage.xml"); //$NON-NLS-1$
		checkVPEForHelloPage("RemoveSubstitutedELExpressionHelloPage.xml"); //$NON-NLS-1$
		checkVPEForTestPageFolder("RemoveSubstitutedELExpressionTestPage.xml"); //$NON-NLS-1$
	}
	
	@Override
	protected void tearDown() throws Exception {
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
	
	private void checkVPEForTestPageFolder(String testPageFolder) throws Throwable{
		
		//Open hello page

		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
		SWTBotTree tree = innerBot.tree();
		tree.expandNode(JBT_TEST_PROJECT_NAME)
		.expandNode("WebContent").expandNode(TEST_FOLDER) //$NON-NLS-1$
		.getNode(TEST_PAGE_FOR_FOLDER+".jsp").doubleClick(); //$NON-NLS-1$
		SWTBotEditor editor = bot.editorByTitle(TEST_PAGE_FOR_FOLDER+".jsp"); //$NON-NLS-1$
	//	waitForBlockingJobsAcomplished(VISUAL_REFRESH);
		
		//Check page content
		
		try {
			performContentTestByDocument(testPageFolder, bot.multiPageEditorByTitle(TEST_PAGE_FOR_FOLDER+".jsp")); //$NON-NLS-1$
		} catch (Throwable e) {
			throw e;
		}finally{
			editor.close();
			openPage();
		}

	}
	
}
