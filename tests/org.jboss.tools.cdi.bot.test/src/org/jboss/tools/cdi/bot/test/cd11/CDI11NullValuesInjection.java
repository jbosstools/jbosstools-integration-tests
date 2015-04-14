package org.jboss.tools.cdi.bot.test.cd11;

import static org.junit.Assert.*;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDI11TestBase;
import org.jboss.tools.cdi.reddeer.annotation.CDIWizardType;
import org.jboss.tools.cdi.reddeer.uiutils.CDIWizardHelper;
import org.junit.Test;

@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY8x)
public class CDI11NullValuesInjection extends CDI11TestBase{
	
	@InjectRequirement
	protected static ServerRequirement sr;
	
	@Test
	public void injectNullValue(){
		createClass(PROJECT_NAME, "Bean1");
		createClass(PROJECT_NAME, "Bean2");
		CDIWizardHelper wz = new CDIWizardHelper();
		wz.createCDIComponent(CDIWizardType.QUALIFIER, "Qa", "test", null);
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
		new WaitWhile(new EditorHasValidationMarkers(te));
		te.save();
		new WaitWhile(new EditorHasValidationMarkers(te));
		checkOpenOn("primitiveB","Open @Inject Bean Bean1.getB()", "Bean1.java");
		te.activate();
		checkOpenOn("objectB", "Open @Inject Bean Bean1.getB()", "Bean1.java");
	}
	
	private void checkOpenOn(String text,String openOn,String expectedEditor){
		TextEditor te = new TextEditor();
		te.selectText(text);
		ContentAssistant ca = te.openOpenOnAssistant();
		ca.chooseProposal(openOn);
		assertEquals(expectedEditor,new TextEditor().getTitle());
	}
	
	private void createClass(String project, String className){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(project);
		NewJavaClassWizardDialog c = new NewJavaClassWizardDialog();
		c.open();
		NewJavaClassWizardPage page = c.getFirstPage();
		page.setPackage("test");
		page.setName(className);
		c.finish();
	}

}
