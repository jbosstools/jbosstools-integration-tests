package org.jboss.tools.vpe.ui.bot.test.editor.pagedesign;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.test.WidgetVariables;

public abstract class SubstitutedELTestCase extends PageDesignTestCase{
	
	static final String ADD_EL = "Add EL Reference"; //$NON-NLS-1$
	static final String SUBSTITUTED_EL = "Substituted EL expressions"; //$NON-NLS-1$
	private SWTBotEclipseEditor editor;
	private String editorText;


	SWTBotEclipseEditor getEditor() {
		return editor;
	}

	void setEditor(SWTBotEclipseEditor editor) {
		this.editor = editor;
	}

	String getEditorText() {
		return editorText;
	}

	void setEditorText(String editorText) {
		this.editorText = editorText;
	}
	
	@Override
	protected void closeUnuseDialogs() {
		try {
			bot.shell(ADD_EL).close();
		} catch (WidgetNotFoundException e) {
		}
		super.closeUnuseDialogs();
	}
	
	@Override
	protected boolean isUnuseDialogOpened() {
		boolean isOpened = super.isUnuseDialogOpened();
		try {
			bot.shell(ADD_EL).activate();
			isOpened = true;
		} catch (WidgetNotFoundException e) {
		}
		return isOpened;
	}
	
	
	void checkVPEForTestPage(String testPage) throws Throwable{
		performContentTestByDocument(testPage, bot.multiPageEditorByTitle(TEST_PAGE));
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		bot.toolbarButtonWithTooltip(PAGE_DESIGN).click();
		bot.shell(PAGE_DESIGN).activate();
		bot.tabItem(SUBSTITUTED_EL).activate();
		clearELTable(bot.table());
		try {
			bot.button("OK").click(); //$NON-NLS-1$
		} catch (WidgetNotFoundException e) {
			bot.shell(PAGE_DESIGN).close();
		}
		super.tearDown();
	}
	
	void clearELTable(SWTBotTable table){
		try {
			while (true) {
				table.select(0);
				bot.button("Remove").click(); //$NON-NLS-1$
			}
		} catch (IllegalArgumentException e) {
		}
		catch (WidgetNotFoundException e) {
		}
	}
	
	void checkVPEForHelloPage(String testHelloPage) throws Throwable{
		
		//Open hello page

		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
		SWTBotTree tree = innerBot.tree();
		tree.expandNode(JBT_TEST_PROJECT_NAME)
		.expandNode("WebContent").expandNode("pages").getNode("hello.jsp").doubleClick(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		SWTBotEditor editor = bot.editorByTitle("hello.jsp"); //$NON-NLS-1$
		
		//Check page content
		
		try {
			performContentTestByDocument(testHelloPage, bot.multiPageEditorByTitle("hello.jsp")); //$NON-NLS-1$
		} catch (Throwable e) {
			throw e;
		}finally{
			editor.close();
			openPage();
		}
	}
	
}
