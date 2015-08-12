package org.jboss.tools.deltaspike.ui.bot.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectFirstPage;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.tools.cdi.reddeer.cdi.ui.CDIProjectWizard;
import org.jboss.tools.common.reddeer.preferences.SourceLookupPreferencePage;
import org.junit.Before;
import org.junit.BeforeClass;

public class NewDeltaspikeTestBase {
	
	private static final String DELTASPIKE_LIBS = System.getProperty("deltaspike.libs.dir");
	
	protected static String PROJECT_NAME = "DeltaspikeProject";
	
	@InjectRequirement
	protected static ServerRequirement sr;

	@BeforeClass
	public static void disableSourceLookup() {
		WorkbenchPreferenceDialog wd = new WorkbenchPreferenceDialog();
		wd.open();
		SourceLookupPreferencePage sourceLookupPreferencePage = new SourceLookupPreferencePage();
		wd.select(sourceLookupPreferencePage);
		sourceLookupPreferencePage.setSourceAttachment(
				SourceLookupPreferencePage.SourceAttachmentEnum.NEVER);
		wd.ok();
	}
	
	@Before
	public void prepareWorkspace(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		if(pe.containsProject(PROJECT_NAME)){
			return;
		}
		CDIProjectWizard cw = new CDIProjectWizard();
		cw.open();
		WebProjectFirstPage fp  = new WebProjectFirstPage();
		fp.setProjectName(PROJECT_NAME);
		fp.setTargetRuntime(sr.getRuntimeNameLabelText(sr.getConfig()));
		cw.finish();
		new WaitUntil(new JobIsRunning(),TimePeriod.NORMAL, false);
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
		addDeltaspikeLibs();
	}
	
	private void addDeltaspikeLibs(){
		List<String> libs = new ArrayList<String>();
		File libsFolder = new File(DELTASPIKE_LIBS);
		for(File f: libsFolder.listFiles()){
			if(f.getName().startsWith("deltaspike")){
				libs.add(f.getAbsolutePath());
			}
		}
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
		IJavaProject javaProject = JavaCore.create(project);
		IClasspathEntry[] classpath= null;
		try {
			classpath = javaProject.getRawClasspath();
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IClasspathEntry[] newClasspath = new IClasspathEntry[classpath.length+libs.size()];
		System.arraycopy(classpath, 0, newClasspath, 0, classpath.length);
		for(int i =0; i<libs.size();i++){
			newClasspath[classpath.length+i] = JavaCore.newLibraryEntry(new Path(libs.get(i)), null, null);
		}
		try {
			javaProject.setRawClasspath(newClasspath, null);
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
