package org.jboss.tools.cdi.bot.test.extensions;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

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
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.Marker;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDI11TestBase;
import org.jboss.tools.cdi.reddeer.uiutils.EditorResourceHelper;
import org.junit.Before;
import org.junit.Test;

//based on JBIDE-12645
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY8x)
public class BuiltInContexts extends CDI11TestBase{
	
	public static final String WELD_API_JAR=System.getProperty("jbosstools.test.weld-api.home");
	
	@Before
	public void addLibs(){
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		if(pe.getProject(PROJECT_NAME).containsItem("src","test","BuiltInContexts.java")){
			return;
		}
		NewJavaClassWizardDialog jd = new NewJavaClassWizardDialog();
		jd.open();
		NewJavaClassWizardPage jp = jd.getFirstPage();
		jp.setPackage("test");
		jp.setName("BuiltInContexts");
		jd.finish();
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
		IJavaProject javaProject = JavaCore.create(project);
		IClasspathEntry[] classpath= null;
		try {
			classpath = javaProject.getRawClasspath();
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IClasspathEntry[] newClasspath = new IClasspathEntry[classpath.length+1];
		System.arraycopy(classpath, 0, newClasspath, 0, classpath.length);
		
		newClasspath[classpath.length] = JavaCore.newLibraryEntry(new Path(WELD_API_JAR), null, null);
		try {
			javaProject.setRawClasspath(newClasspath, null);
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EditorResourceHelper rhelper = new EditorResourceHelper();
		try {
			rhelper.replaceClassContentByResource("BuiltInContexts.java", new FileInputStream("resources/cdi11/BuiltInContexts.jav_"), false);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testBuildInContextsWarnings(){
		TextEditor te = new TextEditor("BuiltInContexts.java");
		AbstractWait.sleep(TimePeriod.NORMAL);
		List<Marker> markers = te.getMarkers();
		assertEquals(3,markers.size());
		for(Marker m: te.getMarkers()){
			assertEquals("org.eclipse.ui.workbench.texteditor.warning",m.getType());
			assertEquals("Multiple beans are eligible for injection to the injection point [JSR-346 §5.2.2]", m.getText());
			assertTrue(m.getLineNumber() == 70 || m.getLineNumber() == 73 || m.getLineNumber() == 76);
		}
	}
	
	@Test
	public void testBuildInContextsOpenOns(){
		openOn("dependentContext", "DependentContext");
		openOn("applicationContext", "ApplicationContext");
		openOn("singletonContext", "SingletonContext");
		openOn("unboundRequestContext", "RequestContext");
		openOn("bboundRequestContext", "BoundRequestContext");
		openOn("bboundRequestContext2", "BoundRequestContext");
		openOn("httpRequestContext", "HttpRequestContext");
		openOn("httpRequestContext2", "HttpRequestContext");
		openOn("ejbRequestContext", "EjbRequestContext");
		openOn("ejbRequestContext2", "EjbRequestContext");
		
		openOn("boundConversationContext", "BoundConversationContext");
		openOn("boundConversationContext2", "BoundConversationContext");
		openOn("httpConversationContext", "HttpConversationContext");
		openOn("httpConversationContext2", "HttpConversationContext");
		openOn("boundSessionContext", "BoundSessionContext");
		openOn("boundSessionContext2", "BoundSessionContext");
		
		openOn("httpSessionContext", "HttpSessionContext");
		openOn("httpSessionContext2", "HttpSessionContext");
	}
	
	private void openOn(String text, String proposal){
		TextEditor te = new TextEditor("BuiltInContexts.java");
		te.selectText(text);
		te.openOpenOnAssistant().chooseProposal("Open @Inject Bean "+proposal);
		new DefaultEditor(proposal+".class");
	}
	
	
}
