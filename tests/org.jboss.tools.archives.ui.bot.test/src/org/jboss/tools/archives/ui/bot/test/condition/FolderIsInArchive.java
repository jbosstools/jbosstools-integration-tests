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
import org.jboss.tools.archives.reddeer.component.Archive;

/**
 * Checks if Folder with specified name can be located
 * under specified archive
 * 
 * @author jjankovi
 *
 */
public class FolderIsInArchive implements WaitCondition {

	private Archive archive;
	private String folder;
	
	public FolderIsInArchive(Archive archive, String folder) {
		this.archive = archive;
		this.folder = folder;
	}

	public boolean test() {
		try {
			archive.getArchive(folder);
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
