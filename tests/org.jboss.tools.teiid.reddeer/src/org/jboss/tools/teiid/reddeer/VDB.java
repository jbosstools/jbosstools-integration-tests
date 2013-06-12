package org.jboss.tools.teiid.reddeer;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.view.GuidesView;

/**
 * This class represents a virtual database.
 * 
 * @author apodhrad
 * 
 */
public class VDB {

	private ProjectItem projectItem;
	
	public VDB(ProjectItem projectItem) {
		this.projectItem = projectItem;
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
		projectItem.select();
		new ContextMenu("Modeling", "Deploy").select();
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	/**
	 * Executes this VDB
	 */
	public void executeVDB() {
		projectItem.select();
		new ContextMenu("Modeling", "Execute VDB").select();
	}
	
	/**
	 * Executes this VDB via Modelling actions guides
	 * @param viaGuides
	 */
	public void executeVDB(boolean viaGuides){
		if (viaGuides){
			projectItem.select();
			new GuidesView().chooseAction("Model JDBC Source", "Execute VDB");
			//VDB is selected from previous step
			Bot.get().button("OK").click();
		} else {
			executeVDB();
		}
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
