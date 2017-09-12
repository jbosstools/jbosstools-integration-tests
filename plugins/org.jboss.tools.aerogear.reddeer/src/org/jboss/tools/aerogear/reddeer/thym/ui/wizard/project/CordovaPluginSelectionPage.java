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
package org.jboss.tools.aerogear.reddeer.thym.ui.wizard.project;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.wizard.WizardPage;
import org.eclipse.reddeer.swt.api.Tree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;

public class CordovaPluginSelectionPage extends WizardPage{
	
	
	public CordovaPluginSelectionPage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}

	public List<CordovaPlugin> getPlugins() {
		return getPluginsTree().getItems().stream().map(CordovaPlugin::new).collect(Collectors.toList());
	}
	
	public void selectPlugin(String pluginId) {
		boolean pluginFound = false;
		for(CordovaPlugin plugin: getPlugins()) {
			if(plugin.getPluginId().equals(pluginId)) {
				plugin.check();
				pluginFound = true;
				break;
			}
		}
		if(!pluginFound) {
			fail("plugin "+pluginId+" not found!");
		}
	}
	
	private Tree getPluginsTree() {
		return new DefaultTree();
	}

}
