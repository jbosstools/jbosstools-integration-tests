/*******************************************************************************
 * Copyright (c) 2010-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test.beans.bean.template;

import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.condition.AsYouTypeMarkerExists;
import org.junit.Test;

public class AsYouTypeValidationTemplate extends CDITestBase{

	private static final String ELIGIBLE_VALIDATION_PROBLEM = "Multiple beans are eligible " +
			"for injection to the injection point";
	
	@Test
	public void testAYTValidation() {
		
		beansHelper.createBean("Test", getPackageName(), false,false,false,false,false,false,false,null,null);
		
		//=======================================================================
		// 	Invoke as-you-type validation marker appearance without saving file
		//=======================================================================
		new WaitUntil(new AsYouTypeMarkerExists("Test.java","TODO Auto-generated constructor stub"));
		
		editResourceUtil.replaceInEditor("Test.java","// TODO Auto-generated constructor stub", "");
		
		new WaitWhile(new AsYouTypeMarkerExists("Test.java","TODO Auto-generated constructor stub"));
		
		editResourceUtil.replaceClassContentByResource("Test.java",
				readFile("resources/validation/Test1.java.cdi"),false);
		
		new WaitUntil(new AsYouTypeMarkerExists("Test.java",ELIGIBLE_VALIDATION_PROBLEM));
		
		//==========================================================================
		// 	Invoke as-you-type validation marker disappearance without saving file
		//==========================================================================
		
		editResourceUtil.replaceInEditor("Test.java","@Inject ", "@Inject @Named ", false);
		new WaitWhile(new AsYouTypeMarkerExists("Test.java",ELIGIBLE_VALIDATION_PROBLEM));
	}

}
