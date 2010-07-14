package org.jboss.tools.vpe.ui.bot.test.editor.pagedesign;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.tools.vpe.ui.bot.test.Activator;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

public abstract class PageDesignTestCase extends VPEAutoTestCase{
	
	final static String PAGE_DESIGN = "Page Design Options"; //$NON-NLS-1$

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
	
	@Override
	protected String getPathToResources(String testPage) throws IOException {
		String filePath = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile()+"resources/pagedesign/"+testPage;  //$NON-NLS-1$//$NON-NLS-2$
		File file = new File(filePath);
		if (!file.isFile()) {
			filePath = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile()+"pagedesign/"+testPage; //$NON-NLS-1$ //$NON-NLS-2$
		}
		return filePath;
	}
	
}
