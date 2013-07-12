package org.jboss.tools.jbpm.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.ide.NewJavaProjectWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.jbpm.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.jbpm.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.jbpm.ui.bot.test.wizard.ExportBPMNWizard;
import org.jboss.tools.jbpm.ui.bot.test.wizard.ImportFileWizard;
import org.junit.Before;
import org.junit.Test;

@CleanWorkspace
@Perspective(name = "Java")
public class BPMNConvertCase extends SWTBotTestCase {

	private String projectName = "BPMNConvertProject";
	private String originalFolder = "original";
	private String targetFolder = "target";
	private String file1 = "PolicyPricingProcess.bpmn";

	@Before
	public void prepareProject() {
		// Create Java Project
		NewJavaProjectWizardDialog projectWizard = new NewJavaProjectWizardDialog();
		projectWizard.open();
		projectWizard.getFirstPage().setProjectName(projectName);
		projectWizard.finish();

		// create original folder
		new PackageExplorer().getProject(projectName).select();
		new ShellMenu("File", "New", "Folder").select();
		new LabeledText("Folder name:").setText(originalFolder);
		new PushButton("Finish").click();

		// create target folder
		new PackageExplorer().getProject(projectName).select();
		new ShellMenu("File", "New", "Folder").select();
		new LabeledText("Folder name:").setText(targetFolder);
		new PushButton("Finish").click();

		// import files
		new PackageExplorer().getProject(projectName).getProjectItem(originalFolder).select();
		new ImportFileWizard().importFile("resources/" + originalFolder, originalFolder);
	}

	@Test
	public void convertProcess() {
		new PackageExplorer().getProject(projectName).getProjectItem(originalFolder, file1).select();
		new ExportBPMNWizard().exportFile(projectName, targetFolder);
		new PackageExplorer().getProject(projectName)
				.getProjectItem(targetFolder, "jpdl", file1, "Policy Pricing", "processdefinition.xml").open();
	}

}
