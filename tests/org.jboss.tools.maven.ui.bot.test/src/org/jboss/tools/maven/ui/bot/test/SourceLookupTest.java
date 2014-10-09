package org.jboss.tools.maven.ui.bot.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.maven.reddeer.maven.sourcelookup.ui.preferences.SourceLookupPreferencePage;
import org.jboss.tools.maven.reddeer.maven.sourcelookup.ui.preferences.SourceLookupPreferencePage.SourceAttachment;
import org.jboss.tools.maven.reddeer.preferences.MavenUserPreferencePage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@CleanWorkspace
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY8x)
public class SourceLookupTest extends AbstractMavenSWTBotTest{
	
	private String nonMavenProjectName = "test13848";
	private String nonMavenProjectPath = "projects";
	private static int TEST_NUMBER=0;
	
	@After
	public void delete(){
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject("test13848").delete(true);
	}
	
	//use new settings.xml because of already downloaded source jar
	@Before
	public void setupSettings(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		MavenUserPreferencePage mpreferences = new MavenUserPreferencePage();
		preferenceDialog.select(mpreferences);
		mpreferences.setUserSettings(new File("target/classes/settings"+TEST_NUMBER+".xml").getAbsolutePath());
		mpreferences.ok();
		TEST_NUMBER=1;
	}

	@Test
	public void nonMavenProjectPropt(){
		importProject(nonMavenProjectName, nonMavenProjectPath+"/"+nonMavenProjectName);
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		SourceLookupPreferencePage lookupPage = new SourceLookupPreferencePage();
		preferenceDialog.select(lookupPage);
		lookupPage.toggleAutomaticallyConfigureSourceAttachement(SourceAttachment.PROMPT, true);
		preferenceDialog.ok();
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(nonMavenProjectName).getProjectItem("Referenced Libraries","commons-lang-2.6.jar", "org.apache.commons.lang","Entities.class").open();
		try{
			new DefaultShell("Found source jar for 'commons-lang-2.6.jar'");
		} catch (SWTLayerException ex){
			fail("Source lookup didnt kick in");
		}
		new PushButton("Yes").click();
		//wait for source download
		AbstractWait.sleep(TimePeriod.NORMAL);
		TextEditor ed = new TextEditor("Entities.class");
		assertEquals(ed.getText().replace(" ", "").replaceAll("\\r\\n", ""), readFile("resources/classes/Entities").replace(" ", "").replace("\n", ""));
	}
	
	@Test
	public void nonMavenProjectAlways(){
		importProject(nonMavenProjectName, nonMavenProjectPath+"/"+nonMavenProjectName);
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		SourceLookupPreferencePage lookupPage = new SourceLookupPreferencePage();
		preferenceDialog.select(lookupPage);

		lookupPage.toggleAutomaticallyConfigureSourceAttachement(SourceAttachment.ALWAYS, true);
		preferenceDialog.ok();
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(nonMavenProjectName).getProjectItem("Referenced Libraries","commons-lang-2.6.jar", "org.apache.commons.lang","Entities.class").open();
		try{
			new DefaultShell("Found source jar for 'commons-lang-2.6.jar'");
		} catch (SWTLayerException ex){
			TextEditor ed = new TextEditor("Entities.class");
			assertEquals(ed.getText().replace(" ", "").replaceAll("\\r\\n", ""), readFile("resources/classes/Entities").replace(" ", "").replace("\n", ""));
			return;
		}
		fail("Source lookup shouldt ask if we want to download source file");
	}
	
	@Test
	public void nonMavenProjectNever(){
		importProject(nonMavenProjectName, nonMavenProjectPath+"/"+nonMavenProjectName);
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		SourceLookupPreferencePage lookupPage = new SourceLookupPreferencePage();
		preferenceDialog.select(lookupPage);

		lookupPage.toggleAutomaticallyConfigureSourceAttachement(SourceAttachment.NEVER, true);
		preferenceDialog.ok();
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(nonMavenProjectName).getProjectItem("Referenced Libraries","commons-lang-2.6.jar", "org.apache.commons.lang","Entities.class").open();
		try{
			new DefaultShell("Found source jar for 'commons-lang-2.6.jar'");
		} catch (SWTLayerException ex){
			TextEditor ed = new TextEditor("Entities.class");
			assertNotEquals(ed.getText().replace(" ", "").replaceAll("\\r\\n", ""), readFile("resources/classes/Entities").replace(" ", "").replace("\n", ""));
			return;
		}
		fail("Source lookup shouldt ask if we want to download source file");
	}
	
	
	
	
	private void importProject(String name, String path){
		ExternalProjectImportWizardDialog importDialog = new ExternalProjectImportWizardDialog();
		importDialog.open();
		WizardProjectsImportPage importPage = importDialog.getFirstPage();
		importPage.copyProjectsIntoWorkspace(true);
		try {
			importPage.setRootDirectory((new File(path)).getParentFile().getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		importPage.selectProjects(name);
		importDialog.finish();
	}
	
	private String readFile(String pathToFile){
		BufferedReader br =null;
		String toReturn = null;
		try {
			br = new BufferedReader(new FileReader(pathToFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			StringBuilder sb = new StringBuilder();
		    String line1 = br.readLine();

		    while (line1 != null) {
		    	sb.append(line1);
		        line1 = br.readLine();
		        if(line1 != null){
		        	sb.append('\n');
		        }
		    }
		    toReturn =  sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return toReturn;
	}
	
	
}
