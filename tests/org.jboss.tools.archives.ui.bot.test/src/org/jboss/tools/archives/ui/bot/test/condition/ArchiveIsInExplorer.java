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
package org.jboss.tools.archives.ui.bot.test.condition;

import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.tools.archives.reddeer.archives.ui.ProjectArchivesExplorer;

/**
 * Checks if Archive with specified name can be located
 * via Project Archives explorer
 * 
 * @author jjankovi
 *
 */
public class ArchiveIsInExplorer implements WaitCondition {

	private String archiveName;
	private ProjectArchivesExplorer projectArchivesExplorer;
	
	public ArchiveIsInExplorer(String archiveName,
			ProjectArchivesExplorer projectArchivesExplorer) {
		this.archiveName = archiveName;
		this.projectArchivesExplorer = projectArchivesExplorer;
	}

	public boolean test() {
		try {
			projectArchivesExplorer.getArchive(archiveName);
			return true;
		} catch (Exception exc) {
			return false;
		}
	}

	public String description() {
		// TODO Auto-generated method stub
		return null;
	}

}
