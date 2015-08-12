package org.jboss.tools.usercase.ticketmonster.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.condition.ProblemExists;
import org.jboss.reddeer.eclipse.condition.ServerExists;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.jdt.ui.NewEnumWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewEnumWizardPage;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.wst.server.ui.Runtime;
import org.jboss.reddeer.eclipse.wst.server.ui.RuntimePreferencePage;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewRuntimeWizardDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewRuntimeWizardPage;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardPage;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.api.Editor;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.central.reddeer.projects.ArchetypeProject;
import org.jboss.tools.central.reddeer.wizards.JBossCentralProjectWizard;
import org.jboss.tools.maven.reddeer.maven.ui.preferences.ConfiguratorPreferencePage;
import org.jboss.tools.maven.reddeer.preferences.MavenPreferencePage;
import org.jboss.tools.maven.reddeer.preferences.MavenUserPreferencePage;
import org.jboss.tools.maven.reddeer.project.examples.wizard.ArchetypeExamplesWizardFirstPage;
import org.jboss.tools.maven.reddeer.project.examples.wizard.ArchetypeExamplesWizardPage;
import org.jboss.tools.maven.reddeer.project.examples.wizard.ExampleRequirement;
import org.jboss.tools.maven.reddeer.project.examples.wizard.NewProjectExamplesStacksRequirementsPage;
import org.jboss.tools.maven.reddeer.wizards.ConfigureMavenRepositoriesWizard;
import org.jboss.tools.runtime.reddeer.wizard.DownloadRuntimesTaskWizard;
import org.jboss.tools.runtime.reddeer.wizard.TaskWizardFirstPage;
import org.jboss.tools.runtime.reddeer.wizard.TaskWizardLoginPage;
import org.jboss.tools.runtime.reddeer.wizard.TaskWizardSecondPage;
import org.jboss.tools.runtime.reddeer.wizard.TaskWizardThirdPage;
import org.junit.BeforeClass;

public class TicketMonsterBaseTest {
	
	public static final String USER_SETTINGS = "target/classes/settings.xml"; 
	public static final String JBOSS_AS_DOWNLOAD_DIR = "target/requirements/downloadedJBossAS";
	public static final String JBOSS_EAP_6_1 = System.getProperty("jbosstools.test.jboss.eap.home.6.1");
	public static final String JBOSS_USERNAME = System.getProperty("jboss.username");
	public static final String JBOSS_PASSWORD = System.getProperty("jboss.password");
	public static final String JAVA_EE_PROJECT = "Java EE Web Project";
	public static final String JAVA_EE_PROJECT_G = "org.jboss.tools.examples";
	public static final String JAVA_EE_PROJECT_P = "org.jboss.tools.examples";
	public static final String JAVA_EE_PROJECT_A = "jboss-javaee6-webapp";
	public static final String JAVA_EE_PROJECT_V = "0.0.1-SNAPSHOT";
	public static final String RED_HAT_REPO_ID = "redhat-techpreview-all-repository";
	public static final String RED_HAT_REPO_NAME = "Red Hat Tech Preview repository (all)";
	public static final String RED_HAT_REPO_URL = "http://maven.repository.redhat.com/techpreview/all/";
	public static final String JBOSS_AS_71_NAME = "JBoss AS 7.1.1 (Brontes)";
	public static final String JBOSS_AS_71_RUNTIME = "jboss-as-7.1.1.Final Runtime";
	public static final String EAP_61_NAME = "JBoss EAP 6.1.0";
	public static final String EAP_61_RUNTIME = "jboss-eap-6.1 Runtime";
	public static final String EAP_61_NAME_WITHOUT_RUNTIME = "jboss-eap-6.1";
	public static final String TICKET_MONSTER_NAME = "ticket-monster";
	public static final String TICKET_MONSTER_PACKAGE = "org.jboss.jdf.example.ticketmonster";
	public static final String ENTERPRISE_BOM_VERSION = "1.0.4.Final-redhat-4";
	
	public static final String PACKAGE_CONTROLLER = "controller";
	public static final String PACKAGE_DATA = "data";
	public static final String PACKAGE_MODEL = "model";
	public static final String PACKAGE_REST = "rest";
	public static final String PACKAGE_SERVICE = "service";
	public static final String PACKAGE_UTIL = "util";
	
	protected static String version = System.getProperty("bom.version");
	protected ProjectExplorer projectExplorer = new ProjectExplorer();
	
	
	@BeforeClass
	public static void beforeClass(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		MavenPreferencePage mpreferencesp = new MavenPreferencePage();
		preferenceDialog.select(mpreferencesp);
		mpreferencesp.updateIndexesOnStartup(false);
		preferenceDialog.ok();
		
		preferenceDialog.open();
		MavenUserPreferencePage mpreferences = new MavenUserPreferencePage();
		preferenceDialog.select(mpreferences);
		mpreferences.setUserSettings(new File(USER_SETTINGS).getAbsolutePath());
		mpreferences.ok();
	}
	
