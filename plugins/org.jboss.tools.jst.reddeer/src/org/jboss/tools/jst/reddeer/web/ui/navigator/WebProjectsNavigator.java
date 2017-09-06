/*******************************************************************************
 * Copyright (c) 2007-2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.reddeer.web.ui.navigator;

import org.eclipse.reddeer.eclipse.ui.navigator.resources.AbstractExplorer;
/**
 * Represents Web Projects view in Eclipse
 * 
 * @author Vlado Pakan
 *
 */
public class WebProjectsNavigator extends AbstractExplorer {
    
	/**
	 * Constructs the view with Web Projects.
	 */
	public WebProjectsNavigator() {
		super("Web Projects");
	}

}