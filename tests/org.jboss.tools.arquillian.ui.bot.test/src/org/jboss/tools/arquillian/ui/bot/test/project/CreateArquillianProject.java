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

import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.tools.arquillian.ui.bot.test.AbstractArquillianTestCase;
import org.junit.Test;

/**
 * Create simple Maven project and enable Arquillian support for it. <br>
 * 
 * Check that:
 * <ul>
 * 	<li> there are no errors or warnings in Problems view
 * </ul>
 * @author Lucia Jelinkova
 *
 */
@OpenPerspective(JavaPerspective.class)
public class CreateArquillianProject extends AbstractArquillianTestCase{

	@Test
	public void testProjectCreation(){
		prepareProject();
		checkProblems();
	}

}
