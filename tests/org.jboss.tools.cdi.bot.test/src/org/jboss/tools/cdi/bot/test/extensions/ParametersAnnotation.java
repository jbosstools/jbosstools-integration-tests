package org.jboss.tools.cdi.bot.test.extensions;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDI11TestBase;
import org.junit.Before;
import org.junit.Test;

//based on JBIDE-12644
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY8x)
public class ParametersAnnotation extends CDI11TestBase{
	
	public static final String WELD_SE_JAR=System.getProperty("jbosstools.test.weld-se.home");
	
	@Before
	public void addLibs(){
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
		IJavaProject javaProject = JavaCore.create(project);
		IClasspathEntry[] classpath= null;
		try {
			classpath = javaProject.getRawClasspath();
		} catch (JavaModelException e) {
			fail(Arrays.toString(e.getStackTrace()));
		}
		IClasspathEntry[] newClasspath = new IClasspathEntry[classpath.length+1];
		System.arraycopy(classpath, 0, newClasspath, 0, classpath.length);
		
		newClasspath[classpath.length] = JavaCore.newLibraryEntry(new Path(WELD_SE_JAR), null, null);
		try {
			javaProject.setRawClasspath(newClasspath, null);
		} catch (JavaModelException e) {
			fail(Arrays.toString(e.getStackTrace()));
		}
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).select();
		new ContextMenu("Refresh").select();
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
		NewJavaClassWizardDialog jd = new NewJavaClassWizardDialog();
		jd.open();
		NewJavaClassWizardPage jp = jd.getFirstPage();
		jp.setName("ParametersExtension");
		jp.setPackage("cdi.parameters");
		jd.finish();
		
	}
	
	@Test
	public void testParametersAnnotation(){
		TextEditor te = new TextEditor("ParametersExtension.java");
		te.insertLine(2, "import javax.enterprise.context.ApplicationScoped;");
		te.insertLine(3, "import javax.inject.Inject;");
		te.insertLine(4, "import java.util.List;");
		te.insertLine(5, "import org.jboss.weld.environment.se.bindings.Parameters;");
		te.insertLine(6, "@ApplicationScoped");
		AbstractWait.sleep(TimePeriod.NORMAL);
		te.insertLine(8, "@Inject @Parameters List<String> parameters;");
		te.insertLine(9, "@Inject @Parameters String[] paramsArray;");
		new WaitWhile(new EditorHasValidationMarkers(te),TimePeriod.NORMAL, false);
		assertEquals(0,te.getMarkers().size());
		te.save();
		assertEquals(0,te.getMarkers().size());
		
		
	}
	
	

}
