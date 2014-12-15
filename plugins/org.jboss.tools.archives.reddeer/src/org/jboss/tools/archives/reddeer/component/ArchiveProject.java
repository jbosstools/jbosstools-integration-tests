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
package org.jboss.tools.archives.reddeer.component;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.TreeItemHasMinChildren;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.archives.reddeer.archives.ui.NewJarDialog;

/**
 * Archive Project accessible via Project Archives view
 * 
 * @author jjankovi
 *
 */
public class ArchiveProject {

	private TreeItem archiveProject;
	private ArchiveContextMenuAction menuAction;

	public ArchiveProject(TreeItem archiveProject) {
		this.archiveProject = archiveProject;
		menuAction = new ArchiveContextMenuAction();
	}
	
	public String getName() {
		return archiveProject.getText();
	}
	
	public NewJarDialog newJarArchive() {
		archiveProject.select();
		return menuAction.createNewJarArchive();
		
	}
	
	public void buildProjectFull() {
		archiveProject.select();
		menuAction.buildProjectFull();
		new WaitWhile(new JobIsRunning());
	}
	
	public Archive getArchive(String archiveName) {
		new WaitUntil(new TreeItemHasMinChildren(archiveProject, 1), TimePeriod.NORMAL, false);
		return new Archive(archiveProject.getItem(archiveName));
	}
	
}
