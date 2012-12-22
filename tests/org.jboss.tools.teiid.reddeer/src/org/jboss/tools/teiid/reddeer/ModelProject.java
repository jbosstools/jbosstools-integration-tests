package org.jboss.tools.teiid.reddeer;

import org.jboss.reddeer.eclipse.datatools.ui.FlatFileProfile;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.wizard.ImportFlatSourceWizard;
import org.jboss.tools.teiid.reddeer.wizard.TeiidWizardPage;

/**
 * This class represents a model project.
 * 
 * @author apodhrad
 *
 */
public class ModelProject extends Project {

	public ModelProject(TreeItem treeItem) {
		super(treeItem);
	}

	/**
	 * Imports flat file source.
	 * 
	 * @param name
	 * @param fileName
	 * @param flatFileProfile
	 * @param headerLine
	 * @param dataLine
	 */
	public void importFlatFileSource(String name, String fileName, FlatFileProfile flatFileProfile,
			int headerLine, int dataLine) {
		select();

		TeiidWizardPage page = null;
		ImportFlatSourceWizard wizard = new ImportFlatSourceWizard();

		// Data File Source Selection
		page = wizard.openWizard();
		page.fillWizardPage("dataFileSource", flatFileProfile.getName());
		page.fillWizardPage("checkedFile", fileName);
		page.fillWizardPage("sourceName", name + "Source");

		// Flat File Column Format Definition
		page = wizard.nextPage();

		// Flat File Delimited Columns Parser Settings
		page = wizard.nextPage();
		if (headerLine > 0) {
			page.fillWizardPage("columnNamesInHeader", true);
			page.fillWizardPage("headerLine", headerLine);
		} else {
			page.fillWizardPage("columnNamesInHeader", false);
		}
		page.fillWizardPage("dataLine", dataLine);

		// View Model Definition
		page = wizard.nextPage();
		page.fillWizardPage("viewName", name + "View");
		page.fillWizardPage("tableName", name + "Table");

		wizard.finish();
	}

	/**
	 * Imports flat file source with headerLine = 1 and dataLine = 2
	 * 
	 * @param name
	 * @param fileName
	 * @param flatFileProfile
	 */
	public void importFlatFileSource(String name, String fileName, FlatFileProfile flatFileProfile) {
		importFlatFileSource(name, fileName, flatFileProfile, 1, 2);
	}

	/**
	 * Creates a new VDB
	 * @param name
	 * @return
	 */
	public VDB createVDB(String name) {
		select();
		new ContextMenu("New", "Teiid VDB").select();
		new LabeledText("VDB Name:").setText(name);
		new PushButton("Finish").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		return getVDB(name + ".vdb");
	}

	/**
	 * Returns VDB with a given name
	 * @param name
	 * @return
	 */
	public VDB getVDB(String name) {
		TreeItem vdbItem = getTreeItem().getItem(name);
		return new VDB(vdbItem, this, new String[] { name });
	}
}
