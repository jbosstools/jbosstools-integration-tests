/*******************************************************************************
 * Copyright (c) 2010-2020 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi12.AsYouTypeValidationTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi12.BeanValidationQuickFixTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi12.NullValuesInjectionTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi12.VetoedAnnotationTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.decorator.cdi12.DecoratorFromWebBeanTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.decorator.cdi12.DecoratorValidationQuickFixTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.dialog.cdi12.AllAssignableDialogTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.dialog.cdi12.AssignableDialogFilterTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.ibinding.cdi12.IBindingValidationQuickFixTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.interceptor.cdi12.InterceptorValidationQuickFixTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.named.cdi12.NamedComponentsSearchingTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.named.cdi12.NamedRefactoringTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.openon.cdi12.BeanInjectOpenOnTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.openon.cdi12.FindObserverEventTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.qualifier.cdi12.QualifierValidationQuickFixTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.scope.cdi12.ScopeValidationQuickFixTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.stereotype.cdi12.StereotypeValidationQuickFixTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.annotation.cdi12.BeanParametersAnnotationTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.bean.cdi12.ExcludeBeanTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.cdi12.BeansXMLBeansEditorTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.cdi12.BeansXMLDiscoveryModesTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.cdi12.BeansXMLUITestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.cdi12.BeansXMLValidationTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.completion.cdi12.BeansXMLCompletionTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.discovery.cdi12.BeanDiscoveryInExplicitArchivesTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.discovery.cdi12.BeanDiscoveryInImplicitArchivesTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.openon.cdi12.BeansXMLOpenOnTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi12.BeansXMLAsYouTypeValidationTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi12.BeansXMLValidationQuickFixTestCDI12;
import org.jboss.tools.cdi.bot.test.microprofiile.cdi12.ConfigPropertyTestCDI12;
import org.jboss.tools.cdi.bot.test.validation.cdi12.CDIValidatorTestCDI12;
import org.jboss.tools.cdi.bot.test.weld.cdi12.WeldBuiltInContextsTestCDI12;
import org.jboss.tools.cdi.bot.test.weld.cdi12.WeldExcludeTestCDI12;
import org.jboss.tools.cdi.bot.test.weld.cdi12.WeldParametersAnnotationTestCDI12;
import org.jboss.tools.cdi.bot.test.weld.cdi12.WeldScanTestCDI12;
import org.jboss.tools.cdi.bot.test.wizard.cdi12.CDIWebProjectWizardTestCDI12;
import org.jboss.tools.cdi.bot.test.wizard.cdi12.DynamicWebProjectWithCDITestCDI12;
import org.jboss.tools.cdi.bot.test.wizard.cdi12.EjbProjectWithCDITestCDI12;
import org.jboss.tools.cdi.bot.test.wizard.cdi12.UtilityProjectWithCDITestCDI12;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({	
	AsYouTypeValidationTestCDI12.class,
	BeanValidationQuickFixTestCDI12.class,
	NullValuesInjectionTestCDI12.class,
	DecoratorFromWebBeanTestCDI12.class,
	DecoratorValidationQuickFixTestCDI12.class,
	AllAssignableDialogTestCDI12.class,
	AssignableDialogFilterTestCDI12.class,
	IBindingValidationQuickFixTestCDI12.class,
	InterceptorValidationQuickFixTestCDI12.class,
	NamedComponentsSearchingTestCDI12.class, 
	NamedRefactoringTestCDI12.class,
	BeanInjectOpenOnTestCDI12.class,
	FindObserverEventTestCDI12.class,
	QualifierValidationQuickFixTestCDI12.class,
	ScopeValidationQuickFixTestCDI12.class,
	StereotypeValidationQuickFixTestCDI12.class,
	BeansXMLCompletionTestCDI12.class,
	BeansXMLOpenOnTestCDI12.class,
	BeansXMLBeansEditorTestCDI12.class,
	BeansXMLValidationTestCDI12.class,
	BeansXMLAsYouTypeValidationTestCDI12.class,
	BeansXMLValidationQuickFixTestCDI12.class,
	CDIValidatorTestCDI12.class,
	WeldBuiltInContextsTestCDI12.class,
	WeldExcludeTestCDI12.class,
	WeldParametersAnnotationTestCDI12.class,
	WeldScanTestCDI12.class,
	CDIWebProjectWizardTestCDI12.class,
	DynamicWebProjectWithCDITestCDI12.class,
	EjbProjectWithCDITestCDI12.class,
	UtilityProjectWithCDITestCDI12.class,
	BeansXMLUITestCDI12.class,
	BeansXMLDiscoveryModesTestCDI12.class,
	ExcludeBeanTestCDI12.class,
	VetoedAnnotationTestCDI12.class,
	BeanDiscoveryInExplicitArchivesTestCDI12.class,
	BeanDiscoveryInImplicitArchivesTestCDI12.class,
	BeanParametersAnnotationTestCDI12.class,
	ConfigPropertyTestCDI12.class
})
public class CDI12SuiteTest {

}
