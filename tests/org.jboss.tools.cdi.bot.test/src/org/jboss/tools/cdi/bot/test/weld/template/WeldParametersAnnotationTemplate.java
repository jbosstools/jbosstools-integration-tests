package org.jboss.tools.cdi.bot.test.weld.template;

import static org.junit.Assert.*;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.junit.Before;
import org.junit.Test;

//based on JBIDE-12644
public class WeldParametersAnnotationTemplate extends CDITestBase{
	
	public static final String WELD_SE_JAR=System.getProperty("jbosstools.test.weld-se.home");
	
	@Before
	public void addLibs(){
		projectHelper.addLibrariesIntoProject(PROJECT_NAME, WELD_SE_JAR);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).select();
		new ContextMenu("Refresh").select();
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
		NewJavaClassWizardDialog jd = new NewJavaClassWizardDialog();
		jd.open();
		NewJavaClassWizardPage jp = new NewJavaClassWizardPage();
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
		new WaitWhile(new JobIsRunning());
		new WaitWhile(new EditorHasValidationMarkers(te),TimePeriod.NORMAL, false);
		assertEquals(0,te.getMarkers().size());
		te.save();
		new WaitUntil(new JobIsRunning(),TimePeriod.SHORT,false);
		new WaitWhile(new JobIsRunning());
		assertEquals(0,te.getMarkers().size());
		
		
	}
	
	

}
