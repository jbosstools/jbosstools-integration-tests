package org.jboss.tools.teiid.reddeer;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;

/**
 * This class represents a virtual database.
 * 
 * @author apodhrad
 * 
 */
public class VDB extends ProjectItem {

	public VDB(TreeItem treeItem, Project project, String[] path) {
		super(treeItem, project, path);
	}

	/**
	 * Adds a module to this VDB
	 * 
	 * @param module
	 */
//	public void addModule(String module) {
//		VDBEditor vdbEditor = getVDBEditor();
//		vdbEditor.show();
//		vdbEditor.addModel(getProject().getName(), module);
//		vdbEditor.save();
//		vdbEditor.close();
//	}

	/**
	 * Deployes this VDB
	 */
	public void deployVDB() {
		select();
		new ContextMenu("Modeling", "Deploy").select();
	}

	/**
	 * Executes this VDB
	 */
	public void executeVDB() {
		select();
		new ContextMenu("Modeling", "Execute VDB").select();
	}

	/**
	 * Return VDB editor
	 * @return
	 */
//	public VDBEditor getVDBEditor() {
//		open();
//		return VDBEditor.getInstance(getText());
//	}
}
