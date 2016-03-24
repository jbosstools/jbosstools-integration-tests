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
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.tools.docker.reddeer.ui.DockerExplorer;

/**
 * 
 * @author jkopriva
 *
 */

public class DockerConnectionWizard extends WizardPage{
	
	
	public DockerConnectionWizard(){
		super();
	}
	
	public void open() {
		DockerExplorer de = new DockerExplorer();
		de.open();
		new DefaultToolItem("Add Connection").click();
		new WaitUntil(new ShellWithTextIsActive("New Docker Connection"));
	}

	public void finish() {
		String shell = new DefaultShell().getText();
		new WaitUntil(new ShellWithTextIsAvailable("New Docker Connection"));
		Button finishButton = new PushButton("Finish");
		finishButton.click();

		new WaitWhile(new ShellWithTextIsActive(shell), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	public void setConnectionName(String name){
		new LabeledText("Connection name:").setText(name);
	}
	
	public void setUnixSocket(String unixSocket){
		setConnectionName(unixSocket);
		new CheckBox("Use custom connection settings:").click();
		new RadioButton("Unix socket").click();
		new LabeledText("Location:").setText(unixSocket);
		
	}
	
	public void setTcpConnection(String uri){		
		setTcpConnection(uri, null, false);
	}
	
	public void setTcpConnection(String uri, String authentificationPath, boolean pingConnection){
		setTcpUri(uri);
		if(authentificationPath!=null){
			new CheckBox("Enable authentication").click();
			new LabeledText("Path:").setText(authentificationPath);
		}
		if(pingConnection){
			pingConnection();
		}
	}
	
	public void setTcpUri(String uri){
		setConnectionName(uri);
		new CheckBox("Use custom connection settings:").click();
		new LabeledText("Location:").setText("");
		new RadioButton("TCP Connection").click();
		new LabeledText("URI:").setText(uri);
	}
		
	public void pingConnection(){
		Button testConnectionButton = new PushButton("Test Connection");
		testConnectionButton.click();
		new WaitUntil(new ShellWithTextIsAvailable("Success"));
		Button okButton = new PushButton("OK");
		okButton.click();
	}
	
	


}
