package org.jboss.tools.cdi.bot.test.cd11;

import static org.junit.Assert.*;

import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.jboss.reddeer.workbench.impl.editor.Marker;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDI11TestBase;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY8x)
public class CDI11DiscoveryModes extends CDI11TestBase{

	@InjectRequirement
	protected static ServerRequirement sr;
	
	@Before
	public void addBeans(){
		createClass(PROJECT_NAME, "Bean1");
		createClass(PROJECT_NAME, "Bean2");
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem("src","test","Bean1.java").open();
		TextEditor ed = new TextEditor("Bean1.java");
		ed.insertLine(1, "import javax.inject.Inject;");
		ed.insertLine(4, "@Inject");
		ed.insertLine(5, "Bean2 bean2;");
		ed.save();
	}
	
	@After
	public void clean(){
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.deleteAllProjects();
	}
	
	private void createClass(String project, String className){
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.selectProjects(project);
		NewJavaClassWizardDialog c = new NewJavaClassWizardDialog();
		c.open();
		NewJavaClassWizardPage page = c.getFirstPage();
		page.setPackage("test");
		page.setName(className);
		c.finish();
	}
	
	@Test
	public void testModeAll(){
		setMode("all");
		TextEditor ed = new TextEditor("Bean1.java");
		try{
			new WaitUntil(new EditorHasValidationMarkers(ed));
		} catch (WaitTimeoutExpiredException ex){
			fail("this is known issue JBIDE-18964");
		}
		List<Marker> markers = ed.getMarkers();
		assertEquals(1,markers.size());
		assertEquals("No bean is eligible for injection to the injection point [JSR-299 §5.2.1]",markers.get(0).getText());
		assertEquals(6,markers.get(0).getLineNumber());
	}
	
	@Test
	public void testModeAnnotated(){
		setMode("annotated");
		TextEditor ed = new TextEditor("Bean1.java");
		List<Marker> markers = ed.getMarkers();
		assertEquals(0,markers.size());
		ed.insertLine(1, "import javax.enterprise.context.ApplicationScoped;");
		ed.insertLine(3, "@ApplicationScoped");
		new WaitUntil(new EditorHasValidationMarkers(ed, 6));
		markers = ed.getMarkers();
		assertEquals(1,markers.size());
		assertEquals("No bean is eligible for injection to the injection point [JSR-299 §5.2.1]",markers.get(0).getText());
		assertEquals(6,markers.get(0).getLineNumber());
		
		ed = new TextEditor("Bean2.java");
		ed.insertLine(1, "import javax.enterprise.context.ApplicationScoped;");
		ed.insertLine(2, "@ApplicationScoped");
		ed.save();
		
		ed = new TextEditor("Bean1.java");
		new WaitUntil(new EditorHasValidationMarkers(ed),TimePeriod.NORMAL, false);
		markers = ed.getMarkers();
		try{
			assertEquals(0,markers.size());
		} catch (AssertionError ex){
			fail("this is known issue JBIDE-18964");
		}
	}
	
	@Test
	public void testModeNone(){
		setMode("none");
		TextEditor ed = new TextEditor("Bean1.java");
		new WaitUntil(new EditorHasValidationMarkers(ed),TimePeriod.NORMAL, false);
		List<Marker> markers = ed.getMarkers();
		assertEquals(0,markers.size());
	}
	
	@Test
	public void testWithoutBeansXml(){
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem("WebContent","WEB-INF","beans.xml").delete();
		TextEditor ed = new TextEditor("Bean1.java");
		new WaitUntil(new EditorHasValidationMarkers(ed),TimePeriod.NORMAL, false);
		List<Marker> markers = ed.getMarkers();
		assertEquals(0,markers.size());
		ed.insertLine(1, "import javax.enterprise.context.ApplicationScoped;");
		ed.insertLine(3, "@ApplicationScoped");
		new WaitUntil(new EditorHasValidationMarkers(ed));
		markers = ed.getMarkers();
		assertEquals(1,markers.size());
		assertEquals("No bean is eligible for injection to the injection point [JSR-299 §5.2.1]",markers.get(0).getText());
		assertEquals(6,markers.get(0).getLineNumber());
		
		ed = new TextEditor("Bean2.java");
		ed.insertLine(1, "import javax.enterprise.context.ApplicationScoped;");
		ed.insertLine(3, "@ApplicationScoped");
		ed.save();
		
		ed = new TextEditor("Bean1.java");
		ed.save();
		new WaitUntil(new EditorHasValidationMarkers(ed),TimePeriod.NORMAL, false);
		markers = ed.getMarkers();
		assertEquals(0,markers.size());
		
		
	}
	
	private void setMode(String mode){
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem("WebContent","WEB-INF","beans.xml").open();
		EditorPartWrapper beans = new EditorPartWrapper();
		beans.activateTreePage();
		beans.setBeanDiscoveryMode(mode);
		beans.save();
	}
	
	
}
