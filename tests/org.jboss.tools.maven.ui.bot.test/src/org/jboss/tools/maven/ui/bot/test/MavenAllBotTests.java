/*************************************************************************************
 * Copyright (c) 2008-2011 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.maven.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * 
 * This is a swtbot testcase for an eclipse application.
 * 
 */
@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
/*
	ArchetypesTest.class,
	
	PerspectiveTest.class,
//	/MavenProfilesTest.class,
	MaterializeLibraryTest.class,

	EARProjectTest.class,
	SeamProjectTest.class, 
	JSFProjectTest.class, 
	
	JPAConfiguratorTest.class,
	JSFConfiguratorTest.class,
	
	SeamConfiguratorTest.class,

	CDIConfiguratorTest.class, 
	JAXRSConfiguratorTest.class,
	PortletConfiguratorTest.class,
	
	MavenRepositories.class,
	*/
	MavenConversionTest.class,
	RemoteRepositoriesPreferenceTest.class,
	
	SourceLookupTest.class
})
public class MavenAllBotTests {

}