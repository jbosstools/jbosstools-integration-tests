/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.docker.reddeer.ui;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.swt.api.TreeItem;


/**
 * 
 * @author jkopriva
 *
 */

public class ConnectionItem extends AbstractDockerExplorerItem{

	protected final Logger log = Logger.getLogger(ProjectItem.class);
	
	public ConnectionItem(TreeItem treeItem) {
		super(treeItem);
	}

	@Override
	public void select() {
		activateWrappingView();
		treeItem.select();	
	}

}