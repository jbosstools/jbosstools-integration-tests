package org.jboss.tools.vpe.ui.bot.test.wizard;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.jboss.tools.vpe.editor.template.VpeAnyData;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.osgi.framework.Bundle;

public class ExportUnknownTagsWizardTest extends VPEAutoTestCase {

	private final String STORED_TAGS_PATH = "storedTags.xml"; //$NON-NLS-1$
	
	public ExportUnknownTagsWizardTest() {
	}

	@Override
	protected void closeUnuseDialogs() {

	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}
	
	public void testExportWizard() throws Throwable {
		/*
		 * Load templates and add them to the model 
		 */
		Bundle bundle = Platform.getBundle("org.jboss.tools.vpe.ui.bot.test"); //$NON-NLS-1$
		URL url = bundle.getEntry("/resources"); //$NON-NLS-1$
		url = FileLocator.resolve(url);
		IPath path = new Path(url.getPath());
		File file = path.append(STORED_TAGS_PATH).toFile();
        assertTrue("File '" + file.getAbsolutePath() +"' does not exist.", file.exists()); //$NON-NLS-1$ //$NON-NLS-2$
		List<VpeAnyData> templates = VpeTemplateManager.getInstance().getAnyTemplates(new Path(file.getAbsolutePath()));
		VpeTemplateManager.getInstance().setAnyTemplates(templates);
		/*
		 * Open wizard page
		 */
		bot.menu("File").menu("Export...").click(); //$NON-NLS-1$ //$NON-NLS-2$
		bot.shell("Export").activate(); //$NON-NLS-1$
		SWTBotTree importTree = bot.tree();
		importTree.expandNode("Other").select("Unknown tags templates"); //$NON-NLS-1$ //$NON-NLS-2$
		bot.button(WidgetVariables.NEXT_BUTTON).click();
		/*
		 * Check table values
		 */
		String taglib = bot.table().cell(0, 0);
        assertEquals("Wrong table value.", "taglibName:tagName", taglib); //$NON-NLS-1$  //$NON-NLS-2$
        taglib = bot.table().cell(1, 0);
        assertEquals("Wrong table value.", "lib:tag", taglib); //$NON-NLS-1$ //$NON-NLS-2$
        assertFalse("Finish button should be disabled.", //$NON-NLS-1$ 
        		bot.button(WidgetVariables.FINISH_BUTTON).isEnabled());
        /*
         * Enter the path to store the tags.
         */
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				JBT_TEST_PROJECT_NAME);
        path = project.getLocation().append(STORED_TAGS_PATH);
        bot.text().setText(path.toOSString());
        /*
         * Check that finish button is enabled and press it.
         */
        assertTrue("Finish button should be enabled.", //$NON-NLS-1$ 
        bot.button(WidgetVariables.FINISH_BUTTON).isEnabled());
        bot.button(WidgetVariables.FINISH_BUTTON).click();
        /*
         * Check that file has been created and saved.
         */
		 file =  new File(path.toOSString());
		 assertTrue("File '" + file.getAbsolutePath() +"' does not exist.", file.exists()); //$NON-NLS-1$ //$NON-NLS-2$
	}

}
