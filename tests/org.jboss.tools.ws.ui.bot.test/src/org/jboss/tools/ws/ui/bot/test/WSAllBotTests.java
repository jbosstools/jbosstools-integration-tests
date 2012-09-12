 /*******************************************************************************
  * Copyright (c) 2007-2011 Red Hat, Inc.
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
import org.jboss.tools.ws.ui.bot.test.integration.JAXRSToolingIntegrationTest;
import org.jboss.tools.ws.ui.bot.test.integration.SOAPWSToolingIntegrationTest;
import org.jboss.tools.ws.ui.bot.test.rest.DefaultValueAnnotationSupportTest;
import org.jboss.tools.ws.ui.bot.test.rest.MatrixAnnotationSupportTest;
import org.jboss.tools.ws.ui.bot.test.rest.PathAnnotationSupportTest;
import org.jboss.tools.ws.ui.bot.test.rest.QueryAnnotationSupportTest;
import org.jboss.tools.ws.ui.bot.test.rest.completion.RESTfulCompletionTest;
import org.jboss.tools.ws.ui.bot.test.rest.explorer.RESTfulExplorerTest;
import org.jboss.tools.ws.ui.bot.test.rest.validation.ApplicationPathAnnotationTest;
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
import org.jboss.tools.ws.ui.bot.test.wstester.WsTesterTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 
 * @author Lukas Jungmann
 * @author jjankovi
 */
@RunWith(RequirementAwareSuite.class)
@SuiteClasses({
	SampleSoapWebServiceTest.class,
	SampleRESTWebServiceTest.class,
	SimpleSoapWebServiceTest.class,
	SimpleRESTWebServiceTest.class,
//	AnnotationPropertiesTest.class, // not implemented yet
	RESTfulExplorerTest.class, 
	PathAnnotationSupportTest.class, 
	QueryAnnotationSupportTest.class,
	MatrixAnnotationSupportTest.class,
	DefaultValueAnnotationSupportTest.class,
	RESTfulValidationTest.class,
	RESTfulCompletionTest.class,
	JAXRSToolingIntegrationTest.class,
	SOAPWSToolingIntegrationTest.class,
//	WSTesterPromptValuesSupportTest.class, // not implemented yet
	ApplicationPathAnnotationTest.class,
	BottomUpWSTest.class,
	TopDownWSTest.class,
	WsClientTest.class,
	WsTesterTest.class,
	EAPFromJavaTest.class,
	EAPFromWSDLTest.class
	})
public class WSAllBotTests extends AbstractTestSuite {
	
}
