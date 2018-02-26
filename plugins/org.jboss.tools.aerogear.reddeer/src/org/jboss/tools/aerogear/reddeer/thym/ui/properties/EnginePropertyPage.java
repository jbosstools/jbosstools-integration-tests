/******************************************************************************* 
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.aerogear.reddeer.thym.ui.properties;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.exception.SWTLayerException;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;

/**
 * Represents hybrid mobile project property page
 * @author Vlado Pakan
 * @author Pavol Srna
 *
 */
public class EnginePropertyPage extends PropertyPage{
	public EnginePropertyPage(ReferencedComposite referencedComposite) {
		super(referencedComposite, "Hybrid Mobile Engine");
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
	 * @return list of versions
	 */
	public List<String> getAvailableVersions (Platform platform) {
		return getAvailableVersions(platform, false);
	}
	
	/**
	 * Returns all available engine versions
	 * @param platform
	 * @param skipNightly - omit nightly versions if true
	 * @return list of versions
	 */
	public List<String> getAvailableVersions (Platform platform, boolean skipNightly){
		LinkedList<String> result = new LinkedList<String>();
		List<TreeItem> tiVersions = new DefaultTree(1).getAllItems();
		for (TreeItem tiVersion : tiVersions){
			if(tiVersion.getText().contains(platform.toString())){
				if(skipNightly){
					if(!tiVersion.getText().contains("nightly")){
						result.add(getVersionFromEgineName(tiVersion.getText()));
					}
				}else{
					result.add(getVersionFromEgineName(tiVersion.getText()));
				}
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
		android {public String toString(){return "android";}},
		ios {public String toString(){return "ios";}}
	}
}
