package org.jboss.tools.arquillian.ui.bot.test.project;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.arquillian.ui.bot.reddeer.maven.UpdateMavenProjectDialog;
import org.jboss.tools.arquillian.ui.bot.reddeer.profile.AddArquillianProfilesDialog;
import org.jboss.tools.arquillian.ui.bot.test.AbstractArquillianTestCase;
import org.jboss.tools.maven.reddeer.profiles.SelectProfilesDialog;
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
		selectMavenProfile();
		forceMavenRepositoryUpdate();
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
	
	private void selectMavenProfile() {
		log.step("Select maven profile");
		SelectProfilesDialog dialog = new SelectProfilesDialog(PROJECT_NAME);
		dialog.open();
		dialog.activateProfile(PROFILE_NAME);
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(),TimePeriod.VERY_LONG);
	}
	
	private void forceMavenRepositoryUpdate() {
		log.step("Force Maven update snapshots/releases");
		UpdateMavenProjectDialog dialog = new UpdateMavenProjectDialog();
		dialog.open(getProject());
		dialog.forceUpdate(true);
		dialog.ok();
	}
}
