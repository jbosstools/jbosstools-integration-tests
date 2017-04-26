/*******************************************************************************
 * Copyright (c) 2016-2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.livereload.reddeer.requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.ide.eclipse.as.reddeer.server.wizard.page.NewServerWizardPageWithErrorCheck;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.wst.server.ui.cnf.ServersView2;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizard;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.requirements.server.ConfiguredServerInfo;
import org.jboss.reddeer.requirements.server.IServerReqConfig;
import org.jboss.reddeer.requirements.server.ServerReqBase;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.livereload.reddeer.requirement.LivereloadServerRequirement.LivereloadServer;

public class LivereloadServerRequirement extends ServerReqBase implements Requirement<LivereloadServer>{
	
	private LivereloadServer server;
	
	private static ConfiguredServerInfo lastServerConfiguration;
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface LivereloadServer {
		ServerReqState state() default ServerReqState.RUNNING;
		String name();
		boolean cleanup() default true;
	}


	@Override
	public boolean canFulfill() {
		return true;
	}


	@Override
	public void fulfill() {
		ServersView2 sw = new ServersView2();
		sw.open();
		try{
			sw.getServer(server.name());
			//already exists, do nothing
		} catch (EclipseLayerException e) {
			lastServerConfiguration = new ConfiguredServerInfo(server.name(), null);
			NewServerWizard serverW = new NewServerWizard();
			serverW.open();
			NewServerWizardPageWithErrorCheck sp = new NewServerWizardPageWithErrorCheck();
				
			sp.selectType("Basic","LiveReload Server");
			sp.setName(server.name());
			serverW.finish();
			setupServerState(server.state());
		}
		
		
		
	}


	@Override
	public void setDeclaration(LivereloadServer declaration) {
		this.server = declaration;
		
	}


	@Override
	public void cleanUp() {
		if(server.cleanup()){
			ServersView2 sw = new ServersView2();
			sw.open();
			sw.getServer(lastServerConfiguration.getServerName()).delete(true);
		}
		
	}


	@Override
	public IServerReqConfig getConfig() {
		return null;
	}


	@Override
	public ConfiguredServerInfo getConfiguredConfig() {
		return lastServerConfiguration;
	}
	
}
