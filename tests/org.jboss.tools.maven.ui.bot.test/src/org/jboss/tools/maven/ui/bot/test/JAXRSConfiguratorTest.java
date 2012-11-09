/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.maven.ui.bot.test;

import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * @author Rastislav Wagner
 * 
 */
public class JAXRSConfiguratorTest extends AbstractConfiguratorsTest{
	
	private SWTUtilExt botUtil= new SWTUtilExt(bot);
	
	@BeforeClass
	public static void beforeClass(){
		setPerspective("Java EE");
	}

	@Test
	public void testJAXRSConfigurator() throws Exception {
		createMavenizedDynamicWebProject(PROJECT_NAME_JAXRS+"_noRuntime", false);
		addDependencies(PROJECT_NAME_JAXRS+"_noRuntime", "com.cedarsoft.rest", "jersey", "1.0.0",null);
		updateConf(botUtil,PROJECT_NAME_JAXRS+"_noRuntime");
		assertTrue("Project "+PROJECT_NAME_JAXRS+"_noRuntime"+" with jersey dependency doesn't have "+JAXRS_NATURE+" nature.",hasNature(PROJECT_NAME_JAXRS+"_noRuntime", JAXRS_NATURE));
		clean();
		
		createMavenizedDynamicWebProject(PROJECT_NAME_JAXRS+"_noRuntime", false);
		addDependencies(PROJECT_NAME_JAXRS+"_noRuntime", "org.jboss.jbossas", "jboss-as-resteasy", "6.1.0.Final",null);
		updateConf(botUtil,PROJECT_NAME_JAXRS+"_noRuntime");
		assertTrue("Project "+PROJECT_NAME_JAXRS+"_noRuntime"+" with resteasy dependency doesn't have "+JAXRS_NATURE+" nature.",hasNature(PROJECT_NAME_JAXRS+"_noRuntime", JAXRS_NATURE));
		clean();
		
		createMavenizedDynamicWebProject(PROJECT_NAME_JAXRS, true);
		assertTrue("Project "+PROJECT_NAME_JAXRS+" doesn't have "+JAXRS_NATURE+" nature.",hasNature(PROJECT_NAME_JAXRS, JAXRS_NATURE));
	}

}