package org.jboss.tools.ws.ui.bot.test.rest;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ws.reddeer.editor.ExtendedTextEditor;
import org.junit.Test;

/**
 * Testing support for custom Name Binding annotation<br/><br/>
 * 
 * Run with J2EE7+ server
 * 
 * @author Radoslav Rabara
 * 
 * @see http://tools.jboss.org/documentation/whatsnew/jbosstools/4.2.0.Beta1.html#webservices
 * @since JBT 4.2.0.Beta1
 */
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY)
public class NameBindingAnnotationSupportTest extends RESTfulTestBase {
	
	@Override
	public void setup() {
		// no setup required
	}
	
	@Test
	public void definingNameBindingAnnotationWithoutTargetAndRetentionTest() {
		/* import the project */
		String projectName = "namebinding1";
		importAndCheckErrors(projectName);

		openJavaFile(projectName, "org.rest.test", "Authorized.java");

		/* remove @Target and @Retention annotations*/
		ExtendedTextEditor textEditor = new ExtendedTextEditor();
		textEditor.removeLine("@Target({ElementType.METHOD, ElementType.TYPE})");
		textEditor.removeLine("@Retention(RetentionPolicy.RUNTIME)");

		/* there should be 2 errors complaining about missing deleted annotations */
		assertCountOfValidationErrors(projectName, 2);
		assertCountOfValidationErrors(projectName, "Retention", 1);
		assertCountOfValidationErrors(projectName, "Target", 1);
		assertCountOfErrors(projectName, 2);

		/* prepare editor */
		openAuthorizedJavaFile(projectName);
		TextEditor editor = setCursorPositionToTextInTextEditor("Authorized");

		/* check that there are quick fixes for both required annotations */
		editor.openQuickFixContentAssistant().chooseProposal("Add @Retention annotation on type 'Authorized'");
		new TextEditor().save();

		/* one error should disappear as a result of using a quickfix */
		assertCountOfValidationErrors(projectName, 1);
		assertCountOfValidationErrors(projectName, "Retention", 0);

		/* apply the second quixk fix */
		editor.activate();
		editor.openQuickFixContentAssistant().chooseProposal("Add @Target annotation on type 'Authorized'");
		new TextEditor().save();

		/* both quickfixes were used which means that there should be no error */
		assertCountOfValidationErrors(projectName, 0);
		assertCountOfValidationErrors(projectName, "Retention", 0);

	}

	@Test
	public void usingNameBindingAnnotationWithoutFilterOrInterceptor() {
		/* import the project */
		String projectName = "namebinding2";
		importRestWSProject(projectName);

		/* remove the filter */
		new ProjectExplorer().getProject(projectName)
			.getProjectItem("Java Resources", "src", "org.rest.test", "Filter.java").delete();
		
		/* there should be an error */
		assertCountOfValidationErrors(projectName, 1);
		assertCountOfValidationErrors(projectName, "no JAX-RS filter or interceptor", 1);
	}

	private void openAuthorizedJavaFile(String projectName) {
		openJavaFile(projectName, "org.rest.test", "Authorized.java");
	}
}
