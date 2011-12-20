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
import org.jboss.tools.ws.ui.bot.test.rest.explorer.test.RESTfulExplorerValidationTest;
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
 * System properties:
 *  -Dswtbot.test.properties.file=$PATH
 *  -Dusage_reporting_enabled=$BOOLEAN
 *  
 *  Format of swtbot.properties file:
 *  SERVER=EAP|JBOSS_AS,<server version>,<jre version to run with>|default,<server home>
 *  
 *  Sample swtbot.properties file:
 *
 *  SERVER=JBOSS_AS,6.0,default,/home/lukas/latest/jboss-6.0.0.Final
 *  JAVA=1.6,/space/java/sdk/jdk1.6.0_22
 *  
 *  
 *  Suite duration: aprox. 13min
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
	RESTfulExplorerValidationTest.class, 
	BottomUpWSTest.class,
	TopDownWSTest.class,
	WsClientTest.class,
	WsTesterTest.class,
	EAPFromJavaTest.class,
	EAPFromWSDLTest.class
	})
public class WSAllBotTests {
	
}
