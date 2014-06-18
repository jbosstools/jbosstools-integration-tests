/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.seam3.bot.test.uiutils;

import org.jboss.tools.cdi.reddeer.cdi.text.ext.hyperlink.AssignableBeansDialog;

/**
 * 
 * @author jjankovi
 *
 */
public class AssignableBeansDialogExt extends AssignableBeansDialog {

	private static final String ELIMINATED_DEFAULT_BEAN = "Eliminated @DefaultBean";
	
	public AssignableBeansDialogExt() {
		super();	
		showDefaultBeans();
	}
	
	public AssignableBeansDialog hideDefaultBeans() {
		getTreeItem(ELIMINATED_DEFAULT_BEAN).setChecked(false);
		return this;
	}
	
	public AssignableBeansDialog showDefaultBeans() {
		getTreeItem(ELIMINATED_DEFAULT_BEAN).setChecked(true);
		return this;
	}
	
}
