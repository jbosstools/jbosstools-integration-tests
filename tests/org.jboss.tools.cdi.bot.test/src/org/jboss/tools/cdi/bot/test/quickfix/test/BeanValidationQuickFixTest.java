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
import org.jboss.tools.cdi.reddeer.validators.BeanValidationProvider;
import org.jboss.tools.cdi.reddeer.validators.IValidationProvider;
import org.junit.Test;

/**
 * Test operates on quick fixes used for validation errors of CDI bean component
 * 
 * @author Jaroslav Jankovic
 */
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
@CleanWorkspace
public class BeanValidationQuickFixTest extends CDITestBase {
	
	private static IValidationProvider validationProvider = new BeanValidationProvider();
	
	public IValidationProvider validationProvider() {
		return validationProvider;
	}
	
	// https://issues.jboss.org/browse/JBIDE-8550
	@Test
	public void testSerializableManagedBean() {
		
		String className = "ManagedBean";
		
		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, className, 
				getPackageName(), null, BeanValidationQuickFixTest.class.getResourceAsStream("/resources/quickfix/bean/SerializableBean.java.cdi"));
		editResourceUtil.replaceInEditor(className+".java","BeanComponent", className);		
		
		quickFixHelper.checkQuickFix(ValidationType.SERIALIZABLE, getProjectName(), validationProvider());
		
	}
	
	// https://issues.jboss.org/browse/JBIDE-7664
	@Test
	public void testConstructor() {
		
		String className = "Bean1";
		
		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, className, 
				getPackageName(), null, BeanValidationQuickFixTest.class.getResourceAsStream("/resources/quickfix/bean/ConstructorWithParam.java.cdi"));		
		editResourceUtil.replaceInEditor(className+".java","BeanComponent", className);		
		
		quickFixHelper.checkQuickFix(ValidationType.DISPOSES, getProjectName(), validationProvider());
		
		editResourceUtil.replaceClassContentByResource(className+".java", BeanValidationQuickFixTest.class
				.getResourceAsStream("/resources/quickfix/bean/ConstructorWithParam.java.cdi"), false);
		
		editResourceUtil.replaceInEditor(className+".java", "@Disposes", "@Observes");
		editResourceUtil.replaceInEditor(className+".java", "import javax.enterprise.inject.Disposes;", 
				"import javax.enterprise.event.Observes;");
		editResourceUtil.replaceInEditor(className+".java", "BeanComponent", className);		
		
		quickFixHelper.checkQuickFix(ValidationType.OBSERVES, getProjectName(), validationProvider());
	}
	
	// https://issues.jboss.org/browse/JBIDE-7665
	@Test
	public void testProducer() {
		
		String className = "Bean2";
		
		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, className, 
				getPackageName(), null, BeanValidationQuickFixTest.class.getResourceAsStream("/resources/quickfix/bean/ProducerWithParam.java.cdi"));
		
		editResourceUtil.replaceInEditor(className+".java","BeanComponent", className);
		
		quickFixHelper.checkQuickFix(ValidationType.DISPOSES, getProjectName(), validationProvider());
		
		editResourceUtil.replaceClassContentByResource(className+".java",BeanValidationQuickFixTest.class
				.getResourceAsStream("/resources/quickfix/bean/ProducerWithParam.java.cdi"), false);
		editResourceUtil.replaceInEditor(className+".java","BeanComponent", className);
	
		editResourceUtil.replaceInEditor(className+".java","@Disposes", "@Observes");
		editResourceUtil.replaceInEditor(className+".java","import javax.enterprise.inject.Disposes;", 
				"import javax.enterprise.event.Observes;");
		
		quickFixHelper.checkQuickFix(ValidationType.OBSERVES, getProjectName(), validationProvider());
		
	}
	
	// https://issues.jboss.org/browse/JBIDE-7667
	@Test
	public void testInjectDisposer() {
			
		String className = "Bean3";
		
		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, className, 
				getPackageName(), null, BeanValidationQuickFixTest.class.getResourceAsStream("/resources/quickfix/bean/BeanInjectDisposes.java.cdi"));
		
		editResourceUtil.replaceInEditor(className+".java","BeanComponent", className);
		
		quickFixHelper.checkQuickFix(ValidationType.DISPOSES, getProjectName(), validationProvider());
				
	}
	
	// https://issues.jboss.org/browse/JBIDE-7667
	@Test
	public void testInjectObserver() {
		
		String className = "Bean4";
		
		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, className, 
				getPackageName(), null, BeanValidationQuickFixTest.class.getResourceAsStream("/resources/quickfix/bean/BeanInjectDisposes.java.cdi"));
		
		editResourceUtil.replaceInEditor(className+".java","import javax.enterprise.inject.Disposes;", 
				"import javax.enterprise.event.Observes;");
		editResourceUtil.replaceInEditor(className+".java","@Disposes", "@Observes");
		editResourceUtil.replaceInEditor(className+".java","BeanComponent", className);
		
		quickFixHelper.checkQuickFix(ValidationType.OBSERVES, getProjectName(), validationProvider());
		
	}
	
	// https://issues.jboss.org/browse/JBIDE-7667
	@Test
	public void testInjectProducer() {
		
		String className = "Bean5";
		
		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, className, 
				getPackageName(), null, BeanValidationQuickFixTest.class.getResourceAsStream("/resources/quickfix/bean/BeanInjectProducer.java.cdi"));
		
		editResourceUtil.replaceInEditor(className+".java","BeanComponent", className);
			
		quickFixHelper.checkQuickFix(ValidationType.PRODUCES, getProjectName(), validationProvider());
			
	}
	
	// https://issues.jboss.org/browse/JBIDE-7668
	@Test
	public void testObserverWithDisposer() {
			
		String className = "Bean6";
		
		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, className, 
				getPackageName(), null, BeanValidationQuickFixTest.class.getResourceAsStream("/resources/quickfix/bean/ObserverWithDisposer.java.cdi"));
		
		editResourceUtil.replaceInEditor(className+".java","BeanComponent", className);
			
		quickFixHelper.checkQuickFix(ValidationType.OBSERVES, getProjectName(), validationProvider());
			
	}
	
}
