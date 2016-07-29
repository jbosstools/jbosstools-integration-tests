package org.jboss.tools.cdi.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi10.AsYouTypeValidationTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi10.BeanValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi10.NullValuesInjectionTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.decorator.cdi10.DecoratorFromWebBeanTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.decorator.cdi10.DecoratorValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.dialog.cdi10.AllAssignableDialogTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.dialog.cdi10.AssignableDialogFilterTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.ibinding.cdi10.IBindingValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.interceptor.cdi10.InterceptorValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.named.cdi10.NamedComponentsSearchingTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.named.cdi10.NamedRefactoringTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.openon.cd10.BeanInjectOpenOnTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.openon.cd10.FindObserverEventTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.qualifier.cdi10.QualifierValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.scope.cdi10.ScopeValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.stereotype.cdi10.StereotypeValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.cdi10.BeansXMLBeansEditorTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.cdi10.BeansXMLValidationTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.completion.cdi10.BeansXMLCompletionTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.openon.cdi10.BeansXMLOpenOnTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi10.BeansXMLAsYouTypeValidationTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi10.BeansXMLValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.validation.cdi10.CDIValidatorTestCDI10;
import org.jboss.tools.cdi.bot.test.weld.cdi10.WeldBuiltInContextsTestCDI10;
import org.jboss.tools.cdi.bot.test.weld.cdi10.WeldExcludeTestCDI10;
import org.jboss.tools.cdi.bot.test.weld.cdi10.WeldParametersAnnotationTestCDI10;
import org.jboss.tools.cdi.bot.test.weld.cdi10.WeldScanTestCDI10;
import org.jboss.tools.cdi.bot.test.wizard.cdi10.CDIWebProjectWizardTestCDI10;
import org.jboss.tools.cdi.bot.test.wizard.cdi10.DynamicWebProjectWithCDITestCDI10;
import org.jboss.tools.cdi.bot.test.wizard.cdi10.EjbProjectWithCDITestCDI10;
import org.jboss.tools.cdi.bot.test.wizard.cdi10.UtilityProjectWithCDITestCDI10;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({	
	AsYouTypeValidationTestCDI10.class,
	BeanValidationQuickFixTestCDI10.class,
	NullValuesInjectionTestCDI10.class,
	DecoratorFromWebBeanTestCDI10.class,
	DecoratorValidationQuickFixTestCDI10.class,
	AllAssignableDialogTestCDI10.class,
	AssignableDialogFilterTestCDI10.class,
	IBindingValidationQuickFixTestCDI10.class,
	InterceptorValidationQuickFixTestCDI10.class,
	NamedComponentsSearchingTestCDI10.class,
	NamedRefactoringTestCDI10.class,
	BeanInjectOpenOnTestCDI10.class,
	FindObserverEventTestCDI10.class,
	QualifierValidationQuickFixTestCDI10.class,
	ScopeValidationQuickFixTestCDI10.class,
	StereotypeValidationQuickFixTestCDI10.class,
	BeansXMLCompletionTestCDI10.class,
	BeansXMLOpenOnTestCDI10.class,
	BeansXMLBeansEditorTestCDI10.class,
	BeansXMLValidationTestCDI10.class,
	BeansXMLAsYouTypeValidationTestCDI10.class,
	BeansXMLValidationQuickFixTestCDI10.class,
	CDIValidatorTestCDI10.class,
	WeldBuiltInContextsTestCDI10.class,
	WeldExcludeTestCDI10.class,
	WeldParametersAnnotationTestCDI10.class,
	WeldScanTestCDI10.class,
	CDIWebProjectWizardTestCDI10.class,
	DynamicWebProjectWithCDITestCDI10.class,
	EjbProjectWithCDITestCDI10.class,
	UtilityProjectWithCDITestCDI10.class,
})
public class CDI10SuiteTest {

}
