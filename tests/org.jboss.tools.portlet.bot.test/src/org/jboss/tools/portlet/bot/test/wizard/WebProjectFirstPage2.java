/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.portlet.bot.test.wizard;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectFirstPage;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Just a temporary class until RedDeer version >= 2.0.0 is used (waiting for
 * https://github.com/jboss-reddeer/reddeer/pull/1567)
 * 
 * @author rhopp
 *
 */

public class WebProjectFirstPage2 extends WebProjectFirstPage {

	/**
	 * Activates project facet.
	 * 
	 * @param facetPath
	 *            path to Facet in tree of facets.
	 * @param version
	 *            facet version, can be null - than version is left default
	 */
	public void activateFacet(final String version, final String... facetPath) {
		new PushButton("Modify...").click();
		new DefaultShell("Project Facets");
		DefaultTreeItem facetTreeItem = new DefaultTreeItem(facetPath);
		facetTreeItem.select();
		facetTreeItem.setChecked(true);
		if (version != null) {
			new ContextMenu("Change Version...").select();
			new DefaultShell("Change Version");
			new LabeledCombo("Version:").setSelection(version);
			new PushButton("OK").click();
			new DefaultShell("Project Facets");
		}
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable("Project Facets"));
	}

}
