/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.rest.explorer.test;

import org.junit.Test;



/**
 * Test operates on exploring RESTFul services in RESTful explorer 
 * @author jjankovi
 *
 */
public class RESTfulServicesExplorerTest extends RESTfulTestBase {

	protected String getWsProjectName() {
		return "RestServicesExplorer";
	}
	
	protected String getWsPackage() {
		return "org.rest.explorer.services.test";
	}

	protected String getWsName() {
		return "RestService";
	}
	
	@Override
	public void setup() {
		if (!projectExists(getWsProjectName())) {
			projectHelper.createProject(getWsProjectName());
			addRestSupport(getWsProjectName());
		}
	}
	
	@Override
	public void cleanup() {
		if (projectExists(getWsProjectName())) {
			removeRestSupport(getWsProjectName());
			projectExplorer.deleteAllProjects();			
		}
	}
	
	@Test
	public void testAddingSimpleRESTMethods() {
		//only get, post, put, delete
	}
	
	@Test
	public void testAddingAdvancedRESTMethods() {
		//with additional path param
	}
	
	@Test
	public void testEditingSimpleRESTMethods() {
		//only get, post, put, delete
	}
	
	@Test
	public void testEditingAdvancedRESTMethods() {
		////with additional path param
	}
	
	@Test
	public void testDeletingRESTMethods() {
		//deleting all rest methods
	}
	
}
