package org.jboss.tools.aerogear.reddeer.ui.properties;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.ui.dialogs.ProjectPropertyPage;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;

/**
 * Represents hybrid mobile project property page
 * @author Vlado Pakan
 * @author Pavol Srna
 *
 */
public class EnginePropertyPage extends ProjectPropertyPage{
	public EnginePropertyPage(Project project) {
		super(project, "Hybrid Mobile Engine");
	}
	/**
	 * Returns current checked version on platform defined via parameter
	 * @param platform
	 * @return
	 */
	public String getVersion (Platform platform){
		String result = null;
		List<TreeItem> tiVersions = new DefaultTree(1).getAllItems();
		Iterator<TreeItem> itVersion = tiVersions.iterator();
		while (result == null && itVersion.hasNext()){
			TreeItem tiVersion = itVersion.next();
			if (tiVersion.isChecked() && tiVersion.getText().contains(platform.toString())){
				result = getVersionFromEgineName(tiVersion.getText());
			}
		}
		return result;
		
	}
	/**
	 * Returns all available engine versions
	 * @return
	 */
	public List<String> getAvailableVersions (Platform platform) {
		LinkedList<String> result = new LinkedList<String>();
		List<TreeItem> tiVersions = new DefaultTree(1).getAllItems();
		for (TreeItem tiVersion : tiVersions){
			if(tiVersion.getText().contains(platform.toString())){
				result.add(getVersionFromEgineName(tiVersion.getText()));
			}
		}
		return result;
	}
	/**
	 * Checks version
	 * @param version
	 */
	public void checkVersion (String version, Platform platform){
		List<TreeItem> tiVersions = new DefaultTree(1).getAllItems();
		Iterator<TreeItem> itVersion = tiVersions.iterator();
		boolean notChecked = true;
		while (notChecked && itVersion.hasNext()){
			TreeItem tiVersion = itVersion.next();
			if (tiVersion.getText().equals(platform.toString() + "@" + version)){
				tiVersion.setChecked(true);
				notChecked = false;
			}
		}
		
		if (notChecked){
			throw new SWTLayerException("Unable to check mobile engine version: " + version);
		}

	}
	
	private String getVersionFromEgineName (String engineName){
		return engineName.substring(engineName.indexOf('@') + 1,
			engineName.length()).trim();
	}
	
	public enum Platform {
		android {public String toString(){return "cordova-android";}},
		ios {public String toString(){return "cordova-ios";}}
	}
}
