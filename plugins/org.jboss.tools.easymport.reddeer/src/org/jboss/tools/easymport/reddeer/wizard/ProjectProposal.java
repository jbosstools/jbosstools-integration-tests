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

public class ProjectProposal {
	
	private String folder;
	private List<String> importAsList;
	
	public ProjectProposal(String folder) {
		this.folder = folder;
		this.importAsList = new ArrayList<String>();
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public List<String> getImportAsList() {
		return importAsList;
	}
	
	public void addImportAs(String importType){
		importAsList.add(importType);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ProjectProposal){
			ProjectProposal proposal = (ProjectProposal) obj;
			if (proposal.getFolder().equals(folder)){
				return proposal.getImportAsList().equals(this.getImportAsList());
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Folder: "+folder+", ");
		sb.append(importAsList.stream().collect(Collectors.joining(",")));
		return sb.toString();
	}

}
