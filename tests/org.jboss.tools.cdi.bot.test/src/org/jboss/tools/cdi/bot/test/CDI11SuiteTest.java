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
package org.jboss.tools.cdi.bot.test;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi11.AsYouTypeValidationTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi11.BeanValidationQuickFixTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi11.NullValuesInjectionTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi11.VetoedAnnotationTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.decorator.cdi11.DecoratorFromWebBeanTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.decorator.cdi11.DecoratorValidationQuickFixTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.dialog.cdi11.AllAssignableDialogTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.dialog.cdi11.AssignableDialogFilterTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.ibinding.cdi11.IBindingValidationQuickFixTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.interceptor.cdi11.InterceptorValidationQuickFixTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.named.cdi11.NamedComponentsSearchingTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.named.cdi11.NamedRefactoringTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.openon.cdi11.BeanInjectOpenOnTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.openon.cdi11.FindObserverEventTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.qualifier.cdi11.QualifierValidationQuickFixTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.scope.cdi11.ScopeValidationQuickFixTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.stereotype.cdi11.StereotypeValidationQuickFixTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.annotation.cdi11.BeanParametersAnnotationTest;
import org.jboss.tools.cdi.bot.test.beansxml.bean.cdi11.ExcludeBeanTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.cdi11.BeansXMLBeansEditorTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.cdi11.BeansXMLDiscoveryModesTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.cdi11.BeansXMLUITestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.cdi11.BeansXMLValidationTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.completion.cdi11.BeansXMLCompletionTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.discovery.cdi11.BeanDiscoveryInExplicitArchivesTest;
import org.jboss.tools.cdi.bot.test.beansxml.discovery.cdi11.BeanDiscoveryInImplicitArchivesTest;
import org.jboss.tools.cdi.bot.test.beansxml.openon.cdi11.BeansXMLOpenOnTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi11.BeansXMLAsYouTypeValidationTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi11.BeansXMLValidationQuickFixTestCDI11;
import org.jboss.tools.cdi.bot.test.validation.cdi11.CDIValidatorTestCDI11;
import org.jboss.tools.cdi.bot.test.weld.cdi11.WeldBuiltInContextsTestCDI11;
import org.jboss.tools.cdi.bot.test.weld.cdi11.WeldExcludeTestCDI11;
import org.jboss.tools.cdi.bot.test.weld.cdi11.WeldParametersAnnotationTestCDI11;
import org.jboss.tools.cdi.bot.test.weld.cdi11.WeldScanTestCDI11;
import org.jboss.tools.cdi.bot.test.wizard.cdi11.CDIWebProjectWizardTestCDI11;
import org.jboss.tools.cdi.bot.test.wizard.cdi11.DynamicWebProjectWithCDITestCDI11;
import org.jboss.tools.cdi.bot.test.wizard.cdi11.EjbProjectWithCDITestCDI11;
import org.jboss.tools.cdi.bot.test.wizard.cdi11.UtilityProjectWithCDITestCDI11;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({	
	AsYouTypeValidationTestCDI11.class,
	BeanValidationQuickFixTestCDI11.class,
	NullValuesInjectionTestCDI11.class,
	DecoratorFromWebBeanTestCDI11.class,
	DecoratorValidationQuickFixTestCDI11.class,
	AllAssignableDialogTestCDI11.class,
	AssignableDialogFilterTestCDI11.class,
	IBindingValidationQuickFixTestCDI11.class,
	InterceptorValidationQuickFixTestCDI11.class,
	NamedComponentsSearchingTestCDI11.class, 
	NamedRefactoringTestCDI11.class,
	BeanInjectOpenOnTestCDI11.class,
	FindObserverEventTestCDI11.class,
	QualifierValidationQuickFixTestCDI11.class,
	ScopeValidationQuickFixTestCDI11.class,
	StereotypeValidationQuickFixTestCDI11.class,
	BeansXMLCompletionTestCDI11.class,
	BeansXMLOpenOnTestCDI11.class,
	BeansXMLBeansEditorTestCDI11.class,
	BeansXMLValidationTestCDI11.class,
	BeansXMLAsYouTypeValidationTestCDI11.class,
	BeansXMLValidationQuickFixTestCDI11.class,
	CDIValidatorTestCDI11.class,
	WeldBuiltInContextsTestCDI11.class,
	WeldExcludeTestCDI11.class,
	WeldParametersAnnotationTestCDI11.class,
	WeldScanTestCDI11.class,
	CDIWebProjectWizardTestCDI11.class,
	DynamicWebProjectWithCDITestCDI11.class,
	EjbProjectWithCDITestCDI11.class,
	UtilityProjectWithCDITestCDI11.class,
	BeansXMLUITestCDI11.class,
	BeansXMLDiscoveryModesTestCDI11.class,
	ExcludeBeanTestCDI11.class,
	VetoedAnnotationTestCDI11.class,
	BeanDiscoveryInExplicitArchivesTest.class,
	BeanDiscoveryInImplicitArchivesTest.class,
	BeanParametersAnnotationTest.class
})
public class CDI11SuiteTest {

}
