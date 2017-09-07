package org.jboss.tools.cdi.bot.test.weld.template;

import static org.junit.Assert.assertEquals;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewClassCreationWizard;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.swt.impl.menu.ContextMenu;
import org.eclipse.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
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
		new ContextMenu().getItem("Refresh").select();
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
		NewClassCreationWizard jd = new NewClassCreationWizard();
		jd.open();
		NewClassWizardPage jp = new NewClassWizardPage(jd);
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
		AbstractWait.sleep(TimePeriod.DEFAULT);
		te.insertLine(8, "@Inject @Parameters List<String> parameters;");
		te.insertLine(9, "@Inject @Parameters String[] paramsArray;");
		new WaitWhile(new JobIsRunning());
		new WaitWhile(new EditorHasValidationMarkers(te),TimePeriod.DEFAULT, false);
		assertEquals(0,te.getMarkers().size());
		te.save();
		new WaitUntil(new JobIsRunning(),TimePeriod.SHORT,false);
		new WaitWhile(new JobIsRunning());
		assertEquals(0,te.getMarkers().size());
		
		
	}
	
	

}
