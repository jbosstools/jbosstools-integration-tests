/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.seam3.bot.test;

import org.jboss.tools.cdi.seam3.bot.test.tests.DefaultBeansTest;
import org.jboss.tools.cdi.seam3.bot.test.tests.ExactAnnotationTest;
import org.jboss.tools.cdi.seam3.bot.test.tests.VetoAnnotationTest;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Suite of tests executed on jenkins slave
 * @author Jaroslav Jankovic
 */
@RunWith(RequirementAwareSuite.class)
@SuiteClasses({
	
	DefaultBeansTest.class,
	ExactAnnotationTest.class,
	VetoAnnotationTest.class,
	
	
	
	/** Not stable yet
	
	RequiresAnnotationTest.class,
	NamedPackagesTest.class,
	FullyQualifiedTest.class,
	LoggerSupportTest.class,
	InterfaceAndAbstractValidationTest.class,
	MessageLoggerAnnotationTest.class,
	ResourceOpenOnTest.class,
	GenericOpenOnTest.class,
	SeamConfigValidationTest.class,
	SeamConfigClassBaseOpenOnTest.class,
	SeamConfigEEOpenOnTest.class,
	SeamConfigInjectOpenOnTest.class,
	SeamConfigCodeCompletionTest.class,
	
	**/
})
public class JenkinsTestSuite {
		
}
