/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.archives.reddeer.archives.ui;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.archives.reddeer.component.ArchiveProject;

/**
 * Project Archives View implementation
 * 
 * @author jjankovi
 *
 */
public class ProjectArchivesView extends WorkbenchView {

	public ProjectArchivesView() {
		super("JBoss Tools", "Project Archives");
	}
	
	public void buildArchiveNode() {
		new DefaultToolItem("Build Archive Node").click();
		new WaitWhile(new JobIsRunning());
	}
	
	public ArchiveProject getProject(String projectName) {
		return new ArchiveProject(new DefaultTreeItem(projectName));
	}
	
}
