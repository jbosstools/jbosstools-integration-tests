package org.jboss.tools.arquillian.ui.bot.test.project;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.tools.arquillian.ui.bot.reddeer.profile.AddArquillianProfilesDialog;
import org.jboss.tools.arquillian.ui.bot.test.AbstractArquillianTestCase;
import org.junit.Test;


/**
 * Add remote Wildfly Arquillian profile using right-click on project. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class AddArquillianProfile extends AbstractArquillianTestCase {

	private static final Logger log = Logger.getLogger(AddArquillianProfile.class);
	
	@Test
	public void testAddingProfile(){
		addProfile();
		checkProblems();
	}
	
	private void addProfile(){
		log.step("Add Arquillian profile");
		PackageExplorer explorer = new PackageExplorer();
		explorer.open();
		Project project = explorer.getProject(PROJECT_NAME);
		
		AddArquillianProfilesDialog dialog = new AddArquillianProfilesDialog();
		dialog.open(project);
		dialog.selectProfile(PROFILE_NAME);
		dialog.ok();
	}
}
