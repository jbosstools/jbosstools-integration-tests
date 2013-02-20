package org.jboss.tools.teiid.reddeer;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.wizard.TeiidImportWizard;

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
	
	public void importModel(TeiidImportWizard importWizard) {
		select();
		importWizard.execute();
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
