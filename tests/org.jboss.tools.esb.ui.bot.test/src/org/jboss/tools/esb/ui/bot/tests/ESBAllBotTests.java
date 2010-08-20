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
	})
@RunWith(RequirementAwareSuite.class)
public class ESBAllBotTests {

}
