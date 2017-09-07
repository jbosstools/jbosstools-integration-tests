/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi10.AsYouTypeValidationTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi10.BeanValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi10.NullValuesInjectionTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi11.AsYouTypeValidationTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi11.BeanValidationQuickFixTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi11.NullValuesInjectionTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi11.VetoedAnnotationTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.decorator.cdi10.DecoratorFromWebBeanTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.decorator.cdi10.DecoratorValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.decorator.cdi11.DecoratorFromWebBeanTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.decorator.cdi11.DecoratorValidationQuickFixTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.dialog.cdi10.AllAssignableDialogTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.dialog.cdi10.AssignableDialogFilterTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.dialog.cdi11.AllAssignableDialogTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.dialog.cdi11.AssignableDialogFilterTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.ibinding.cdi10.IBindingValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.ibinding.cdi11.IBindingValidationQuickFixTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.interceptor.cdi10.InterceptorValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.interceptor.cdi11.InterceptorValidationQuickFixTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.named.cdi10.NamedComponentsSearchingTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.named.cdi10.NamedRefactoringTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.named.cdi11.NamedComponentsSearchingTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.named.cdi11.NamedRefactoringTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.openon.cd10.BeanInjectOpenOnTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.openon.cd10.FindObserverEventTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.openon.cdi11.BeanInjectOpenOnTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.openon.cdi11.FindObserverEventTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.qualifier.cdi10.QualifierValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.qualifier.cdi11.QualifierValidationQuickFixTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.scope.cdi10.ScopeValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.scope.cdi11.ScopeValidationQuickFixTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.stereotype.cdi10.StereotypeValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.stereotype.cdi11.StereotypeValidationQuickFixTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.bean.cdi11.ExcludeBeanTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.cdi10.BeansXMLBeansEditorTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.cdi10.BeansXMLValidationTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.cdi11.BeansXMLBeansEditorTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.cdi11.BeansXMLDiscoveryModesTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.cdi11.BeansXMLUITestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.cdi11.BeansXMLValidationTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.completion.cdi10.BeansXMLCompletionTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.completion.cdi11.BeansXMLCompletionTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.openon.cdi10.BeansXMLOpenOnTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.openon.cdi11.BeansXMLOpenOnTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi10.BeansXMLAsYouTypeValidationTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi10.BeansXMLValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi11.BeansXMLAsYouTypeValidationTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi11.BeansXMLValidationQuickFixTestCDI11;
import org.jboss.tools.cdi.bot.test.validation.cdi10.CDIValidatorTestCDI10;
import org.jboss.tools.cdi.bot.test.validation.cdi11.CDIValidatorTestCDI11;
import org.jboss.tools.cdi.bot.test.weld.cdi10.WeldBuiltInContextsTestCDI10;
import org.jboss.tools.cdi.bot.test.weld.cdi10.WeldExcludeTestCDI10;
import org.jboss.tools.cdi.bot.test.weld.cdi10.WeldParametersAnnotationTestCDI10;
import org.jboss.tools.cdi.bot.test.weld.cdi10.WeldScanTestCDI10;
import org.jboss.tools.cdi.bot.test.weld.cdi11.WeldBuiltInContextsTestCDI11;
import org.jboss.tools.cdi.bot.test.weld.cdi11.WeldExcludeTestCDI11;
import org.jboss.tools.cdi.bot.test.weld.cdi11.WeldParametersAnnotationTestCDI11;
import org.jboss.tools.cdi.bot.test.weld.cdi11.WeldScanTestCDI11;
import org.jboss.tools.cdi.bot.test.wizard.cdi10.CDIWebProjectWizardTestCDI10;
import org.jboss.tools.cdi.bot.test.wizard.cdi10.DynamicWebProjectWithCDITestCDI10;
import org.jboss.tools.cdi.bot.test.wizard.cdi10.EjbProjectWithCDITestCDI10;
import org.jboss.tools.cdi.bot.test.wizard.cdi10.UtilityProjectWithCDITestCDI10;
import org.jboss.tools.cdi.bot.test.wizard.cdi11.CDIWebProjectWizardTestCDI11;
import org.jboss.tools.cdi.bot.test.wizard.cdi11.DynamicWebProjectWithCDITestCDI11;
import org.jboss.tools.cdi.bot.test.wizard.cdi11.EjbProjectWithCDITestCDI11;
import org.jboss.tools.cdi.bot.test.wizard.cdi11.UtilityProjectWithCDITestCDI11;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author rawagner
 * @author Lukas Jungmann
 * @author Jaroslav Jankovic
 */
