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

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.archives.reddeer.component.Archive;
import org.jboss.tools.archives.reddeer.component.ArchiveContextMenuAction;

/**
 * Simulates Project Archives explorer which is maintaned in
 * Project Explorer view
 * 
 * @author jjankovi
 *
 */
public class ProjectArchivesExplorer {

	private ProjectExplorer projectExplorer = new ProjectExplorer();
	private ArchiveContextMenuAction menuAction;
	private TreeItem explorer = null;
	
	private static final String PROJECT_ARCHIVES_NODE = "Project Archives";
	
	
	public ProjectArchivesExplorer(String project) {
		openExplorer(project);
		explorer.expand();
		menuAction = new ArchiveContextMenuAction();
	}
	
	private void openExplorer(String project) {
		projectExplorer.open();
		explorer = new DefaultTreeItem(project, PROJECT_ARCHIVES_NODE);
	}
	
	public NewJarDialog newJarArchive() {
		explorer.select();
		return menuAction.createNewJarArchive();
		
	}
	
	public void buildProjectFull() {
		explorer.select();
		menuAction.buildProjectFull();
	}
	
	public Archive getArchive(String archiveName) {
		return new Archive(explorer.getItem(archiveName));
	}
	
}
