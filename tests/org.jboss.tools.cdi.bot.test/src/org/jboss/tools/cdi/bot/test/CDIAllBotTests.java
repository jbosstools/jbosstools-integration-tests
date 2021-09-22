/*******************************************************************************
 * Copyright (c) 2010-2020 Red Hat, Inc.
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
import org.jboss.tools.cdi.bot.test.beans.bean.cdi12.AsYouTypeValidationTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi12.BeanValidationQuickFixTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi12.NullValuesInjectionTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi12.VetoedAnnotationTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi20.AsYouTypeValidationTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi20.BeanValidationQuickFixTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi20.NullValuesInjectionTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi20.VetoedAnnotationTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.decorator.cdi10.DecoratorFromWebBeanTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.decorator.cdi10.DecoratorValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.decorator.cdi12.DecoratorFromWebBeanTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.decorator.cdi12.DecoratorValidationQuickFixTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.decorator.cdi20.DecoratorFromWebBeanTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.decorator.cdi20.DecoratorValidationQuickFixTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.dialog.cdi10.AllAssignableDialogTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.dialog.cdi10.AssignableDialogFilterTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.dialog.cdi12.AllAssignableDialogTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.dialog.cdi12.AssignableDialogFilterTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.dialog.cdi20.AllAssignableDialogTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.dialog.cdi20.AssignableDialogFilterTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.ibinding.cdi10.IBindingValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.ibinding.cdi12.IBindingValidationQuickFixTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.ibinding.cdi20.IBindingValidationQuickFixTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.interceptor.cdi10.InterceptorValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.interceptor.cdi12.InterceptorValidationQuickFixTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.interceptor.cdi20.InterceptorValidationQuickFixTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.named.cdi10.NamedComponentsSearchingTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.named.cdi10.NamedRefactoringTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.named.cdi12.NamedComponentsSearchingTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.named.cdi12.NamedRefactoringTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.named.cdi20.NamedComponentsSearchingTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.named.cdi20.NamedRefactoringTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.openon.cd10.BeanInjectOpenOnTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.openon.cd10.FindObserverEventTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.openon.cdi12.BeanInjectOpenOnTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.openon.cdi12.FindObserverEventTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.openon.cdi20.BeanInjectOpenOnTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.openon.cdi20.FindObserverEventTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.qualifier.cdi10.QualifierValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.qualifier.cdi12.QualifierValidationQuickFixTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.qualifier.cdi20.QualifierValidationQuickFixTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.scope.cdi10.ScopeValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.scope.cdi12.ScopeValidationQuickFixTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.scope.cdi20.ScopeValidationQuickFixTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.stereotype.cdi10.StereotypeValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.stereotype.cdi12.StereotypeValidationQuickFixTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.stereotype.cdi20.StereotypeValidationQuickFixTestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.annotation.cdi12.BeanParametersAnnotationTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.annotation.cdi20.BeanParametersAnnotationTestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.bean.cdi12.ExcludeBeanTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.bean.cdi20.ExcludeBeanTestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.cdi10.BeansXMLBeansEditorTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.cdi10.BeansXMLValidationTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.cdi12.BeansXMLBeansEditorTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.cdi12.BeansXMLDiscoveryModesTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.cdi12.BeansXMLUITestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.cdi12.BeansXMLValidationTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.cdi20.BeansXMLBeansEditorTestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.cdi20.BeansXMLDiscoveryModesTestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.cdi20.BeansXMLUITestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.cdi20.BeansXMLValidationTestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.completion.cdi10.BeansXMLCompletionTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.completion.cdi12.BeansXMLCompletionTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.completion.cdi20.BeansXMLCompletionTestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.discovery.cdi12.BeanDiscoveryInExplicitArchivesTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.discovery.cdi12.BeanDiscoveryInImplicitArchivesTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.discovery.cdi20.BeanDiscoveryInExplicitArchivesTestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.discovery.cdi20.BeanDiscoveryInImplicitArchivesTestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.openon.cdi10.BeansXMLOpenOnTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.openon.cdi12.BeansXMLOpenOnTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.openon.cdi20.BeansXMLOpenOnTestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi10.BeansXMLAsYouTypeValidationTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi10.BeansXMLValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi12.BeansXMLAsYouTypeValidationTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi12.BeansXMLValidationQuickFixTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi20.BeansXMLAsYouTypeValidationTestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi20.BeansXMLValidationQuickFixTestCDI20;
import org.jboss.tools.cdi.bot.test.microprofiile.cdi12.ConfigPropertyTestCDI12;
import org.jboss.tools.cdi.bot.test.microprofiile.cdi20.ConfigPropertyTestCDI20;
import org.jboss.tools.cdi.bot.test.validation.cdi10.CDIValidatorTestCDI10;
import org.jboss.tools.cdi.bot.test.validation.cdi12.CDIValidatorTestCDI12;
import org.jboss.tools.cdi.bot.test.validation.cdi20.CDIValidatorTestCDI20;
import org.jboss.tools.cdi.bot.test.weld.cdi10.WeldBuiltInContextsTestCDI10;
import org.jboss.tools.cdi.bot.test.weld.cdi10.WeldExcludeTestCDI10;
import org.jboss.tools.cdi.bot.test.weld.cdi10.WeldParametersAnnotationTestCDI10;
import org.jboss.tools.cdi.bot.test.weld.cdi10.WeldScanTestCDI10;
import org.jboss.tools.cdi.bot.test.weld.cdi12.WeldBuiltInContextsTestCDI12;
import org.jboss.tools.cdi.bot.test.weld.cdi12.WeldExcludeTestCDI12;
import org.jboss.tools.cdi.bot.test.weld.cdi12.WeldParametersAnnotationTestCDI12;
import org.jboss.tools.cdi.bot.test.weld.cdi12.WeldScanTestCDI12;
import org.jboss.tools.cdi.bot.test.weld.cdi20.WeldBuiltInContextsTestCDI20;
import org.jboss.tools.cdi.bot.test.weld.cdi20.WeldExcludeTestCDI20;
import org.jboss.tools.cdi.bot.test.weld.cdi20.WeldParametersAnnotationTestCDI20;
import org.jboss.tools.cdi.bot.test.weld.cdi20.WeldScanTestCDI20;
import org.jboss.tools.cdi.bot.test.wizard.cdi10.CDIWebProjectWizardTestCDI10;
import org.jboss.tools.cdi.bot.test.wizard.cdi10.DynamicWebProjectWithCDITestCDI10;
import org.jboss.tools.cdi.bot.test.wizard.cdi10.EjbProjectWithCDITestCDI10;
import org.jboss.tools.cdi.bot.test.wizard.cdi10.UtilityProjectWithCDITestCDI10;
import org.jboss.tools.cdi.bot.test.wizard.cdi12.CDIWebProjectWizardTestCDI12;
import org.jboss.tools.cdi.bot.test.wizard.cdi12.DynamicWebProjectWithCDITestCDI12;
import org.jboss.tools.cdi.bot.test.wizard.cdi12.EjbProjectWithCDITestCDI12;
import org.jboss.tools.cdi.bot.test.wizard.cdi12.UtilityProjectWithCDITestCDI12;
import org.jboss.tools.cdi.bot.test.wizard.cdi20.CDIWebProjectWizardTestCDI20;
import org.jboss.tools.cdi.bot.test.wizard.cdi20.DynamicWebProjectWithCDITestCDI20;
import org.jboss.tools.cdi.bot.test.wizard.cdi20.EjbProjectWithCDITestCDI20;
import org.jboss.tools.cdi.bot.test.wizard.cdi20.UtilityProjectWithCDITestCDI20;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author rawagner
 * @author Lukas Jungmann
 * @author Jaroslav Jankovic
 * @author odockal
 * @author zcervink
 */
