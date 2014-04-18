package org.jboss.tools.hb.ui.bot.test.ant;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.junit.Test;

/**
 * Hibernate export ant code generation ui bot test. 
 * Hibernate code generation configuration is used to be exported as an ant file  
 * 
 * @author jpeterka
 * 
 */
public class ExportAntCodeGenerationTest {
	final String prjName = "hibernate35";
	final String genCfg = "hb35hsqldb";
	final String antCfg = "build.hb.xml";

	@Test
	public void hibernateCodeGeneration() {
		
		ExternalProjectImportWizardDialog d = new ExternalProjectImportWizardDialog();
		d.open();
		WizardProjectsImportPage firstPage = d.getFirstPage();
		firstPage.setRootDirectory("/resources/prj/");
		firstPage.selectProjects("hibernatelib","hibernate35");
		
		exportAntCodeGeneration();
		checkGeneratedAntcode();
		
		d.finish();
	}

	private void exportAntCodeGeneration() {
		
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.getProject(prjName).select();
		new ContextMenu("Export...").select();

		new DefaultTreeItem("Hibernate", "Ant Code Generation").select();
	}

	private void checkGeneratedAntcode() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		projectExplorer.selectProjects(prjName);
	}

}
