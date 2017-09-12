package org.jboss.tools.cdi.bot.test.beans.bean.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.jface.text.contentassist.ContentAssistant;
import org.eclipse.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.junit.Test;

public class NullValuesInjectionTemplate extends CDITestBase{
	
	protected String CDIVersion;
	
	//cdi1.1+
	@Test
	public void injectNullValue(){
		beansHelper.createClass("Bean1", "test");
		beansHelper.createClass("Bean2", "test");
		beansHelper.createQualifier("Qa","test", false,false);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem("Java Resources","src","test","Bean1.java").open();
		TextEditor te = new TextEditor("Bean1.java");
		te.insertLine(2, "import javax.enterprise.inject.Produces;");
		te.insertLine(3, "import javax.enterprise.context.ApplicationScoped;");
		te.insertLine(4, "@ApplicationScoped");
		te.insertLine(6, "@Produces");
		te.insertLine(7, "@Qa");
		te.insertLine(8, "public Boolean getB(){return null;}");
		new WaitWhile(new EditorHasValidationMarkers(te));
		te.save();
		new WaitWhile(new EditorHasValidationMarkers(te));
		te = new TextEditor("Bean2.java");
		te.insertLine(2, "import javax.inject.Inject;");
		te.insertLine(3, "import javax.enterprise.context.ApplicationScoped;");
		te.insertLine(4, "@ApplicationScoped");
		te.insertLine(6, "@Inject @Qa boolean primitiveB;");
		te.insertLine(7, "@Inject @Qa Boolean objectB;");
		if(CDIVersion.equals("1.0")){
			new WaitUntil(new EditorHasValidationMarkers(te),TimePeriod.DEFAULT,false);
			assertTrue(new EditorHasValidationMarkers(te).test());
			te.save();
			new WaitWhile(new JobIsRunning());
			new WaitUntil(new EditorHasValidationMarkers(te),TimePeriod.DEFAULT,false);
			assertTrue(new EditorHasValidationMarkers(te).test());
		} else {
			new WaitUntil(new EditorHasValidationMarkers(te),TimePeriod.DEFAULT,false);
			new WaitWhile(new EditorHasValidationMarkers(te));
			te.save();
			new WaitWhile(new JobIsRunning());
			new WaitUntil(new EditorHasValidationMarkers(te),TimePeriod.DEFAULT,false);
			new WaitWhile(new EditorHasValidationMarkers(te));
			checkOpenOn("Bean2.java","primitiveB","Open @Inject Bean Bean1.getB()", "Bean1.java");
			checkOpenOn("Bean2.java","objectB", "Open @Inject Bean Bean1.getB()", "Bean1.java");
		}
	}
	
	private void checkOpenOn(String editor, String text,String openOn,String expectedEditor){
		TextEditor te = new TextEditor(editor);
		te.selectText(text);
		ContentAssistant ca = te.openOpenOnAssistant();
		ca.chooseProposal(openOn);
		assertEquals(expectedEditor,new TextEditor().getTitle());
	}

}
