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

import org.jboss.ide.eclipse.as.reddeer.server.wizard.page.NewServerWizardPageWithErrorCheck;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardDialog;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.requirements.server.ConfiguredServerInfo;
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
		lastServerConfiguration = new ConfiguredServerInfo(server.name(), null);
		NewServerWizardDialog serverW = new NewServerWizardDialog();
		serverW.open();
		NewServerWizardPageWithErrorCheck sp = new NewServerWizardPageWithErrorCheck();
			
		sp.selectType("Basic","LiveReload Server");
		sp.setName(server.name());
		serverW.finish();
		setupServerState(server.state(), lastServerConfiguration);
		
	}


	@Override
	public void setDeclaration(LivereloadServer declaration) {
		this.server = declaration;
		
	}


	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
		
	}
	
}
