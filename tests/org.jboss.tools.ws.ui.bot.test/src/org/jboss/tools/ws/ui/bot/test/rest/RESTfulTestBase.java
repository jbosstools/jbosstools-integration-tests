/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.rest;

import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ws.ui.bot.test.WSTestBase;

/**
 * Test base for bot tests using RESTFul support
 * 
 * @author jjankovi
 * 
 */
@Require(server = @Server(state=ServerState.NotRunning), perspective = "Java EE")
public class RESTfulTestBase extends WSTestBase {

	protected final RESTfulHelper restfulHelper = new RESTfulHelper();
	
	protected final String CONFIGURE_MENU_LABEL = "Configure";
	protected final String REST_SUPPORT_MENU_LABEL_ADD = "Add JAX-RS 1.1 support...";
	protected final String REST_SUPPORT_MENU_LABEL_REMOVE = "Remove JAX-RS 1.1 support...";
	protected final String REST_EXPLORER_LABEL = "JAX-RS REST Web Services";
	protected final String REST_EXPLORER_LABEL_BUILD = "Building RESTful Web Services...";
	
	protected final String BASIC_WS_RESOURCE = "/resources/restful/BasicRestfulWS.java.ws";
	
	protected final String ADVANCED_WS_RESOURCE = "/resources/restful/AdvancedRestfulWS.java.ws";
	
	protected final String EMPTY_WS_RESOURCE = "/resources/restful/EmptyRestfulWS.java.ws";
	
	protected final String SIMPLE_REST_WS_RESOURCE = "/resources/restful/SimpleRestWS.java.ws";

	protected String getWsPackage() {
		return "org.rest.test";
	}
	
	protected String getWsName() {
		return "RestService";
	}
	
	@Override
	public void setup() {		
		prepareRestProject();
	}
	
	@Override
	public void cleanup() {		
		
	}
	
	protected void prepareRestProject() {
		
		if (!projectExists(getWsProjectName())) {
			
			//importing project without targeted runtime set
			importWSTestProject("resources/projects/" + 
					getWsProjectName(), getWsProjectName());
			
			projectExplorer.selectProject(getWsProjectName());
			eclipse.cleanAllProjects();
			bot.sleep(Timing.time3S());
			
			if (!restfulHelper.isRestSupportEnabled(getWsProjectName())) {	
				// workaround for EAP 5.1
				if (configuredState.getServer().type.equals("EAP") && 
					configuredState.getServer().version.equals("5.1")) {
					restfulHelper.addRestEasyLibs(getWsProjectName());
				}
				restfulHelper.addRestSupport(getWsProjectName());
			}
		}
	}
	
}
