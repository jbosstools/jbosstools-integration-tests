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

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.tools.archives.reddeer.archives.ui.ProjectArchivesView;

/**
 * Checks if Archive with specified name can be located
 * via Project Archives view
 * 
 * @author jjankovi
 *
 */
public class ArchiveIsInView implements WaitCondition {

	private String archiveName;
	private ProjectArchivesView projectArchivesView;
	
	public ArchiveIsInView(String archiveName,
			ProjectArchivesView projectArchivesView) {
		this.archiveName = archiveName;
		this.projectArchivesView = projectArchivesView;
	}

	public boolean test() {
		try {
			projectArchivesView.getProject().getArchive(archiveName);
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
