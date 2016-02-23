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
package org.jboss.tools.easymport.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



/**
 * This class represents imported project as it is displayed after import in {@link EasymportJobReportDialog}.
 * 
 * @author rhopp
 *
 */
public class ImportedProject {

	private String projectName;
	private List<String> importedAs;
	private String relativePath;
	
	public ImportedProject(String projectName, String relativePath) {
		this.projectName = projectName;
		this.relativePath = relativePath;
		importedAs = new ArrayList<String>();
	}

	public void addImportedAs(String type){
		importedAs.add(type);
	}
	
	public String getProjectName() {
		return projectName;
	}

	public List<String> getImportedAsList() {
		return importedAs;
	}

	public String getRelativePath() {
		return relativePath;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ImportedProject){
			ImportedProject project = (ImportedProject) obj;
			if (project.getProjectName().equals(projectName) && project.getRelativePath().equals(relativePath)){
				return project.getImportedAsList().equals(this.getImportedAsList());
			}
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ProjectName: "+projectName+", ");
		sb.append("relative path: \""+relativePath+"\", ");
		sb.append(importedAs.stream().collect(Collectors.joining(",")));
		return sb.toString();
	}
	
}
