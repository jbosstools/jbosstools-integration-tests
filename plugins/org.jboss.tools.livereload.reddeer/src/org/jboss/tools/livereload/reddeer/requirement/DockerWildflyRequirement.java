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

import org.jboss.ide.eclipse.as.reddeer.server.wizard.page.NewServerAdapterPage;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.page.NewServerRSIWizardPage;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.page.NewServerWizardPageWithErrorCheck;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.page.NewServerAdapterPage.Profile;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.platform.RunningPlatform;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.rse.ui.view.System;
import org.jboss.reddeer.eclipse.rse.ui.view.SystemView;
import org.jboss.reddeer.eclipse.rse.ui.wizard.NewConnectionWizardDialog;
import org.jboss.reddeer.eclipse.rse.ui.wizard.NewConnectionWizardMainPage;
import org.jboss.reddeer.eclipse.rse.ui.wizard.NewConnectionWizardSelectionPage;
import org.jboss.reddeer.eclipse.rse.ui.wizard.SystemPasswordPromptDialog;
import org.jboss.reddeer.eclipse.rse.ui.wizard.NewConnectionWizardSelectionPage.SystemType;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardDialog;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.requirements.server.ConfiguredServerInfo;
import org.jboss.reddeer.requirements.server.ServerReqBase;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.api.Editor;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.livereload.reddeer.requirement.DockerWildflyRequirement.DockerWildfly;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;
import com.spotify.docker.client.messages.ContainerConfig.Builder;

public class DockerWildflyRequirement extends ServerReqBase implements Requirement<DockerWildfly> {

	private String ipAddress;
	private DockerClient docker;
	private String widlflyContId;
	private static ConfiguredServerInfo lastServerConfiguration;
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
	public boolean canFulfill() {
		return true;
	}

	@Override
	public void fulfill() {
		try {
			lastServerConfiguration = new ConfiguredServerInfo(config.name(), null);
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
			ServersView sw = new ServersView();
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
		ServersView sw = new ServersView();
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
		NewServerWizardDialog serverW = new NewServerWizardDialog();
		// setup remote system first
		setupRemoteSystem();

		// -- Open 'New Server' wizard
		serverW.open();
		// -- Select the server type and fill in server name, then continue
		// on next page
		NewServerWizardPageWithErrorCheck sp = new NewServerWizardPageWithErrorCheck();
		sp.selectType("JBoss Community", "WildFly 10.x");
		sp.setName(config.name());
		sp.checkErrors();
		serverW.next();

		// -- Select server profile (Remote)
		NewServerAdapterPage ap = new NewServerAdapterPage();
		ap.setProfile(Profile.REMOTE);

		// TODO Fix this in NewServerAdapterPage
		// ap.setAssignRuntime(false);
		CheckBox extManaged = new CheckBox("Server lifecycle is externally managed.");
		extManaged.toggle(true);

		CheckBox check = new CheckBox("Assign a runtime to this server");
		check.toggle(false);
		serverW.next();

		NewServerRSIWizardPage rsp = new NewServerRSIWizardPage();
		rsp.setRemoteServerHome(config.homeFolder());
		rsp.selectHost(ipAddress); // host was configured in
									// setupRemoteSystem
		serverW.finish();
	}

	protected void setupRemoteSystem() {
		SystemView sview = new SystemView();
		sview.open();
		NewConnectionWizardDialog connW = sview.newConnection();
		NewConnectionWizardSelectionPage sp = new NewConnectionWizardSelectionPage();
		sp.selectSystemType(SystemType.SSH_ONLY);
		connW.next();
		NewConnectionWizardMainPage mp = new NewConnectionWizardMainPage();
		mp.setHostName(ipAddress);
		connW.finish();

		// TODO FIX in as.reddeer
		System system = sview.getSystem(ipAddress);

		// system.connect("root", "root");

		if (RunningPlatform.isOSX()) {
			new DefaultTreeItem(ipAddress, "Ssh Shells").select();
			new ContextMenu("Properties").select();
			Shell propShell = new DefaultShell("Properties for Ssh Shells");
			new DefaultTreeItem("Subsystem").select();
			new LabeledText("Port (1-65535):").setText(macSSHPort);
			new OkButton().click();
			new WaitWhile(new ShellIsAvailable(propShell));
		}

		sview.open();
		new DefaultTreeItem(ipAddress).select();

		new ContextMenu("Connect").select();
		new SystemPasswordPromptDialog();
		new LabeledText("User ID:").setText(config.userName());
		new LabeledText("Password (optional):").setText(config.pass());
		new PushButton("OK").click();
		
		new WaitUntil(new ShellWithTextIsAvailable("Warning"), TimePeriod.LONG, false);

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
				if (lastServerConfiguration != null) {
					ServersView sw = new ServersView();
					sw.open();
					sw.getServer(config.name()).delete();
					lastServerConfiguration = null;
					SystemView sview = new SystemView();
					sview.open();
					sview.getSystem(ipAddress).delete();
				}
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

}
