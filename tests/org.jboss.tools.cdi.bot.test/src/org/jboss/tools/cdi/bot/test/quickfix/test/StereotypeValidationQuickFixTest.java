/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.bot.test.quickfix.test;


import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.annotation.CDIWizardType;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;
import org.jboss.tools.cdi.reddeer.validators.IValidationProvider;
import org.jboss.tools.cdi.reddeer.validators.StereotypeValidationProvider;
import org.junit.Test;

/**
 * Test operates on quick fixes used for validation errors of CDI Stereotype component
 * 
 * @author Jaroslav Jankovic
 */
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
@CleanWorkspace
public class StereotypeValidationQuickFixTest extends CDITestBase {
	
	private static IValidationProvider validationProvider = new StereotypeValidationProvider();

	public IValidationProvider validationProvider() {
		return validationProvider;
	}
	
	// https://issues.jboss.org/browse/JBIDE-7630
	@Test
	public void testTargetAnnotation() {
		
		String className = "Stereotype1";
		
		wizard.createCDIComponent(CDIWizardType.STEREOTYPE, className, getPackageName(), null);
		
		editResourceUtil.replaceInEditor(className+".java","@Target({ TYPE, METHOD, FIELD })", 
				"@Target({ TYPE, FIELD })");
		
		quickFixHelper.checkQuickFix(ValidationType.TARGET, "@Target({TYPE, METHOD, FIELD})", 
				getProjectName(), validationProvider());

		editResourceUtil.replaceInEditor(className+".java","@Target({TYPE, METHOD, FIELD})", "");
		
		quickFixHelper.checkQuickFix(ValidationType.TARGET, getProjectName(), validationProvider());
	}
	
	// https://issues.jboss.org/browse/JBIDE-7631
	@Test
	public void testRetentionAnnotation() {
		
		String className = "Stereotype2";

		wizard.createCDIComponent(CDIWizardType.STEREOTYPE, className, getPackageName(), null);
		
		editResourceUtil.replaceInEditor(className+".java","@Retention(RUNTIME)", "@Retention(CLASS)");
		
		quickFixHelper.checkQuickFix(ValidationType.RETENTION, getProjectName(), validationProvider());
				
		editResourceUtil.replaceInEditor(className+".java","@Retention(RUNTIME)", "");
		
		quickFixHelper.checkQuickFix(ValidationType.RETENTION, getProjectName(), validationProvider());
		
	}
	
	// https://issues.jboss.org/browse/JBIDE-7634
	@Test
	public void testNamedAnnotation() {
		
		String className = "Stereotype3";
		
		wizard.createCDIComponentWithContent(CDIWizardType.STEREOTYPE, className, 
				getPackageName(), null, StereotypeValidationQuickFixTest.class.getResourceAsStream("/resources/quickfix/stereotype/StereotypeWithNamed.java.cdi"));
	
		editResourceUtil.replaceInEditor(className+".java","StereotypeComponent", className);
		
		quickFixHelper.checkQuickFix(ValidationType.NAMED, getProjectName(), validationProvider());
		
	}
	
	// https://issues.jboss.org/browse/JBIDE-7640
	@Test	
	public void testTypedAnnotation() {
		
		String className = "Stereotype4";
		
		wizard.createCDIComponentWithContent(CDIWizardType.STEREOTYPE, className, 
				getPackageName(), null, StereotypeValidationQuickFixTest.class.getResourceAsStream("/resources/quickfix/stereotype/StereotypeWithTyped.java.cdi"));
		
		editResourceUtil.replaceInEditor(className+".java","StereotypeComponent", className);
		
		quickFixHelper.checkQuickFix(ValidationType.TYPED, getProjectName(), validationProvider());
		
	}	

}
