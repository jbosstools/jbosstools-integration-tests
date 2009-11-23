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
	PROJECT_EXPLORER, WELCOME;
	
	
	public String getGroupLabel() {
		String viewLabel = "";	
		switch (this) {
			case PROJECT_EXPLORER: viewLabel =  IDELabel.ViewGroup.GENERAL; break;
			default: fail("Unknown View Type");
		}
		return viewLabel;	
	}
	
	public String getViewLabel() {
		String viewLabel = "";	
		switch (this) {
			case PROJECT_EXPLORER: viewLabel =  IDELabel.View.PROJECT_EXPLORER; break;
			default: fail("Unknown View Type");
		}
		return viewLabel;
	}
}