@RunWith(RedDeerSuite.class)
@SuiteClasses({	
	
	AsYouTypeValidationTestCDI10.class,
	AsYouTypeValidationTestCDI11.class,
	BeanValidationQuickFixTestCDI10.class,
	BeanValidationQuickFixTestCDI11.class,
	NullValuesInjectionTestCDI10.class,
	NullValuesInjectionTestCDI11.class,
	DecoratorFromWebBeanTestCDI10.class,
	DecoratorFromWebBeanTestCDI11.class,
	DecoratorValidationQuickFixTestCDI10.class,
	DecoratorValidationQuickFixTestCDI11.class,
	AllAssignableDialogTestCDI10.class,
	AllAssignableDialogTestCDI11.class,
	AssignableDialogFilterTestCDI10.class,		
	AssignableDialogFilterTestCDI11.class,//failed
	IBindingValidationQuickFixTestCDI10.class,
	IBindingValidationQuickFixTestCDI11.class,
	InterceptorValidationQuickFixTestCDI10.class,
	InterceptorValidationQuickFixTestCDI11.class,
	NamedComponentsSearchingTestCDI10.class,
	NamedComponentsSearchingTestCDI11.class, 
	NamedRefactoringTestCDI10.class,
	NamedRefactoringTestCDI11.class,
	BeanInjectOpenOnTestCDI10.class,
	BeanInjectOpenOnTestCDI11.class,
	FindObserverEventTestCDI10.class,
	FindObserverEventTestCDI11.class,
	QualifierValidationQuickFixTestCDI10.class,
	QualifierValidationQuickFixTestCDI11.class,
	ScopeValidationQuickFixTestCDI10.class,
	ScopeValidationQuickFixTestCDI11.class,
	StereotypeValidationQuickFixTestCDI10.class,
	StereotypeValidationQuickFixTestCDI11.class,
	BeansXMLCompletionTestCDI10.class,
	BeansXMLCompletionTestCDI11.class,
	BeansXMLOpenOnTestCDI10.class,
	BeansXMLOpenOnTestCDI11.class,
	BeansXMLBeansEditorTestCDI10.class,
	BeansXMLBeansEditorTestCDI11.class,
	BeansXMLValidationTestCDI10.class,
	BeansXMLValidationTestCDI11.class,
	BeansXMLAsYouTypeValidationTestCDI10.class,
	BeansXMLAsYouTypeValidationTestCDI11.class,
	BeansXMLValidationQuickFixTestCDI10.class,
	BeansXMLValidationQuickFixTestCDI11.class,
	CDIValidatorTestCDI10.class,
	CDIValidatorTestCDI11.class,
	WeldBuiltInContextsTestCDI10.class,
	WeldBuiltInContextsTestCDI11.class,
	WeldExcludeTestCDI10.class,
	WeldExcludeTestCDI11.class,
	WeldParametersAnnotationTestCDI10.class,
	WeldParametersAnnotationTestCDI11.class,
	WeldScanTestCDI10.class,
	WeldScanTestCDI11.class,
	CDIWebProjectWizardTestCDI10.class,
	CDIWebProjectWizardTestCDI11.class,
	DynamicWebProjectWithCDITestCDI10.class,
	DynamicWebProjectWithCDITestCDI11.class,
	EjbProjectWithCDITestCDI10.class,
	EjbProjectWithCDITestCDI11.class,
	UtilityProjectWithCDITestCDI10.class,
	UtilityProjectWithCDITestCDI11.class,

	
	
	BeansXMLUITestCDI11.class,
	BeansXMLDiscoveryModesTestCDI11.class,
	ExcludeBeanTestCDI11.class,
	VetoedAnnotationTestCDI11.class,
	
})
public class CDIAllBotTests {
		
}
