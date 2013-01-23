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

import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.bot.test.annotations.CDIWizardType;
import org.jboss.tools.cdi.bot.test.condition.AsYouTypeMarkerExistsCondition;
import org.junit.After;
import org.junit.Before;
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
	
	@After
	public void cleanUp() {
		bot.activeEditor().save();
	}
	
	@Test
	public void testJavaValidation() {
		
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
		
		ITextEditor activeTextEditor = asYouTypeValidationHelper.getActiveTextEditor();
		
		IAnnotationModel annotationModel = asYouTypeValidationHelper.
				getAnnotationModel(activeTextEditor);
		
		assertTrue(asYouTypeValidationHelper.markerExists((annotationModel), 
				null, ELIGIBLE_VALIDATION_PROBLEM));
		
		//==========================================================================
		// 	Invoke as-you-type validation marker disappearance without saving file
		//==========================================================================
		
		editResourceUtil.replaceInEditor("@Inject ", "@Inject @Named ", false);
		bot.waitWhile(new AsYouTypeMarkerExistsCondition(ELIGIBLE_VALIDATION_PROBLEM));
	}
	
}