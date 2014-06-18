/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.seam3.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.cdi.seam3.bot.test.tests.DefaultBeansTest;
import org.jboss.tools.cdi.seam3.bot.test.tests.ExactAnnotationTest;
import org.jboss.tools.cdi.seam3.bot.test.tests.FullyQualifiedTest;
import org.jboss.tools.cdi.seam3.bot.test.tests.GenericOpenOnTest;
import org.jboss.tools.cdi.seam3.bot.test.tests.InterfaceAndAbstractValidationTest;
import org.jboss.tools.cdi.seam3.bot.test.tests.LoggerSupportTest;
import org.jboss.tools.cdi.seam3.bot.test.tests.MessageLoggerAnnotationTest;
import org.jboss.tools.cdi.seam3.bot.test.tests.NamedPackagesTest;
import org.jboss.tools.cdi.seam3.bot.test.tests.RequiresAnnotationTest;
import org.jboss.tools.cdi.seam3.bot.test.tests.ResourceOpenOnTest;
import org.jboss.tools.cdi.seam3.bot.test.tests.SeamConfigClassBaseOpenOnTest;
import org.jboss.tools.cdi.seam3.bot.test.tests.SeamConfigCodeCompletionTest;
import org.jboss.tools.cdi.seam3.bot.test.tests.SeamConfigEEOpenOnTest;
import org.jboss.tools.cdi.seam3.bot.test.tests.SeamConfigInjectOpenOnTest;
import org.jboss.tools.cdi.seam3.bot.test.tests.SeamConfigValidationTest;
import org.jboss.tools.cdi.seam3.bot.test.tests.VetoAnnotationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Suite duration: aprox. 18min
 * 
 * @author Jaroslav Jankovic
 */
@RunWith(RedDeerSuite.class)
@SuiteClasses({	
	
	ResourceOpenOnTest.class,
	
	GenericOpenOnTest.class,
	
	DefaultBeansTest.class, //failing, maybe bug
	
	ExactAnnotationTest.class,
	VetoAnnotationTest.class,
	
	RequiresAnnotationTest.class,
	
	NamedPackagesTest.class,
	
	
	FullyQualifiedTest.class,
	
	
	LoggerSupportTest.class,
	
	InterfaceAndAbstractValidationTest.class,
	
	MessageLoggerAnnotationTest.class,
	
	SeamConfigClassBaseOpenOnTest.class,
	SeamConfigEEOpenOnTest.class,
	
	SeamConfigInjectOpenOnTest.class,
	
	
	SeamConfigValidationTest.class,
	
	SeamConfigCodeCompletionTest.class,
	
	})
public class CDISeam3AllBotTests {
	
}
