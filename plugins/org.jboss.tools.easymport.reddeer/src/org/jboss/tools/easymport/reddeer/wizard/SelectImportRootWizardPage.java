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

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class SelectImportRootWizardPage extends WizardPage {
	
	private static final Logger log = Logger.getLogger(WizardProjectsImportPage.class);
	
	public void selectDirectory(String path){
		log.info("Selecting directory \""+path+"\" in SelectImportRootWizardPage.");
		LabeledText selectDirText = new LabeledText("Select root directory:");
		selectDirText.setText(path);
	}
	
	
	public List<String> getDetectors(){
		List<String> resultList = new ArrayList<String>();
		new DefaultLink("Show available detectors").click();
		new DefaultShell("Available detectors");
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
	
	public void selectRawProject(){
		new RadioButton("Import raw project (I'll configure it later)").click();
	}
	
	public void selectRootProjectOnly(){
		new RadioButton("Import and configure root project only").click();
	}
	
	public void selectNestedProjects(){
		new RadioButton("Detect and configure nested projects under the given location").click();
	}

}
