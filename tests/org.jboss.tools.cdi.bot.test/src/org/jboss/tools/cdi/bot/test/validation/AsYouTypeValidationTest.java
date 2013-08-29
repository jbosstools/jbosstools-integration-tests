/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.bot.test.validation;

import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.bot.test.annotations.CDIWizardType;
import org.jboss.tools.cdi.bot.test.condition.AsYouTypeMarkerExistsCondition;
import org.jboss.tools.ui.bot.ext.Timing;
import org.junit.After;
import org.junit.Test;

/**
 * Tests as-you-type validation in java editor
 * 
 * @author jjankovi
 * 
 */
public class AsYouTypeValidationTest extends CDITestBase {

	private static final String ELIGIBLE_VALIDATION_PROBLEM = "Multiple beans are eligible " +
			"for injection to the injection point.*";
	
	private static final String BEAN_IS_NOT_ALTERNATIVE = ".*is not an alternative bean class.*";
	
	@After
	public void cleanUp() {
		bot.activeEditor().save();
	}
	
	/**
	 * failing
	 * reported by jjankovi JBIDE-12575
	 */
	@Test
	public void testJavaAYTValidation() {
		
		wizard.createCDIComponent(
				CDIWizardType.BEAN, "Test", getPackageName(), null);
		
		//=======================================================================
		// 	Invoke as-you-type validation marker appearance without saving file
		//=======================================================================
		
		editResourceUtil.replaceInEditor("// TODO Auto-generated constructor stub", "");
		
		bot.waitWhile(new AsYouTypeMarkerExistsCondition("TODO Auto-generated constructor stub"));
		
		editResourceUtil.replaceClassContentByResource(AsYouTypeValidationTest.class.
				getResourceAsStream("/resources/validation/Test1.java.cdi"), 
				false, false, getPackageName(), "Test");
		
		bot.waitUntil(new AsYouTypeMarkerExistsCondition(ELIGIBLE_VALIDATION_PROBLEM));
		
		//==========================================================================
		// 	Invoke as-you-type validation marker disappearance without saving file
		//==========================================================================
		
		editResourceUtil.replaceInEditor("@Inject ", "@Inject @Named ", false);
		bot.waitWhile(new AsYouTypeMarkerExistsCondition(ELIGIBLE_VALIDATION_PROBLEM));
	}
	
	/**
	 * failing
	 * reported by jjankovi JBIDE-12575
	 */
	@Test
	public void testBeansXmlAYTValidation() {
		
		wizard.createCDIComponent(
				CDIWizardType.BEAN, "A1", getPackageName(), null);
		
		wizard.createCDIComponent(
				CDIWizardType.BEAN, "A2", getPackageName(), "alternative");
		
		//=======================================================================
		// 	Invoke as-you-type validation marker appearance without saving file
		//=======================================================================
		
		beansHelper.createBeansXMLWithAlternative(getProjectName(), 
				getPackageName(), "A1", false);
		
		bot.waitUntil(new AsYouTypeMarkerExistsCondition(BEAN_IS_NOT_ALTERNATIVE), 
				Timing.time10S());
		
		//==========================================================================
		// 	Invoke as-you-type validation marker disappearance without saving file
		//==========================================================================
		
		editResourceUtil.replaceInEditor("A1", "A2", false);
		
		bot.waitWhile(new AsYouTypeMarkerExistsCondition(BEAN_IS_NOT_ALTERNATIVE));
	}
	
}