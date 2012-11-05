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
public class CDIConfiguratorTest extends AbstractConfiguratorsTest{
	
	private SWTUtilExt botUtil= new SWTUtilExt(bot);
	public static final String CDI_API_VERSION="1.1.EDR1.2";
	public static final String SEAM_FACES_VERSION="3.0.0.Alpha3";
	public static final String SEAM_INTERNATIONAL_VERSION="3.0.0.Alpha1";
	public static final String DELTASPIKE_CORE_API_VERSION="0.3-incubating";
	public static final String DELTASPIME_CORE_IMPL_VERSION="0.3-incubating";
	
	@BeforeClass
	public static void beforeClass(){
		setPerspective("Java EE");
	}
	
	@Test
	public void testCDIConfigurator() throws Exception{
		createMavenizedDynamicWebProject(PROJECT_NAME_CDI+"_noRuntime", false);
		addDependencies(PROJECT_NAME_CDI+"_noRuntime", "javax.enterprise", "cdi-api",CDI_API_VERSION,null);
		updateConf(botUtil,PROJECT_NAME_CDI+"_noRuntime");
		assertTrue("Project "+PROJECT_NAME_CDI+"_noRuntime"+" with cdi dependency doesn't have "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI+"_noRuntime", CDI_NATURE));
		clean();
		
