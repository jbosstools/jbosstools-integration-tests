package org.jboss.tools.maven.ui.bot.test.apt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.StyledText;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.api.Editor;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.maven.reddeer.apt.ui.preferences.AnnotationProcessingSettingsPage;
import org.jboss.tools.maven.reddeer.apt.ui.preferences.AnnotationProcessingSettingsPage.ProcessingMode;
import org.jboss.tools.maven.ui.bot.test.AbstractMavenSWTBotTest;
import org.junit.After;
import org.junit.Test;

@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY8x)
public class APTPropertiesPageTest extends AbstractMavenSWTBotTest{
	
	@InjectRequirement
    private ServerRequirement sr;
	
	private static final String PROJECT_NAME = "test";
	private List<String> defaultPaths = Arrays.asList(PROJECT_NAME+"/src/main/java",
			PROJECT_NAME+"/src/main/resources",PROJECT_NAME+"/src/test/java",
			PROJECT_NAME+"/src/test/resources");
	
	private String generatedSources =  "generated-sources/";
	private String target = "target/";
	
	private String defaultCompilerPath = target+generatedSources+"annotations";
	private String defaultProcessorPath = target+generatedSources+"apt";
	
	@After
	public void cleanup(){
		deleteProjects(true);
	}
	
	@Test
	public void testProcessingModesCompiler(){
		prepareProject();
		setProcessingMode(ProcessingMode.NONE);
		addDependency(PROJECT_NAME, "org.hibernate","hibernate-jpamodelgen" , "1.2.0.Final");
		updateConf(PROJECT_NAME);
		checkBuildPath(PROJECT_NAME, defaultPaths, null);
		checkAnnotationProcessingSettings(PROJECT_NAME, false, null, null);
		setProcessingMode(ProcessingMode.JDT);
		checkBuildPath(PROJECT_NAME, defaultPaths, PROJECT_NAME+"/"+defaultCompilerPath);
		checkAnnotationProcessingSettings(PROJECT_NAME, true, defaultCompilerPath, null);
		setProcessingMode(ProcessingMode.MAVEN);
		checkBuildPath(PROJECT_NAME, defaultPaths, PROJECT_NAME+"/"+defaultCompilerPath);
		checkAnnotationProcessingSettings(PROJECT_NAME, true, defaultCompilerPath, null);
	}
	
	@Test
	public void testCompilerArgs(){
		prepareProject();
		addDependency(PROJECT_NAME, "org.hibernate","hibernate-jpamodelgen" , "1.2.0.Final");
		setProcessingMode(ProcessingMode.JDT);
		List<String> args = Arrays.asList("test1=value1","test2=value2");
		setCompilerArgs(PROJECT_NAME, args);
		updateConf(PROJECT_NAME);
		checkAnnotationProcessingSettings(PROJECT_NAME, true, defaultCompilerPath, args);
	}
	
	@Test
	public void testCompilerArgument(){
		prepareProject();
		addDependency(PROJECT_NAME, "org.hibernate","hibernate-jpamodelgen" , "1.2.0.Final");
		setProcessingMode(ProcessingMode.JDT);
		List<String> arguments = Arrays.asList("test1=value1","test2=value2");
		String argumentWithOther = "otherParam=param";
		setCompilerArgument(PROJECT_NAME, arguments,argumentWithOther);
		updateConf(PROJECT_NAME);
		checkAnnotationProcessingSettings(PROJECT_NAME, true, defaultCompilerPath, arguments);
	}

	@Test
	public void testCompilerArguments(){
		prepareProject();
		addDependency(PROJECT_NAME, "org.hibernate","hibernate-jpamodelgen" , "1.2.0.Final");
		setProcessingMode(ProcessingMode.JDT);
		List<String> arguments = Arrays.asList("test1=value1","test2=value2");
		String otherArg = "other=v3";
		setCompilerArguments(PROJECT_NAME, arguments,otherArg);
		updateConf(PROJECT_NAME);
		checkAnnotationProcessingSettings(PROJECT_NAME, true, defaultCompilerPath, arguments);
	}
			
	@Test
	public void testProcessorOptionMap(){
		prepareProject();
		addProcessorPlugin(PROJECT_NAME);
		addDependency(PROJECT_NAME, "org.hibernate","hibernate-jpamodelgen" , "1.2.0.Final");
		setProcessingMode(ProcessingMode.JDT);
		List<String> arguments = Arrays.asList("test1=value1","test2=value2");
		setProcessorOptionMap(PROJECT_NAME, arguments);
		updateConf(PROJECT_NAME);
		checkAnnotationProcessingSettings(PROJECT_NAME, true, defaultProcessorPath, arguments);
	}
	
