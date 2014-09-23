package org.jboss.tools.usercase.ticketmonster.ui.bot.test.part2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jboss.reddeer.eclipse.condition.ProblemsExists;
import org.jboss.reddeer.eclipse.condition.ProblemsExists.ProblemType;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.central.reddeer.wizards.JBossCentralProjectWizard;
import org.jboss.tools.maven.reddeer.maven.ui.preferences.ConfiguratorPreferencePage;
import org.jboss.tools.maven.reddeer.preferences.MavenUserPreferencePage;
import org.jboss.tools.maven.reddeer.project.examples.wizard.ArchetypeExamplesWizardFirstPage;
import org.jboss.tools.maven.reddeer.project.examples.wizard.ArchetypeExamplesWizardPage;
import org.jboss.tools.maven.reddeer.project.examples.wizard.ExampleRequirement;
import org.jboss.tools.maven.reddeer.project.examples.wizard.NewProjectExamplesStacksRequirementsPage;
import org.jboss.tools.maven.reddeer.wizards.AddRepositoryDialog;
import org.jboss.tools.maven.reddeer.wizards.ConfigureMavenRepositoriesWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class Chapter8CreateJavaEE6ProjectTest extends AbstractPart2Test{
	
	private static int testNumber = 0;
	
	@Before
	public void setMavenUserSettings(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		MavenUserPreferencePage mpreferences = new MavenUserPreferencePage();
		preferenceDialog.select(mpreferences);
		String path = new File("target/classes/settings"+testNumber+".xml").getAbsolutePath();
		mpreferences.setUserSettings(path);
		mpreferences.ok();
		preferenceDialog.open();
		ConfiguratorPreferencePage pp = new ConfiguratorPreferencePage();
		preferenceDialog.select(pp);
		ConfigureMavenRepositoriesWizard cw = pp.configureRepositories();
		cw.removeAllRepos();
		String repo = cw.chooseRepositoryFromList("jboss-public-repository", true);
		cw.confirm();
		cw = pp.configureRepositories();
		cw.removeRepo(repo);
		cw.confirm();
		pp.ok();
		changeLocalRepo(path);
		testNumber++;
		removeAllRuntimes();
	}
	
	private void changeLocalRepo(String settingsPath){
		File settings = new File(settingsPath);
		try	{
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(settings);
			Text a = doc.createTextNode(new File("target/classes/repository"+testNumber).getAbsolutePath()); 
			Element p = doc.createElement("localRepository"); 
			p.appendChild(a); 
			NodeList nodes = doc.getElementsByTagName("settings");
			NodeList child = nodes.item(0).getChildNodes();
			for(int i=0;i<child.getLength();i++){
				if(child.item(i).getNodeName().equals("localRepository")){
					return;
				}
			}
			nodes.item(0).appendChild(p);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			Result output = new StreamResult(settings);
			Source input = new DOMSource(doc);
			transformer.transform(input, output);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
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
		JBossCentralProjectWizard wz = new JBossCentralProjectWizard();
		wz.open(JAVA_EE_PROJECT);
		NewProjectExamplesStacksRequirementsPage fp = (NewProjectExamplesStacksRequirementsPage)wz.getWizardPage(0);
		List<ExampleRequirement> reqs = fp.getRequirements();
		assertFalse(reqs.get(0).isMet());
		fp.setTargetRuntime("<none>");
		assertFalse(reqs.get(0).isMet());
		ArchetypeExamplesWizardPage tp = (ArchetypeExamplesWizardPage)wz.getWizardPage(2);
		String projectName = tp.getArtifactID();
		Table table = tp.getTableSuffix();
		assertEquals("false", table.getItem("enterprise").getText(1));
		wz.finish(projectName);
		new WaitWhile(new ProblemsExists(ProblemType.ERROR), TimePeriod.LONG);
	}
	
	@Test
	public void createTicketMonsterNoRuntime(){
		JBossCentralProjectWizard wz = new JBossCentralProjectWizard();
		wz.open(JAVA_EE_PROJECT);
		NewProjectExamplesStacksRequirementsPage fp = (NewProjectExamplesStacksRequirementsPage)wz.getWizardPage(0);
		List<ExampleRequirement> reqs = fp.getRequirements();
		assertFalse(reqs.get(0).isMet());
		ArchetypeExamplesWizardFirstPage sp = (ArchetypeExamplesWizardFirstPage)wz.getWizardPage(1);
		sp.setProjectName(TICKET_MONSTER_NAME);
		sp.setPackage(TICKET_MONSTER_PACKAGE);
		ArchetypeExamplesWizardPage tp = (ArchetypeExamplesWizardPage)wz.getWizardPage(2);
		assertEquals(TICKET_MONSTER_PACKAGE,tp.getGroupID());
		assertEquals(TICKET_MONSTER_NAME, tp.getArtifactID());
		assertEquals(TICKET_MONSTER_PACKAGE, tp.getPackage());
		Table table = tp.getTableSuffix();
		assertEquals("false", table.getItem("enterprise").getText(1));
		wz.finish(TICKET_MONSTER_NAME);
		new WaitWhile(new ProblemsExists(ProblemType.ERROR), TimePeriod.LONG);
		ProjectExplorer pe = new ProjectExplorer();
		assertTrue("Project is missing in project explorer",pe.containsProject(TICKET_MONSTER_NAME));
	}
	
	@Test
	public void downloadAndInstallRuntime_AS7(){
		JBossCentralProjectWizard wz = new JBossCentralProjectWizard();
		wz.open(JAVA_EE_PROJECT);
		NewProjectExamplesStacksRequirementsPage fp = (NewProjectExamplesStacksRequirementsPage)wz.getWizardPage(0);
		List<ExampleRequirement> reqs = fp.getRequirements();
		assertFalse(reqs.get(0).isMet());
		downloadAndInstallRuntime(JBOSS_AS_71_NAME, wz, false);
		fp.setTargetRuntime(JBOSS_AS_71_RUNTIME);
		reqs = fp.getRequirements();
		for(ExampleRequirement r: reqs){
			assertTrue(r.isMet());
		}
		ArchetypeExamplesWizardPage tp = (ArchetypeExamplesWizardPage)wz.getWizardPage(2);
		String projectName = tp.getArtifactID();
		Table table = tp.getTableSuffix();
		assertEquals("false", table.getItem("enterprise").getText(1));
		wz.finish(projectName);
		new WaitWhile(new ProblemsExists(ProblemType.ERROR), TimePeriod.LONG);;
	}
	
	@Test
	public void downloadAndInstallRuntime_EAP6(){
		removeRepos();
		
		JBossCentralProjectWizard wz = new JBossCentralProjectWizard();
		wz.open(JAVA_EE_PROJECT);
		NewProjectExamplesStacksRequirementsPage fp = (NewProjectExamplesStacksRequirementsPage)wz.getWizardPage(0);
		List<ExampleRequirement> reqs = fp.getRequirements();
		assertFalse(reqs.get(0).isMet());
		downloadAndInstallRuntime(EAP_61_NAME, wz, true);
		reqs = fp.getRequirements();
		assertTrue(reqs.get(0).isMet());
		fp.setTargetRuntime(EAP_61_RUNTIME);
		AddRepositoryDialog repoPage= fp.addEAPMavenRepositoryUsingWarningLink();
		assertEquals(RED_HAT_REPO_ID,repoPage.getProfileID());
		assertEquals(RED_HAT_REPO_ID,repoPage.getRepositoryID());
		assertEquals(RED_HAT_REPO_NAME,repoPage.getRepositoryName());
		assertEquals(RED_HAT_REPO_URL,repoPage.getRepositoryURL());
		assertTrue(repoPage.isActiveByDefault());
		repoPage.ok();
		ConfigureMavenRepositoriesWizard repoDialog = new ConfigureMavenRepositoriesWizard();
		repoDialog.confirm();
		ArchetypeExamplesWizardFirstPage sp = (ArchetypeExamplesWizardFirstPage)wz.getWizardPage(1);
		assertEquals(JAVA_EE_PROJECT_A,sp.getProjectName());
		assertEquals(JAVA_EE_PROJECT_P,sp.getPackage());
		ArchetypeExamplesWizardPage tp = (ArchetypeExamplesWizardPage)wz.getWizardPage(2);
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
		new WaitWhile(new ProblemsExists(ProblemType.ERROR), TimePeriod.LONG);
	}

}
