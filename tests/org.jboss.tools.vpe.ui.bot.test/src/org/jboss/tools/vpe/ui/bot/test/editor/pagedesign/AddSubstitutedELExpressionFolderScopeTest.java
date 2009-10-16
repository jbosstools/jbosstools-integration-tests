package org.jboss.tools.vpe.ui.bot.test.editor.pagedesign;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.test.WidgetVariables;

public class AddSubstitutedELExpressionFolderScopeTest extends SubstitutedELTestCase{
	
	private static final String TEST_PAGE_FOR_FOLDER = "testPage";
	private static final String TEST_FOLDER = "testFolder";

	//Do not edit this variable as test will fail
	private static final String EL_VALUE = "Any Name";
	
	public void testAddSubstitutedELExpressionFolderScope() throws Throwable{
		
		//Test open page
		openPage();
		setEditor(bot.editorByTitle(TEST_PAGE).toTextEditor());
		setEditorText(getEditor().getText());
		
		//Test create new folder
		
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
		innerBot.tree().expandNode(JBT_TEST_PROJECT_NAME).expandNode("WebContent")
		.select();
		
		bot.menu("File").menu("New").menu("Folder").click();
		
		bot.shell("New Folder").activate();
		bot.textWithLabel("Folder name:").setText(TEST_FOLDER);
		bot.button("Finish").click();
		
		//Test create page in new folder
		
		innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
		innerBot.tree().expandNode(JBT_TEST_PROJECT_NAME).expandNode("WebContent")
		.getNode(TEST_FOLDER).select();
		
		bot.menu("File").menu("New").menu("JSP File").click();
		
		bot.shell("New File JSP").activate();
		bot.textWithLabel("Name*").setText(TEST_PAGE_FOR_FOLDER);
		bot.button("Finish").click();
		delay();
		SWTBotEclipseEditor editorForTestPage = bot.editorByTitle(TEST_PAGE_FOR_FOLDER+".jsp").toTextEditor();
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

		bot.button("Add").click();
		delay();
		bot.shell(ADD_EL).activate();
		bot.textWithLabel("El Name*").setText("user.name");
		bot.textWithLabel("Value").setText(EL_VALUE);
		bot.radio("Folder: Any Page at the Same Folder").click();
		bot.button("Finish").click();
		
		//Test check table with ELs
		
		bot.shell(PAGE_DESIGN).activate();
		SWTBotTable table = bot.table();
		String elName = table.cell(0, "El Expression");
		String scope = table.cell(0, "Scope");
		String elValue = table.cell(0, "Value");
		assertEquals("user.name",elName);
		assertEquals("Folder",scope);
		assertEquals(EL_VALUE, elValue);
		
		//Test close Design Options
		
		bot.button("OK").click();
	//	waitForBlockingJobsAcomplished(VISUAL_REFRESH);
		
		//Check page content
		
		checkVPEForTestPage("AddSubstitutedELExpressionTestPage.xml");
		checkVPEForHelloPage("AddSubstitutedELExpressionHelloPage.xml");
		checkVPEForTestPageFolder("RemoveSubstitutedELExpressionTestPage.xml");
		
		//Test check the page in the same folder
		
		
		
		//Test open Page Design Options
		
		bot.toolbarButtonWithTooltip(PAGE_DESIGN).click();
		bot.shell(PAGE_DESIGN).activate();
		
		//Test choose Substituted EL tab
		
		bot.tabItem(SUBSTITUTED_EL).activate();
		
		//Delete item
		
		bot.table().select(0);
		bot.button("Remove").click();
		bot.button("OK").click();
		
		//Check VPE content
		
		checkVPEForTestPage("RemoveSubstitutedELExpressionTestPage.xml");
		checkVPEForHelloPage("RemoveSubstitutedELExpressionHelloPage.xml");
		checkVPEForTestPageFolder("RemoveSubstitutedELExpressionTestPage.xml");
	}
	
	@Override
	protected void tearDown() throws Exception {
		bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).setFocus();
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
		innerBot.tree().expandNode(JBT_TEST_PROJECT_NAME).expandNode("WebContent")
		.getNode(TEST_FOLDER).select();
		bot.menu("Edit").menu("Delete").click();
		bot.shell("Confirm Delete").activate();
		bot.button("OK").click();
		delay();
		super.tearDown();
	}
	
	private void checkVPEForTestPageFolder(String testPageFolder) throws Throwable{
		
		//Open hello page

		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
		SWTBotTree tree = innerBot.tree();
		tree.expandNode(JBT_TEST_PROJECT_NAME)
		.expandNode("WebContent").expandNode(TEST_FOLDER)
		.getNode(TEST_PAGE_FOR_FOLDER+".jsp").doubleClick();
		SWTBotEditor editor = bot.editorByTitle(TEST_PAGE_FOR_FOLDER+".jsp");
	//	waitForBlockingJobsAcomplished(VISUAL_REFRESH);
		
		//Check page content
		
		try {
			performContentTestByDocument(testPageFolder, bot.multiPageEditorByTitle(TEST_PAGE_FOR_FOLDER+".jsp"));
		} catch (Throwable e) {
			throw e;
		}finally{
			editor.close();
			openPage();
		}

	}
	
}