	@Test
	public void testProcessingModesProcessor(){
		prepareProject();
		addProcessorPlugin(PROJECT_NAME);
		setProcessingMode(ProcessingMode.NONE);
		addDependency(PROJECT_NAME, "org.hibernate","hibernate-jpamodelgen" , "1.2.0.Final");
		updateConf(PROJECT_NAME);
		checkBuildPath(PROJECT_NAME, defaultPaths, null);
		checkAnnotationProcessingSettings(PROJECT_NAME, false, null, null);
		setProcessingMode(ProcessingMode.JDT);
		checkBuildPath(PROJECT_NAME, defaultPaths, PROJECT_NAME+"/"+defaultProcessorPath);
		checkAnnotationProcessingSettings(PROJECT_NAME, true, defaultProcessorPath, null);
		setProcessingMode(ProcessingMode.MAVEN);
		checkBuildPath(PROJECT_NAME, defaultPaths, PROJECT_NAME+"/"+defaultProcessorPath);
		checkAnnotationProcessingSettings(PROJECT_NAME, false, null, null);	
	}
	
	@Test
	public void testCompilerCustomOutput(){
		prepareProject();
		String customFolder = "customCompilerFolder";
		setCompilerCustomOutputFolder(PROJECT_NAME, generatedSources+customFolder);
		setProcessingMode(ProcessingMode.NONE);
		addDependency(PROJECT_NAME, "org.hibernate","hibernate-jpamodelgen" , "1.2.0.Final");
		updateConf(PROJECT_NAME);
		checkBuildPath(PROJECT_NAME, defaultPaths, null);
		checkAnnotationProcessingSettings(PROJECT_NAME, false, null, null);
		setProcessingMode(ProcessingMode.JDT);
		checkBuildPath(PROJECT_NAME, defaultPaths, PROJECT_NAME+"/"+target+generatedSources+customFolder);
		checkAnnotationProcessingSettings(PROJECT_NAME, true, target+generatedSources+customFolder, null);
		setProcessingMode(ProcessingMode.MAVEN);
		checkBuildPath(PROJECT_NAME, defaultPaths, PROJECT_NAME+"/"+target+generatedSources+customFolder);
		checkAnnotationProcessingSettings(PROJECT_NAME, true, target+generatedSources+customFolder, null);
	}
	
	@Test
	public void testProcessorCustomOutput(){
		prepareProject();
		String customFolder = "customProcessorFolder";
		addProcessorPlugin(PROJECT_NAME);
		setProcessorCustomOutputFolder(PROJECT_NAME, generatedSources+customFolder);
		setProcessingMode(ProcessingMode.NONE);
		addDependency(PROJECT_NAME, "org.hibernate","hibernate-jpamodelgen" , "1.2.0.Final");
		updateConf(PROJECT_NAME);
		checkBuildPath(PROJECT_NAME, defaultPaths, null);
		checkAnnotationProcessingSettings(PROJECT_NAME, false, null, null);
		setProcessingMode(ProcessingMode.JDT);
		checkBuildPath(PROJECT_NAME, defaultPaths, PROJECT_NAME+"/"+target+generatedSources+customFolder);
		checkAnnotationProcessingSettings(PROJECT_NAME, true, target+generatedSources+customFolder, null);
		setProcessingMode(ProcessingMode.MAVEN);
		checkBuildPath(PROJECT_NAME, defaultPaths, PROJECT_NAME+"/"+target+generatedSources+customFolder);
		checkAnnotationProcessingSettings(PROJECT_NAME, false, null, null);
	}
	
