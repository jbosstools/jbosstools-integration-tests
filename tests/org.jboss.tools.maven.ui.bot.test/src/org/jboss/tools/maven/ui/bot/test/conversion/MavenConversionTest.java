package org.jboss.tools.maven.ui.bot.test.conversion;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.link.AnchorLink;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.maven.ui.bot.test.AbstractMavenSWTBotTest;
import org.junit.After;
import org.junit.Test;

@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY8x)
public class MavenConversionTest extends AbstractMavenSWTBotTest{
	
	private static final Logger log = Logger.getLogger(MavenConversionTest.class);
	
	public static final String WEB_PROJECT_NAME = "Web Project";
	
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
		new PushButton("Finish").click();
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(WEB_PROJECT_NAME).select();
		new ContextMenu("Properties").select();
		new DefaultShell("Properties for "+WEB_PROJECT_NAME);
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
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive("Properties for "+WEB_PROJECT_NAME),TimePeriod.NORMAL);
	}
	
	@Test
	public void keepDependenciesAfterConversion(){
		createWithRuntime();
		new CheckBox("Delete original references from project").toggle(false);
		new PushButton("Finish").click();
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(WEB_PROJECT_NAME).select();
		new ContextMenu("Properties").select();
		new DefaultShell("Properties for "+WEB_PROJECT_NAME);
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
						libs.get(i).contains(sr.getRuntimeNameLabelText(sr.getConfig())));
			}
		}
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive("Properties for "+WEB_PROJECT_NAME),TimePeriod.NORMAL);
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
		new WaitUntil(new WidgetIsEnabled(new PushButton("Finish")));
		new PushButton("Finish").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
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
		if(pe.containsProject(WEB_PROJECT_NAME)){
			return;
		}
		createWebProject(WEB_PROJECT_NAME, sr.getRuntimeNameLabelText(sr.getConfig()), false);
		pe.open();
		pe.getProject(WEB_PROJECT_NAME).select();
		new ContextMenu("Configure","Convert to Maven Project").select();
		new DefaultShell("Create new POM");
		new PushButton("Finish").click();
		new DefaultShell("Convert to Maven Dependencies");
		new WaitUntil(new WidgetIsEnabled(new PushButton("Finish")), TimePeriod.LONG);
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

}
