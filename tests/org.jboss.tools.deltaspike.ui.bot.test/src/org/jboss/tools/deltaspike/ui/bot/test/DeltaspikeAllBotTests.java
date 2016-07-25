/*******************************************************************************


 * 
 * 
 * 
 * 
 * 
 * 
 * [45Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.deltaspike.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({
	//ValidationsInPreferenceTest.class,
	ExcludesAnnotationTest.class,
	
	ConfigPropertyAnnotationTest.class,
	MessageBundleAnnotationTest.class,
	JmxBroadcaster.class,
	ExceptionHandlerAnnotationTest.class,
	ExceptionHandlerMethodsTest.class,
	SecuresAnnotationTest.class,
	SecuredAnnotationTest.class,
	MessageContextAnnotationTest.class,
	PartialBeanTest.class
	
})
public class DeltaspikeAllBotTests {
}
