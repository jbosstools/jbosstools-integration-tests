package org.jboss.tools.cdi.bot.test.weld.template;

import static org.junit.Assert.*;

import java.util.List;

import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.jboss.reddeer.workbench.impl.editor.Marker;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.jboss.tools.cdi.reddeer.validators.IValidationProvider;
import org.jboss.tools.cdi.reddeer.validators.ValidationProblem;
import org.junit.Before;
import org.junit.Test;

//based on JBIDE-14765
public class WeldExcludeTemplate extends CDITestBase{
	
	//public static final String WELD_API_JAR=System.getProperty("jbosstools.test.weld-api.home");
	protected IValidationProvider validationProvider= null;
	
	@Before
	public void prepareWorkspace1(){
		//projectHelper.addLibrariesIntoProject(PROJECT_NAME, WELD_API_JAR);
		excludeInBeansXml();
		createBeans();
	}
	
	private void createBeans(){
		beansHelper.createBean("Bean1",  "exclude.p1", false, false, false, false, false, false,
				false, null, "@ApplicationScoped");
		beansHelper.createBean("Bean2",  "exclude.p1", false, false, false, false, false, false,
				false, null, "@ApplicationScoped");
		beansHelper.createBean("Bean3",  "exclude.p2", false, false, false, false, false, false,
				false, null, "@ApplicationScoped");
		beansHelper.createBean("Bean4",  "exclude.p2.p3", false, false, false, false, false, false,
				false, null, "@ApplicationScoped");
		beansHelper.createBean("Bean5",  "exclude.p4", false, false, false, false, false, false,
				false, null, "@ApplicationScoped");
		beansHelper.createBean("Bean6",  "exclude.p4.p5", false, false, false, false, false, false,
				false, null, "@ApplicationScoped");
		beansHelper.createBean("Bean7",  "exclude.p6", false, false, false, false, false, false,
				false, null, "@ApplicationScoped");
	}
	
	private void excludeInBeansXml() {
		EditorPartWrapper beans = beansXMLHelper.openBeansXml(PROJECT_NAME);
		beans.activateSourcePage();
		editResourceUtil.replaceInEditor("xsi:schemaLocation", 
			       "xmlns:weld=\"http://jboss.org/schema/weld/beans\" xsi:schemaLocation" );
		editResourceUtil.replaceInEditor("\">", 
				" http://jboss.org/schema/weld/beans http://jboss.org/schema/weld/beans_1_1.xsd\">");
		editResourceUtil.replaceInEditor("</beans>", readFile("resources/excluded/weld/beans.xm_"),true);
		beans.save();
	}
	
	@Test
	public void testExcludedBeans(){
		NewJavaClassWizardDialog jd = new NewJavaClassWizardDialog();
		jd.open();
		NewJavaClassWizardPage jp = new NewJavaClassWizardPage();
		jp.setName("TestExcluded");
		jp.setPackage("test");
		jd.finish();
		
		TextEditor ed = new TextEditor("TestExcluded.java");
		editResourceUtil.replaceClassContentByResource("TestExcluded.java",
				readFile("resources/excluded/TestExcluded.jav_"), false);
		
		String warning = "org.eclipse.ui.workbench.texteditor.warning";
		
		new WaitUntil(new EditorHasValidationMarkers(ed));
		
		List<Marker> markers = ed.getMarkers();
		assertEquals(4, markers.size());
		
		ValidationProblem expected = validationProvider.getValidationProblem(ValidationType.NO_BEAN_ELIGIBLE);
		
		for(Marker m: ed.getMarkers()){
			
			assertTrue((m.getLineNumber() == 14 && m.getType().equals(warning) && m.getText().contains(expected.getMessage())
					&& m.getText().contains(expected.getJSR())) ||
					(m.getLineNumber() == 16 && m.getType().equals(warning) && m.getText().contains(expected.getMessage())
					&& m.getText().contains(expected.getJSR())) ||
					(m.getLineNumber() == 18 && m.getType().equals(warning) && m.getText().contains(expected.getMessage())
					&& m.getText().contains(expected.getJSR())) ||
					(m.getLineNumber() == 19 && m.getType().equals(warning) && m.getText().contains(expected.getMessage())
					&& m.getText().contains(expected.getJSR())) );
		}
		
		ProblemsView pw = new ProblemsView();
		pw.open();
		assertEquals(4,pw.getProblems(ProblemType.ANY).size());
		
		List<Problem> foundProblems = validationHelper.findProblems(expected);
		assertEquals(4,foundProblems.size());
	}
	
	

}