	private void addProcessorPlugin(String project){
		Editor e= openPom(project);
		StyledText stext = new DefaultStyledText();
		int pos = stext.getPositionOfText("</plugin>");
		String processorPlugin = null;
		try {
			processorPlugin = new Scanner(new FileInputStream("resources/pom/processorPlugin")).useDelimiter("\\A").next();
		} catch (FileNotFoundException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		stext.selectPosition(pos+9);
		stext.insertText(processorPlugin);
		e.save();
		
	}
	
	private void setProcessingMode(ProcessingMode mode){
		WorkbenchPreferenceDialog pd = new WorkbenchPreferenceDialog();
		pd.open();
		AnnotationProcessingSettingsPage ap = new AnnotationProcessingSettingsPage();
		pd.select(ap);
		ap.setAnnotationProcessingMode(mode);
		new OkButton().click();
		try{
			new DefaultShell("Maven Annotation Processing Settings");
			new YesButton().click();
		} catch (SWTLayerException ex){
			
		}
		new WaitWhile(new ShellWithTextIsAvailable("Preferences"));
		new WaitWhile(new JobIsRunning(),TimePeriod.ETERNAL);
		
	}
	
	private void setCompilerCustomOutputFolder(String project, String customFolder){
		Editor e= openPom(project);
		StyledText stext = new DefaultStyledText();
		int pos = stext.getPositionOfText("</target>");
		stext.selectPosition(pos+9);
		stext.insertText("<generatedSourcesDirectory>${project.build.directory}/"+customFolder+
				"</generatedSourcesDirectory>");
		e.save();
	}
	
	private void setCompilerArgs(String project, List<String> args){
		Editor e = openPom(project);
		StyledText stext = new DefaultStyledText();
		int pos = stext.getPositionOfText("</target>");
		stext.selectPosition(pos+9);
		stext.insertText("</compilerArgs>");
		for(String arg: args){
			stext.insertText("<arg>-A"+arg+"</arg>");
		}
		stext.insertText("<compilerArgs>");
		e.save();
	}
	
	private void setProcessorOptionMap(String project, List<String> args){
		Editor e = openPom(project);
		StyledText stext = new DefaultStyledText();
		int pos = stext.getPositionOfText("</executions>");
		stext.selectPosition(pos+13);
		stext.insertText("</optionMap></configuration>");
		for(String arg: args){
			String[] a= arg.split("=");
			stext.insertText("<"+a[0]+">"+a[1]+"</"+a[0]+">");
		}
		stext.insertText("<configuration><optionMap>");
		e.save();
		
	}
	
	private void setCompilerArguments(String project, List<String> args, String otherArg){
		Editor e = openPom(project);
		StyledText stext = new DefaultStyledText();
		int pos = stext.getPositionOfText("</target>");
		stext.selectPosition(pos+9);
		stext.insertText("</compilerArguments>");
		for(String arg: args){
			String[] a= arg.split("=");
			stext.insertText("<A"+a[0]+">"+a[1]+"</A"+a[0]+">");
		}
		if(otherArg != null){
			String[] a= otherArg.split("=");
			stext.insertText("<"+a[0]+">"+a[1]+"</"+a[0]+">");
		}
		stext.insertText("<compilerArguments>");
		e.save();
		
	}
	
	private void setCompilerArgument(String project, List<String> arguments, String otherArg){
		Editor e = openPom(project);
		StyledText stext = new DefaultStyledText();
		int pos = stext.getPositionOfText("</target>");
		stext.selectPosition(pos+9);
		stext.insertText("</compilerArgument>");
		for(String s: arguments){
			stext.insertText(" -A"+s);
		}
		if(otherArg != null){
			stext.insertText(otherArg);
		}
		stext.insertText("<compilerArgument>");
		e.save();
	}
	
	
	private void setProcessorCustomOutputFolder(String project, String customFolder){
		Editor e = openPom(project);
		StyledText stext = new DefaultStyledText();
		int pos = stext.getPositionOfText("</executions>");
		stext.selectPosition(pos+13);
		stext.insertText("<configuration><defaultOutputDirectory>"
				+ "${project.build.directory}/"+ customFolder
				+ "</defaultOutputDirectory></configuration>");
		e.save();
	}
	
	
	
	private void checkBuildPath(String project, List<String> paths, String additionalPath){
		openProperties(project);
		new DefaultTreeItem("Java Build Path").select();
		new DefaultTabItem("Source").activate();
		List<TreeItem> items = new DefaultTree(1).getItems();
		if(additionalPath != null){
			assertEquals(paths.size()+1,items.size());
		} else {
			assertEquals(paths.size(),items.size());
		}
		List<String> itemsPaths = new ArrayList<String>();
		for(TreeItem i: items){
			itemsPaths.add(i.getText());
		}
		assertTrue(itemsPaths.containsAll(paths));
		if(additionalPath != null){
			assertTrue(itemsPaths.contains(additionalPath));
		}
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for "+PROJECT_NAME));
	}
	
	private void checkAnnotationProcessingSettings(String project, boolean enabled, String sourceDir, List<String>args){
		openProperties(project);
		new DefaultTreeItem("Java Compiler","Annotation Processing").select();
		CheckBox c = new CheckBox("Enable annotation processing");
		if(!enabled){
			assertTrue(!c.isEnabled() || !c.isChecked());
		} else {
			assertTrue(c.isEnabled() && c.isChecked());
			assertEquals(sourceDir, new DefaultText(1).getText());
		}
		if(args != null){
			Table t = new DefaultTable();
			List<TableItem> items = t.getItems();
			assertEquals(args.size(),items.size());
			List<String> compilerArgs = new ArrayList<String>();
			for(TableItem i: items){
				compilerArgs.add(i.getText(0) + "="+i.getText(1));
			}
			assertTrue(args.containsAll(compilerArgs));
		}
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for "+PROJECT_NAME));
		
	}
	
	private void prepareProject(){
		createBasicMavenProject(PROJECT_NAME, PROJECT_NAME, "war","1.7");
		openProperties(PROJECT_NAME);
		new DefaultTreeItem("Targeted Runtimes").select();
		new DefaultTableItem(sr.getRuntimeNameLabelText(sr.getConfig())).setChecked(true);
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for "+PROJECT_NAME));
		new WaitWhile(new JobIsRunning());
		NewJavaClassWizardDialog jd = new NewJavaClassWizardDialog();
		jd.open();
		NewJavaClassWizardPage jp = new NewJavaClassWizardPage();
		jp.setPackage(PROJECT_NAME);
		jp.setName("Member");
		jd.finish();
		TextEditor ed = new TextEditor("Member.java");
		try {
			ed.setText(new Scanner(new FileInputStream("resources/classes/Member.jav_")).useDelimiter("\\A").next());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ed.save();
		addDependency(PROJECT_NAME, "org.hibernate.javax.persistence", "hibernate-jpa-2.0-api", "1.0.1.Final");
		addDependency(PROJECT_NAME, "org.hibernate", "hibernate-validator", "5.1.3.Final");
	}
	
	private void openProperties(String project){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(project).select();
		new ContextMenu("Properties").select();
		new DefaultShell("Properties for "+project);
	}

}
