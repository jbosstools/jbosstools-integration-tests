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
import org.jboss.tools.ws.ui.bot.test.jbt.SampleWSTest;
import org.jboss.tools.ws.ui.bot.test.jbt.WsTesterTest;
import org.jboss.tools.ws.ui.bot.test.wtp.BottomUpWSTest;
import org.jboss.tools.ws.ui.bot.test.wtp.TopDownWSTest;
import org.jboss.tools.ws.ui.bot.test.wtp.WsClientTest;
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
	SampleWSTest.class,
	BottomUpWSTest.class,
	TopDownWSTest.class,
	WsClientTest.class,
	WsTesterTest.class
	})
public class WSAllBotTests {
}