	public void createTicketMonsterEAP6(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		if(pe.containsProject(TICKET_MONSTER_NAME)){
			return;
		}
		removeRepos();
		addEnterpriseRepo();
		ArchetypeProject archetypeProject = new ArchetypeProject(JAVA_EE_PROJECT, TICKET_MONSTER_NAME, false);
		JBossCentralProjectWizard wz = new JBossCentralProjectWizard(archetypeProject);
		wz.open();
		NewProjectExamplesStacksRequirementsPage fp = new NewProjectExamplesStacksRequirementsPage();
		List<ExampleRequirement> reqs = fp.getRequirements();
		assertFalse(reqs.get(0).isMet());
		installRuntime(EAP_61_NAME, wz, true);
		fp.setTargetRuntime(EAP_61_RUNTIME);
		wz.next();
		ArchetypeExamplesWizardFirstPage sp = new ArchetypeExamplesWizardFirstPage();
		sp.setProjectName(TICKET_MONSTER_NAME);
		sp.setPackage(TICKET_MONSTER_PACKAGE);
		wz.next();
		ArchetypeExamplesWizardPage tp = new ArchetypeExamplesWizardPage();
		assertEquals(TICKET_MONSTER_PACKAGE,tp.getGroupID());
		assertEquals(TICKET_MONSTER_NAME, tp.getArtifactID());
		assertEquals(TICKET_MONSTER_PACKAGE, tp.getPackage());
		//not in newset archetype
		//Table table = tp.getTableSuffix();
		//version = table.getItem("jboss-bom-enterprise-version").getText(1);
		//assertEquals("true", table.getItem("enterprise").getText(1));
		wz.finish();
		new WaitWhile(new ProblemExists(ProblemType.ERROR));
		pe.open();
		pe.getProject(TICKET_MONSTER_NAME);
		createEAPServerRuntime();
	}

	protected void installRuntime(String runtimeName,JBossCentralProjectWizard wz, boolean eap){
		NewProjectExamplesStacksRequirementsPage fp = new NewProjectExamplesStacksRequirementsPage();
		List<ExampleRequirement> reqs = fp.getRequirements();
		
		
		reqs.get(0).install();
		assertEquals("Preferences",new DefaultShell().getText());
		
		//avoiding native dialog	
		
		createEAPRuntime(JBOSS_EAP_6_1);
	}
	
	protected void downloadAndInstallRuntime(String runtimeName,JBossCentralProjectWizard wz, boolean eap){
		NewProjectExamplesStacksRequirementsPage fp = new NewProjectExamplesStacksRequirementsPage();
		DownloadRuntimesTaskWizard downloadRuntime = null;
		for(ExampleRequirement er: fp.getRequirements()){
			if(er.getType().equals("server/runtime")){
				downloadRuntime = er.downloadAndInstall();
				break;
			}
		}
		assertNotNull("No requirement with server/runtime type found",downloadRuntime);
		if(eap){
			downloadRuntime.eapDialog();
		} else {
			downloadRuntime.asDialog();
		}
		TaskWizardFirstPage downloadFirst = new TaskWizardFirstPage();
		downloadFirst.selectRuntime(runtimeName);
		if(eap){
			downloadRuntime.next();
			TaskWizardLoginPage downloadTask = new TaskWizardLoginPage();
			downloadTask.setUsername(JBOSS_USERNAME);
			downloadTask.setPassword(JBOSS_PASSWORD);
		}
		downloadRuntime.next();
		TaskWizardSecondPage downloadSecond = new TaskWizardSecondPage();
		AbstractWait.sleep(TimePeriod.LONG); //wait for credentials validation;
		downloadSecond.acceptLicense(true);
		downloadRuntime.next();
		TaskWizardThirdPage downloadThird = new TaskWizardThirdPage();
		downloadThird.setDownloadFolder(new File(JBOSS_AS_DOWNLOAD_DIR).getAbsolutePath());
		downloadThird.setInstallFolder(new File(JBOSS_AS_DOWNLOAD_DIR).getAbsolutePath());
		downloadRuntime.finish();
		
	}
	
	
	private static void createEAPRuntime(String homeDir){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		RuntimePreferencePage rp = new RuntimePreferencePage();
		preferenceDialog.select(rp);
		
		for(Runtime runtime: rp.getServerRuntimes()){
			if(runtime.getName().equals(EAP_61_RUNTIME)){
				preferenceDialog.ok();
			}
		}
		NewRuntimeWizardDialog rd = rp.addRuntime();
		new NewRuntimeWizardPage().selectType("Red Hat JBoss Middleware","JBoss Enterprise Application Platform 6.1+ Runtime");
		new NextButton().click();
		new LabeledText("Home Directory").setText(homeDir);
		new LabeledText("Name").setText(EAP_61_RUNTIME);
		rd.finish();
		preferenceDialog.ok();
	}
	

