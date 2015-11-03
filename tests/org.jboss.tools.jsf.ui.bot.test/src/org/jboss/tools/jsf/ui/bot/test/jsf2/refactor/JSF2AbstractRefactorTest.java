package org.jboss.tools.jsf.ui.bot.test.jsf2.refactor;

import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.jsf.ui.bot.test.JSFAutoTestCase;
import org.jboss.tools.jst.reddeer.web.ui.NewXHTMLFileWizardPage;
import org.jboss.tools.jst.reddeer.web.ui.NewXHTMLWizard;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

public abstract class JSF2AbstractRefactorTest extends JSFAutoTestCase {

	protected static final String JSF2_Test_Page_Name = "jsf2TestPage"; //$NON-NLS-1$

	protected void createCompositeComponent() throws Exception {
		packageExplorer.open();
		try {
			packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent","resources").select();
		} catch (EclipseLayerException ele) {
			packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent").select();
			new ShellMenu("File","New","Folder").select();
			new DefaultShell(IDELabel.Shell.NEW_FOLDER);
			new LabeledText("Folder name:").setText("resources");
			new FinishButton().click();
		}
		try {
			packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent","resources","mycomp").select();
		} catch (EclipseLayerException ele) {
			packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent","resources").select();
			new ShellMenu("File","New","Folder").select();
			new DefaultShell(IDELabel.Shell.NEW_FOLDER);
			new LabeledText("Folder name:").setText("mycomp");
			new FinishButton().click();
		}
		try {
			packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent","resources","mycomp","echo.xhtml");
		} catch (EclipseLayerException ele) {
			packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent","resources","mycomp").select();
			NewXHTMLWizard newXHTMLWizard = new NewXHTMLWizard();
			newXHTMLWizard.open();
			NewXHTMLFileWizardPage newXHTMLFileWizardPage = new NewXHTMLFileWizardPage();
			newXHTMLFileWizardPage.setFileName("echo");
			newXHTMLFileWizardPage.selectParentFolder(JBT_TEST_PROJECT_NAME,"WebContent","resources","mycomp");
			newXHTMLWizard.finish();
			TextEditor editor = new TextEditor("echo.xhtml");
			editor.setText(loadFileContent("refactor/compositeComponent.html"));
			editor.save();
		}
	}

	protected void createTestPage() throws Exception {
		packageExplorer.open();
		try {
			packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent",JSF2_Test_Page_Name + ".xhtml").open();
		} catch (EclipseLayerException ele) {
			packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent").select();
			NewXHTMLWizard newXHTMLWizard = new NewXHTMLWizard();
			newXHTMLWizard.open();
			NewXHTMLFileWizardPage newXHTMLFileWizardPage = new NewXHTMLFileWizardPage();
			newXHTMLFileWizardPage.setFileName(JSF2_Test_Page_Name);
			newXHTMLWizard.finish();
		}
		TextEditor editor = new TextEditor(JSF2_Test_Page_Name + ".xhtml");
		editor.setText(loadFileContent("refactor/jsf2TestPage.html"));
		editor.save();
		editor.close();
	}

}
