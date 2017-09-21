/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
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

import org.eclipse.reddeer.eclipse.exception.EclipseLayerException;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersView2;
import org.eclipse.reddeer.eclipse.wst.server.ui.wizard.NewServerWizard;
import org.eclipse.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardPage;
import org.eclipse.reddeer.junit.requirement.Requirement;
import org.eclipse.reddeer.junit.requirement.configuration.RequirementConfiguration;
import org.eclipse.reddeer.requirements.server.AbstractServerRequirement;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.tools.livereload.reddeer.requirement.LivereloadServerRequirement.LivereloadServer;

public class LivereloadServerRequirement extends AbstractServerRequirement implements Requirement<LivereloadServer>{
	
	private LivereloadServer server;
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface LivereloadServer {
		ServerRequirementState state() default ServerRequirementState.RUNNING;
		String name();
		boolean cleanup() default true;
	}


	@Override
	public void fulfill() {
		ServersView2 sw = new ServersView2();
		sw.open();
		try{
			sw.getServer(server.name());
			//already exists, do nothing
		} catch (EclipseLayerException e) {
			NewServerWizard serverW = new NewServerWizard();
			serverW.open();
			NewServerWizardPage sp = new NewServerWizardPage(serverW);
				
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
			sw.getServer(getServerName()).delete(true);
		}
		
	}


	@Override
	public LivereloadServer getDeclaration() {
		return server;
	}


	@Override
	public String getServerName() {
		return server.name();
	}


	@Override
	public String getRuntimeName() {
		return server.name();
	}


	@Override
	public RequirementConfiguration getConfiguration() {
		return null;
	}
	
}
