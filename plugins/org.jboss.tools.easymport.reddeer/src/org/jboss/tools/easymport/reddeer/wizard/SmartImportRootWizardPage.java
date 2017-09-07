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

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.eclipse.reddeer.jface.wizard.WizardPage;
import org.eclipse.reddeer.swt.api.Combo;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.label.DefaultLabel;
import org.eclipse.reddeer.swt.impl.link.DefaultLink;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;

public class SmartImportRootWizardPage extends WizardPage {
	
	private static final Logger log = Logger.getLogger(WizardProjectsImportPage.class);
	
	public SmartImportRootWizardPage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}
	
	public void selectDirectory(String path){
		log.info("Selecting directory \""+path+"\" in SelectImportRootWizardPage.");
		Combo selectDirText = new LabeledCombo("Import source:");
		selectDirText.setText(path);
	}
	
	
	public List<String> getDetectors(){
		List<String> resultList = new ArrayList<String>();
		new DefaultLink("installed project configurators").click();
		new DefaultShell("Installed project configuratos");
		String labelText = new DefaultLabel(1).getText();
		String[] split = labelText.split("\n");
		for (String row: split) {
			if (row.startsWith("*")){
				resultList.add(row.substring(2));
			}
		}
		new OkButton().click();
		return resultList;
	}
	
	public void setSearchForNestedProjects(boolean value){
		new CheckBox("Search for nested projects").toggle(value);
	}
	
	public void setDetectAndConfigureNatures(boolean value){
		new CheckBox("Detect and configure project natures").toggle(value);
	}
	
	public void setHideOpenProjects(boolean value){
		new CheckBox("Hide already open projects").toggle(value);
	}
	
	public List<ProjectProposal> getAllProjectProposals() {

		DefaultTree tree = new DefaultTree();
		List<ProjectProposal> returnList = parseTree(tree);
		return returnList;
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
