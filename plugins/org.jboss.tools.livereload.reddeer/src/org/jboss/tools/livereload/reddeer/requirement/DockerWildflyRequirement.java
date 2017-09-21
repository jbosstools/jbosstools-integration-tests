/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.livereload.reddeer.requirement;

import static org.junit.Assert.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersView2;
import org.eclipse.reddeer.eclipse.wst.server.ui.wizard.NewServerWizard;
import org.eclipse.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardPage;
import org.eclipse.reddeer.junit.requirement.configuration.RequirementConfiguration;
import org.eclipse.reddeer.requirements.server.AbstractServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.page.NewServerAdapterPage;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.page.NewServerRSIWizardPage;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.page.NewServerAdapterPage.Profile;
import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.common.platform.RunningPlatform;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.eclipse.rse.ui.dialogs.SystemPasswordPromptDialog;
import org.eclipse.reddeer.eclipse.rse.ui.view.System;
import org.eclipse.reddeer.eclipse.rse.ui.view.SystemViewPart;
import org.eclipse.reddeer.eclipse.rse.ui.wizards.newconnection.RSEDefaultNewConnectionWizardMainPage;
import org.eclipse.reddeer.eclipse.rse.ui.wizards.newconnection.RSEMainNewConnectionWizard;
import org.eclipse.reddeer.eclipse.rse.ui.wizards.newconnection.RSENewConnectionWizardSelectionPage;
import org.eclipse.reddeer.eclipse.rse.ui.wizards.newconnection.RSENewConnectionWizardSelectionPage.SystemType;
import org.eclipse.reddeer.junit.requirement.Requirement;
import org.eclipse.reddeer.swt.api.Shell;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.button.YesButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.workbench.api.Editor;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.livereload.reddeer.requirement.DockerWildflyRequirement.DockerWildfly;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;
import com.spotify.docker.client.messages.ContainerConfig.Builder;

public class DockerWildflyRequirement extends AbstractServerRequirement implements Requirement<DockerWildfly> {

	private String ipAddress;
	private DockerClient docker;
	private String widlflyContId;
	private static String macSSHPort = "2022";

	DockerWildfly config;

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface DockerWildfly {
		String imageName();

		String name();

		String homeFolder();

		String userName();

		String pass();

		boolean cleanup() default true;
	}

