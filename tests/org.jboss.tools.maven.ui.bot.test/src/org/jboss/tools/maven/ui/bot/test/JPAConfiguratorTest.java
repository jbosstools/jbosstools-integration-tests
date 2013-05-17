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

import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.eclipse.core.runtime.CoreException;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Rastislav Wagner
 * 
 */
public class JPAConfiguratorTest extends AbstractConfiguratorsTest {
	
	private String projectNameNoRuntime = PROJECT_NAME_JPA + "_noRuntime";

	@BeforeClass
	public static void before() {
		setPerspective("Java EE");
	}
	
	@After
	public void clean(){
		deleteProjects(true, true);
	}
	
	@Test
	public void testJPAConfigurator() throws UnsupportedEncodingException, ParserConfigurationException, TransformerException, CoreException{
		createWebProject(projectNameNoRuntime, null, false);
		convertToMavenProject(projectNameNoRuntime, "war", false);
		addPersistence(projectNameNoRuntime);
		updateConf(projectNameNoRuntime);
		assertTrue("Project " + projectNameNoRuntime+ " with persistence.xml file doesn't have " + JPA_NATURE+ " nature.",hasNature(projectNameNoRuntime, JPA_NATURE, null));
	}
}