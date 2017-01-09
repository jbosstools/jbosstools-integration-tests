/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.central.test.ui.reddeer.projects;

import org.jboss.tools.central.reddeer.projects.ArchetypeProject;

/**
 * 
 * @author rhopp
 *
 */

public class JavaEEWebProject extends ArchetypeProject {

	public JavaEEWebProject(boolean blank) {
		super("Java EE Web Project", "jboss-javaee-webapp", blank);
	}
	
	@Override
	public String getReadmeString(){
		return ".cheatsheet.xml";
	}

}
