 /*******************************************************************************
  * Copyright (c) 2007-2014 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.ws.ui.bot.test.annotation.AnnotationPropertiesTest;
import org.jboss.tools.ws.ui.bot.test.annotation.HTTPMethodAnnotationQuickFixTest;
import org.jboss.tools.ws.ui.bot.test.cxf.CxfWsClientTest;
import org.jboss.tools.ws.ui.bot.test.facet.JAXRSFacetTest;
import org.jboss.tools.ws.ui.bot.test.integration.JAXRSToolingIntegrationTest;
import org.jboss.tools.ws.ui.bot.test.integration.SOAPWSToolingIntegrationTest;
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
import org.jboss.tools.ws.ui.bot.test.sample.test.SampleSoapWebServiceTest;
import org.jboss.tools.ws.ui.bot.test.sample.test.SimpleWebServiceTest;
import org.jboss.tools.ws.ui.bot.test.webservice.BottomUpWSTest;
import org.jboss.tools.ws.ui.bot.test.webservice.TopDownWSTest;
import org.jboss.tools.ws.ui.bot.test.webservice.eap.EAPFromJavaTest;
import org.jboss.tools.ws.ui.bot.test.webservice.eap.EAPFromWSDLTest;
import org.jboss.tools.ws.ui.bot.test.wsclient.WsClientTest;
import org.jboss.tools.ws.ui.bot.test.wstester.WSTesterPromptValuesSupportTest;
import org.jboss.tools.ws.ui.bot.test.wstester.WsTesterTest;
import org.jboss.tools.ws.ui.bot.test.wstester.XmlJsonFormattingTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * WebServices All UI Bot Tests<br/><br/>
 * 
 * @author Lukas Jungmann
 * @author jjankovi
 * @author Radoslav Rabara
 * 
 */
@RunWith(RedDeerSuite.class)
@SuiteClasses({
	JBossWSPreferencesTest.class,
	
	// JAX-RS webservices
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
	BeanParamAnnotationSupportTest.class,
	RESTfulValidationTest.class,
	RESTfulCompletionTest.class,
	AsYouTypeValidationTest.class,
	ValidatingRelatedRSElementsTest.class,
	JAXRSToolingIntegrationTest.class,
	XmlJsonFormattingTest.class,
	WSTesterPromptValuesSupportTest.class,
	ApplicationValidationTest.class,
	JaxRsValidatorTest.class,
	HTTPMethodAnnotationQuickFixTest.class,
	ParamConverterSupportTest.class,
	FiltersInterceptorsSupportTest.class,
	NameBindingAnnotationSupportTest.class,
	PreMatchingAnnotationSupportTest.class,
	
	// SOAP webservices
	SampleSoapWebServiceTest.class,
	SimpleWebServiceTest.class,
	SOAPWSToolingIntegrationTest.class,
	BottomUpWSTest.class,
	TopDownWSTest.class, 
	WsClientTest.class, 
	WsTesterTest.class, 
	EAPFromJavaTest.class,
	EAPFromWSDLTest.class,
	
	CxfWsClientTest.class
	})
public class WSAllBotTests extends AbstractTestSuite {
	
}