@RunWith(RedDeerSuite.class)
@SuiteClasses({	
	
	AsYouTypeValidationTestCDI10.class,
	AsYouTypeValidationTestCDI12.class,
	AsYouTypeValidationTestCDI20.class,
	BeanValidationQuickFixTestCDI10.class,
	BeanValidationQuickFixTestCDI12.class,
	BeanValidationQuickFixTestCDI20.class,
	NullValuesInjectionTestCDI10.class,
	NullValuesInjectionTestCDI12.class,
	NullValuesInjectionTestCDI20.class,
	DecoratorFromWebBeanTestCDI10.class,
	DecoratorFromWebBeanTestCDI12.class,
	DecoratorFromWebBeanTestCDI20.class,
	DecoratorValidationQuickFixTestCDI10.class,
	DecoratorValidationQuickFixTestCDI12.class,
	DecoratorValidationQuickFixTestCDI20.class,
	AllAssignableDialogTestCDI10.class,
	AllAssignableDialogTestCDI12.class,
	AllAssignableDialogTestCDI20.class,
	AssignableDialogFilterTestCDI10.class,		
	AssignableDialogFilterTestCDI12.class,//failed
	AssignableDialogFilterTestCDI20.class,
	IBindingValidationQuickFixTestCDI10.class,
	IBindingValidationQuickFixTestCDI12.class,
	IBindingValidationQuickFixTestCDI20.class,
	InterceptorValidationQuickFixTestCDI10.class,
	InterceptorValidationQuickFixTestCDI12.class,
	InterceptorValidationQuickFixTestCDI20.class,
	NamedComponentsSearchingTestCDI10.class,
	NamedComponentsSearchingTestCDI12.class,
	NamedComponentsSearchingTestCDI20.class, 
	NamedRefactoringTestCDI10.class,
	NamedRefactoringTestCDI12.class,
	NamedRefactoringTestCDI20.class,
	BeanInjectOpenOnTestCDI10.class,
	BeanInjectOpenOnTestCDI12.class,
	BeanInjectOpenOnTestCDI20.class,
	FindObserverEventTestCDI10.class,
	FindObserverEventTestCDI12.class,
	FindObserverEventTestCDI20.class,
	QualifierValidationQuickFixTestCDI10.class,
	QualifierValidationQuickFixTestCDI12.class,
	QualifierValidationQuickFixTestCDI20.class,
	ScopeValidationQuickFixTestCDI10.class,
	ScopeValidationQuickFixTestCDI12.class,
	ScopeValidationQuickFixTestCDI20.class,
	StereotypeValidationQuickFixTestCDI10.class,
	StereotypeValidationQuickFixTestCDI12.class,
	StereotypeValidationQuickFixTestCDI20.class,
	BeansXMLCompletionTestCDI10.class,
	BeansXMLCompletionTestCDI12.class,
	BeansXMLCompletionTestCDI20.class,
	BeansXMLOpenOnTestCDI10.class,
	BeansXMLOpenOnTestCDI12.class,
	BeansXMLOpenOnTestCDI20.class,
	BeansXMLBeansEditorTestCDI10.class,
	BeansXMLBeansEditorTestCDI12.class,
	BeansXMLBeansEditorTestCDI20.class,
	BeansXMLValidationTestCDI10.class,
	BeansXMLValidationTestCDI12.class,
	BeansXMLValidationTestCDI20.class,
	BeansXMLAsYouTypeValidationTestCDI10.class,
	BeansXMLAsYouTypeValidationTestCDI12.class,
	BeansXMLAsYouTypeValidationTestCDI20.class,
	BeansXMLValidationQuickFixTestCDI10.class,
	BeansXMLValidationQuickFixTestCDI12.class,
	BeansXMLValidationQuickFixTestCDI20.class,
	CDIValidatorTestCDI10.class,
	CDIValidatorTestCDI12.class,
	CDIValidatorTestCDI20.class,
	WeldBuiltInContextsTestCDI10.class,
	WeldBuiltInContextsTestCDI12.class,
	WeldBuiltInContextsTestCDI20.class,
	WeldExcludeTestCDI10.class,
	WeldExcludeTestCDI12.class,
	WeldExcludeTestCDI20.class,
	WeldParametersAnnotationTestCDI10.class,
	WeldParametersAnnotationTestCDI12.class,
	WeldParametersAnnotationTestCDI20.class,
	WeldScanTestCDI10.class,
	WeldScanTestCDI12.class,
	WeldScanTestCDI20.class,
	CDIWebProjectWizardTestCDI10.class,
	CDIWebProjectWizardTestCDI12.class,
	CDIWebProjectWizardTestCDI20.class,
	DynamicWebProjectWithCDITestCDI10.class,
	DynamicWebProjectWithCDITestCDI12.class,
	DynamicWebProjectWithCDITestCDI20.class,
	EjbProjectWithCDITestCDI10.class,
	EjbProjectWithCDITestCDI12.class,
	EjbProjectWithCDITestCDI20.class,
	UtilityProjectWithCDITestCDI10.class,
	UtilityProjectWithCDITestCDI12.class,
	UtilityProjectWithCDITestCDI20.class,
	
	BeansXMLUITestCDI12.class,
	BeansXMLUITestCDI20.class,
	BeansXMLDiscoveryModesTestCDI12.class,
	BeansXMLDiscoveryModesTestCDI20.class,
	ExcludeBeanTestCDI12.class,
	ExcludeBeanTestCDI20.class,
	VetoedAnnotationTestCDI12.class,
	VetoedAnnotationTestCDI20.class,
	BeanDiscoveryInImplicitArchivesTestCDI12.class,
	BeanDiscoveryInImplicitArchivesTestCDI20.class,
	BeanDiscoveryInExplicitArchivesTestCDI12.class,
	BeanDiscoveryInExplicitArchivesTestCDI20.class,
	BeanParametersAnnotationTestCDI12.class,
	BeanParametersAnnotationTestCDI20.class,
	ConfigPropertyTestCDI12.class,
	ConfigPropertyTestCDI20.class,
	
})
public class CDIAllBotTests {
		
}
