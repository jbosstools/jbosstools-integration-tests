package org.jboss.tools.deltaspike.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewClassCreationWizard;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.eclipse.reddeer.workbench.impl.editor.Marker;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.tools.cdi.reddeer.uiutils.EditorResourceHelper;
import org.jboss.tools.deltaspike.ui.bot.test.condition.ClassHasErrorOrWarningMarker;
import org.junit.Before;
import org.junit.Test;


//based on JBIDE-13419
public class PartialBeanTest extends DeltaspikeTestBase{
	
	private String projectName = "partialBean";
	
	@RequirementRestriction 
	public static Collection<RequirementMatcher> getRestrictionMatcher() {
		return getServerRuntimeRestriction();
	}
	
	@InjectRequirement
	private ServerRequirement sr;
	
	@Before
	public void importProject(){
		importDeltaspikeProject(projectName, sr);
	}
	
	@Test
	public void testPartialBean(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(projectName).select();
		String classesPath = "resources/classes/binding/";
		
		createClassWithContent("ExamplePartialBeanBinding", classesPath+"ExamplePartialBeanBinding.jav_");
		new WaitWhile(new ClassHasErrorOrWarningMarker("ExamplePartialBeanBinding"));
		
		// Each partial bean should be bound to an invocation handler.
		createClassWithContent("ExamplePartialBeanInterface", classesPath+"ExamplePartialBeanInterface.jav_");
		
		// remove this and use asserErrorExists when JBIDE-18852 will be resolved
		TextEditor ed1 = new TextEditor("ExamplePartialBeanInterface.java");
		try{
			new WaitUntil(new EditorHasValidationMarkers(ed1),TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex){
			fail("this is known issue JBIDE-18852");
		}
		assertEquals(1,ed1.getMarkers().size());
		Marker marker = ed1.getMarkers().get(0);
		assertEquals("Partial bean test.ExamplePartialBeanInterface should have an invocation handler for binding annotation test.ExamplePartialBeanBinding", marker.getText());
		assertEquals("org.eclipse.ui.workbench.texteditor.warning", marker.getType());
		
		//assertErrorExists("ExamplePartialBeanInterface","Partial bean test.ExamplePartialBeanInterface should have an invocation handler for binding annotation test.ExamplePartialBeanBinding");
		
		// Each partial bean should be bound to an invocation handler.
		createClassWithContent("ExamplePartialBeanAbstractClass", classesPath+"ExamplePartialBeanAbstractClass.jav_");
		assertErrorExists("ExamplePartialBeanAbstractClass","Partial bean test.ExamplePartialBeanAbstractClass should have an invocation handler for binding annotation test.ExamplePartialBeanBinding");
		
		createClassWithContent("ExamplePartialBeanImplementation", classesPath+"ExamplePartialBeanImplementation.jav_");
		new WaitWhile(new ClassHasErrorOrWarningMarker("ExamplePartialBeanImplementation"));
		new WaitWhile(new ClassHasErrorOrWarningMarker("ExamplePartialBeanAbstractClass"));
		new WaitWhile(new ClassHasErrorOrWarningMarker("ExamplePartialBeanInterface"));
		
		//Class annotated with a binding annotation should be either abstract, or interface, or implement InvocationHandler.
		TextEditor ed = new TextEditor("ExamplePartialBeanImplementation.java");
		ed.setText(ed.getText().replace("import java.lang.reflect.InvocationHandler;", ""));
		ed.setText(ed.getText().replace("implements InvocationHandler", ""));
		ed.save();
		assertErrorExists("ExamplePartialBeanImplementation","Binding annotation test.ExamplePartialBeanBinding can be applied only to abstract classes, interfaces, and classes implementing InvocationHandler");
	
		replaceClassContent("ExamplePartialBeanImplementation", classesPath+"ExamplePartialBeanImplementation.jav_");
		new WaitWhile(new ClassHasErrorOrWarningMarker("ExamplePartialBeanImplementation"));
		
		//There should be no more than one invocation handler for each binding annotation.
		createClassWithContent("AnotherImplementation", classesPath+"AnotherImplementation.jav_");
		assertErrorExists("AnotherImplementation","Multiple handlers are found for binding annotation test.ExamplePartialBeanBinding");
		assertErrorExists("ExamplePartialBeanImplementation","Multiple handlers are found for binding annotation test.ExamplePartialBeanBinding");
		pe.open();
		pe.getProject(projectName).getProjectItem("Java Resources","src","test","AnotherImplementation.java").delete();
		new WaitWhile(new ClassHasErrorOrWarningMarker("ExamplePartialBeanImplementation"));
		
		// Invocation handler class should be normal-scoped.
		ed = new TextEditor("ExamplePartialBeanImplementation.java");
		ed.setText(ed.getText().replace("@ApplicationScoped", "@Dependent"));
		ed.setText(ed.getText().replace("import javax.enterprise.context.ApplicationScoped", "import javax.enterprise.context.Dependent"));
		ed.save();
		assertErrorExists("ExamplePartialBeanImplementation","Invocation handler class should be a normal-scoped bean");
	
		replaceClassContent("ExamplePartialBeanImplementation", classesPath+"ExamplePartialBeanImplementation.jav_");
		new WaitWhile(new ClassHasErrorOrWarningMarker("ExamplePartialBeanImplementation"));
		
		createClassWithContent("AnotherBinding", classesPath+"AnotherBinding.jav_");
		new WaitWhile(new ClassHasErrorOrWarningMarker("AnotherBinding"));
		
		//Deltaspike implementation of the extension reads the first binding annotation on a class and ignores the next ones. Hence, they should be marked with a warning:
		ed = new TextEditor("ExamplePartialBeanImplementation.java");
		int pos = ed.getPositionOfText("@ExamplePartialBeanBinding");
		ed.insertText(pos, "@AnotherBinding\n");
		ed.save();
		assertErrorExists("ExamplePartialBeanImplementation","Binding annotation test.ExamplePartialBeanBinding is ignored because class is already annotated with binding annotation test.AnotherBinding");
		
	}
	
	private void createClassWithContent(String className, String contentPath){
		NewClassCreationWizard jd = new NewClassCreationWizard();
		jd.open();
		NewClassWizardPage jp = new NewClassWizardPage(jd);
		jp.setName(className);
		jp.setPackage("test");
		jd.finish();
		replaceClassContent(className, contentPath);
	}
	
	private void replaceClassContent(String className, String contentPath){
		EditorResourceHelper eh = new EditorResourceHelper();
		eh.replaceClassContentByResource(className+".java", readFile(contentPath), false);
	}
	
	private void assertErrorExists(String className, String errorMessage){
		ClassHasErrorOrWarningMarker cond = new ClassHasErrorOrWarningMarker(className);
		new WaitUntil(cond ,TimePeriod.LONG);
		
		List<Marker> markers = cond.getResult();
		assertEquals(1,markers.size());
		
		Marker marker = markers.get(0);
		assertEquals(errorMessage, marker.getText());
		assertEquals("org.eclipse.ui.workbench.texteditor.warning", marker.getType());
	}

	protected static String readFile(String path) {
		Scanner s = null;
		try {
			s = new Scanner(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			fail("unable to find file "+path);
		}
		Scanner s1 = s.useDelimiter("\\A");
		String file =  s.next();
		s.close();
		s1.close();
		return file;
	}
}
