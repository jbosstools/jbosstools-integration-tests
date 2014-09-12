package org.jboss.tools.ws.ui.bot.test.annotation;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
@JBossServer(state=ServerReqState.STOPPED)
@OpenPerspective(JavaEEPerspective.class)
public class HTTPMethodAnnotationQuickFixTest extends RESTfulTestBase {

	@Override
	public void setup() {
		
	}

	@Override
	public void cleanup() {
		deleteAllProjects();
	}

	@Test
	public void testHTTPMethodWithoutParameters() {
		/* import the project */
		String projectName = "httpAnnot1";
		importWSTestProject(projectName);

		/* assert that there is one Java problem */
		assertCountOfErrors(1);

		/* open MyAnnot.java */
		openMyAnnotJavaFile(projectName);
		TextEditor editor = setCursorPositionToLineInTextEditor("@HttpMethod");

		/* check that there are quick fixes for both required annotations */
		editor.openQuickFixContentAssistant().chooseProposal(
				"Add missing attributes");
		new TextEditor().save();
		AbstractWait.sleep(TimePeriod.getCustom(2));

		/* assert that there is one JAX-RS errors - empty value */
		assertCountOfValidationErrors(projectName, 1);
	}

	/**
	 * Fails due to JBIDE-18256
	 * 
	 * @see https://issues.jboss.org/browse/JBIDE-18256
	 * 
	 * Resolved - No JAX-RS problems when importing a project that contains
	 * HTTPMethod annotation without @Target and @Retention
	 * {@link https://issues.jboss.org/browse/JBIDE-15428}
	 * 
	 * Resolved - Quick fix for HTTPMethod annotation without @Target and
	 * @Retention doesn't add all imports
	 * {@link https://issues.jboss.org/browse/JBIDE-17667}
	 */
	@Test
	public void testTargetRetentionQuickFixes() {
		/* import the project */
		String projectName = "httpAnnot2";
		importWSTestProject(projectName);

		/* assert that there are two JAX-RS errors */
		assertCountOfValidationErrors(projectName, 2);

		/* open MyAnnot.java */
		openMyAnnotJavaFile(projectName);
		TextEditor editor = setCursorPositionToLineInTextEditor("MyAnnot");

		/* check that there are quick fixes for both required annotations */
		editor.openQuickFixContentAssistant().chooseProposal(
				"Add @Target annotation on type 'MyAnnot'");
		AbstractWait.sleep(TimePeriod.getCustom(1));//makes a delay between applying quickfixes

		/* there is need to wait a while until validation starts to work */
		editor.openQuickFixContentAssistant().chooseProposal(
				"Add @Retention annotation on type 'MyAnnot'");

		/* save edited file */
		new TextEditor().save();
		AbstractWait.sleep(TimePeriod.getCustom(2));

		/* assert that there are no JAX-RS errors */
		assertCountOfValidationErrors(projectName, 0);
	}

	private void openMyAnnotJavaFile(String projectName) {
		openJavaFile(projectName, "test", "MyAnnot.java");
	}
}
