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
 * Checks if project with given name has project archives support
 * enabled
 * 
 * @author jjankovi
 *
 */
public class ExplorerInProjectExplorer implements WaitCondition {

	private String project;
	
	public ExplorerInProjectExplorer(String project) {
		this.project = project;
	}
	
	@Override
	public boolean test() {
		try {
			new ProjectArchivesExplorer(project);
			return true;
		} catch (Exception exc) {
			return false;
		}
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
