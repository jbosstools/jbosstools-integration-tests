/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation    
 ******************************************************************************/
package org.jboss.tools.arquillian.ui.bot.test.project;

import org.jboss.tools.arquillian.ui.bot.test.AbstractArquillianTestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Add remote Wildfly Arquillian profile using right-click on project.
 * 
 * @author Lucia Jelinkova
 *
 */
public class AddArquillianProfile extends AbstractArquillianTestCase {

	@Before
	public void before() {
		prepareProject();
	}

	@Test
	public void testAddingProfile() {
		addArquillianProfile();
		selectMavenProfile();
		forceMavenRepositoryUpdate();
		checkProblems();
	}

}
