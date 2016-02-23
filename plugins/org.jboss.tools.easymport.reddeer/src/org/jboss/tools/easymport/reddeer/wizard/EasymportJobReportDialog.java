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

import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;

public class EasymportJobReportDialog extends DefaultShell {
	
	public EasymportJobReportDialog() {
		super("Nested Projects");
	}
	
	public List<ImportedProject> getImportedProjects(){
		ArrayList<ImportedProject> resultList = new ArrayList<ImportedProject>();
		DefaultTable table = new DefaultTable();
		for (TableItem tableItem: table.getItems()) {
			ImportedProject importedProject = new ImportedProject(tableItem.getText(0), tableItem.getText(2));
			resultList.add(fillImportedProject(importedProject, tableItem.getText(1)));
		}
		return resultList;
	}

	private ImportedProject fillImportedProject(ImportedProject importedProject, String text) {
		String[] types = text.split(",");
		for (String type : types) {
			importedProject.addImportedAs(type);
		}
		return importedProject;
	}

}
