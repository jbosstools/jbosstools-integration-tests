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

package org.jboss.tools.arquillian.ui.bot.test.testcase;

import org.jboss.tools.arquillian.ui.bot.test.AbstractArquillianTestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Creates Arquillian test case and checks that
 * 
 * <ul>
 * <li>there are no errors in Problems view
 * </ul>
 * 
 * @author Lucia Jelinkova
 *
 */
public class CreateArquillianTestCase extends AbstractArquillianTestCase {

	@Before
	public void before() {
		prepareProject();
		addArquillianProfile();
		selectMavenProfile();
		forceMavenRepositoryUpdate();
	}

	@Test
	public void testTestCaseCreation() {
		createTestCase();
		changeContent();
		checkProblems();
	}

}
