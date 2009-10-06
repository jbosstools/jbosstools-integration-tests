package org.jboss.tools.vpe.ui.bot.test.editor.pagedesign;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.jboss.tools.vpe.ui.bot.test.Activator;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

public abstract class PageDesignTestCase extends VPEAutoTestCase{
	
	final static String PAGE_DESIGN = "Page Design Options";

	@Override
	protected void closeUnuseDialogs() {
		try {
			bot.shell(PAGE_DESIGN).close();
		} catch (WidgetNotFoundException e) {
		}
	}

	@Override
	protected boolean isUnuseDialogOpened() {
		boolean isOpened = false;
		try {
			bot.shell(PAGE_DESIGN).activate();
			isOpened = true;
		} catch (WidgetNotFoundException e) {
		}
		return isOpened;
	}
	
	void closePage(){
		bot.editorByTitle(TEST_PAGE).close();
	}
	
	void openPage(){
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
		SWTBotTree tree = innerBot.tree();
		tree.expandNode(JBT_TEST_PROJECT_NAME)
		.expandNode("WebContent").expandNode("pages").getNode(TEST_PAGE).doubleClick();
	}
	
	@Override
	protected String getPathToResources(String testPage) throws IOException {
		String filePath = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile()+"resources/pagedesign/"+testPage;
		File file = new File(filePath);
		if (!file.isFile()) {
			filePath = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile()+"pagedesign/"+testPage;
		}
		return filePath;
	}
	
}
