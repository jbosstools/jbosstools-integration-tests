package org.jboss.tools.usercase.ticketmonster.ui.bot.test.part2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.jboss.reddeer.eclipse.condition.ProblemsExists;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.maven.reddeer.wizards.AddRepositoryDialog;
import org.jboss.tools.maven.reddeer.wizards.ConfigureMavenRepositoriesWizard;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard.DownloadEAPRuntimeThirdPage;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard.DownloadRuntimeDialog;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard.DownloadRuntimeFirstPage;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard.DownloadRuntimeSecondPage;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard.DownloadRuntimeThirdPage;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard.ExampleRequirement;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard.NewExampleWizard;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard.NewExampleWizardFirstPage;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard.NewExampleWizardSecondPage;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard.NewExampleWizardThirdPage;
import org.junit.After;
import org.junit.Test;

public class Chapter8CreateJavaEE6ProjectTest extends AbstractPart2Test{
	
	@After
	public void deleteProjects(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		for(Project p: pe.getProjects()){
			p.delete(true);
		}
		removeAllRuntimes();
	}
	
	@Test
	public void javaEEProjectnoRuntimeSelected(){
		NewExampleWizard wz = new NewExampleWizard();
		wz.open(JAVA_EE_PROJECT);
		NewExampleWizardFirstPage fp = (NewExampleWizardFirstPage)wz.getWizardPage();
		List<ExampleRequirement> reqs = fp.getRequirements();
		assertFalse(reqs.get(0).isMet());
		fp.setTargetRuntime("<none>");
		assertFalse(reqs.get(0).isMet());
		wz.selectPage(2);
		NewExampleWizardThirdPage tp = (NewExampleWizardThirdPage)wz.getWizardPage();
		String projectName = tp.getArtifactID();
		Table table = tp.getTableSuffix();
		assertEquals("false", table.getItem("enterprise").getText(1));
		wz.finish(projectName);
		new WaitUntil(new ProblemsExists(false));
	}
	
	@Test
	public void createTicketMonsterNoRuntime(){
		NewExampleWizard wz = new NewExampleWizard();
		wz.open(JAVA_EE_PROJECT);
		NewExampleWizardFirstPage fp = (NewExampleWizardFirstPage)wz.getWizardPage();
		List<ExampleRequirement> reqs = fp.getRequirements();
		assertFalse(reqs.get(0).isMet());
		wz.selectPage(1);
		NewExampleWizardSecondPage sp = (NewExampleWizardSecondPage)wz.getWizardPage();
		sp.setProjectName(TICKET_MONSTER_NAME);
		sp.setPackage(TICKET_MONSTER_PACKAGE);
		wz.selectPage(2);
		NewExampleWizardThirdPage tp = (NewExampleWizardThirdPage)wz.getWizardPage();
		assertEquals(TICKET_MONSTER_PACKAGE,tp.getGroupID());
		assertEquals(TICKET_MONSTER_NAME, tp.getArtifactID());
		assertEquals(TICKET_MONSTER_PACKAGE, tp.getPackage());
		Table table = tp.getTableSuffix();
		assertEquals("false", table.getItem("enterprise").getText(1));
		wz.finish(TICKET_MONSTER_NAME);
		new WaitUntil(new ProblemsExists(false));
		ProjectExplorer pe = new ProjectExplorer();
		pe.getProject(TICKET_MONSTER_NAME);
	}
	
	@Test
	public void downloadAndInstallRuntime_AS7(){
		NewExampleWizard wz = new NewExampleWizard();
		wz.open(JAVA_EE_PROJECT);
		NewExampleWizardFirstPage fp = (NewExampleWizardFirstPage)wz.getWizardPage();
		List<ExampleRequirement> reqs = fp.getRequirements();
		assertFalse(reqs.get(0).isMet());
		downloadAndInstallRuntime(JBOSS_AS_71_NAME, wz, false);
		fp.setTargetRuntime(JBOSS_AS_71_RUNTIME);
		reqs = fp.getRequirements();
		for(ExampleRequirement r: reqs){
			assertTrue(r.isMet());
		}
		wz.selectPage(2);
		NewExampleWizardThirdPage tp = (NewExampleWizardThirdPage)wz.getWizardPage();
		String projectName = tp.getArtifactID();
		Table table = tp.getTableSuffix();
		assertEquals("false", table.getItem("enterprise").getText(1));
		wz.finish(projectName);
		new WaitUntil(new ProblemsExists(false));
	}
	
