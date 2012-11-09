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
public class SeamConfiguratorTest extends AbstractConfiguratorsTest{

	private SWTUtilExt botUtil= new SWTUtilExt(bot);
	
	@BeforeClass
	public static void beforeClass(){
		setPerspective("Java EE");
	}
	
	@Test
	public void testSeamConfigurator() throws Exception{
		//createMavenizedDynamicWebProject(PROJECT_NAME_SEAM+"_noRuntime", false);
		//addDependencies(PROJECT_NAME_SEAM+"_noRuntime", "org.jboss.seam", "jboss-seam", "2.3.0.Final","ejb");
		//updateConf(botUtil,PROJECT_NAME_SEAM+"_noRuntime");
		//assertTrue("Project "+PROJECT_NAME_SEAM+"_noRuntime"+" with jboss-seam dependency doesn't have "+SEAM_NATURE+" nature.",hasNature(PROJECT_NAME_SEAM+"_noRuntime", SEAM_NATURE));
		//clean();
		
		createMavenizedDynamicWebProject(PROJECT_NAME_SEAM+"_noRuntime", false);
		addDependencies(PROJECT_NAME_SEAM+"_noRuntime", "org.jboss.seam", "jboss-seam-ui", "2.3.0.Final",null);
		updateConf(botUtil,PROJECT_NAME_SEAM+"_noRuntime");
		assertTrue("Project "+PROJECT_NAME_SEAM+"_noRuntime"+" with jboss-seam-ui dependency doesn't have "+SEAM_NATURE+" nature.",hasNature(PROJECT_NAME_SEAM+"_noRuntime", SEAM_NATURE));
		clean();
		
		createMavenizedDynamicWebProject(PROJECT_NAME_SEAM+"_noRuntime", false);
		addDependencies(PROJECT_NAME_SEAM+"_noRuntime", "org.jboss.seam", "jboss-seam-pdf", "2.3.0.Final",null);
		updateConf(botUtil,PROJECT_NAME_SEAM+"_noRuntime");
		assertTrue("Project "+PROJECT_NAME_SEAM+"_noRuntime"+" with jboss-seam-pdf dependency doesn't have "+SEAM_NATURE+" nature.",hasNature(PROJECT_NAME_SEAM+"_noRuntime", SEAM_NATURE));
		clean();
		
		createMavenizedDynamicWebProject(PROJECT_NAME_SEAM+"_noRuntime", false);
		addDependencies(PROJECT_NAME_SEAM+"_noRuntime", "org.jboss.seam", "jboss-seam-remoting", "2.3.0.Final",null);
		updateConf(botUtil,PROJECT_NAME_SEAM+"_noRuntime");
		assertTrue("Project "+PROJECT_NAME_SEAM+"_noRuntime"+" with jboss-seam-remoting dependency doesn't have "+SEAM_NATURE+" nature.",hasNature(PROJECT_NAME_SEAM+"_noRuntime", SEAM_NATURE));
		clean();
		
		createMavenizedDynamicWebProject(PROJECT_NAME_SEAM+"_noRuntime", false);
		addDependencies(PROJECT_NAME_SEAM+"_noRuntime", "org.jboss.seam", "jboss-seam-ioc", "2.3.0.Final",null);
		updateConf(botUtil,PROJECT_NAME_SEAM+"_noRuntime");
		assertTrue("Project "+PROJECT_NAME_SEAM+"_noRuntime"+" with jboss-seam-ioc dependency doesn't have "+SEAM_NATURE+" nature.",hasNature(PROJECT_NAME_SEAM+"_noRuntime", SEAM_NATURE));
		clean();
	}
}