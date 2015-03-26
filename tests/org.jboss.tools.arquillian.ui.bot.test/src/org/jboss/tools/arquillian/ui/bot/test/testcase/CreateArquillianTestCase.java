package org.jboss.tools.arquillian.ui.bot.test.testcase;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.arquillian.ui.bot.reddeer.junit.ArquillianJUnitTestCaseWizard;
import org.jboss.tools.arquillian.ui.bot.reddeer.junit.JUnitTestCaseWizardPage;
import org.jboss.tools.arquillian.ui.bot.test.AbstractArquillianTestCase;
import org.junit.Test;

/**
 * Creates Arquillian test case and checks that
 * 
 * <ul>
 * 	<li> there are no errors in Problems view
 * </ul>
 * @author Lucia Jelinkova
 *
 */
public class CreateArquillianTestCase extends AbstractArquillianTestCase {

	protected static String SOURCE_FOLDER = PROJECT_NAME + "/src/test/java";

	@Test
	public void testTestCaseCreation(){
		createTestCase();
		changeContent();
		checkProblems();
	}
	
	private void createTestCase(){
		ArquillianJUnitTestCaseWizard wizard = new ArquillianJUnitTestCaseWizard();
		wizard.open();
		
		JUnitTestCaseWizardPage page = new JUnitTestCaseWizardPage();
		page.setSourceFolder(SOURCE_FOLDER);
		page.setPackage(PACKAGE);
		page.setName(TEST_CASE);
		
		wizard.finish();
	}
	
	private void changeContent() {
		TextEditor editor = new TextEditor(TEST_CASE + ".java");
		editor.selectText("fail(\"Not yet implemented\");");
		KeyboardFactory.getKeyboard().type(SWT.DEL);
		editor.save();
	}
}
