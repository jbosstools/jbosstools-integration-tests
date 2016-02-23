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

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;

public class ImportProposalWizardPage extends WizardPage {

	public List<ProjectProposal> getAllProjectProposals() {

		DefaultTree tree = new DefaultTree();
		List<ProjectProposal> returnList = parseTree(tree);
		return returnList;
	}

	public void selectAll() {
		new PushButton("Select All").click();
	}

	public void deselectAll() {
		new PushButton("Deselect All").click();
	}

	public void useAdditionalAnalysis(boolean checked) {
		new CheckBox("Use additional analysis after import to detect nested project under selected projects "
				+ "(BEWARE: this may create new projects in your workspace without ability to review it first!)")
						.toggle(checked);
	}

	private List<ProjectProposal> parseTree(DefaultTree tree) {
		List<ProjectProposal> returnList = new ArrayList<>();
		for (TreeItem treeItem : tree.getAllItems()) {
			ProjectProposal projectProposal = new ProjectProposal(treeItem.getCell(0));
			returnList.add(fillProjectProposal(projectProposal, treeItem));
		}
		return returnList;
	}

	private ProjectProposal fillProjectProposal(ProjectProposal projectProposal, TreeItem treeItem) {
		String cell = treeItem.getCell(1);
		String[] split = cell.split(",");
		for (String type : split) {
			if (!type.equals("")) {
				projectProposal.addImportAs(type);
			}
		}
		return projectProposal;
	}

}
