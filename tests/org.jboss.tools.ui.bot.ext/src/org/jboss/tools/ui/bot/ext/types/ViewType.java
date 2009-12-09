 /*******************************************************************************
  * Copyright (c) 2007-2009 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.ui.bot.ext.types;

import static org.junit.Assert.fail;

/**
 * Provides functionality to locate appropriate view type
 * @author jpeterka
 *
 */
public enum ViewType {
	PACKAGE_EXPLORER, PROJECT_EXPLORER, WELCOME, DATA_SOURCE_EXPLORER;
	
	
	public String getGroupLabel() {
		String viewLabel = "";	
		switch (this) {
			case PROJECT_EXPLORER: viewLabel =  IDELabel.ViewGroup.GENERAL; break;
			case PACKAGE_EXPLORER: viewLabel = IDELabel.ViewGroup.JAVA; break;
			case DATA_SOURCE_EXPLORER: viewLabel = IDELabel.ViewGroup.DATA_MANAGEMENT; break;
			default: fail("Unknown View Type");
		}
		return viewLabel;	
	}
	
	public String getViewLabel() {
		String viewLabel = "";	
		switch (this) {
			case PROJECT_EXPLORER: viewLabel =  IDELabel.View.PROJECT_EXPLORER; break;
			case PACKAGE_EXPLORER: viewLabel = IDELabel.View.PACKAGE_EXPLORER; break;
			case DATA_SOURCE_EXPLORER: viewLabel = IDELabel.View.DATA_SOURCE_EXPLORER; break;
			default: fail("Unknown View Type");
		}
		return viewLabel;
	}
}