	@Override
	public void fulfill() {
		try {
			docker = DefaultDockerClient.fromEnv().build();
			docker.pull(config.imageName());
			Builder builder = ContainerConfig.builder();
			if (RunningPlatform.isOSX()) {
				// http://stackoverflow.com/questions/37965790/how-do-i-ssh-to-a-docker-in-mac-container
				// map container port 22 [ssh] to host 2022
				final Map<String, List<PortBinding>> portBindings = new HashMap<>();

				List<PortBinding> hostPorts1 = new ArrayList<>();
				hostPorts1.add(PortBinding.of("0.0.0.0", macSSHPort));
				List<PortBinding> hostPorts2 = new ArrayList<>();
				hostPorts2.add(PortBinding.of("0.0.0.0", "8081"));
				portBindings.put("22/tcp", hostPorts1);
				portBindings.put("8081/tcp", hostPorts2);

				HostConfig hostConfig = HostConfig.builder().portBindings(portBindings).build();
				builder.hostConfig(hostConfig);
			}

			ContainerConfig wfConfig = builder.image(config.imageName()).build();
			final ContainerCreation creation = docker.createContainer(wfConfig);
			widlflyContId = creation.id();

			docker.startContainer(widlflyContId);

			IPIsAssigned ipIsAssigned = new IPIsAssigned();
			new WaitUntil(ipIsAssigned);
			if(RunningPlatform.isOSX()){
				ipAddress = "localhost";
			} else { 
				ipAddress = ipIsAssigned.getIP();
			}
		} catch (Exception e) {
			try {
				removeDocker();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			throw new AssertionError("failed docker comm. Make sure docker is installed on your system.", e);
		}
		try{
			setupRemoteAdapter();
			setupPortOffset();
			ServersView2 sw = new ServersView2();
			sw.open();
			sw.getServer(config.name()).start();
		} catch (Exception e) {
			try {
				removeDocker();
			} catch (Exception ex) {
				ex.printStackTrace();
			} throw e;
		}

	}

	private void setupPortOffset() {
		ServersView2 sw = new ServersView2();
		sw.open();
		for (TreeItem i : new DefaultTree().getItems()) {
			if (i.getText().startsWith(config.name())) {
				i.doubleClick();
				break;
			}
		}
		Editor serverEditor = new DefaultEditor(config.name());
		new CheckBox("Detect from Local Runtime").toggle(false);
		new LabeledText("Port Offset").setText("1");
		serverEditor.close(true);

	}

	private void setupRemoteAdapter() {
		NewServerWizard serverW = new NewServerWizard();
		// setup remote system first
		setupRemoteSystem();

		// -- Open 'New Server' wizard
		serverW.open();
		// -- Select the server type and fill in server name, then continue
		// on next page
		NewServerWizardPage sp = new NewServerWizardPage(serverW);
		sp.selectType("JBoss Community", "WildFly 10.x");
		sp.setName(config.name());
		serverW.next();

		// -- Select server profile (Remote)
		NewServerAdapterPage ap = new NewServerAdapterPage(serverW);
		ap.setProfile(Profile.REMOTE);

		// TODO Fix this in NewServerAdapterPage
		// ap.setAssignRuntime(false);
		CheckBox extManaged = new CheckBox("Server lifecycle is externally managed.");
		extManaged.toggle(true);

		CheckBox check = new CheckBox("Assign a runtime to this server");
		check.toggle(false);
		serverW.next();

		NewServerRSIWizardPage rsp = new NewServerRSIWizardPage(serverW);
		rsp.setRemoteServerHome(config.homeFolder());
		rsp.selectHost(ipAddress); // host was configured in
									// setupRemoteSystem
		serverW.finish();
	}

	protected void setupRemoteSystem() {
		SystemViewPart sview = new SystemViewPart();
		sview.open();
		RSEMainNewConnectionWizard connW = sview.newConnection();
		RSENewConnectionWizardSelectionPage sp = new RSENewConnectionWizardSelectionPage(connW);
		sp.selectSystemType(SystemType.SSH_ONLY);
		connW.next();
		RSEDefaultNewConnectionWizardMainPage mp = new RSEDefaultNewConnectionWizardMainPage(connW);
		mp.setHostName(ipAddress);
		connW.finish();

		// TODO FIX in as.reddeer
		System system = sview.getSystem(ipAddress);

		// system.connect("root", "root");

		if (RunningPlatform.isOSX()) {
			new DefaultTreeItem(ipAddress, "Ssh Shells").select();
			new ContextMenuItem("Properties").select();
			Shell propShell = new DefaultShell("Properties for Ssh Shells");
			new DefaultTreeItem("Subsystem").select();
			new LabeledText("Port (1-65535):").setText(macSSHPort);
			new OkButton().click();
			new WaitWhile(new ShellIsAvailable(propShell));
		}

		sview.open();
		new DefaultTreeItem(ipAddress).select();

		new ContextMenuItem("Connect").select();
		new SystemPasswordPromptDialog();
		new LabeledText("User ID:").setText(config.userName());
		new LabeledText("Password (optional):").setText(config.pass());
		new PushButton("OK").click();
		
		new WaitUntil(new ShellIsAvailable("Warning"), TimePeriod.LONG, false);

		try {
			Shell shell = new DefaultShell("Warning");
			new YesButton().click();
			new WaitWhile(new ShellIsAvailable(shell), TimePeriod.LONG);

			try {
				// known_hosts does not exist - create ? shell
				Shell hostsShell = new DefaultShell("Warning");
				new YesButton().click();
				new WaitWhile(new ShellIsAvailable(hostsShell), TimePeriod.LONG);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (Exception e) {
			// ssh keys shell was not opened
		}
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		assertTrue(system.isConnected());

	}

	@Override
	public void setDeclaration(DockerWildfly declaration) {
		this.config = declaration;

	}

	@Override
	public void cleanUp() {
		if (config.cleanup()) {
			try{
				ServersView2 sw = new ServersView2();
				sw.open();
				sw.getServer(config.name()).delete();
				SystemViewPart sview = new SystemViewPart();
				sview.open();
				sview.getSystem(ipAddress).delete();
			} finally {
				try {
					removeDocker();
				} catch (Exception e) {
					throw new AssertionError("failed to remove docker container", e);
				}
			}
		}

	}
	
	private void removeDocker() throws Exception{
		if (docker != null) {
			docker.killContainer(widlflyContId);

			// Remove container
			docker.removeContainer(widlflyContId);
			
			//remove image
			docker.removeImage(config.imageName());

			// Close the docker client
			docker.close();
		}
	}

	private class IPIsAssigned extends AbstractWaitCondition {

		private String ip;

		@Override
		public boolean test() {
			try {
				ip = docker.inspectContainer(widlflyContId).networkSettings().ipAddress();
				return ip != null && !ip.isEmpty();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		public String getIP() {
			return ip;
		}

	}

	@Override
	public String getServerName() {
		return config.name();
	}

	@Override
	public String getRuntimeName() {
		return config.name();
	}

	@Override
	public RequirementConfiguration getConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DockerWildfly getDeclaration() {
		return config;
	}

}
