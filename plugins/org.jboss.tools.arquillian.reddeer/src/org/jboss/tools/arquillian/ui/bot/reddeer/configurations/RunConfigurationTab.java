/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation    
 ******************************************************************************/

package org.jboss.tools.arquillian.ui.bot.reddeer.configurations;

import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;

/**
 * Abstract tab in run configuration
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class RunConfigurationTab {

	private String name;
	
	public RunConfigurationTab(String name) {
		this.name = name;
	}
	
	/**
	 * Activates the tab
	 */
	public void activate(){
		new DefaultCTabItem(name).activate();
	}
}