	@Test
	public void installAndConfigureRuntime_EAP6(){
		removeRepos();
		
		NewExampleWizard wz = new NewExampleWizard();
		wz.open(JAVA_EE_PROJECT);
		NewExampleWizardFirstPage fp = (NewExampleWizardFirstPage)wz.getWizardPage();
		List<ExampleRequirement> reqs = fp.getRequirements();
		assertFalse(reqs.get(0).isMet());
		downloadAndInstallRuntime(EAP_61_NAME, wz, true);
		reqs = fp.getRequirements();
		assertFalse(reqs.get(0).isMet());
		installRuntime(EAP_61_NAME, wz, true);
		fp.setTargetRuntime(EAP_61_RUNTIME);
		AbstractWait.sleep(1000);
		AddRepositoryDialog repoPage= fp.addEAPMavenRepositoryUsingWarningLink();
		assertEquals(RED_HAT_REPO_ID,repoPage.getProfileID());
		assertEquals(RED_HAT_REPO_ID,repoPage.getRepositoryID());
		assertEquals(RED_HAT_REPO_NAME,repoPage.getRepositoryName());
		assertEquals(RED_HAT_REPO_URL,repoPage.getRepositoryURL());
		assertTrue(repoPage.isActiveByDefault());
		repoPage.ok();
		ConfigureMavenRepositoriesWizard repoDialog = new ConfigureMavenRepositoriesWizard();
		repoDialog.confirm();
		wz.selectPage(1);
		NewExampleWizardSecondPage sp = (NewExampleWizardSecondPage)wz.getWizardPage();
		assertEquals(JAVA_EE_PROJECT_A,sp.getProjectName());
		assertEquals(JAVA_EE_PROJECT_P,sp.getPackage());
		wz.selectPage(2);
		NewExampleWizardThirdPage tp = (NewExampleWizardThirdPage)wz.getWizardPage();
		assertEquals(JAVA_EE_PROJECT_G,tp.getGroupID());
		assertEquals(JAVA_EE_PROJECT_A,tp.getArtifactID());
		assertEquals(JAVA_EE_PROJECT_V,tp.getVersion());
		assertEquals(JAVA_EE_PROJECT_G,tp.getPackage());
		String projectName = tp.getArtifactID();
		Table table = tp.getTableSuffix();
		assertEquals("true", table.getItem("enterprise").getText(1));
		assertEquals(ENTERPRISE_BOM_VERSION, table.getItem("jboss-bom-enterprise-version").getText(1));
		assertEquals("Java EE 6 webapp project", table.getItem("name").getText(1));
		wz.finish(projectName);
		new WaitUntil(new ProblemsExists(false));
	}
	
	private void downloadAndInstallRuntime(String runtimeName,NewExampleWizard wz, boolean eap){
		NewExampleWizardFirstPage fp = (NewExampleWizardFirstPage)wz.getWizardPage();
		List<ExampleRequirement> reqs = fp.getRequirements();
		File downloadDir = new File(JBOSS_AS_DOWNLOAD_DIR);
		if(downloadDir.exists()){
			deleteRecursive(downloadDir);
		}
		downloadDir.mkdirs();
		
		DownloadRuntimeDialog downloadRD = reqs.get(0).downloadAndInstall();
		if(eap){
			downloadRD.eapDialog();
		} else {
			downloadRD.asDialog();
		}
		DownloadRuntimeFirstPage firstPage = (DownloadRuntimeFirstPage) downloadRD.getWizardPage();
		firstPage.selectRuntime(runtimeName);
		downloadRD.selectPage(1);
		DownloadRuntimeSecondPage secondPage = (DownloadRuntimeSecondPage)downloadRD.getWizardPage();
		secondPage.acceptLicense(true);
		downloadRD.selectPage(2);
		if(!eap){
			DownloadRuntimeThirdPage thirdPage = (DownloadRuntimeThirdPage)downloadRD.getWizardPage();
			thirdPage.setInstallFolder(downloadDir.getAbsolutePath());
			thirdPage.setDownloadFolder(downloadDir.getAbsolutePath());
		} else{
			DownloadEAPRuntimeThirdPage thirdPage = (DownloadEAPRuntimeThirdPage)downloadRD.getWizardPage();
			assertTrue(thirdPage.containsWarning());
		}
		downloadRD.finish();
	}
	
	
	private boolean deleteRecursive(File path){
        boolean ret = true;
        if (path.isDirectory()){
            for (File f : path.listFiles()){
                ret = ret && deleteRecursive(f);
            }
        }
        return ret && path.delete();
    }

}
