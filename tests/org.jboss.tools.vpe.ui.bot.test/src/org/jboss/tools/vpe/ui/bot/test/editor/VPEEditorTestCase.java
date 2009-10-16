package org.jboss.tools.vpe.ui.bot.test.editor;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.jboss.tools.vpe.ui.bot.test.Activator;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

public abstract class VPEEditorTestCase extends VPEAutoTestCase{

	private String editorText;
	private SWTBotEclipseEditor editor;
	
	String getEditorText() {
		return editorText;
	}

	void setEditorText(String textEditor) {
		this.editorText = textEditor;
	}

	SWTBotEclipseEditor getEditor() {
		return editor;
	}

	void setEditor(SWTBotEclipseEditor editor) {
		this.editor = editor;
	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}
	
	@Override
	protected void closeUnuseDialogs() {
	}
	
	@Override
	protected String getPathToResources(String testPage) throws IOException {
		String filePath = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile()+"resources/editor/"+testPage;
		File file = new File(filePath);
		if (!file.isFile()) {
			filePath = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile()+"editor/"+testPage;
		}
		return filePath;
	}
	
	void openPage(){
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
		SWTBotTree tree = innerBot.tree();
		tree.expandNode(JBT_TEST_PROJECT_NAME)
		.expandNode("WebContent").expandNode("pages").getNode(TEST_PAGE).doubleClick();
	}
	
	void checkVPE(String testPage) throws Throwable{
		waitForBlockingJobsAcomplished(VISUAL_UPDATE);
		performContentTestByDocument(testPage, bot.multiPageEditorByTitle(TEST_PAGE));
	}

	@Override
	protected void tearDown() throws Exception {

		//Restore page state before tests

		editor.setFocus();
		bot.menu("Edit").menu("Select All").click();
		bot.menu("Edit").menu("Delete").click();
		editor.setText(editorText);
		editor.save();
		super.tearDown();
	}

}
