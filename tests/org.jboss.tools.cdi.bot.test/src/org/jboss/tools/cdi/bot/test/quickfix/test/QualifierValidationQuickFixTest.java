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
import org.jboss.reddeer.eclipse.jdt.ui.NewAnnotationCreationWizard;
import org.jboss.reddeer.eclipse.jdt.ui.NewAnnotationWizardPage;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.annotation.CDIWizardType;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;
import org.jboss.tools.cdi.reddeer.validators.IValidationProvider;
import org.jboss.tools.cdi.reddeer.validators.QualifierValidationProvider;
import org.junit.Test;

/**
 * Test operates on quick fixes used for validation errors of CDI Qualifier component
 * 
 * @author Jaroslav Jankovic
 */
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
@CleanWorkspace
public class QualifierValidationQuickFixTest extends CDITestBase {
	
	private static IValidationProvider validationProvider = new QualifierValidationProvider();

	public IValidationProvider validationProvider() {
		return validationProvider;
	}
	
	// https://issues.jboss.org/browse/JBIDE-7630
	@Test
	public void testTargetAnnotation() {
		
		String className = "Qualifier1";
		
		wizard.createCDIComponent(CDIWizardType.QUALIFIER, className, getPackageName(), null);
		
		editResourceUtil.replaceInEditor(className+".java","@Target({ TYPE, METHOD, PARAMETER, FIELD })", 
				"@Target({ TYPE, FIELD })");
		
		quickFixHelper.checkQuickFix(ValidationType.TARGET, 
				"@Target({TYPE, METHOD, FIELD, PARAMETER})", getProjectName(), validationProvider());
		
		editResourceUtil.replaceInEditor(className+".java","@Target({TYPE, METHOD, FIELD, PARAMETER})", "");
		
		quickFixHelper.checkQuickFix(ValidationType.TARGET, getProjectName(), validationProvider());
	}
	
	// https://issues.jboss.org/browse/JBIDE-7631
	@Test
	public void testRetentionAnnotation() {
		
		String className = "Qualifier2";

		wizard.createCDIComponent(CDIWizardType.QUALIFIER, className, getPackageName(), null);
				
		editResourceUtil.replaceInEditor(className+".java","@Retention(RUNTIME)", "@Retention(CLASS)");
		
		quickFixHelper.checkQuickFix(ValidationType.RETENTION, getProjectName(), validationProvider());
		
		editResourceUtil.replaceInEditor(className+".java","@Retention(RUNTIME)", "");
		
		quickFixHelper.checkQuickFix(ValidationType.RETENTION, getProjectName(), validationProvider());
		
	}
	
	// https://issues.jboss.org/browse/JBIDE-7641
	@Test
	public void testNonbindingAnnotation() {
	
		String className = "Qualifier3";
		
		NewAnnotationCreationWizard aw = new NewAnnotationCreationWizard();
		aw.open();
		NewAnnotationWizardPage ap = aw.getFirstPage();
		ap.setPackage(getPackageName());
		ap.setName("AAnnotation");
		aw.finish();
		new TextEditor("AAnnotation.java");
		
		wizard.createCDIComponentWithContent(CDIWizardType.QUALIFIER, className, 
				getPackageName(), null, QualifierValidationQuickFixTest.class.getResourceAsStream("/resources/quickfix/" +
						"qualifier/QualifierWithAnnotation.java.cdi"));
	
		editResourceUtil.replaceInEditor(className+".java","QualifierComponent", className);
	
		quickFixHelper.checkQuickFix(ValidationType.NONBINDING, getProjectName(), validationProvider());
				
		editResourceUtil.replaceClassContentByResource(className+".java", QualifierValidationQuickFixTest.class
				.getResourceAsStream("/resources/quickfix/qualifier/QualifierWithStringArray.java.cdi"), false);
		editResourceUtil.replaceInEditor(className+".java","QualifierComponent", className);
		
		quickFixHelper.checkQuickFix(ValidationType.NONBINDING, getProjectName(), validationProvider());
	}
}