	protected void removeRepos(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		preferenceDialog.select(new ConfiguratorPreferencePage());		
		ConfigureMavenRepositoriesWizard rd = new ConfigureMavenRepositoriesWizard();
		rd.open();
		if(rd.removeAllRepos()){
			rd.confirm();
		} else {
			rd.cancel();
		}
		preferenceDialog.ok();
	}
	
	protected void addEnterpriseRepo(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		preferenceDialog.select(new ConfiguratorPreferencePage());
		ConfigureMavenRepositoriesWizard rd = new ConfigureMavenRepositoriesWizard();
		rd.open();
		rd.chooseRepositoryFromList("redhat-techpreview-all-repository", true,false);
		rd.confirm();
		preferenceDialog.ok();
	}
	
	protected void createJavaClass(String name, String classPackage){
		NewJavaClassWizardDialog newJava  = new NewJavaClassWizardDialog();
		newJava.open();
		NewJavaClassWizardPage newJavaPage = new NewJavaClassWizardPage();
		newJavaPage.setName(name);
		newJavaPage.setPackage(classPackage);
		newJavaPage.setSourceFolder(TICKET_MONSTER_NAME+"/src/main/java");
		newJava.finish();
	}
	
	protected void createEnumClass(String name, String classPackage){
		NewEnumWizardDialog newEnum  = new NewEnumWizardDialog();
		newEnum.open();
		NewEnumWizardPage newEnumPage = newEnum.getFirstPage();
		newEnumPage.setName(name);
		newEnumPage.setPackage(classPackage);
		newEnumPage.setSourceFolder(TICKET_MONSTER_NAME+"/src/main/java");
		newEnum.finish();
	}
	
	protected void replaceEditorContentWithText(String editor, String text, int line, int column, boolean deletecontent, boolean save){
		Editor defeditor = new DefaultEditor(editor);
		if(deletecontent){
			new DefaultStyledText().setText("");
		}
		new DefaultStyledText().insertText(line, column, text);
		if(save){
			defeditor.save();
		}
		
	}
	
	protected void replaceEditorContentWithFile(String editor, String pathToFile, int line, int column, boolean deletecontent, boolean save){
		Editor defeditor = new DefaultEditor(editor);
		
		BufferedReader br =null;
		String fileContent = null;
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
		    fileContent = sb.toString();
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
		if(deletecontent){
			new DefaultStyledText().setText("");
		}
		new DefaultStyledText().insertText(line, column, fileContent);
		if(save){
			defeditor.save();
		}
	}
	
	protected String fileToString(String pathToFile){
		BufferedReader br =null;
		String fileContent = null;
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
		    fileContent = sb.toString();
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
		
		return fileContent;
	}
	
	protected void deployProject(String projectName, String targetedRuntime){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(projectName).select();
		new ContextMenu("Run As","1 Run on Server").select();
		new DefaultShell("Run On Server");
		new DefaultTreeItem("localhost",targetedRuntime).select();
		new PushButton("Next >").click();
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive("Run On Server"));
		new WaitUntil(new ConsoleHasText("Deployed \""+projectName+".war\""),TimePeriod.LONG);
	}
	
	protected void createEAPServerRuntime(){
		ServersView sview = new ServersView();
		sview.open();
		try{
			sview.getServer(EAP_61_NAME_WITHOUT_RUNTIME);
		} catch(EclipseLayerException ex){
			NewServerWizardDialog sdialog = sview.newServer();
			NewServerWizardPage newServerWizardPage = new NewServerWizardPage();
			newServerWizardPage.selectType("Red Hat JBoss Middleware","JBoss Enterprise Application Platform 6.1+");
			newServerWizardPage.setName(EAP_61_NAME_WITHOUT_RUNTIME);
			sdialog.next();
			sdialog.finish();
			new WaitUntil(new ServerExists(EAP_61_NAME_WITHOUT_RUNTIME));
		}
	}
	
	protected void replaceEditorContentWithURL(String editor, String url, int line, int column, boolean deletecontent, boolean save){
		Scanner scanner = null;
		String urlContent = null;
		try {
			scanner = new Scanner(new URL(url).openStream(), "UTF-8");
			urlContent = scanner.useDelimiter("\\A").next();
			replaceEditorContentWithText(editor, urlContent, line, column, deletecontent, save);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(scanner != null){
				scanner.close();
			}
		}
	}
}
