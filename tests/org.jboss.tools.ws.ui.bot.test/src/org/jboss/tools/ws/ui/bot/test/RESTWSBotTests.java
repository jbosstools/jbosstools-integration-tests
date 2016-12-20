package org.jboss.tools.ws.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.ws.ui.bot.test.annotation.AnnotationPropertiesTest;
import org.jboss.tools.ws.ui.bot.test.annotation.HTTPMethodAnnotationQuickFixTest;
import org.jboss.tools.ws.ui.bot.test.facet.JAXRSFacetTest;
import org.jboss.tools.ws.ui.bot.test.integration.JAXRSToolingIntegrationTest;
import org.jboss.tools.ws.ui.bot.test.preferences.JBossWSPreferencesTest;
import org.jboss.tools.ws.ui.bot.test.rest.AsYouTypeValidationTest;
import org.jboss.tools.ws.ui.bot.test.rest.CreateJAXRSApplicationTest;
import org.jboss.tools.ws.ui.bot.test.rest.CreateJAXRSResourceTest;
import org.jboss.tools.ws.ui.bot.test.rest.FiltersInterceptorsSupportTest;
import org.jboss.tools.ws.ui.bot.test.rest.NameBindingAnnotationSupportTest;
import org.jboss.tools.ws.ui.bot.test.rest.PreMatchingAnnotationSupportTest;
import org.jboss.tools.ws.ui.bot.test.rest.ValidatingRelatedRSElementsTest;
import org.jboss.tools.ws.ui.bot.test.rest.completion.RESTfulCompletionTest;
import org.jboss.tools.ws.ui.bot.test.rest.explorer.RESTfulExplorerTest;
import org.jboss.tools.ws.ui.bot.test.rest.explorer.RESTfulSupportTest;
import org.jboss.tools.ws.ui.bot.test.rest.param.BeanParamAnnotationSupportTest;
import org.jboss.tools.ws.ui.bot.test.rest.param.DefaultValueAnnotationSupportTest;
import org.jboss.tools.ws.ui.bot.test.rest.param.MatrixParamAnnotationSupportTest;
import org.jboss.tools.ws.ui.bot.test.rest.param.ParamConverterSupportTest;
import org.jboss.tools.ws.ui.bot.test.rest.param.PathParamAnnotationSupportTest;
import org.jboss.tools.ws.ui.bot.test.rest.param.QueryParamAnnotationSupportTest;
import org.jboss.tools.ws.ui.bot.test.rest.validation.ApplicationValidationTest;
import org.jboss.tools.ws.ui.bot.test.rest.validation.JaxRsValidatorTest;
import org.jboss.tools.ws.ui.bot.test.rest.validation.RESTfulValidationTest;
import org.jboss.tools.ws.ui.bot.test.wstester.WSTesterPromptValuesSupportTest;
import org.jboss.tools.ws.ui.bot.test.wstester.XmlJsonFormattingTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({
	JBossWSPreferencesTest.class,
	JAXRSFacetTest.class,
	CreateJAXRSApplicationTest.class,
	CreateJAXRSResourceTest.class,
	AnnotationPropertiesTest.class,
	RESTfulSupportTest.class,
	RESTfulExplorerTest.class, 
	PathParamAnnotationSupportTest.class,
	QueryParamAnnotationSupportTest.class,
	MatrixParamAnnotationSupportTest.class,
	DefaultValueAnnotationSupportTest.class,	
	
	//Automatic project build gets stuck, build projects manually
	BeanParamAnnotationSupportTest.class,
	RESTfulValidationTest.class,
	HTTPMethodAnnotationQuickFixTest.class,
	FiltersInterceptorsSupportTest.class,
	NameBindingAnnotationSupportTest.class,
	ValidatingRelatedRSElementsTest.class,
	ApplicationValidationTest.class,
	JaxRsValidatorTest.class,
	ParamConverterSupportTest.class,
	PreMatchingAnnotationSupportTest.class,
	
	RESTfulCompletionTest.class,
	AsYouTypeValidationTest.class,
	WSTesterPromptValuesSupportTest.class,
	XmlJsonFormattingTest.class,
	JAXRSToolingIntegrationTest.class,
})
public class RESTWSBotTests {

}
