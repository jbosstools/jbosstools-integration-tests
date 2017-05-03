/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.docker.reddeer.core.ui.wizards;

import java.util.List;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.core.condition.JobIsRunning;

/**
 * 
 * @author jkopriva
 *
 */

public class ImageSearchPage extends WizardPage{

	public ImageSearchPage() {
		super();
		new WaitUntil(new ShellIsAvailable("Search and pull a Docker image"));
	}

	public void finish() {
		new FinishButton().click();
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	public void searchImage(String imageName){
		new LabeledText("Image:").setText(imageName);
		new PushButton("Search").click();
		new WaitWhile(new JobIsRunning());
	}
	
	public void searchImage(){
		new PushButton("Search").click();
		new WaitWhile(new JobIsRunning());
	}
	
	public List<TableItem> getSearchResults() {
		return new DefaultTable().getItems();
	}
	
	public boolean searchResultsContains(String imageName){
		for(TableItem item:getSearchResults()){
			if(imageName.contains(item.getText())){
				return true;
			}
		}
		return false;
	}
	
	public void next(){
		new NextButton().click();
		new WaitWhile(new JobIsRunning());
	}
	
	public void selectImage(String imageName){
		for(TableItem item:getSearchResults()){
			if(imageName.contains(item.getText())){
				item.select();
			}
		}
	}

	
	
}
