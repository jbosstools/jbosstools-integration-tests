package org.jboss.tools.maven.ui.bot.test.conversion;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.condition.ProblemExists;
import org.eclipse.reddeer.eclipse.ui.dialogs.PropertyDialog;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.condition.ControlIsEnabled;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.exception.SWTLayerException;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.link.AnchorLink;
import org.eclipse.reddeer.swt.impl.link.DefaultLink;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.swt.impl.tab.DefaultTabItem;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.maven.reddeer.requirement.NewRepositoryRequirement.DefineMavenRepository;
import org.jboss.tools.maven.reddeer.requirement.NewRepositoryRequirement.MavenRepository;
import org.jboss.tools.maven.reddeer.requirement.NewRepositoryRequirement.PredefinedMavenRepository;
import org.jboss.tools.maven.ui.bot.test.AbstractMavenSWTBotTest;
import org.junit.After;
import org.junit.Test;

@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerRequirementState.PRESENT)
@DefineMavenRepository(
		predefinedRepositories = { 
				@PredefinedMavenRepository(ID="jboss-public-repository",snapshots=true) },
		newRepositories = { 
				@MavenRepository(url="https://repository.jboss.org/maven2/", ID="jboss-maven-repository", snapshots=true)}
)
public class MavenConversionTest extends AbstractMavenSWTBotTest{
	
	private static final Logger log = Logger.getLogger(MavenConversionTest.class);
	
	public static final String WEB_PROJECT_NAME = "WebProject";
	
	private List<String> expectedLibsKeep= new ArrayList<String>(
			Arrays.asList("JRE","Maven Dependencies","Runtime"));
	 
	@InjectRequirement
    private ServerRequirement sr;
	
	@After
	public void clean(){
		deleteProjects(true);
	}
	
	@Test
	public void deleteDependenciesAfterConversion(){
		createWithRuntime();
		new CheckBox("Delete original references from project").toggle(true);
		finishConversionDialog();
		checkProblemsAndResolve();
		PropertyDialog pd = openPropertiesProject(WEB_PROJECT_NAME);
		new DefaultTreeItem("Java Build Path").select();
		new DefaultTabItem("Libraries").activate();
		List<TreeItem> it = new DefaultTree(1).getItems();
		log.debug("Libraries found after conversion:");
		for(TreeItem i: it){
			log.debug("  "+i.getText());
		}
		assertTrue("project contains more libraries than expected",it.size()==2);
		for(TreeItem i: it){
            if(!(i.getText().contains("JRE") || i.getText().contains("Maven Dependencies"))){
                fail("Some dependencies are missing after conversion");
            }
        }
		pd.ok();
		new WaitWhile(new ShellIsAvailable("Properties for "+WEB_PROJECT_NAME),TimePeriod.DEFAULT);
	}
	
	@Test
	public void keepDependenciesAfterConversion(){
		createWithRuntime();
		new CheckBox("Delete original references from project").toggle(false);
		finishConversionDialog();
		checkProblemsAndResolve();
		PropertyDialog pd = openPropertiesProject(WEB_PROJECT_NAME);
		new DefaultTreeItem("Java Build Path").select();
		new DefaultTabItem("Libraries").activate();
		List<TreeItem> it = new DefaultTree(1).getItems();
		List<String> libs = new ArrayList<String>();
		log.debug("Libraries found after conversion:");
		for(TreeItem i: it){
			String lib = i.getText();
			log.debug("  "+lib);
			libs.add(lib);
		}
		assertTrue("project contains more libraries than expected",libs.size()==3);
		Collections.sort(libs);
		Collections.sort(expectedLibsKeep);
		
		for(int i=0;i<3;i++){
			assertTrue("Missing library "+expectedLibsKeep.get(i)+" but was"+libs.get(i),
					libs.get(i).contains(expectedLibsKeep.get(i)));
			if(libs.get(i).contains("Runtime")){
				assertTrue("Wrong runtime added after conversion",
						libs.get(i).contains(sr.getRuntimeName()));
			}
		}
		pd.ok();
		new WaitWhile(new ShellIsAvailable("Properties for "+WEB_PROJECT_NAME),TimePeriod.DEFAULT);
	}
	
