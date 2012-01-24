package org.jboss.tools.portlet.ui.bot.test.compatibility;

import static org.jboss.tools.portlet.ui.bot.matcher.factory.DefaultMatchersFactory.isNumberOfErrors;
import static org.jboss.tools.portlet.ui.bot.matcher.factory.WorkspaceMatchersFactory.isExistingProject;

import org.jboss.tools.portlet.ui.bot.task.importing.project.ExistingProjectImportTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.importing.project.ExistingProjectWizardPageFillingTask;
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
 * Tests that project from JBDS 4 can work in JBDS 5 without any problems. 
 * 
 * @author Lucia Jelinkova
 *
 */
@Require(server=@Server(version="5.0", operator=">", state=ServerState.Present), db=@DB, seam=@Seam)
public class JBDS4vs5Compatibility extends SWTTaskBasedTestCase {

	private static final String ZIP_FILE = "resources/org/jboss/tools/portlet/ui/bot/test/compatibility/jbds-compatibility.zip";
	
	private static final String CORE_PROJECT_NAME = "jbds-4-core";
	
	private static final String JSF_PROJECT_NAME = "jbds-4-jsf"; 
	
	private static final String SEAM_PROJECT_NAME = "jbds-4-seam"; 
	
	@Test
	public void testCompatibility(){
		doPerform(new ExistingProjectImportTask());
		doPerform(getWizardFillingTask());
		
		doAssertThatInWorkspace(CORE_PROJECT_NAME, isExistingProject());
		doAssertThatInWorkspace(JSF_PROJECT_NAME, isExistingProject());
		doAssertThatInWorkspace(SEAM_PROJECT_NAME, isExistingProject());
		doAssertThatInWorkspace(0, isNumberOfErrors());
	}

	private WizardFillingTask getWizardFillingTask(){
		WizardFillingTask wizard = new WizardFillingTask();
		wizard.addWizardPage(getWizardPage());
		return wizard;
	}

	private ExistingProjectWizardPageFillingTask getWizardPage(){
		ExistingProjectWizardPageFillingTask task = new ExistingProjectWizardPageFillingTask();
		task.setZipFilePath(getZIPFileLocation());
		task.setProjectNames(CORE_PROJECT_NAME, JSF_PROJECT_NAME, SEAM_PROJECT_NAME);
		task.setCopyProjectsIntoWorkspace(true);
		return task;
	}
	
	private String getZIPFileLocation(){
		return SWTUtilExt.getPathToFileWithinPlugin(Activator.PLUGIN_ID, ZIP_FILE);
	}
}
