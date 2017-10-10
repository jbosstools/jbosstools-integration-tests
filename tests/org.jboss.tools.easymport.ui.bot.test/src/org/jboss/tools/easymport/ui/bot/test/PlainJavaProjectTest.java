/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.easymport.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.easymport.reddeer.wizard.ImportedProject;
import org.jboss.tools.easymport.reddeer.wizard.ProjectProposal;

public class PlainJavaProjectTest extends ProjectTestTemplate {

	@Override
	public File getProjectPath() {
		return new File("target/resources/PlainJavaProject");
	}

	@Override
	List<ProjectProposal> getExpectedProposals() {
		ArrayList<ProjectProposal> returnList = new ArrayList<ProjectProposal>();
		ProjectProposal projectProposal = new ProjectProposal("PlainJavaProject");
		returnList.add(projectProposal);
		return returnList;
	}

	@Override
	List<ImportedProject> getExpectedImportedProjects() {
		ArrayList<ImportedProject> returnList = new ArrayList<ImportedProject>();
		ImportedProject project = new ImportedProject("PlainJavaProject", "");
		project.addImportedAs("Java");
		returnList.add(project);
		return returnList;
	}

	@Override
	void checkImportedProject() {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("PlainJavaProject");
		String[] natureIds = {};
		try {
			natureIds = project.getDescription().getNatureIds();
		} catch (CoreException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals("Project should have exactly 1 nature", 1, natureIds.length);
		assertEquals("Project should have java nature", "org.eclipse.jdt.core.javanature", natureIds[0]);
	}
}
