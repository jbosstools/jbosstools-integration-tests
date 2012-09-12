 /*******************************************************************************
  * Copyright (c) 2007-2009 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.esb.ui.bot.tests;
 
import org.jboss.tools.esb.ui.bot.tests.examples.HelloWorld;
import org.jboss.tools.esb.ui.bot.tests.examples.HelloWorldAction;
import org.jboss.tools.esb.ui.bot.tests.examples.HelloWorldFileAction;
import org.jboss.tools.esb.ui.bot.tests.examples.SmooksCSV2XML;
import org.jboss.tools.esb.ui.bot.tests.examples.SmooksXML2POJO;
import org.jboss.tools.esb.ui.bot.tests.examples.SmooksXML2XMLSimple;
import org.jboss.tools.esb.ui.bot.tests.examples.SmooksXML2XMLDateManipulation;
import org.jboss.tools.esb.ui.bot.tests.examples.SimpleEAPTest;
import org.jboss.tools.esb.ui.bot.tests.examples.WebServiceConsumer1;
import org.jboss.tools.esb.ui.bot.tests.examples.WebServiceProducer;
import org.jboss.tools.esb.ui.bot.tests.examples.WebServiceProducerHttp;
import org.jboss.tools.esb.ui.bot.tests.examples.WebServiceProducerSocket;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({	
	CreateRuntimeFromESB.class,
	CreateRuntimeFromSOA.class,
	NewProjectUsingRuntime.class,
	NewProjectUsingBundledInEAP.class,
	Editing.class,
	HelloWorld.class,	
	HelloWorldAction.class,
	HelloWorldFileAction.class,
	SmooksCSV2XML.class,
	SmooksXML2POJO.class,
	SmooksXML2XMLDateManipulation.class,
	SmooksXML2XMLSimple.class,
	WebServiceConsumer1.class,
	WebServiceProducer.class,  // http://lists.jboss.org/pipermail/jbosstools-dev/2008-December/002559.html
	WebServiceProducerHttp.class,
	WebServiceProducerSocket.class
	//SimpleEAPTest.class
	})
@RunWith(RequirementAwareSuite.class)
public class ESBAllBotTests {

}