		createMavenizedEJBProject(PROJECT_NAME_CDI_EJB+"_noRuntime", false);
		addDependencies(PROJECT_NAME_CDI_EJB+"_noRuntime", "javax.enterprise", "cdi-api",CDI_API_VERSION,null);
		updateConf(botUtil,PROJECT_NAME_CDI_EJB+"_noRuntime");;
		assertTrue("Project "+PROJECT_NAME_CDI_EJB+"_noRuntime"+" with cdi dependency doesn't have "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI_EJB+"_noRuntime", CDI_NATURE));
		clean();

		createMavenizedDynamicWebProject(PROJECT_NAME_CDI, true);
		assertFalse("Project "+PROJECT_NAME_CDI+" has "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI, CDI_NATURE));
		clean();
		
		createMavenizedEJBProject(PROJECT_NAME_CDI_EJB, true);
		assertFalse("Project "+PROJECT_NAME_CDI_EJB+" has "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI_EJB, CDI_NATURE));
		clean();
		
		//https://issues.jboss.org/browse/JBIDE-8755
		createMavenizedDynamicWebProject(PROJECT_NAME_CDI+"_noRuntime_seam", false);
		addDependencies(PROJECT_NAME_CDI+"_noRuntime_seam", "org.jboss.seam.faces", "seam-faces", SEAM_FACES_VERSION,null);
		updateConf(botUtil,PROJECT_NAME_CDI+"_noRuntime_seam");
		assertTrue("Project "+PROJECT_NAME_CDI+"_noRuntime_seam"+" with seam-faces3 dependency doesn't have "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI+"_noRuntime_seam", CDI_NATURE));
		clean();
		
		createMavenizedEJBProject(PROJECT_NAME_CDI_EJB+"_noRuntime_seam", false);
		addDependencies(PROJECT_NAME_CDI_EJB+"_noRuntime_seam", "org.jboss.seam.faces", "seam-faces", SEAM_FACES_VERSION,null);
		updateConf(botUtil,PROJECT_NAME_CDI_EJB+"_noRuntime_seam");
		assertTrue("Project "+PROJECT_NAME_CDI_EJB+"_noRuntime_seam"+" with seam-faces3 dependency doesn't have "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI_EJB+"_noRuntime_seam", CDI_NATURE));
		clean();
		
		createMavenizedDynamicWebProject(PROJECT_NAME_CDI+"_noRuntime_seam", false);
		addDependencies(PROJECT_NAME_CDI+"_noRuntime_seam", "org.jboss.seam.international", "seam-international", SEAM_INTERNATIONAL_VERSION,null);
		updateConf(botUtil,PROJECT_NAME_CDI+"_noRuntime_seam");
		assertTrue("Project "+PROJECT_NAME_CDI+"_noRuntime_seam"+" with seam3 dependency doesn't have "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI+"_noRuntime_seam", CDI_NATURE));
		
		createMavenizedEJBProject(PROJECT_NAME_CDI_EJB+"_noRuntime_seam", false);
		addDependencies(PROJECT_NAME_CDI_EJB+"_noRuntime_seam", "org.jboss.seam.international", "seam-international", SEAM_INTERNATIONAL_VERSION,null);
		updateConf(botUtil,PROJECT_NAME_CDI_EJB+"_noRuntime_seam");
		assertTrue("Project "+PROJECT_NAME_CDI_EJB+"_noRuntime_seam"+" with seam3 dependency doesn't have "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI_EJB+"_noRuntime_seam", CDI_NATURE));
		clean();
		
		createMavenizedEJBProject(PROJECT_NAME_CDI_EJB+"_noRuntime_deltaspike-api", false);
		addDependencies(PROJECT_NAME_CDI_EJB+"_noRuntime_deltaspike-api", "org.apache.deltaspike.core", "deltaspike-core-api", DELTASPIKE_CORE_API_VERSION,null);
		updateConf(botUtil,PROJECT_NAME_CDI_EJB+"_noRuntime_deltaspike-api");
		assertTrue("Project "+PROJECT_NAME_CDI_EJB+"_noRuntime_deltaspike-api"+" with deltaspike-api dependency doesn't have "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI_EJB+"_noRuntime_deltaspike-api", CDI_NATURE));
		clean();
		
		createMavenizedDynamicWebProject(PROJECT_NAME_CDI+"_noRuntime_deltaspike-api", false);
		addDependencies(PROJECT_NAME_CDI+"_noRuntime_deltaspike-api", "org.apache.deltaspike.core", "deltaspike-core-api", DELTASPIKE_CORE_API_VERSION,null);
		updateConf(botUtil,PROJECT_NAME_CDI+"_noRuntime_deltaspike-api");
		assertTrue("Project "+PROJECT_NAME_CDI+"_noRuntime_deltaspike-api"+" with deltaspike-api dependency doesn't have "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI+"_noRuntime_deltaspike-api", CDI_NATURE));
		clean();
		
		createMavenizedEJBProject(PROJECT_NAME_CDI_EJB+"_noRuntime_deltaspike-impl", false);
		addDependencies(PROJECT_NAME_CDI_EJB+"_noRuntime_deltaspike-impl", "org.apache.deltaspike.core", "deltaspike-core-impl", DELTASPIME_CORE_IMPL_VERSION,null);
		updateConf(botUtil,PROJECT_NAME_CDI_EJB+"_noRuntime_deltaspike-impl");
		assertTrue("Project "+PROJECT_NAME_CDI_EJB+"_noRuntime_deltaspike-impl"+" with deltaspike-impl dependency doesn't have "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI_EJB+"_noRuntime_deltaspike-impl", CDI_NATURE));
		clean();
		
		createMavenizedDynamicWebProject(PROJECT_NAME_CDI+"_noRuntime_deltaspike-impl", false);
		addDependencies(PROJECT_NAME_CDI+"_noRuntime_deltaspike-impl", "org.apache.deltaspike.core", "deltaspike-core-impl", DELTASPIME_CORE_IMPL_VERSION,null);
		updateConf(botUtil,PROJECT_NAME_CDI+"_noRuntime_deltaspike-impl");
		assertTrue("Project "+PROJECT_NAME_CDI+"_noRuntime_deltaspike-impl"+" with deltaspike-impl dependency doesn't have "+CDI_NATURE+" nature.",hasNature(PROJECT_NAME_CDI+"_noRuntime_deltaspike-impl", CDI_NATURE));
		
	}

}