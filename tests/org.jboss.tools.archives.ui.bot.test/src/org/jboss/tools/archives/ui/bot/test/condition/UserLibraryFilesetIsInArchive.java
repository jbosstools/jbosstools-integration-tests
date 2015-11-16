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

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.tools.archives.reddeer.component.Archive;

/**
 * Checks if User library fileset with specified name 
 * can be located under specified archive
 * 
 * @author jjankovi
 *
 */
public class UserLibraryFilesetIsInArchive extends AbstractWaitCondition {

	private Archive archive;
	private String userLibraryFileset;
	
	public UserLibraryFilesetIsInArchive(Archive archive, 
			String userLibraryFileset) {
		this.archive = archive;
		this.userLibraryFileset = userLibraryFileset;
	}

	public boolean test() {
		try {
			archive.getUserLibraryFileset(userLibraryFileset);
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
