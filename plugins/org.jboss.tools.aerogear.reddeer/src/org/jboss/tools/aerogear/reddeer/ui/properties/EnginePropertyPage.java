package org.jboss.tools.aerogear.reddeer.ui.properties;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.dialogs.ProjectPropertyPage;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.table.DefaultTable;

/**
 * Represents hybrid mobile project property page
 * @author vlado pakan
 *
 */
public class EnginePropertyPage extends ProjectPropertyPage{
	public EnginePropertyPage(Project project) {
		super(project, "Hybrid Mobile Engine");
	}
	/**
	 * Returns current checked version
	 * @return
	 */
	public String getVersion (){
		String result = null;
		List<TableItem> tiVersions = new DefaultTable().getItems();
		Iterator<TableItem> itVersion = tiVersions.iterator();
		while (result == null && itVersion.hasNext()){
			TableItem tiVersion = itVersion.next();
			if (tiVersion.isChecked()){
				result = getVersionFromEgineName(tiVersion.getText(0));
			}
		}
		return result;
		
	}
	/**
	 * Returns all available engine versions
	 * @return
	 */
	public List<String> getAvailableVersions () {
		LinkedList<String> result = new LinkedList<String>();
		List<TableItem> tiVersions = new DefaultTable().getItems();
		for (TableItem tiVersion : tiVersions){
			result.add(getVersionFromEgineName(tiVersion.getText(0)));
		}
		return result;
	}
	/**
	 * Checks version
	 * @param version
	 */
	public void checkVersion (String version){
		List<TableItem> tiVersions = new DefaultTable().getItems();
		Iterator<TableItem> itVersion = tiVersions.iterator();
		boolean notChecked = true;
		while (notChecked && itVersion.hasNext()){
			TableItem tiVersion = itVersion.next();
			if (getVersionFromEgineName(tiVersion.getText(0)).equals(version)){
				tiVersion.setChecked(true);
				notChecked = false;
			}
		}
		
		if (notChecked){
			throw new SWTLayerException("Unable to check mobile engine version: " + version);
		}

	}
	
	private String getVersionFromEgineName (String engineName){
		return engineName.substring(engineName.indexOf('[') + 1,
			engineName.length() - 1).trim();
	}
}