	@Test
	public void changeIdentifiedDependency(){
		createWithRuntime();
		new DefaultTable().getItem(1).doubleClick(2);
		
		new DefaultShell("Edit dependency");
		new LabeledText("Group Id:").setText("maven.conversion.test.groupID");
		new LabeledText("Artifact Id").setText("maven.conversion.test.artifactID");
		new LabeledText("Version:").setText("1.0.0");
		new LabeledText("Classifier:").setText("b3");
		new LabeledCombo("Type:").setSelection("rar");
		new LabeledCombo("Scope:").setSelection("provided");
		new CheckBox("Optional").toggle(true);
		new PushButton("OK").click();
		new DefaultShell("Convert to Maven Dependencies");
		finishConversionDialog();
		List<String> toCheck = new ArrayList<String>();
		toCheck.add("<groupId>maven.conversion.test.groupID</groupId>");
		toCheck.add("<artifactId>maven.conversion.test.artifactID</artifactId>");
		toCheck.add("<version>1.0.0</version>");
		toCheck.add("<classifier>b3</classifier>");
		toCheck.add("<type>rar</type>");
		toCheck.add("<scope>provided</scope>");
		toCheck.add("<optional>true</optional>");
		checkDependency(WEB_PROJECT_NAME, toCheck);
		
	}
	
	@Test
	public void testAddRepositoryLinkInConversionWizard(){
		createWithRuntime();
		new DefaultTable().getItem(1).doubleClick(2);
		
		new DefaultShell("Edit dependency");
		new LabeledText("Group Id:").setText("antlr");
		new LabeledText("Artifact Id").setText("antlr");
		new LabeledText("Version:").setText("non-existing-version");
		new LabeledCombo("Type:").setText("jar");
		new PushButton("OK").click();
		new DefaultShell("Convert to Maven Dependencies");
		new AnchorLink("here").click();
		boolean shellIsOpened = true;
		try{
			new DefaultShell("Maven Repositories");
		} catch (SWTLayerException ex){
			shellIsOpened = false;
		}
		if(shellIsOpened){
			new PushButton("Cancel").click();
		}
		new PushButton("Skip Dependency Conversion").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		if(!shellIsOpened){
			fail("Shell Maven Repositories was not opened after clicking on 'here' link");
		}

	}
	
	
	@Test
	public void testRemoteRepositoriesLinkInConversionWizard(){
		createWithRuntime();
		new DefaultLink("Manage remote repositories used to identify dependencies.").click();
		boolean shellIsOpened = true;
		try{
			new DefaultShell("Preferences (Filtered)");
		} catch (SWTLayerException ex){
			shellIsOpened = false;
		}
		if(shellIsOpened){
			new PushButton("Cancel").click();
		}
		new PushButton("Skip Dependency Conversion").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		if(!shellIsOpened){
			fail("Shell Preferences was not opened after clicking on 'remote repositories' link");
		}

	}
	
	private void createWithRuntime(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		createWebProject(WEB_PROJECT_NAME, sr.getRuntimeName(), false);
		pe.open();
		pe.getProject(WEB_PROJECT_NAME).select();
		new ContextMenuItem("Configure","Convert to Maven Project").select();
		new DefaultShell("Create new POM");
		new PushButton("Finish").click();
		new DefaultShell("Convert to Maven Dependencies");
		new WaitUntil(new ControlIsEnabled(new PushButton("Finish")), TimePeriod.LONG);
	}
		
	
	private void checkDependency(String projectName, List<String> valuesToCheck){
		ProjectExplorer pe = new ProjectExplorer();
	    pe.open();
	    pe.getProject(projectName).getProjectItem("pom.xml").open();
	    new DefaultEditor("pom.xml");
	    new DefaultCTabItem("pom.xml").activate();
	    String text = new DefaultStyledText().getText();
	    for(String value: valuesToCheck){
	        assertTrue(text.contains(value));
	    }
	}
	
	private void checkProblemsAndResolve(){
		new WaitUntil(new ProblemExists(ProblemType.ERROR),TimePeriod.DEFAULT,false);
		ProblemsView pw = new ProblemsView();
		pw.open();
		if(pw.getProblems(ProblemType.ERROR).size() > 0){
			updateConf(WEB_PROJECT_NAME,true);
			try{
				new WaitWhile(new ProblemExists(ProblemType.ERROR));
			} catch (WaitTimeoutExpiredException ex){
				ex.addMessageDetail("Some problems still exist. Dependecies probably were not downloaded successfully");
				throw ex;
			}
		}
	}
	
	private void finishConversionDialog(){
		new WaitUntil(new ControlIsEnabled(new PushButton("Finish")));
		new PushButton("Finish").click();
		int i =1;
		while(i>0){
			try{
				new WaitUntil(new JobIsRunning());
			} catch (WaitTimeoutExpiredException ex){
				break;
			}
			new WaitWhile(new JobIsRunning(),TimePeriod.VERY_LONG);
		}
	}

}
