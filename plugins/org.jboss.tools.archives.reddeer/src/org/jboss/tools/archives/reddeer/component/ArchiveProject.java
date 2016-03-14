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
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.condition.TreeItemHasMinChildren;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
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
	protected static final Logger log = Logger.getLogger(ArchiveProject.class);

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
		if(archiveProject.getItems().isEmpty()){
			log.debug("Archive project does not contain any archives");
			new ShellMenu("Project","Clean...").select();
			new DefaultShell("Clean");
			new OkButton().click();
			new WaitWhile(new ShellWithTextIsAvailable("Clean"));
			new WaitWhile(new JobIsRunning());
		}
		new WaitUntil(new TreeItemHasMinChildren(archiveProject, 1), TimePeriod.NORMAL, false);
		return new Archive(archiveProject.getItem(archiveName));
	}
	
}
