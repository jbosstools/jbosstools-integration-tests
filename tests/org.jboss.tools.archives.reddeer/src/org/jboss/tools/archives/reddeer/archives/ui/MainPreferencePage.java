package org.jboss.tools.archives.reddeer.archives.ui;

import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.button.CheckBox;

/**
* Represents Preference page: 
* 		Project Archives
* 
* @author jjankovi
*
*/
public class MainPreferencePage extends PreferencePage {

	public MainPreferencePage() {
		super("Project Archives");
	}
	
	public void enableIncrementalBuilder(boolean enable) {
		new CheckBox("Enable incremental builder").toggle(enable);
	}
	
	public boolean isIncrementalBuilderEnabled() {
		return new CheckBox("Enable incremental builder").isChecked();
	}
	
	public void showBuildErrorDialog(boolean show) {
		new CheckBox("Show build error dialog").toggle(show);
	}
	
	public boolean isBuildErrorDialogShown() {
		return new CheckBox("Show build error dialog").isChecked();
	}
	
	public void showOutputPathNextToPackages(boolean show) {
		new CheckBox("Show output path next to packages.").toggle(show);
	}
	
	public boolean isOutputPathNextToPackagesShown() {
		return new CheckBox("Show output path next to packages.").isChecked();
	}
	
	public void showRootDirectoryOfFilesets(boolean show) {
		new CheckBox("Show the root directory of filesets.").toggle(show);
	}
	
	public boolean isRootDirectoryOfFilesetsShown() {
		return new CheckBox("Show the root directory of filesets.").isChecked();
	}
	
	public void showProjectAtTheRoot(boolean show) {
		new CheckBox("Show project at the root").toggle(show);
	}
	
	public boolean isProjectAtTheRootShown() {
		return new CheckBox("Show project at the root").isChecked();
	}
	
	public void showAllProjectsThatContainPackages(boolean show) {
		new CheckBox("Show all projects that contain packages").toggle(show);
	}
	
	public boolean areAllProjectsThatContainPackagesShown() {
		return new CheckBox("Show all projects that contain packages").isChecked();
	}
	
	public void showNodeInAllProjects(boolean show) {
		new CheckBox("Show node in all projects").toggle(show);
	}
	
	public boolean isNodeInAllProjectShown() {
		return new CheckBox("Show node in all projects").isChecked();
	}
	
	public void enableDefaultExcludes(boolean enable) {
		new CheckBox("Enable Default Excludes").toggle(enable);
	}
	
	public boolean isDefaultExcludesEnabled() {
		return new CheckBox("Enable Default Excludes").isChecked();
	}
}
