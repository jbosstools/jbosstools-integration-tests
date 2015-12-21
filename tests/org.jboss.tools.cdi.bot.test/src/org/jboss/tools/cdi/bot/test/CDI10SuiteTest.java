package org.jboss.tools.cdi.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
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
