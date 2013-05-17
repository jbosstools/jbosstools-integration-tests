package org.jboss.tools.portlet.ui.bot.test.template;

import static org.jboss.tools.portlet.ui.bot.matcher.factory.DefaultMatchersFactory.isNumberOfErrors;
import static org.jboss.tools.portlet.ui.bot.matcher.factory.WorkspaceMatchersFactory.isExistingProject;

import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.tools.portlet.ui.bot.test.Activator;
import org.jboss.tools.portlet.ui.bot.test.testcase.SWTTaskBasedTestCase;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.DB;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Seam;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.junit.Test;

/**
 * Tests that project from previous versions of JBDS can work in actual version without any problems. Note
 * that the project need to have EPP-5.2.2 (Seam 2.2) as its target runtime at the time of import.
 * 
 * @author Petr Suchy
 * 
 */
@Require(server = @Server(version = "5.2.2", state = ServerState.Present), db = @DB, seam = @Seam)
public abstract class JBDSCompatibilityGateinTemplate extends SWTTaskBasedTestCase {
	
	protected abstract String getZipFilePath();
	protected abstract String[] getProjectNames(); 

	@Test
	public void testCompatibility() {
		
		ExternalProjectImportWizardDialog dialog = new ExternalProjectImportWizardDialog();
		dialog.open();
		WizardProjectsImportPage wizard = dialog.getFirstPage();
		wizard.setArchiveFile(getZIPFileLocation());
		dialog.finish();
		
		String[] projects = getProjectNames();
		doAssertThatInWorkspace(projects[0], isExistingProject());
		doAssertThatInWorkspace(projects[1], isExistingProject());
		doAssertThatInWorkspace(projects[2], isExistingProject());
		doAssertThatInWorkspace(0, isNumberOfErrors());
	}
	
	private String getZIPFileLocation() {
		return SWTUtilExt.getPathToFileWithinPlugin(Activator.PLUGIN_ID, getZipFilePath());
	}
	
}
