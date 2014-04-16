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

import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.jboss.tools.ws.ui.bot.test.annotation.AnnotationPropertiesTest;
import org.jboss.tools.ws.ui.bot.test.annotation.HTTPMethodAnnotationQuickFixTest;
import org.jboss.tools.ws.ui.bot.test.cxf.CxfWsClientTest;
import org.jboss.tools.ws.ui.bot.test.facet.JAXRSFacetTest;
import org.jboss.tools.ws.ui.bot.test.integration.JAXRSToolingIntegrationTest;
import org.jboss.tools.ws.ui.bot.test.integration.SOAPWSToolingIntegrationTest;
import org.jboss.tools.ws.ui.bot.test.preferences.JBossWSPreferencesTest;
import org.jboss.tools.ws.ui.bot.test.rest.DefaultValueAnnotationSupportTest;
import org.jboss.tools.ws.ui.bot.test.rest.MatrixParamAnnotationSupportTest;
import org.jboss.tools.ws.ui.bot.test.rest.PathParamAnnotationSupportTest;
import org.jboss.tools.ws.ui.bot.test.rest.QueryParamAnnotationSupportTest;
import org.jboss.tools.ws.ui.bot.test.rest.completion.RESTfulCompletionTest;
import org.jboss.tools.ws.ui.bot.test.rest.explorer.RESTfulExplorerTest;
import org.jboss.tools.ws.ui.bot.test.rest.explorer.RESTfulSupportTest;
import org.jboss.tools.ws.ui.bot.test.rest.validation.ApplicationValidationTest;
import org.jboss.tools.ws.ui.bot.test.rest.validation.JaxRsValidatorTest;
import org.jboss.tools.ws.ui.bot.test.rest.validation.RESTfulValidationTest;
import org.jboss.tools.ws.ui.bot.test.sample.test.SampleRESTWebServiceTest;
import org.jboss.tools.ws.ui.bot.test.sample.test.SampleSoapWebServiceTest;
import org.jboss.tools.ws.ui.bot.test.sample.test.SimpleRESTWebServiceTest;
import org.jboss.tools.ws.ui.bot.test.sample.test.SimpleSoapWebServiceTest;
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
 * 
 * @author Lukas Jungmann
 * @author jjankovi
 * @author Radoslav Rabara
 */
@RunWith(RequirementAwareSuite.class)
@SuiteClasses({
	JBossWSPreferencesTest.class,
	JAXRSFacetTest.class,
	SampleSoapWebServiceTest.class,
	SampleRESTWebServiceTest.class,
	SimpleSoapWebServiceTest.class,
	SimpleRESTWebServiceTest.class,
	AnnotationPropertiesTest.class,
	RESTfulSupportTest.class,
	RESTfulExplorerTest.class,
	PathParamAnnotationSupportTest.class,
	QueryParamAnnotationSupportTest.class,
	MatrixParamAnnotationSupportTest.class,
	DefaultValueAnnotationSupportTest.class,
	RESTfulValidationTest.class,
	RESTfulCompletionTest.class,
	JAXRSToolingIntegrationTest.class,
	SOAPWSToolingIntegrationTest.class,
	XmlJsonFormattingTest.class,
	WSTesterPromptValuesSupportTest.class,
	ApplicationValidationTest.class,
	JaxRsValidatorTest.class,
	HTTPMethodAnnotationQuickFixTest.class,
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
