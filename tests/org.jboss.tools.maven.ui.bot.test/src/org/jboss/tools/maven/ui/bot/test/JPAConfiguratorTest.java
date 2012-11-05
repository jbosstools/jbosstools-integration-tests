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
public class JPAConfiguratorTest extends AbstractConfiguratorsTest{

	private SWTUtilExt botUtil= new SWTUtilExt(bot);
	
	@BeforeClass
	public static void beforeClass(){
		setPerspective("Java EE");
	}
	
	@Test
	public void testJPAConfigurator() throws Exception{
		createMavenizedDynamicWebProject(PROJECT_NAME_JPA+"_noRuntime", false);
		addPersistence(PROJECT_NAME_JPA+"_noRuntime");
		updateConf(botUtil,PROJECT_NAME_JPA+"_noRuntime");
		assertTrue("Project "+PROJECT_NAME_JPA+"_noRuntime"+" with persistence.xml file doesn't have "+JPA_NATURE+" nature.",hasNature(PROJECT_NAME_JPA+"_noRuntime", JPA_NATURE));
		clean();
	}
}