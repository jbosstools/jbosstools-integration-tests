package org.jboss.tools.arquillian.ui.bot.test.project;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.m2e.core.ui.preferences.MavenErrorPreferencePage;
import org.jboss.reddeer.eclipse.m2e.core.ui.preferences.MavenErrorPreferencePage.MavenErrorSeverity;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.arquillian.ui.bot.reddeer.support.AddArquillianSupportDialog;
import org.jboss.tools.arquillian.ui.bot.test.AbstractArquillianTestCase;
import org.jboss.tools.maven.reddeer.wizards.MavenProjectWizard;
import org.jboss.tools.maven.reddeer.wizards.MavenProjectWizardThirdPage;
import org.junit.Before;
import org.junit.Test;

/**
 * Create simple Maven project and enable Arquillian support for it. <br>
 * 
 * Check that:
 * <ul>
 * 	<li> there are no errors or warnings in Problems view
 * </ul>
 * @author Lucia Jelinkova, Len DiMaggio
 *
 */
@OpenPerspective(JavaPerspective.class)
public class CreateArquillianProject extends AbstractArquillianTestCase{
	
	private WorkbenchPreferenceDialog preferencesDialog = new WorkbenchPreferenceDialog();
	private MavenErrorPreferencePage theMavenPrefPage = new MavenErrorPreferencePage();

	private static final Logger log = Logger.getLogger(CreateArquillianProject.class);
	
	@Before
	public void setupProject(){

		/* Set the Maven->Error preference to ignore plugin lifecyle errors */
		log.info("Set the Maven->Error preference to ignore plugin lifecyle errors");
		preferencesDialog.open();
		preferencesDialog.select(theMavenPrefPage);		
		MavenErrorPreferencePage theMavenPrefPage = new MavenErrorPreferencePage();
		theMavenPrefPage.setPluginExecution(MavenErrorSeverity.IGNORE);
		preferencesDialog.ok();
		
		log.step("Create maven project with name " + PROJECT_NAME);
		MavenProjectWizard wizard = new MavenProjectWizard();
		wizard.open();
		
		// Check "Create a simple project"
		new CheckBox().toggle(true);
		wizard.next();
		
		MavenProjectWizardThirdPage thirdPage = new MavenProjectWizardThirdPage();
		thirdPage.setGAV(PROJECT_NAME, PROJECT_NAME, null);
		
		wizard.finish();
		
		forceMavenRepositoryUpdate();
	}

	@Test
	public void testProjectCreation(){
		addArquillianSupport();
		checkProblems();
	}

	private void addArquillianSupport() {
		log.step("Add Arquillian support");
		Project project = getProject();
		
		AddArquillianSupportDialog dialog = new AddArquillianSupportDialog();
		dialog.open(project);
		dialog.ok();
	}
}
