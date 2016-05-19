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

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.reference.ReferencedComposite;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.tools.docker.reddeer.ui.DockerExplorer;

/**
 * 
 * @author jkopriva
 *
 */

public class RunADockerImagePageTwoWizard extends WizardPage{
	
	
	public RunADockerImagePageTwoWizard(){
		super();
	}

	public void finish() {
		String shell = new DefaultShell().getText();
		new FinishButton().click();

		new WaitWhile(new ShellWithTextIsActive(shell), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	public void addDataVolumeNoExternalMount(String containerPath){
		new PushButton("Add..").click();
		new LabeledText("Container path:").setText(containerPath);
		new RadioButton("No external mount").click();
		new OkButton().click();
	}
	
	public void addDataVolumeToHost(String containerPath, String path){
		addDataVolumeToHost(containerPath,path, false);
	}
	
	public void addDataVolumeToHost(String containerPath, String path, boolean readOnly){
		new PushButton("Add...").click();
		new LabeledText("Container path:").setText(containerPath);
		new RadioButton("Mount a host directory or host file").click();
		new LabeledText("Path:").setText(path);
		new CheckBox("Read-only access").toggle(readOnly);;
		new OkButton().click();
	}
	
	public void addDataVolumeToContainer(String containerPath, String containerName){
		new PushButton("Add...").click();
		new LabeledText("Container path:").setText(containerPath);
		new RadioButton("Mount a data volume container").click();
		new DefaultCombo("Container").setText(containerName);
		new OkButton().click();
	}
	
	public void addEnviromentVariable(String name, String value){
		new PushButton("Add..").click();
		new LabeledText("Name:").setText(name);
		new DefaultCombo("Value:").setText(value);
		new OkButton().click();
	}
	
	public void setResourceLimitation(String CPU, String memoryLimit){
		new CheckBox("Enable resource limitations").click();
		
		switch (CPU) {
        case "Low":
        	new RadioButton("Low").click();
            break;
        case "Medium":
        	new RadioButton("Medium").click();
        	break;
        case "High":
        	new RadioButton("High").click();
        	break;
        default:
            new RadioButton("Medium").click();
		}
            
		new LabeledText("Memory limit:").setText(memoryLimit);
	}
	
	public void setEntrypoint(String Entrypoint){
		new LabeledText("Entrypoint:").setText(Entrypoint);
	}
	
	public void setCommand(String command){
		new LabeledText("Command:").setText(command);
	}	
	
	public void setPublishAllExposedPorts(){
		new CheckBox("Publish all exposed ports to random ports on the host interfaces").click();
	}
	
	public void setKeepSTDINOpen(){
		new CheckBox("Keep STDIN open to Console even if not attached (-i)").click();
	}
	
	public void setAllocatePseudoTTY(){
		new CheckBox("Allocate pseudo-TTY from Console (-t)").click();
	}
	
	public void setAutomaticalyRemove(){
		new CheckBox("Automatically remove the container when it exits (--rm)").click();
	}

	
	


}
