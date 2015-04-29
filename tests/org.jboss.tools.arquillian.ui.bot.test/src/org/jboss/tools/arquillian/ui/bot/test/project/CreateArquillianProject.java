package org.jboss.tools.arquillian.ui.bot.test.project;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.swt.impl.button.CheckBox;
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
 * @author Lucia Jelinkova
 *
 */
public class CreateArquillianProject extends AbstractArquillianTestCase{

	private static final Logger log = Logger.getLogger(CreateArquillianProject.class);
	
	@Before
	public void setupProject(){
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
