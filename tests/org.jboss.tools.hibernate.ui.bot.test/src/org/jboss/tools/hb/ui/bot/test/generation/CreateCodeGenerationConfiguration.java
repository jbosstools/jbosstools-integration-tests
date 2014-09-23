package org.jboss.tools.hb.ui.bot.test.generation;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.hibernate.reddeer.test.HibernateRedDeerTest;

/**
 * Hibernate code generation configuration ui bot test
 * - Code generation configuration can be created
 * - Source code can be generated via hibernate generation configuration
 * @author jpeterka
 * 
 */
public class CreateCodeGenerationConfiguration extends HibernateRedDeerTest {

	private String prjName = "";
		
	
	public String getPrjName() {
		return prjName;
	}

	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}
	
	public void hibernateCodeGeneration() {
		importProject("/resources/prj/hibernatelib");
		importProject("/resources/prj/" + prjName);
		fillMainTab();
		fillExportersTab();
		runCodeGeneration();

		PackageExplorer pe = new PackageExplorer();
		pe.open();
		new DefaultTreeItem(prjName,"gen","org","test","Actor.java");		
	}


	private void runCodeGeneration() {
		new PushButton("Run").click();	
	}

	private void fillMainTab() {
		
		new DefaultTreeItem("Hibernate Code Generation").select();
		new DefaultToolItem("New launch configuration").click();

		new DefaultTreeItem("Hibernate Code Generation","New_configuration").select();
		new LabeledText("Name:").setText(prjName + "-gen");
		new PushButton("Apply");

		// Console Configuration
		new LabeledCombo("Console configuration:").setSelection(prjName);

		// Output directory
		new PushButton("Browse...").click();
		new DefaultShell("Select output directory");
		new DefaultTreeItem(prjName);
		new PushButton("Create New Folder...").click();
		new DefaultShell("New Folder");
		new LabeledText("Folder name:").setText("gen");
		new OkButton().click();
		new DefaultTreeItem(prjName, "gen");
		new OkButton().click();

		// Create console configuration
		new LabeledText("Package:").setText("org.test");
	}

	private void fillExportersTab() {
		DefaultTable t = new DefaultTable();
		t.getItem(0).setChecked(true);
	}


}
