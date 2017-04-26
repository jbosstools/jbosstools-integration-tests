/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.forge.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;


/**
 * Returns true if a project is selected in Project Explorer.
 * 
 * @author jkopriva@redhat.com
 * 
 */
public class ProjectIsSelected extends AbstractWaitCondition {

	private String projectName;
	private ProjectExplorer pe;

	/**
	 * Construct the condition.
	 * 
	 * @param projectName	Name of project, which should be selected in Project Explorer.
	 */
	public ProjectIsSelected(String projectName) {
		this.projectName = projectName;
		this.pe = new ProjectExplorer();
		pe.open();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.reddeer.common.condition.WaitCondition#test()
	 */
	@Override
	public boolean test() {
		if (!pe.getProject(this.projectName).isSelected()) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.reddeer.common.condition.AbstractWaitCondition#description()
	 */
	@Override
	public String description() {
		return "Project has not been selected in Project Explorer!";
	}

}
