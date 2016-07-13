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
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author jkopriva
 *
 */

public class RunADockerImagePageOneWizard extends WizardPage {

	public RunADockerImagePageOneWizard() {
		super();
		new WaitUntil(new ShellWithTextIsAvailable("Run a Docker Image"), TimePeriod.LONG);
	}

	public void finish() {
		new FinishButton().click();
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}

	public void next() {
		new NextButton().click();
	}

	public void setName(String name) {
		new LabeledText("Name:").setText(name);
	}

	public void setEntrypoint(String Entrypoint) {
		new LabeledText("Entrypoint:").setText(Entrypoint);
	}

	public void setCommand(String command) {
		new LabeledText("Command:").setText(command);
	}

	public void setPublishAllExposedPorts(boolean checked) {
		new CheckBox("Publish all exposed ports to random ports on the host interfaces").toggle(checked);
	}

	public void setPublishAllExposedPorts() {
		setPublishAllExposedPorts(true);
	}

	public void setKeepSTDINOpen(boolean checked) {
		new CheckBox("Keep STDIN open to Console even if not attached (-i)").toggle(checked);
	}

	public void setKeepSTDINOpen() {
		setKeepSTDINOpen(true);
	}

	public void setAllocatePseudoTTY(boolean checked) {
		new CheckBox("Allocate pseudo-TTY from Console (-t)").toggle(checked);
	}

	public void setAllocatePseudoTTY() {
		setAllocatePseudoTTY(true);
	}

	public void setAutomaticalyRemove(boolean checked) {
		new CheckBox("Automatically remove the container when it exits (--rm)").toggle(checked);
	}

	public void setAutomaticalyRemove() {
		setAutomaticalyRemove(true);
	}

	public void setGiveExtendedPrivileges(boolean checked) {
		new CheckBox("Give extended privileges to this container (--privileged)").toggle(checked);
	}

	public void setGiveExtendedPrivileges() {
		setGiveExtendedPrivileges(true);
	}

	public void addExposedPort(String containerPort, String hostAddress, String hostPort) {
		new PushButton("Add").click();
		new LabeledText("Container port:").setText(containerPort);
		new LabeledText("Host address:").setText(hostAddress);
		new LabeledText("Host port:").setText(hostPort);
		new OkButton().click();
	}

	public void addLinkToContainer(String containerName, String alias) {
		new PushButton("Add...").click();
		new LabeledCombo("Container:").setText(containerName);
		new LabeledText("Alias:").setText(alias);
		new OkButton().click();
	}

}
